package tests.integration;

import junit.framework.TestCase;
import tests.persistence.DBAccessStub;
import tos.application.*;

public class DBServiceAccessHSQLDBTest extends TestCase {
	public void tearDown() {
		DBService.closeDataAccess();
	}
	
	public void testOpenAccessCloseHSQLDBConnection() {
		System.out.println("Begin Integration Test Accessing Default DB");
		assertTrue(DBService.createDataAccess("TOS","HSQL"));
		assertNotNull(DBService.getDataAccess());
		assertTrue(DBService.closeDataAccess());
		System.out.println("End Integration Test Accessing Default DB");
	}
	
	public void testOpenAccessCloseSTUBDBConnection() {
		System.out.println("Begin Integration Test Accessing Stub DB");
		assertTrue(DBService.createDataAccess(new DBAccessStub("TOS")));
		assertNotNull(DBService.getDataAccess());
		assertTrue(DBService.closeDataAccess());
		System.out.println("End Integration Test Accessing Default DB");
	}
}

