package Controller;

import Observer.Observable;
import Entities.*;
import DB.DBConnectorFacade;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;

public class Controller extends Observable implements ControllerFacade{
	private static Controller warController = new Controller();

    private static LocalTime startRunTime = LocalTime.now();
    private HashMap<Launcher, LauncherController> missileLaunchers;
    private HashMap<MissileDestructor, MissileDestructorController> missileDestructors;
    private HashMap<LauncherDestructor, LauncherDestructorController> launcherDestructors;

    private DBConnectorFacade DbConnector;

//    private Statistics statistics = new Statistics();


    private ExecutorService executorService = Executors.newCachedThreadPool();

    private WarController(){
        missileLaunchers = new HashMap<>();
        missileDestructors = new HashMap<>();
        launcherDestructors = new HashMap<>();
        
        DbConnector = new DBConnectorFacade();
    }
    
    public static void startTime(){
        startRunTime = LocalTime.now();
    }

    public static WarControllerFacade getInstance(){
        return warController;
    }

    @Override
    public void addLauncher(Launcher launcher){
        LauncherController launcherController = new LauncherController(launcher);
        executorService.submit(launcherController);

        missileLaunchers.put(launcher, launcherController);

        publish(subscriber -> subscriber.launcherWasAdded(launcher));
        
        DbConnector.addNewLauncher(launcher);
    }

    @Override
    public void addLauncherDestructor(LauncherDestructor launcherDestructor) {
        launcherDestructors.put(launcherDestructor, new LauncherDestructorController(launcherDestructor));
        publish(subscriber -> subscriber.launcherDestructorWasAdded(launcherDestructor));
        
        DbConnector.addNewDestructor(launcherDestructor);
    }

    @Override
    public void addMissileDestructor(MissileDestructor missileDestructor) {
        missileDestructors.put(missileDestructor, new MissileDestructorController(missileDestructor));
        publish(subscriber -> subscriber.missileDestructorWasAdded(missileDestructor));
        
        DbConnector.addNewDestructor(missileDestructor);
    }

    @Override
    public boolean launchMissile(Launcher launcher, Destination destination, double potentialDamage) {
        final int MAX_TIME = 11;

        int maxFlightTime = new Random().nextInt(MAX_TIME) + 4;
          
        return launchMissile(launcher, destination, potentialDamage, maxFlightTime);
    }

    @Override
    public boolean launchMissile(Launcher launcher, Destination destination, double potentialDamage, long maxFlightTime) {
        Missile missile = new Missile(potentialDamage, destination, maxFlightTime, getTime());
        
        return launchMissile(launcher, missile);
    }

