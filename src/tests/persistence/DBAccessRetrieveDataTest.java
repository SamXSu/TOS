package tests.persistence;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import junit.framework.TestCase;
import tos.objects.*;
import tos.persistence.DBAccessHSQL;

public class DBAccessRetrieveDataTest extends TestCase {
	private DBAccessHSQL dbAccess;
	private Calendar dueDate;
	
	public void setUp() {				
		dbAccess = new DBAccessHSQL("TOS","HSQL");
		dueDate = Calendar.getInstance();
		dueDate.set(2016,10,23,13,50,45);
		dbAccess.connectToDB();
		System.out.println("Unit Test using HSQL DB");
	}
	
	public void tearDown() {	  	
		dbAccess.closeConnectionToDB();
	}
	
	public void testRetrieveListContainingZeroEntries() {
		Board newBoard = new Board("Board 1");
		Section newSection = new Section("Section with No Tasks.");
		UUID boardID = dbAccess.insertBoard(newBoard);
		newBoard = dbAccess.getBoard(boardID);
		UUID sectionID = dbAccess.insertSection(newSection, newBoard);
		newSection = dbAccess.getSection(sectionID);
		
		assertEquals(new ArrayList<Tag>(),dbAccess.getTagsForTask(new UUID(2,0)));
		assertEquals(new ArrayList<Task>(),dbAccess.getTasksInSectionSorted(newSection.getSectionID()));
		
		dbAccess.deleteSection(newSection.getSectionID());
		assertEquals(new ArrayList<Task>(),dbAccess.getSectionsInBoardSorted(newBoard.getBoardID()));
		
		dbAccess.deleteBoard(newBoard.getBoardID());
	}
	
	public void testRetrieveListContainingOneOrMoreTasks() {
		Board newBoard = new Board("Board 1");
		Section newSection = new Section("Section 1");
		Task[] newTask = new Task[5];
		ArrayList<Task> tasksInSection = new ArrayList<Task>();
		ArrayList<Task> dbTasksInSection;
		
		UUID boardID = dbAccess.insertBoard(newBoard);
		newBoard = dbAccess.getBoard(boardID);
		UUID sectionID = dbAccess.insertSection(newSection, newBoard);
		newSection = dbAccess.getSection(sectionID);
		
		newTask[0] = new Task("Task 1",dueDate,"Details","Comments",0);
		UUID taskID = dbAccess.insertTask(newTask[0], newSection);
		newTask[0] = dbAccess.getTask(taskID);
		tasksInSection.add(newTask[0]);
		dbTasksInSection = dbAccess.getTasksInSectionSorted(newSection.getSectionID());
		assertEquals(tasksInSection.get(0).compareTo(dbTasksInSection.get(0)),0);
		
		newTask[1] = new Task("Task 2",dueDate,"Details","Comments",0);
		taskID = dbAccess.insertTask(newTask[1], newSection);
		newTask[1] = dbAccess.getTask(taskID);
		tasksInSection.add(newTask[1]);
		dbTasksInSection = dbAccess.getTasksInSectionSorted(newSection.getSectionID());
		assertEquals(tasksInSection.get(0).compareTo(dbTasksInSection.get(0)),0);
		assertEquals(tasksInSection.get(1).compareTo(dbTasksInSection.get(1)),0);
		
		newTask[2] = new Task("Task 3",dueDate,"Details","Comments",0);
		tasksInSection.add(newTask[2]);
		taskID = dbAccess.insertTask(newTask[2], newSection);
		newTask[2] = dbAccess.getTask(taskID);
		dbTasksInSection = dbAccess.getTasksInSectionSorted(newSection.getSectionID());
		assertEquals(tasksInSection.get(0).compareTo(dbTasksInSection.get(0)),0);
		assertEquals(tasksInSection.get(1).compareTo(dbTasksInSection.get(1)),0);
		assertEquals(tasksInSection.get(2).compareTo(dbTasksInSection.get(2)),0);
		
		newTask[3] = new Task("Task 4",dueDate,"Details","Comments",0);
		tasksInSection.add(newTask[3]);
		taskID = dbAccess.insertTask(newTask[3], newSection);
		newTask[3] = dbAccess.getTask(taskID);
		dbTasksInSection = dbAccess.getTasksInSectionSorted(newSection.getSectionID());
		assertEquals(tasksInSection.get(0).compareTo(dbTasksInSection.get(0)),0);
		assertEquals(tasksInSection.get(1).compareTo(dbTasksInSection.get(1)),0);
		assertEquals(tasksInSection.get(2).compareTo(dbTasksInSection.get(2)),0);
		assertEquals(tasksInSection.get(3).compareTo(dbTasksInSection.get(3)),0);
		
		newTask[4] = new Task("Task 5",dueDate,"Details","Comments",0);
		tasksInSection.add(newTask[4]);
		taskID = dbAccess.insertTask(newTask[4], newSection);
		newTask[4] = dbAccess.getTask(taskID);
		dbTasksInSection = dbAccess.getTasksInSectionSorted(newSection.getSectionID());
		assertEquals(tasksInSection.get(0).compareTo(dbTasksInSection.get(0)),0);
		assertEquals(tasksInSection.get(1).compareTo(dbTasksInSection.get(1)),0);
		assertEquals(tasksInSection.get(2).compareTo(dbTasksInSection.get(2)),0);
		assertEquals(tasksInSection.get(3).compareTo(dbTasksInSection.get(3)),0);
		assertEquals(tasksInSection.get(4).compareTo(dbTasksInSection.get(4)),0);
		
		dbAccess.deleteBoard(newBoard.getBoardID());
	}
	
