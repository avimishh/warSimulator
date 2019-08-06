package Observer;

import Entities.*;

public interface Observer {
    Void launcherWasAdded(Launcher launcher);
    Void launcherDestructorWasAdded(LauncherDestructor launcherDestructor);
    Void missileDestructorWasAdded(MissileDestructor missileDestructor);
    Void missileLaunched(Launcher launcher, Missile missile, String destination);
    Void missileHit(Launcher launcher, Missile missile, String destination);
    Void missileDestructed(MissileDestructor missileDestructor, Missile missile);
    Void tryToDestructLauncher(LauncherDestructor launcherDestructor, Launcher launcher, int time);
    Void launcherDestructed(LauncherDestructor launcherDestructor, Launcher launcher);
    Void launcherWasHidden(LauncherDestructor launcherDestructor, Launcher launcher);
    Void endOfWar();
}