    @Override
    public boolean launchMissile(Launcher launcher, Missile missile){
        boolean hit = false;
        LauncherController launcherController = missileLaunchers.getOrDefault(launcher, null);
        
        if(launcherController == null){
            return false;
        }

        DbConnector.addNewMissile(missile);
        DbConnector.relateMissileToLauncher(missile, launcher);
        DbConnector.logLaunch(launcher, missile);
        DbConnector.updateLoggedLaunch(launcher, missile, DBConnectorFacade.Status.MISS);

        
        publish(subscriber -> subscriber.missileLaunched(launcher, missile, missile.getDestination()));
        
        statistics.increaseNumOfLaunchedMissiles();

        try {
            hit = executorService.submit(() -> launching(launcherController, missile, missile.getFlightTime())).get();

            if(hit){
                publish(subscriber -> subscriber.missileHit(launcher, missile, missile.getDestination()));
                DbConnector.updateLoggedLaunch(launcher, missile, DBConnectorFacade.Status.HIT);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return hit;
    }

    private boolean launching(LauncherController launcherController, Missile missile, long maxFlightTime){
        try {
            boolean hit = false;
            if(!launcherController.isDestructed())
                hit = launcherController.launch(missile, maxFlightTime);

            if(hit) {
                statistics.increaseNumOfHits();
                statistics.raiseDamage(missile.getDamage());
            }
            return hit;
        } catch (InterruptedException |ExecutionException e1) {
            e1.printStackTrace();
            return false;
        }
    }



    @Override
    public boolean destructMissile(MissileDestructor missileDestructor, Missile missile) {
        final int MAX = 5, OFFSET = 1;
        return destructMissile(missileDestructor, missile, new Random().nextInt(MAX) + OFFSET);
    }

    @Override
    public boolean destructMissile(MissileDestructor missileDestructor, Missile missile, long time) {
        boolean destruct = false;
        long timeFromLaunch;
        MissileDestructorController missileDestructorController = missileDestructors.get(missileDestructor);
        for(LauncherController launcherController: missileLaunchers.values()){
            if(launcherController.isCurrentlyLaunching()){
                MissileController missileController = launcherController.getActiveMissileController();

                if(missileController.getMissile().equals(missile)){
                    publish(subscriber -> subscriber.missileDestructed(missileDestructor, missile));

                    sleepSpecificTime(time);

                    timeFromLaunch = getTime() - missileController.getMissile().getLaunchTime();

                    Future<Boolean> destructionFuture = executorService.submit(() ->
                            destructingMissile(missileDestructorController, missileController, timeFromLaunch));
                    try {
                        destruct = destructionFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return destruct;
                }
            }
        }
        return false;
    }

        private boolean destructingMissile(MissileDestructorController missileDestructorController,
                                       MissileController missileController, long timeFromLaunch){
        boolean destruct = missileDestructorController.destruct(missileController, timeFromLaunch); //assumption: always FALSE!

            if(destruct) {
                statistics.increaseNumOfDestructedMissiles();
            }
        return destruct;
    }

    @Override
    public boolean destructLauncher(LauncherDestructor launcherDestructor, Launcher launcher) throws InterruptedException {
        final int MAX_TIME = 5, TIME_OFFSET = 1;
        int destructionTime = new Random().nextInt(MAX_TIME) + TIME_OFFSET;
        return destructLauncher(launcherDestructor, launcher, destructionTime);
    }

    @Override
    public boolean destructLauncher(LauncherDestructor launcherDestructor, Launcher launcher, int destructionTime) throws InterruptedException {
        boolean succeed;
        
        LauncherController launcherController = missileLaunchers.getOrDefault(launcher, null);

        if(launcherController == null)
            throw new IllegalArgumentException("launcher doesn't not exist or already destructed");
        LauncherDestructorController launcherDestructorController =
                launcherDestructors.get(launcherDestructor);

        publish(subscriber -> subscriber.tryToDestructLauncher(launcherDestructor, launcher, destructionTime));
        sleepSpecificTime(destructionTime);

        succeed = launcherDestructorController.destruct(launcherController, getTime());

        if(succeed){
            publish(subscriber -> subscriber.launcherDestructed(launcherDestructor, launcher));
            missileLaunchers.remove(launcher);
            statistics.increaseNumOfDestructedLaunchers();
        }else{
            publish(subscriber -> subscriber.launcherWasHidden(launcherDestructor, launcher));
        }
        
        return succeed;
    }

    @Override
    public Statistics getStatistics() {
        return statistics;
    }

    @Override
    public void exit() {
        executorService.shutdown();
        publish(WarObserver::endOfWar);
        DbConnector.clearWarDataBase();
        DbConnector.closeDB();
        System.exit(0);
    }

    @Override
    public Set<Launcher> retrieveLaunchers() {
        return missileLaunchers.keySet();
    }

    @Override
    public Set<MissileDestructor> retrieveMissileDestructors() {
        return missileDestructors.keySet();
    }

    @Override
    public Set<LauncherDestructor> retrieveLauncherDestructors() {
        return launcherDestructors.keySet();
    }

    @Override
    public Set<Missile> retrieveActiveMissiles() {
        Set<Missile> activeMissiles = new HashSet<>();
        for(LauncherController launcherController : missileLaunchers.values()){
            if(launcherController.isCurrentlyLaunching()) {
                activeMissiles.add(launcherController.getActiveMissileController().getMissile());
            }
        }

        return activeMissiles;
    }

    public long getTime(){
        return SECONDS.between(startRunTime,LocalTime.now());
    }

    private void sleepSpecificTime(long seconds){
        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e){

        }
    }
}
