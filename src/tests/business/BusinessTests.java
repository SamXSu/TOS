package tests.business;

import junit.framework.Test;
import junit.framework.TestSuite;

public class BusinessTests {
	public static TestSuite suite;

    public static Test suite() {
        suite = new TestSuite("Business tests");
        suite.addTestSuite(AccessBoardsTests.class);
        suite.addTestSuite(AccessSectionsTests.class);
        suite.addTestSuite(AccessTasksTests.class);
        return suite;
    }
}
