package tos.application;
import tos.persistence.DBAccess;
import tos.persistence.DBAccessHSQL;

public class DBService {
	private static DBAccess dbAccessService = null;

	public static boolean createDataAccess(DBAccess otherDataAccess) {
		boolean isConnectionOpen = false;
		
		if (otherDataAccess == null) {
			throw new IllegalArgumentException(
					"Illegal Argument Exception: Database object cannot be null.");
		}
		else {
			dbAccessService = otherDataAccess;
			isConnectionOpen = dbAccessService.connectToDB();
			validateConnectionOpen(isConnectionOpen);
		}
			
		return isConnectionOpen;
	}	
	
	public static boolean createDataAccess(String dbName, String dbType) {
		boolean isConnectionOpen = false;
		
		if (dbName == null || dbName.equals("") 
					|| dbType == null || dbType.equals("")) {
			throw new IllegalArgumentException(
						"Illegal Argument Exception: Database name cannot" +
						" be empty or null.");
		}
		else if (dbAccessService == null) {
			dbAccessService = new DBAccessHSQL(dbName,dbType);
			isConnectionOpen = dbAccessService.connectToDB();
			validateConnectionOpen(isConnectionOpen);
		}
		
		return isConnectionOpen;
	}
	
	private static void validateConnectionOpen(boolean isConnectionOpen) {
		if ((!isConnectionOpen && dbAccessService == null) 
					|| dbAccessService == null) {
			throw new NullPointerException("Database connection" +
						" has not been established.");
		}
	}
	
	private static void validateConnectionClosed(boolean isConnectionClosed) {
		if (!isConnectionClosed && dbAccessService != null) {
			throw new NullPointerException("Database connection has not been closed.");
		}
	}
	
	public static DBAccess getDataAccess() {
		validateConnectionOpen(true);
		return dbAccessService;
	}
	
	public static boolean closeDataAccess() {
		boolean isConnectionClosed = false;
		
		if (dbAccessService != null) {
			isConnectionClosed = dbAccessService.closeConnectionToDB();
			if (isConnectionClosed) {
				dbAccessService = null;
			}
			validateConnectionClosed(isConnectionClosed);
		}
		
		return isConnectionClosed;
	}
}
