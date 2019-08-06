package Controller;

import Entities.*;
import Observer.Observer;

import java.util.*;
import java.util.concurrent.*;

public interface ControllerFacade {
	
	/**Main Operations */
	void addLauncher(Launcher launcher) /*throws NoSuchElementException*/;

    void addLauncherDestructor(LauncherDestructor launcherDestructor);

    void addMissileDestructor(MissileDestructor missileDestructor);

    boolean launchMissile(Launcher launcher, String destination, double potentialDamage) throws InterruptedException, ExecutionException;
    boolean launchMissile(Launcher launcher, String destination, double potentialDamage, long maxFlightTime) throws InterruptedException, ExecutionException;
    boolean launchMissile(Launcher launcher, Missile missile);

        boolean destructMissile(MissileDestructor missileDestructor, Missile missile);
   boolean destructMissile(MissileDestructor missileDestructor, Missile missile, long time);

   boolean destructLauncher(LauncherDestructor launcherDestructor, Launcher launcher) throws InterruptedException;
   boolean destructLauncher(LauncherDestructor launcherDestructor, Launcher launcher, int time) throws InterruptedException;

//   Statistics getStatistics();

   void exit();

   /** Support Operations */
   Set<Launcher> retrieveLaunchers();

   Set<MissileDestructor> retrieveMissileDestructors();

   Set<LauncherDestructor> retrieveLauncherDestructors();

   Set<Missile> retrieveActiveMissiles();

   /**Observer - Observable Methods */
   void subscribe(Observer subscriber);

   void unsubscribe(Observer subscriber);

   long getTime();
}
