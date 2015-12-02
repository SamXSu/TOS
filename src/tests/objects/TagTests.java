package tests.objects;

import java.util.UUID;
import tos.objects.*;
import junit.framework.TestCase;

public class TagTests extends TestCase {			
	public void testConstructorsValidData() {
		Tag tag = new Tag("Test",new UUID(2,1),new UUID(1,1));
		
		assertEquals(tag.getTag(),"Test");
		assertEquals(tag.getTagID(),new UUID(2,1));
		assertEquals(tag.getTaskID(),new UUID(1,1));
		
		tag = new Tag("Test1");
		assertEquals(tag.getTag(),"Test1");
		
		tag = new Tag("Test2",new UUID(2,2));
		assertEquals(tag.getTag(),"Test2");
		assertEquals(tag.getTaskID(),new UUID(2,2));
	}
	
	public void testConstructorsEmptyString() {
		try {
			new Tag("");
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		try {
			new Tag("",new UUID(2,2));
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		try {
			new Tag("",new UUID(2,1),new UUID(1,1));
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testConstructorNullArguments() {
		try {
			new Tag(null);
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Tag(null,new UUID(1,1));
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Tag("test",null);
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Tag(null,new UUID(1,1),new UUID(2,1));
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Tag("test",null,new UUID(2,1));
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Tag("test",new UUID(1,1),null);
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}

	public void testAccessorsNotNull() {
		Tag tag = new Tag("Test",new UUID(2,1),new UUID(1,1));
		
		assertNotNull(tag.getTag());
		assertNotNull(tag.getTagID());
		assertNotNull(tag.getTaskID());
		
		tag = new Tag("Test1");
		assertNotNull(tag.getTag());
		
		tag = new Tag("Test2",new UUID(2,2));
		assertNotNull(tag.getTag());
		assertNotNull(tag.getTaskID());
	}
	
	public void testComparison() {
		Tag tag1 = new Tag("a",new UUID(2,1),new UUID(1,1));
		Tag tag2 = new Tag("b",new UUID(2,1),new UUID(1,1));
		assertEquals(tag1.compareTo(tag2),-1);
		assertEquals(tag2.compareTo(tag1),1);
		assertEquals(tag1.compareTo(tag1),0);
		assertEquals(tag2.compareTo(tag2),0);
	}
}

