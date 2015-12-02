package tests.objects;

import java.util.Calendar;
import java.util.UUID;

import junit.framework.TestCase;
import tos.objects.*;

public class TaskTests extends TestCase {
	private Calendar dueDate;
	private Calendar startTimeDate;
	final private int PRIORITY = 0;
	
	public void setUp() {
		dueDate = Calendar.getInstance();
		startTimeDate = Calendar.getInstance();
		dueDate.set(2016,10,23,13,50,45);
		startTimeDate.set(2013,07,04,10,10,30);
	}
	
	public void tearDown() {
		dueDate = null;
	}	

	public void testConstructorValidArguments() {
		Task task = null;
		task = new Task("Test1",dueDate,"Details","Comments", PRIORITY);
		assertNotNull(task);
		assertEquals(task.getTitle(),"Test1");
		assertNotNull(task.getDueDate());
		assertEquals(task.getDetail(),"Details");
		assertEquals(task.getComment(),"Comments");
		assertEquals(task.getPriority(),PRIORITY);
		
		task = new Task(new UUID(1,1),new UUID(1,2),"Test2",
				dueDate,"Details","Comments",PRIORITY,startTimeDate,0,false);
		assertNotNull(task);
		assertEquals(task.getTitle(),"Test2");
		assertEquals(task.getComment(),"Comments");
		assertEquals(task.getDetail(),"Details");
		assertEquals(task.getSectionID(),new UUID(1,2));
		assertEquals(task.getTaskID(),new UUID(1,1));
		assertEquals(task.getPriority(),PRIORITY);
		assertFalse(task.isRunning());
	}
	
	
	public void testConstructorInvalidArguments() {
		try {
			new Task(new UUID(1,1),new UUID(2,1),"",dueDate,
					"Details","Comments",-1,startTimeDate,0,false);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Task("",dueDate,"Details","Comments",-1);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testConstructorNullArguments() {
		try {
			new Task(new UUID(1,1),new UUID(2,1),null,dueDate,
					"Details","Comments",PRIORITY,startTimeDate,0,false);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Task(new UUID(1,1),new UUID(2,1),"Test1",null,
					"Details","Comments",PRIORITY,startTimeDate,0,false);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Task(new UUID(1,1),new UUID(2,1),"Test1",
					dueDate,null,"Comments",PRIORITY,startTimeDate,0,false);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Task(new UUID(1,1),new UUID(2,1),"Test1",
					dueDate,"Details",null,PRIORITY,startTimeDate,0,false);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Task(null,new UUID(2,1),"Test1",dueDate,"Details",null,PRIORITY,
						startTimeDate,0,false);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Task(new UUID(1,1),null,"Test1",dueDate,"Details",null,PRIORITY,
						startTimeDate,0,false);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Task(new UUID(1,1),new UUID(1,5),"Test1",dueDate,"Details",null,PRIORITY,
						null,0,false);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Task(null,dueDate,"","Comments",PRIORITY);
			fail("Should have caught illegal argument exception.");
		}
			catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Task("Title",null,"","Comments",PRIORITY);
			fail("Should have caught illegal argument exception.");
		}
			catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Task("Title",dueDate,null,"Comments",PRIORITY);
			fail("Should have caught illegal argument exception.");
		}
			catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Task("Title",dueDate,"",null,PRIORITY);
			fail("Should have caught illegal argument exception.");
		}
			catch(IllegalArgumentException iae) {
			
		}
	
		try {
			new Task("Title",dueDate,"","comments",-1);
			fail("Should have caught illegal argument exception.");
		}
			catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testConstructorEmptyString() {
		assertNotNull(new Task(new UUID(1,1),new UUID(2,1),"Title",
				dueDate,"","Comments",PRIORITY,startTimeDate,0,false));
		assertNotNull(new Task(new UUID(1,1),new UUID(2,1),"Title",
				dueDate,"Details","",PRIORITY,startTimeDate,0,false));
		assertNotNull(new Task(new UUID(1,1),new UUID(2,1),"",
				dueDate,"Details","Comments",PRIORITY,startTimeDate,0,false));
	}
	
