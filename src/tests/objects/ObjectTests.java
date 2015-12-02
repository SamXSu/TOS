package tests.objects;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ObjectTests {
	public static TestSuite suite;

    public static Test suite()
    {
        suite = new TestSuite("Business tests");
        suite.addTestSuite(BoardTests.class);
        suite.addTestSuite(SectionTests.class);
        suite.addTestSuite(TaskTests.class);
        suite.addTestSuite(TagTests.class);
        suite.addTestSuite(CurrentTimeTests.class);
        return suite;
    }
}

