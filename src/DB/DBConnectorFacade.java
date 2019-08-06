package DB;

import java.util.*;
import Entities.*;

public class DBConnectorFacade {
	public enum dbType {SQL, MONGO}
	public enum Status {MISS, HIT}

	private static dbType type;
	private static DBConnector dbConnector;
	
	
	// Set Type of Desired DB
	public DBConnectorFacade(dbType databaseType)
	{
		type = databaseType;
		
		if (type == dbType.SQL)
			dbConnector = ConnectorSQL.getInstance();
		else if(type == dbType.MONGO)
			dbConnector = ConnectorMongo.getInstance();
		else
			throw new IllegalArgumentException("Illegal Database Type");
	}
	
	public DBConnectorFacade() {
		if (type == dbType.SQL)
			dbConnector = ConnectorSQL.getInstance();
		else if(type == dbType.MONGO)
			dbConnector = ConnectorMongo.getInstance();
		else
			throw new IllegalArgumentException("Illegal Database Type");
	}
	
	public static void setDbType(dbType dbtype) {
		type = dbtype;
	}
	
	// Get all Table/Collection DATA by Name
	public Vector<String[]> getQueryData(String tableName) {
			return dbConnector.getQueryData(tableName);
	}
	
	// Get Collection names
	public List<String> getAllCollectionNames() {
		return dbConnector.getAllCollectionNames();
	}
	
	// Add Missile to DB
	public void addNewMissile(Missile missile) {
		dbConnector.addNewMissile(missile.getId(), missile.getDestination().toString(), missile.getDamage(), missile.getFlyTime());
	}

	// Relate Missile to Destructor in DB
	public void relateMissileToLauncher(Missile missile, Launcher launcher) {
		dbConnector.relateMissileToLauncher(missile.getId(), launcher.getId());
	}
	
	// Add Launcher to DB
	public void addNewLauncher(Launcher launcher) {
		dbConnector.addNewLauncher(launcher.getId(), launcher.isHidden() ? 1 : 0);
	}

	// Add LauncherDestructor to DB
	public void addNewDestructor(LauncherDestructor launcherDestructor) {
		dbConnector.addNewDestructor(launcherDestructor.getId(), launcherDestructor.getType().name());
	}
	// Add MissileDestructor to DB
	public void addNewDestructor(MissileDestructor missileDestructor) {
		dbConnector.addNewDestructor(missileDestructor.getId(), missileDestructor.getClass().getSimpleName());
	}
	
	// Log a Launch 
	public void logLaunch(Launcher launcher, Missile missile) {
		dbConnector.logLaunch(launcher.getId(), missile.getId(), missile.getDestination().toString());
	}
	
	// Update a Launch 
	public void updateLoggedLaunch(Launcher launcher, Missile missile, Status status) {
		dbConnector.updateLoggedLaunch(launcher.getId(), missile.getDestination().toString(), status.name());
	}
	
	// Delete All Entries 
	public void clearWarDataBase() {
		dbConnector.clearWarDataBase();
	}
	
	// Close Connection
	public void closeDB() {
		dbConnector.closeDB();
	}
}