	public void testRetrieveExistingTaskBoardSectionTag() {
		assertNotNull(dbAccess.getTask(new UUID(2,6)));
		assertNotNull(dbAccess.getSection(new UUID(5,1)));
		assertNotNull(dbAccess.getBoard(new UUID(1,0)));
		assertNotNull(dbAccess.getTagsForTask(new UUID(2,6)));
	}
	
	public void testRetrieveListContainingAllGeneratedData() {
		ArrayList<Task> tasks = dbAccess.getAllTasks();
		ArrayList<Section> sections = dbAccess.getAllSections();
		ArrayList<Board> boards = dbAccess.getAllBoards();
		ArrayList<Tag> tags = dbAccess.getAllTags();
		
		assertEquals(tasks.get(0).getTaskID(),new UUID(2,0));
		assertEquals(tasks.get(0).getSectionID(),new UUID(5,0));
		assertEquals(tasks.get(0).getTitle(),"Collapse on ground");
		assertEquals(tasks.get(0).getDetail(),"Details");
		assertEquals(tasks.get(0).getComment(),"Comments");
		assertEquals(tasks.get(0).getPriority(),0);
		assertEquals(tasks.get(1).getTaskID(),new UUID(2,1));
		assertEquals(tasks.get(1).getSectionID(),new UUID(5,0));
		assertEquals(tasks.get(1).getTitle(),"Step 3: Profit!");
		assertEquals(tasks.get(1).getPriority(),1);
		assertEquals(tasks.get(1).getDetail(),"-------------------------------------------" +
				"---------------------------------------------------------------------------");
		assertEquals(tasks.get(1).getComment(),"Comments");
		assertEquals(tasks.get(2).getTaskID(),new UUID(2,2));
		assertEquals(tasks.get(2).getSectionID(),new UUID(5,0));
		assertEquals(tasks.get(2).getTitle(),"Takeover Greenland");
		assertEquals(tasks.get(2).getDetail(),"Details");
		assertEquals(tasks.get(2).getComment(),"Comments");
		assertEquals(tasks.get(2).getPriority(),2);
		assertEquals(tasks.get(3).getSectionID(),new UUID(5,1));
		assertEquals(tasks.get(3).getTitle(),"Drink a gallon of water");
		assertEquals(tasks.get(3).getDetail(),"Details");
		assertEquals(tasks.get(3).getComment(),"Comments");
		assertEquals(tasks.get(3).getPriority(),0);
		assertEquals(tasks.get(4).getTaskID(),new UUID(2,4));
		assertEquals(tasks.get(4).getSectionID(),new UUID(5,1));
		assertEquals(tasks.get(4).getTitle(),"Build a trebuchet");
		assertEquals(tasks.get(4).getDetail(),"Details");
		assertEquals(tasks.get(4).getComment(),"Comments");
		assertEquals(tasks.get(4).getPriority(),1);
		assertEquals(tasks.get(5).getTaskID(),new UUID(2,5));
		assertEquals(tasks.get(5).getSectionID(),new UUID(5,1));
		assertEquals(tasks.get(5).getTitle(),"Step 2: ??????");
		assertEquals(tasks.get(5).getDetail(),"Details");
		assertEquals(tasks.get(5).getComment(),"Comments");
		assertEquals(tasks.get(5).getPriority(),2);
		assertEquals(tasks.get(6).getTaskID(),new UUID(2,6));
		assertEquals(tasks.get(6).getSectionID(),new UUID(5,2));
		assertEquals(tasks.get(6).getTitle(),"Run half-marathon");
		assertEquals(tasks.get(6).getDetail(),"Details");
		assertEquals(tasks.get(6).getComment(),"Comments");
		assertEquals(tasks.get(6).getPriority(),0);
		assertEquals(tasks.get(7).getTaskID(),new UUID(2,7));
		assertEquals(tasks.get(7).getSectionID(),new UUID(5,2));
		assertEquals(tasks.get(7).getTitle(),"Design a trebuchet");
		assertEquals(tasks.get(7).getDetail(),"Details");
		assertEquals(tasks.get(7).getComment(),"Comments");
		assertEquals(tasks.get(7).getPriority(),1);
		assertEquals(tasks.get(8).getTaskID(),new UUID(2,8));
		assertEquals(tasks.get(8).getSectionID(),new UUID(5,2));
		assertEquals(tasks.get(8).getTitle(),"Step 1: Steal underpants");
		assertEquals(tasks.get(8).getDetail(),"Details");
		assertEquals(tasks.get(8).getComment(),"Comments");
		assertEquals(tasks.get(8).getPriority(),2);
		
		assertEquals(sections.get(0).getSectionID(),new UUID(5,0));
		assertEquals(sections.get(0).getBoardID(),new UUID(1,0));
		assertEquals(sections.get(0).getSortOrder(),0);
		assertEquals(sections.get(0).getTitle(),"To-do");
		assertEquals(sections.get(1).getSectionID(),new UUID(5,1));
		assertEquals(sections.get(1).getBoardID(),new UUID(1,0));
		assertEquals(sections.get(1).getTitle(),"Work In Progress");
		assertEquals(sections.get(1).getSortOrder(),1);
		assertEquals(sections.get(2).getSectionID(),new UUID(5,2));
		assertEquals(sections.get(2).getBoardID(),new UUID(1,0));
		assertEquals(sections.get(2).getTitle(),"Done");
		assertEquals(sections.get(2).getSortOrder(),2);
		assertEquals(sections.get(3).getSectionID(),new UUID(5,3));
		assertEquals(sections.get(3).getBoardID(),new UUID(1,0));
		assertEquals(sections.get(3).getTitle(),"sec1");
		assertEquals(sections.get(3).getSortOrder(),3);
		assertEquals(sections.get(4).getSectionID(),new UUID(5,4));
		assertEquals(sections.get(4).getBoardID(),new UUID(1,0));
		assertEquals(sections.get(4).getTitle(),"sec2");
		assertEquals(sections.get(4).getSortOrder(),4);
		
		assertEquals(boards.get(0).getName(),"Default");
		assertEquals(boards.get(0).getBoardID(),new UUID(1,0));
		
		assertEquals(tags.get(0).getTag(),"gnomes");
		assertEquals(tags.get(0).getTagID(),new UUID(7,1));
		
		assertTrue(dbAccess.isIDInDatabase(new UUID(2,0)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(2,1)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(2,2)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(2,3)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(2,4)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(2,5)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(2,6)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(2,7)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(2,8)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(1,0)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(5,0)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(5,1)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(5,2)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(5,3)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(5,4)));
		assertTrue(dbAccess.isIDInDatabase(new UUID(7,1)));
	}
	
