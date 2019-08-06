package DB;

import java.sql.*;
import java.util.*;

public class ConnectorSQL implements DBConnector {
	private static ConnectorSQL theInstance;
	
	private Connection connection;
	private String dbUrl;
	private ResultSet rs;
	private PreparedStatement statement;
	private Statement state;
	
	// Singleton implementation
	public static ConnectorSQL getInstance() {
        if (theInstance == null) {
            if (theInstance == null)
            	theInstance = new ConnectorSQL();
            }
        
        return theInstance;
    }

	// Private C'tor
	@SuppressWarnings("deprecation")
	private ConnectorSQL()
	{
		dbUrl = "jdbc:mysql://localhost/";

		try {
			//Deprecated and should be replaced in the future
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(dbUrl, "root", "");
			
			// Try to create Database + tables
			try {
				String sql = "CREATE DATABASE IF NOT EXISTS war";
				state = connection.createStatement();
				state.executeUpdate(sql);
				dbUrl = dbUrl + "war";	
				connection = DriverManager.getConnection(dbUrl, "root", "");
				
				String destructorSQL =
				"CREATE TABLE IF NOT EXISTS `destructors` "
						+ "( `id` VARCHAR(10) NOT NULL, "
						+ "`type` VARCHAR(40) NULL, " + "PRIMARY KEY (`id`)) ";
				state = connection.createStatement();
				state.executeUpdate(destructorSQL);
				
				String missileSQL =
				"CREATE TABLE IF NOT EXISTS `missiles` "
						+ "(`id` VARCHAR(10) NOT NULL, "
						+ "`destination` VARCHAR(40) NOT NULL, "
						+ "`damage` FLOAT NOT NULL, "
						+ "`flytime` FLOAT NOT NULL, "
						+ "`launcher_id` VARCHAR(10) NULL, "
						+ " PRIMARY KEY (`id`)) ";
				state = connection.createStatement();
				state.executeUpdate(missileSQL);

				String launcherSQL = "CREATE TABLE IF NOT EXISTS `launchers` "
						+ "(`id` VARCHAR(30) NOT NULL, "
						+ "`isHidden` TINYINT(1) NULL,"
						+ "PRIMARY KEY (`id`)) ";
				state = connection.createStatement();
				state.executeUpdate(launcherSQL);
				
				String logSQL = "CREATE TABLE IF NOT EXISTS `log` "
						+ "(`idLauncher` VARCHAR(30) NOT NULL, "
						+ "`idMissile` VARCHAR(30) NULL, "
						+ "`idDestination` VARCHAR(30) NOT NULL, "
						+ "`status` VARCHAR(40) NULL)";
				state = connection.createStatement();
				state.executeUpdate(logSQL);
				
			} catch (SQLException sqlException) {
				if (sqlException.getErrorCode() == 1007) {
					// Database already exists error
					System.out.println(sqlException.getMessage());
				} else {
					sqlException.printStackTrace();
				}
			}

			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			while (e != null) {
				System.out.println(e.getMessage());
				e = e.getNextException();
			}
		}
	}
	
	//Get all Table DATA by Table Name
		public Vector<String[]> getQueryData(String tableName) {
			Vector<String[]> resultData = new Vector<String[]>();
			try {
				statement = (PreparedStatement) connection.prepareStatement("SELECT * FROM " + tableName);
				rs = statement.executeQuery();
				ResultSetMetaData meta = rs.getMetaData();
				int numOfCols = meta.getColumnCount();

				while (rs.next()) {
					String[] record = new String[numOfCols];
					for (int i = 0; i < numOfCols; i++) {
						record[i] = rs.getString(i + 1);
					}
					resultData.addElement(record);
				}
			} catch (SQLException e) {
				while (e != null) {
					e.printStackTrace();
					e = e.getNextException();
				}
			}
			
			return resultData;
		}
	
