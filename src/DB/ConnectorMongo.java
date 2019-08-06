package DB;

import java.util.*;
import org.bson.*;
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class ConnectorMongo implements DBConnector {
private static ConnectorMongo theInstance;
	
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Document> destructorsCollection;
	private MongoCollection<Document> missilesCollection;
	private MongoCollection<Document> launchersCollection;
	private MongoCollection<Document> logCollection;
	
	// Singleton implementation
	public static ConnectorMongo getInstance() {
        if (theInstance == null) {
            if (theInstance == null)
            	theInstance = new ConnectorMongo();
            }
        
        return theInstance;
    }
	
	// Singleton implementation
	private ConnectorMongo() 
	{
		try {
			// Try to connect to MongoDB, create War DB and collections
			this.mongoClient = new MongoClient("localhost", 27017);
			
			this.mongoClient.dropDatabase("War");
			this.database = mongoClient.getDatabase("War");
			
			this.destructorsCollection = database.getCollection("destructors");
			this.missilesCollection = database.getCollection("missiles");
			this.launchersCollection = database.getCollection("launchers");
			this.logCollection = database.getCollection("log");

		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	//Get all Collection DATA by Collection Name
	public Vector<String[]> getQueryData(String collectionName) {
		Vector<String[]> resultData = new Vector<String[]>();
		try {
			MongoCollection<Document> collection = database.getCollection(collectionName);
			FindIterable<Document> collectionData = collection.find();
			for (Document d : collectionData) {    
				String[] data = new String[d.values().toArray().length - 1];
				for(int i = 0; i < data.length; i++)
					data[i] = (d.values().toArray()[i+1]).toString();
				resultData.add(data);
			}
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return resultData;
				
	}
/*	
	// Update Missile status in DB
	public void updateMissileStatus(String id, String status) {
		try {
			missilesCollection.updateOne(Filters.eq("id",id), Updates.set("status",status));
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
*/	
	// Relate Missile to Destructor in DB
	public void relateMissileToLauncher(String id, String launcher_id) {
		try {
			missilesCollection.updateOne(Filters.eq("id",id), Updates.set("launcher_id",launcher_id));
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	// Add Missile to DB
	public void addNewMissile(String id, String destination, double damage, long flyTime) {
		try {
			this.missilesCollection.insertOne(new Document("id", id)
	                .append("destination", destination)
	                .append("damage", damage)
	                .append("flyTime", flyTime)
	                .append("launcher_id", "")
					);
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
		
	// Add Launcher to DB
	public void addNewLauncher(String launcherId, int isHidden) {
		try {
			this.launchersCollection.insertOne(new Document("id", launcherId)
	                .append("isHidden", isHidden)
					);
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
/*		
	// Update Launcher status in DB
	public void updateLauncherStatus(String id, String status) {
		try {
			this.launchersCollection.updateOne(Filters.eq("id",id), Updates.set("status",status));
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
*/
	// Add Destructor to DB
	public void addNewDestructor(String destructorId, String type) {
		try {
			this.destructorsCollection.insertOne(new Document("id", destructorId)
	                .append("type", type)
					);
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	// Log a Launch 
	public void logLaunch(String launcherId, String missileId, String destinationId) {
		try {
			this.logCollection.insertOne(new Document("idLauncher", launcherId)
	                .append("idDestination", destinationId)
	                .append("idMissile", missileId)
	                .append("status", "")
					);
		} catch (MongoException e) {
			e.printStackTrace();
		}		
	}
	
	// Update a Launch 
	public void updateLoggedLaunch(String launcherId, String destinationId, String status) {
		try {
			this.logCollection.updateOne(Filters.and(Filters.eq("idLauncher",launcherId), Filters.eq("idDestination",destinationId)), Updates.set("status",status));
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	// Close Connection
	public void closeDB() {
		try {
			this.mongoClient.close();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	// Delete All Entries 
	public void clearWarDataBase() {
		try {
			this.destructorsCollection.drop();
			this.missilesCollection.drop();
			this.launchersCollection.drop();
			this.logCollection.drop();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	// Get Collection names
	public List<String> getAllCollectionNames() {
		List<String> allCollections = new ArrayList<String>();
		try {
			MongoIterable<String> collectionNames = database.listCollectionNames();
			for(String collectionName : collectionNames)
			{
				allCollections.add(collectionName);
			}
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		return allCollections;
	}
}
