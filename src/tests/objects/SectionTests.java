package tests.objects;

import java.util.UUID;

import junit.framework.TestCase;
import tos.objects.*;

public class SectionTests extends TestCase {
	final private int SORT_ORDER = 0;
	public void testConstructorNullArguments() {
		try {
			new Section(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Section(new UUID(5,5),new UUID(2,5),null,false,SORT_ORDER);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Section(new UUID(5,5),null,"blah",false,SORT_ORDER);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		try {
			new Section(null, new UUID(5,5),"blah",false,SORT_ORDER);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testIntegerBounds() {
		try {
			new Section(new UUID(1,1), new UUID(5,5),"blah",false,-1);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		assertNotNull(new Section(new UUID(1,1), new UUID(5,5),"blah",false,0));
	}
	
	public void testMutatorInvalidArguments() {
		Section section = new Section(new UUID(1,1), new UUID(5,5),"blah",
				false,SORT_ORDER);
		try {
			section.setSortOrder(-1);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testConstructorValidArguments() {
		Section section = new Section("Test");
		assertEquals(section.getTitle(),"Test");
		assertEquals(section.isWIP(),false);
		
		section = new Section(new UUID(5,5),new UUID(2,5),"Test2",true,SORT_ORDER);
		assertEquals(section.getBoardID(),new UUID(2,5));
		assertEquals(section.getSectionID(),new UUID(5,5));
		assertEquals(section.getTitle(),"Test2");
		assertEquals(section.isWIP(),true);
		assertEquals(section.getSortOrder(),SORT_ORDER);
	}
	
	public void testConstructorEmptyString() {
		Section section = new Section("");
		assertEquals(section.getTitle(),"");
		
		section = new Section(new UUID(5,5),new UUID(2,5),"",false,SORT_ORDER);
		assertEquals(section.getBoardID(),new UUID(2,5));
		assertEquals(section.getSectionID(),new UUID(5,5));
		assertEquals(section.getTitle(),"");
		assertEquals(section.getSortOrder(),SORT_ORDER);
	}
	
	public void testMutatorValidArguments() {
		Section section = new Section("Test");
		
		section.setBoardID(new UUID(1,1));
		assertEquals(section.getBoardID(),new UUID(1,1));
			
		section.setSectionID(new UUID(10,10));
		assertEquals(section.getSectionID(),new UUID(10,10));
	
		section.setTitle("Blah");
		assertEquals(section.getTitle(),"Blah");
		
		section.setSortOrder(7);
		assertEquals(section.getSortOrder(),7);
	}
		
	public void testMutatorEmptyStringArgument() {
		Section section = new Section("Test");
		section.setTitle("");
		assertEquals(section.getTitle(),"");
	}
	
	public void testMutatorNullArguments() {
		try {
			Section section = new Section(new UUID(1,1),new UUID(2,2),
					"blah",false,SORT_ORDER);
			section.setTitle(null);
			fail("Should have thrown illegal argument exception");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			Section section = new Section(new UUID(1,1),new UUID(2,2),
					"blah",false,SORT_ORDER);
			section.setBoardID(null);
			fail("Should have thrown illegal argument exception");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			Section section = new Section(new UUID(1,1),new UUID(2,2),
					"blah",false,SORT_ORDER);
			section.setSectionID(null);
			fail("Should have thrown illegal argument exception");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testAccessorsNotNull() {
		Section section = new Section(new UUID(1,1), new UUID(2,1),
				"blah",false,SORT_ORDER);
		assertNotNull(section.getTitle());
		assertNotNull(section.getBoardID());
		assertNotNull(section.getSectionID());
	}
	
	public void testAccessorIsWIP(){
		Section section = new Section(new UUID(1,1), new UUID(2,1),
				"blah",false,SORT_ORDER);
		assertEquals(section.isWIP(),false);
		section = new Section(new UUID(1,1), new UUID(2,1),
				"blah",true,SORT_ORDER);
		assertEquals(section.isWIP(),true);
	}
	
	public void testComparison() {
		Section section1 = new Section(new UUID(1,1),
				new UUID(2,1), "blah",false,4);
		Section section2 = new Section(new UUID(1,1),
				new UUID(2,1), "blah",false,6);
		assertEquals(section1.compareTo(section2),-1);
		assertEquals(section2.compareTo(section1),1);
		assertEquals(section1.compareTo(section1),0);
		assertEquals(section2.compareTo(section2),0);
	}
}
