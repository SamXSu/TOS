package tests.persistence;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PersistenceTests {
	public static TestSuite suite;

    public static Test suite()
    {
        suite = new TestSuite("Business tests");
        suite.addTestSuite(DBAccessOperationsTest.class);
        suite.addTestSuite(DBAccessRetrieveDataTest.class);
        return suite;
    }
}