	public void testRetrieveListContainingOneOrMoreSections() {
		Board newBoard = new Board("Board 1");
		Section[] newSection = new Section[5];
		ArrayList<Section> sectionsInBoard = new ArrayList<Section>();
		ArrayList<Section> dbSectionsInBoard;
		
		UUID boardID = dbAccess.insertBoard(newBoard);
		newBoard = dbAccess.getBoard(boardID);
		
		newSection[0] = new Section("Section 1");
		sectionsInBoard.add(newSection[0]);
		UUID sectionID = dbAccess.insertSection(newSection[0], newBoard);
		newSection[0] = dbAccess.getSection(sectionID);
		dbSectionsInBoard = dbAccess.getSectionsInBoardSorted(newBoard.getBoardID());
		assertEquals(sectionsInBoard.get(0).compareTo(dbSectionsInBoard.get(0)),0);
		
		newSection[1] = new Section("Section 2");
		sectionsInBoard.add(newSection[1]);
		sectionID = dbAccess.insertSection(newSection[1], newBoard);
		newSection[1] = dbAccess.getSection(sectionID);
		dbSectionsInBoard = dbAccess.getSectionsInBoardSorted(newBoard.getBoardID());
		assertEquals(sectionsInBoard.get(0).compareTo(dbSectionsInBoard.get(0)),0);
		assertEquals(sectionsInBoard.get(1).compareTo(dbSectionsInBoard.get(1)),0);
		
		newSection[2] = new Section("Section 3");
		sectionsInBoard.add(newSection[2]);
		sectionID = dbAccess.insertSection(newSection[2], newBoard);
		newSection[2] = dbAccess.getSection(sectionID);
		dbSectionsInBoard = dbAccess.getSectionsInBoardSorted(newBoard.getBoardID());
		assertEquals(sectionsInBoard.get(0).compareTo(dbSectionsInBoard.get(0)),0);
		assertEquals(sectionsInBoard.get(1).compareTo(dbSectionsInBoard.get(1)),0);
		assertEquals(sectionsInBoard.get(2).compareTo(dbSectionsInBoard.get(2)),0);
		
		newSection[3] = new Section("Section 4");
		sectionsInBoard.add(newSection[3]);
		sectionID = dbAccess.insertSection(newSection[3], newBoard);
		newSection[3] = dbAccess.getSection(sectionID);
		dbSectionsInBoard = dbAccess.getSectionsInBoardSorted(newBoard.getBoardID());
		assertEquals(sectionsInBoard.get(0).compareTo(dbSectionsInBoard.get(0)),0);
		assertEquals(sectionsInBoard.get(1).compareTo(dbSectionsInBoard.get(1)),0);
		assertEquals(sectionsInBoard.get(2).compareTo(dbSectionsInBoard.get(2)),0);
		assertEquals(sectionsInBoard.get(3).compareTo(dbSectionsInBoard.get(3)),0);
		
		newSection[4] = new Section("Section 5");
		sectionsInBoard.add(newSection[4]);
		sectionID = dbAccess.insertSection(newSection[4], newBoard);
		newSection[4] = dbAccess.getSection(sectionID);
		dbSectionsInBoard = dbAccess.getSectionsInBoardSorted(newBoard.getBoardID());
		assertEquals(sectionsInBoard.get(0).compareTo(dbSectionsInBoard.get(0)),0);
		assertEquals(sectionsInBoard.get(1).compareTo(dbSectionsInBoard.get(1)),0);
		assertEquals(sectionsInBoard.get(2).compareTo(dbSectionsInBoard.get(2)),0);
		assertEquals(sectionsInBoard.get(3).compareTo(dbSectionsInBoard.get(3)),0);
		assertEquals(sectionsInBoard.get(4).compareTo(dbSectionsInBoard.get(4)),0);
		
		dbAccess.deleteBoard(newBoard.getBoardID());
	}
	
