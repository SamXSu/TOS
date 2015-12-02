package tests.application;

import junit.framework.TestCase;
import tests.persistence.DBAccessStub;
import tos.application.*;
import tos.persistence.DBAccess;

public class DBServicesTest extends TestCase {
	public void tearDown() {
		DBService.closeDataAccess();
	}
	
	public void testNullArguments() {		
		try {
			DBAccess nullDBAccess = null;
			DBService.createDataAccess(nullDBAccess);
			fail("Should have caught illegal argument exception.");
		}
		catch (IllegalArgumentException ex) {
			
		}
	}
	
	public void testEmptyStringArguments() {
		try {
			DBService.createDataAccess("","");
			fail("Should have caught illegal argument exception.");
		}
		catch (IllegalArgumentException iae) {
			
		}		
	}
	
	
	public void testOpenAccessCloseConnection() {
		assertTrue(DBService.createDataAccess(new DBAccessStub("Test")));
		assertNotNull(DBService.getDataAccess());
		assertTrue(DBService.closeDataAccess());
	}
	
	public void testAccessANullConnection() {
		try {
			DBService.getDataAccess();
			fail("DB connection not established exception should have been caught.");
		}
		catch (NullPointerException ex) {
			
		}
		
		try {
			DBService.createDataAccess(new DBAccessStub("blah"));
			DBService.closeDataAccess();
			DBService.getDataAccess();
			fail("DB connection not established exception should have been caught.");
		}
		catch (NullPointerException ex) {
			
		}
	}
	
	public void testCloseANullConnection() {
		assertFalse(DBService.closeDataAccess());
	}
}
