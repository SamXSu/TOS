package tests;
import junit.framework.Test;
import junit.framework.TestSuite;
import tests.application.DBServicesTest;
import tests.business.AccessBoardsTests;
import tests.business.AccessSectionsTests;
import tests.business.AccessTasksTests;
import tests.integration.BusinessPersistenceSeamTest;
import tests.integration.DBServiceAccessHSQLDBTest;
import tests.objects.BoardTests;
import tests.objects.SectionTests;
import tests.objects.TaskTests;
import tests.persistence.DBAccessOperationsTest;
import tests.persistence.DBAccessRetrieveDataTest;


public class RunAllTests {
	public static TestSuite suite;

    public static Test suite()
    {
        suite = new TestSuite("All tests");
        testObjects();
        testPersistence();
        testBusiness();
        testApplication();
        testIntegration();
        return suite;
    }

    private static void testObjects()
    {
        suite.addTestSuite(BoardTests.class);
        suite.addTestSuite(SectionTests.class);
        suite.addTestSuite(TaskTests.class);
    }
    
    private static void testPersistence() {
    	suite.addTestSuite(DBAccessOperationsTest.class);
        suite.addTestSuite(DBAccessRetrieveDataTest.class);
    }
    
    private static void testBusiness() {
    	suite.addTestSuite(AccessBoardsTests.class);
    	suite.addTestSuite(AccessSectionsTests.class);
    	suite.addTestSuite(AccessTasksTests.class);
    }
    
    private static void testApplication() {
    	suite.addTestSuite(DBServicesTest.class);
    }
    
    private static void testIntegration() {
    	suite.addTestSuite(BusinessPersistenceSeamTest.class);
        suite.addTestSuite(DBServiceAccessHSQLDBTest.class);
    }
}