package DB;

import java.util.*;

public interface DBConnector {
	public Vector<String[]> getQueryData(String collectionName);
	public void relateMissileToLauncher(String id, String launcher_id);
	public void addNewMissile(String id, String destination, double damage, long flyTime);
	public void addNewLauncher(String launcherId, int isHidden);
	public void addNewDestructor(String destructorId, String type);
	public void logLaunch(String launcherId, String missileId, String destinationId);
	public void updateLoggedLaunch(String launcherId, String destinationId, String status);
	public void closeDB();
	public void clearWarDataBase();
	public List<String> getAllCollectionNames();
}