	// Add Missile to DB
	public void addNewMissile(String id, String destination, double damage, long flyTime) {
		try {
			 connection.createStatement();
			statement = (PreparedStatement) connection
					.prepareStatement("INSERT INTO war.missiles (id,destination,damage,flyTime) VALUES (?, ?, ?, ?)");
			statement.setString(1, id);
			statement.setString(2, destination);
			statement.setDouble(3, damage);
			statement.setLong(4, flyTime);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			while (e != null) {
				e = e.getNextException();
			}
		}
	}
/*
	// Update Missile status in DB
	public void updateMissileStatus(String id, String status) {
		try {
			statement = (PreparedStatement) connection.prepareStatement("UPDATE war.missiles SET `status` = ? WHERE missiles.id = ?; ");
			statement.setString(1, status);
			statement.setString(2, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
*/
	// Relate Missile to Destructor in DB
	public void relateMissileToLauncher(String id, String launcher_id) {
		try {
			statement = (PreparedStatement) connection.prepareStatement("UPDATE war.missiles SET `launcher_id` = ?  WHERE missiles.id = ?; ");
			statement.setString(1, launcher_id);
			statement.setString(2, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Add Launcher to DB
	public void addNewLauncher(String launcherId, int isHidden) {
		try {
			 connection.createStatement();
			statement = (PreparedStatement) connection
					.prepareStatement("INSERT INTO war.launchers (id, isHidden) VALUES (?, ?)");
			statement.setString(1, launcherId);
			statement.setInt(2, isHidden);
			statement.executeUpdate();
		} catch (SQLException e) {
			while (e != null) {
				e = e.getNextException();
			}
		}
	}
/*
	// Update Launcher status in DB
	public void updateLauncherStatus(String id, String status) {
		try {
			statement = (PreparedStatement) connection.prepareStatement("UPDATE war.launchers SET `status` = ? WHERE launchers.id = ?; ");
			statement.setString(1, status);
			statement.setString(2, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
*/
	// Add Destructor to DB
	public void addNewDestructor(String launcherId, String type) {
		try {
			 connection.createStatement();
			statement = (PreparedStatement) connection
					.prepareStatement("INSERT INTO war.destructors (id, type) VALUES (?, ?)");
			statement.setString(1, launcherId);
			statement.setString(2, type);
			statement.executeUpdate();
		} catch (SQLException e) {
			while (e != null) {
				e = e.getNextException();
			}
		}
	}
	
	// Log a Launch 
	public void logLaunch(String launcherId, String missileId, String destinationId) {
		try {
			 connection.createStatement();
			statement = (PreparedStatement) connection
					.prepareStatement("INSERT INTO war.log (idLauncher, idMissile, idDestination) VALUES (?, ?, ?)");
			statement.setString(1, launcherId);
			statement.setString(2, missileId);
			statement.setString(3, destinationId);
			statement.executeUpdate();
		} catch (SQLException e) {
			while (e != null) {
				e = e.getNextException();
			}
		}
	}
		
	// Update a Launch 
	public void updateLoggedLaunch(String launcherId, String destinationId, String status) {
		try {
			statement = (PreparedStatement) connection.prepareStatement("UPDATE war.log SET `status` = ? WHERE log.idLauncher = ? AND log.idDestination = ?; ");
			statement.setString(1, status);
			statement.setString(2, launcherId);
			statement.setString(3, destinationId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Delete All Entries 
	public void clearWarDataBase() {
		try {
			statement = (PreparedStatement) connection
					.prepareStatement("TRUNCATE TABLE `launchers`");
			statement.execute();
			statement = (PreparedStatement) connection
					.prepareStatement("TRUNCATE TABLE `missiles`");
			statement.execute();
			statement = (PreparedStatement) connection
					.prepareStatement("TRUNCATE TABLE `destructors`");
			statement.execute();
			statement = (PreparedStatement) connection
					.prepareStatement("TRUNCATE TABLE `log`");
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	//Get Table Names
	public List<String> getAllCollectionNames() {
		List<String> allTables = new ArrayList<String>();
		try {
			DatabaseMetaData md = connection.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);

			while (rs.next()) {
				allTables.add(rs.getString(3));
			}
		} catch (SQLException e) {
			while (e != null) {
				e.printStackTrace();
				e = e.getNextException();
			}
		}
		return allTables;
	}

	// Close Connection
	public void closeDB() {
		try {
			if (connection != null) {
				connection.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