	public void testMutatorsNullArguments() {
		Task task = new Task(new UUID(1,1),new UUID(2,1),"Title",dueDate,
				"Details","",PRIORITY,startTimeDate,0,false);
		try {
			task.setComment(null);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			task.setDetail(null);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			task.setDueDate(null);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			task.setTitle(null);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			task.setTaskID(null);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			task.setSectionID(null);
			fail("Should have caught illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try{
			task.setTags(null);
			fail("Should have caught illegal argument exception");
		}
		catch(IllegalArgumentException iae){
			
		}		
	}
	
	public void testMutatorsInvalidArguments() {
		Task task = new Task(new UUID(1,1),new UUID(2,1),"Test",dueDate,
				"Details","Comments",PRIORITY,startTimeDate,0,false);
	
		try{
			task.setPriority(-1);
			fail("Should have thrown Illegal Argument Exception");
		}
		catch(IllegalArgumentException IAU){
		
		}
		
		try {
			task.setPriority(-500);
			fail("Should have thrown Illegal Argument Exception");
		}
		catch(IllegalArgumentException IAU){
		
		}	
	
		try {
			task.setPriority(-1000);
			fail("Should have thrown Illegal Argument Exception");
		}
		catch(IllegalArgumentException IAU){
		
		}	

	}
	
	public void testMutatorsEmptyString() {
		assertNotNull(new Task("",dueDate,"Details","Comments",PRIORITY));
		assertNotNull(new Task("Title",dueDate,"","Comments",PRIORITY));
		assertNotNull(new Task("Title",dueDate,"Details","",PRIORITY));
		assertNotNull(new Task(new UUID(1,1),new UUID(2,1),"",
				dueDate,"Details","Comments",PRIORITY,startTimeDate,0,false));
		assertNotNull(new Task(new UUID(1,1),new UUID(2,1),"Title"
				,dueDate,"","Comments",PRIORITY,startTimeDate,0,false));
		assertNotNull(new Task(new UUID(1,1),new UUID(2,1),"Title"
				,dueDate,"Details","",PRIORITY,startTimeDate,0,false));
	}
	
	public void testMutatorsValidArguments() {			
		Task task = new Task(new UUID(1,1),new UUID(2,1),"Test",
				dueDate,"Details","Comments",PRIORITY,startTimeDate,0,false);
		
		task.setTitle("blah");
		assertEquals(task.getTitle(),"blah");
		
		task.setComment("Blah");
		assertEquals(task.getComment(),"Blah");
		
		task.setDetail("Blah");
		assertEquals(task.getDetail(),"Blah");
		
		task.setSectionID(new UUID(5,5));
		assertEquals(task.getSectionID(),new UUID(5,5));
		
		task.setTaskID(new UUID(2,2));
		assertEquals(task.getTaskID(),new UUID(2,2));
	
		task.setPriority(1);
		assertEquals(task.getPriority(),1);
	}

	public void testAccessorsNotNull() {
		Task task = new Task(new UUID(1,1),new UUID(2,2),"Title",
				dueDate,"Blah","Blah",PRIORITY,startTimeDate,0,false);
		assertNotNull(task.getComment());
		assertNotNull(task.getDetail());
		assertNotNull(task.getTitle());
		assertNotNull(task.getDueDate());
		assertNotNull(task.getSectionID());
		assertNotNull(task.getTaskID());
		assertNotNull(task.getPriority());
		assertNotNull(task.getStartTime());
	}
	
	public void testComparison() {
		Task task1 = new Task(new UUID(1,1),new UUID(2,2),"Title",
				dueDate,"Blah","Blah",4,startTimeDate,0,false);
		Task task2 = new Task(new UUID(1,1),new UUID(2,2),"Title",
				dueDate,"Blah","Blah",6,startTimeDate,0,false);
		assertEquals(task1.compareTo(task2),-1);
		assertEquals(task2.compareTo(task1),1);
		assertEquals(task1.compareTo(task1),0);
		assertEquals(task2.compareTo(task2),0);
	}
	
	public void testCalculateRunningTimeValidData() {
		Calendar currentTime = Calendar.getInstance();
		Task task = new Task(new UUID(1,1),new UUID(2,2),"Title",
				dueDate,"Blah","Blah",4,startTimeDate,0,true);
		
		startTimeDate.set(2013,07,04,10,10,30);
		currentTime.set(2013,07,04,10,10,30);
		CurrentTime.createCurrentTime(currentTime);
		
		assertEquals(0,task.getRuntime());
		
		currentTime.set(2013,07,04,10,16,00);
		CurrentTime.createCurrentTime(currentTime);
		
		assertEquals(330000,task.getRuntime());
	}
	
	public void testCalculateRunningTimeOverdueDate() {
		Calendar currentTime = Calendar.getInstance();
		Task task = new Task(new UUID(1,1),new UUID(2,2),"Title",
				dueDate,"Blah","Blah",4,startTimeDate,0,true);
		
		startTimeDate.set(2013,07,04,10,10,30);
		currentTime.set(2013,07,04,10,10,29);
		CurrentTime.createCurrentTime(currentTime);
		
		assertTrue(task.isRunning());
		assertEquals(-1000,task.getRuntime());
		
		currentTime.set(2013,07,04,01,16,00);
		CurrentTime.createCurrentTime(currentTime);
		
		assertTrue(task.isRunning());
		assertEquals(-32070000,task.getRuntime());		
	}
	
	public void testCalculateRunningTimeSwitchingStates() {
		Calendar currentTime = Calendar.getInstance();
		Task task = new Task(new UUID(1,1),new UUID(2,2),"Title",
				dueDate,"Blah","Blah",4,startTimeDate,0,true);
		
		startTimeDate.set(2013,07,04,10,10,30);
		currentTime.set(2013,07,04,10,10,60);
		CurrentTime.createCurrentTime(currentTime);
		
		assertEquals(30000,task.getRuntime());
		
		task.switchState();
		assertFalse(task.isRunning());
		
		currentTime.set(2013,07,04,12,16,00);
		CurrentTime.createCurrentTime(currentTime);
		
		assertEquals(30000,task.getRuntime());
		
		task.switchState();
		assertTrue(task.isRunning());
		
		assertEquals(30000,task.getRuntime());
		
		currentTime.set(2013,07,04,12,18,00);
		CurrentTime.createCurrentTime(currentTime);
		
		assertEquals(150000,task.getRuntime());
	}
}