	public void testRetrieveMethodsWithNullArguments() {		
		try {
			dbAccess.getTasksInSectionSorted(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.getSectionsInBoardSorted(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.getSection(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.getTask(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.getBoard(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.getTagsForTask(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testRetrieveNonExistantData() {
		Board newBoard = new Board("Board doesn't exist in the database.");
		Section newSection = new Section("Section that doesn't exist in the database.");
		
		try {
			assertEquals(new ArrayList<Task>(),dbAccess.getTasksInSectionSorted(newSection.getSectionID()));
			fail("Should have thrown illegal argument exception.");
		}
		catch (IllegalArgumentException iae) {
			
		}
		
		try {
			assertEquals(new ArrayList<Task>(),dbAccess.getSectionsInBoardSorted(newBoard.getBoardID()));
			fail("Should have thrown illegal argument exception.");
		}
		catch (IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.getSection(new UUID(9,9));
			fail("Should have thrown illegal argument exception.");
		}
		catch (IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.getBoard(new UUID(9,9));
			fail("Should have thrown illegal argument exception.");
		}
		catch (IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.getTask(new UUID(9,9));
			fail("Should have thrown illegal argument exception.");
		}
		catch (IllegalArgumentException iae) {
			
		}	
		
		try {
			dbAccess.getTagsForTask(new UUID(9,9));
			fail("Should have thrown illegal argument exception.");
		}
		catch (IllegalArgumentException iae) {
			
		}	
	}
}
