package tests.persistence;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import tos.persistence.*;
import tos.objects.*;
import junit.framework.TestCase;

public class DBAccessOperationsTest extends TestCase{
	private DBAccessHSQL dbAccess;
	private Calendar dueDate;
	
	public void setUp() {
		dueDate = Calendar.getInstance();
		dueDate.set(2016,10,23,13,50,45);
		dbAccess = new DBAccessHSQL("TOS","HSQL");
		assertTrue(dbAccess.connectToDB());
		System.out.println("Unit Test using HSQL DB");
	}
	
	public void tearDown() {
		assertTrue(dbAccess.closeConnectionToDB());
		dueDate = null;
	}
	
	public void testSaveDataToTable() {
		Board newBoard = new Board("Default");
		UUID boardID = dbAccess.insertBoard(newBoard);
		assertTrue(dbAccess.isIDInDatabase(boardID));
		newBoard = dbAccess.getBoard(boardID);
		
		Board newBoard1 = new Board("Default1");
		boardID = dbAccess.insertBoard(newBoard1);
		assertTrue(dbAccess.isIDInDatabase(boardID));
		newBoard1 = dbAccess.getBoard(boardID);
		
		Board newBoard2 = new Board("Default2");
		boardID = dbAccess.insertBoard(newBoard2);
		assertTrue(dbAccess.isIDInDatabase(boardID));
		newBoard2 = dbAccess.getBoard(boardID);
		
		Section newSection = new Section("ToDo");
		UUID sectionID = dbAccess.insertSection(newSection, newBoard);
		assertTrue(dbAccess.isIDInDatabase(sectionID));
		newSection = dbAccess.getSection(sectionID);
		
		Section newSection1 = new Section("Test1");
		sectionID = dbAccess.insertSection(newSection1, newBoard2);
		assertTrue(dbAccess.isIDInDatabase(sectionID));
		newSection1 = dbAccess.getSection(sectionID);
		
		Section newSection2 = new Section("Test2");
		sectionID = dbAccess.insertSection(newSection2, newBoard2);
		assertTrue(dbAccess.isIDInDatabase(sectionID));
		newSection2 = dbAccess.getSection(sectionID);
		
		Section newSection3 = new Section("Test3");
		sectionID = dbAccess.insertSection(newSection3, newBoard1);
		assertTrue(dbAccess.isIDInDatabase(sectionID));
		newSection3 = dbAccess.getSection(sectionID);
		
		Task newTask = new Task("Make list of items for yard sale.",
				dueDate,"Details","Comments",0);
		UUID taskID = dbAccess.insertTask(newTask, newSection2);
		assertTrue(dbAccess.isIDInDatabase(taskID));
		newTask = dbAccess.getTask(taskID);
		
		Task newTask2 = new Task("Make list of items for donation.",
				dueDate,"Details","Comments",0);
		taskID = dbAccess.insertTask(newTask2, newSection);
		assertTrue(dbAccess.isIDInDatabase(taskID));
		newTask2 = dbAccess.getTask(taskID);
		
		newTask = new Task("Make list of items to pack for trip.",
				dueDate,"Details","Comments",0);
		taskID = dbAccess.insertTask(newTask, newSection);
		assertTrue(dbAccess.isIDInDatabase(taskID));
		
		dbAccess.deleteBoard(newBoard1.getBoardID());
		dbAccess.deleteBoard(newBoard2.getBoardID());
		dbAccess.deleteBoard(newBoard.getBoardID());
	}
	
	public void testNullSave() {
		Board newBoard = new Board("Default");
		Section newSection = new Section("ToDo");
		Task newTask = new Task("Make list of items for yard sale.",
				dueDate,"Details","Comments",0);

		UUID boardID = dbAccess.insertBoard(newBoard);
		newBoard = dbAccess.getBoard(boardID);
		UUID sectionID = dbAccess.insertSection(newSection, newBoard);
		newSection = dbAccess.getSection(sectionID);
		try {
			dbAccess.insertTask(newTask, null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.insertTask(null, newSection);
			fail("Failed to catch null pointer exception.");
		}
		catch(NullPointerException npe) {
			
		}
		
		try {
			dbAccess.insertTask(null, null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.insertSection(null, newBoard);
			fail("Failed to catch null pointer exception.");
		} catch(NullPointerException npe) {
			
		}
		
		try {
			dbAccess.insertSection(newSection, null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.insertSection(null, null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.insertBoard(null);
			fail("Failed to catch null pointer exception.");
		} catch(NullPointerException npe) {
			
		}
		
		try {
			dbAccess.insertTagForTask(new UUID(2,1), null);
			fail("Failed to catch null pointer exception.");
		} catch(IllegalArgumentException npe) {
			
		}
		
		dbAccess.deleteBoard(newBoard.getBoardID());
	}
	
	public void testInvalidData() {
		Board newBoard = new Board("Default");
		Task newTask = new Task("Make list of items for yard sale.",
				dueDate,"Details","Comments",0);
		Section newSection = new Section(new UUID(1,1),new UUID(8,2),
				"I don't exist in database.",false,0);
		try {
			dbAccess.insertTask(newTask, newSection);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.insertSection(newSection, newBoard);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.insertTagForTask(new UUID(2,1), "");
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.insertTagForTask(new UUID(9,9), "");
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testNullDelete() {
		try {
			dbAccess.deleteTask(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.deleteSection(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.deleteBoard(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.deleteTagForTask(null,new UUID(2,1));
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.deleteTagForTask(new UUID(7,1),null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testNullUpdate() {
		try {
			dbAccess.updateBoard(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.updateSection(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.updateTask(null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testDeleteOneExisting() {
		Board newBoard = new Board("Default");
		Section newSection = new Section("ToDo");
		Tag tag = new Tag("gnome");
		ArrayList<Tag> tagsForTask;
		Task newTask = new Task("Make list of items for yard sale.",
				dueDate,"Details","Comments",0);
		
		UUID boardID = dbAccess.insertBoard(newBoard);
		newBoard = dbAccess.getBoard(boardID);
		UUID sectionID = dbAccess.insertSection(newSection, newBoard);
		newSection = dbAccess.getSection(sectionID);
		UUID taskID = dbAccess.insertTask(newTask, newSection);
		newTask = dbAccess.getTask(taskID);
		dbAccess.insertTagForTask(taskID, tag.getTag());
		
		tagsForTask = dbAccess.getTagsForTask(taskID);
		dbAccess.deleteTagForTask(tagsForTask.get(0).getTagID(),taskID);
		tagsForTask = dbAccess.getTagsForTask(taskID);
		assertEquals(tagsForTask.size(),0);
		
		dbAccess.deleteTask(taskID);
		assertFalse(dbAccess.getAllTasks().contains(newTask));
		
		dbAccess.deleteSection(newSection.getSectionID());
		assertFalse(dbAccess.getAllSections().contains(newSection));
		
		dbAccess.deleteBoard(newBoard.getBoardID());
		assertFalse(dbAccess.getAllBoards().contains(newBoard));
	}
	
	public void testDeleteManyExisting() {
		Board[] boards = new Board[5];
		Section[] sections = new Section[5];
		Tag[] tags = new Tag[5];
		Task[] tasks = new Task[5]; 
		ArrayList<Tag> tagsForTask;
		
		for (int index=0; index < 5; index++) {
			tasks[index] = new Task("Make list of items for yard sale."
					,dueDate,"Details","Comments",0);
			boards[index] = new Board("Test Board");
			sections[index] = new Section("Test Section");
			tags[index] = new Tag("Tag #" + index);
			UUID boardID = dbAccess.insertBoard(boards[index]);
			boards[index] = dbAccess.getBoard(boardID);
			UUID sectionID = dbAccess.insertSection(sections[index], boards[index]);
			sections[index] = dbAccess.getSection(sectionID);
			UUID taskID = dbAccess.insertTask(tasks[index], sections[index]);
			tasks[index] = dbAccess.getTask(taskID);
			dbAccess.insertTagForTask(tasks[index].getTaskID(),
					tags[index].getTag());
		}
		
		for (int index=0; index < 5; index++) {
			tagsForTask = dbAccess.getTagsForTask(tasks[index].getTaskID());
			
			for (int tagindex=0; tagindex < tagsForTask.size(); tagindex++) {
				dbAccess.deleteTagForTask(tagsForTask.get(tagindex).getTagID(),
						tasks[index].getTaskID());
			}	
			assertEquals(dbAccess.getTagsForTask(
					tasks[index].getTaskID()).size(),0);
			
			dbAccess.deleteTask(tasks[index].getTaskID());
			assertFalse(dbAccess.getAllTasks().contains(tasks[index]));
			
			dbAccess.deleteSection(sections[index].getSectionID());
			assertFalse(dbAccess.getAllSections().contains(sections[index]));
			
			dbAccess.deleteBoard(boards[index].getBoardID());
			assertFalse(dbAccess.getAllBoards().contains(boards[index]));
		}
		
	}
	
	public void testOnDeleteCascadeManySectionsTasksOnBoard() {
		Board board = new Board("Default");
		Section[] section = new Section[5];
		Task[] task = new Task[5];
		
		section[0] = new Section("Section");
		section[1] = new Section("Section");
		section[2] = new Section("Section");
		section[3] = new Section("Section");
		section[4] = new Section("Section");
		
		task[0] = new Task("Task",dueDate,"Details","Comments",0);
		task[1] = new Task("Task",dueDate,"Details","Comments",0);
		task[2] = new Task("Task",dueDate,"Details","Comments",0);
		task[3] = new Task("Task",dueDate,"Details","Comments",0);
		task[4] = new Task("Task",dueDate,"Details","Comments",0);
		UUID boardID = dbAccess.insertBoard(board);
		board = dbAccess.getBoard(boardID);
		UUID sectionID = dbAccess.insertSection(section[0], board);
		section[0] = dbAccess.getSection(sectionID);
		sectionID = dbAccess.insertSection(section[1], board);
		section[1] = dbAccess.getSection(sectionID);
		sectionID = dbAccess.insertSection(section[2], board);
		section[2] = dbAccess.getSection(sectionID);
		sectionID = dbAccess.insertSection(section[3], board);
		section[3] = dbAccess.getSection(sectionID);
		sectionID = dbAccess.insertSection(section[4], board);
		section[4] = dbAccess.getSection(sectionID);
		dbAccess.insertTask(task[0], section[0]);
		dbAccess.insertTask(task[1], section[2]);
		dbAccess.insertTask(task[2], section[0]);
		dbAccess.insertTask(task[3], section[2]);
		dbAccess.insertTask(task[4], section[3]);
		dbAccess.deleteBoard(board.getBoardID());
		assertFalse(dbAccess.getAllBoards().contains(board));
		assertFalse(dbAccess.getAllSections().contains(section[0]));
		assertFalse(dbAccess.getAllSections().contains(section[1]));
		assertFalse(dbAccess.getAllSections().contains(section[2]));
		assertFalse(dbAccess.getAllSections().contains(section[3]));
		assertFalse(dbAccess.getAllSections().contains(section[4]));
		assertFalse(dbAccess.getAllTasks().contains(task[0]));
		assertFalse(dbAccess.getAllTasks().contains(task[1]));
		assertFalse(dbAccess.getAllTasks().contains(task[2]));
		assertFalse(dbAccess.getAllTasks().contains(task[3]));
		assertFalse(dbAccess.getAllTasks().contains(task[4]));
		assertFalse(dbAccess.getAllTags().contains("blah"));
		assertFalse(dbAccess.getAllTags().contains("blah2"));
		assertFalse(dbAccess.getAllTags().contains("blah3"));
	}	
	
	public void testUpdateDeleteNonExistingData() {
		Board newBoard = new Board("Default");
		Section newSection = new Section(
				"This section doesn't exist in the database.");
		Task newTask = new Task(
				"This task doesn't exist in the database.",dueDate,
				"Details","Comments",0);
		
		try {
			dbAccess.deleteTask(newTask.getTaskID());
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.deleteSection(newSection.getSectionID());
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.deleteBoard(newBoard.getBoardID());
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.updateBoard(newBoard);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.updateSection(newSection);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			dbAccess.updateTask(newTask);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}		
		
		try {
			dbAccess.deleteTagForTask(new UUID(2,1), new UUID(9,9));
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testUpdateExistingData() {
		Board newBoard = new Board("Default");
		Section newSection = new Section("ToDo");
		Task newTask = new Task("Make list of items for yard sale.",
				dueDate,"Details","Comments",0);
		
		UUID boardID = dbAccess.insertBoard(newBoard);
		newBoard = dbAccess.getBoard(boardID);
		UUID sectionID = dbAccess.insertSection(newSection, newBoard);
		newSection = dbAccess.getSection(sectionID);
		UUID taskID = dbAccess.insertTask(newTask, newSection);
		newTask = dbAccess.getTask(taskID);
		
		newTask.setTitle("Make list of items for donation.");
		dbAccess.updateTask(newTask);
		Task updatedTask = dbAccess.getTask(newTask.getTaskID());
		assertEquals(updatedTask.getTitle(),"Make list of items for donation.");
		
		newSection.setTitle("Work In Progress.");
		dbAccess.updateSection(newSection);
		Section updatedSection = dbAccess.getSection(newSection.getSectionID());
		assertEquals(updatedSection.getTitle(),"Work In Progress.");
		
		newBoard.setName("Not Default.");
		dbAccess.updateBoard(newBoard);
		Board updatedBoard = dbAccess.getBoard(newBoard.getBoardID());
		assertEquals(updatedBoard.getName(),"Not Default.");
		
		dbAccess.updateTask(newTask);
		updatedTask = dbAccess.getTask(newTask.getTaskID());
		
		dbAccess.deleteBoard(newBoard.getBoardID());
	}
	
	public void testInterleaveInsertUpdateDeleteData() {
		Task updatedTask;
		Section updatedSection;
		Board updatedBoard;
		Board newBoard = new Board("Default");
		UUID boardID = dbAccess.insertBoard(newBoard);
		newBoard = dbAccess.getBoard(boardID);
		assertTrue(dbAccess.isIDInDatabase(newBoard.getBoardID()));
		
		Section newSection = new Section("ToDo");
		UUID sectionID = dbAccess.insertSection(newSection, newBoard);
		newSection = dbAccess.getSection(sectionID);
		assertTrue(dbAccess.isIDInDatabase(newSection.getSectionID()));
		
		newBoard.setName("Test");
		dbAccess.updateBoard(newBoard);
		updatedBoard = dbAccess.getBoard(newBoard.getBoardID());
		assertEquals(updatedBoard.getName(),"Test");
		
		Task newTask = new Task("Make list of items for yard sale.",
				dueDate,"Details","Comments",0);
		UUID taskID = dbAccess.insertTask(newTask, newSection);
		newTask = dbAccess.getTask(taskID);
		assertTrue(dbAccess.isIDInDatabase(newTask.getTaskID()));
		
		newTask = new Task("Make list of items for donation.",
				dueDate,"Details","Comments",0);
		taskID = dbAccess.insertTask(newTask, newSection);
		newTask = dbAccess.getTask(taskID);
		assertTrue(dbAccess.isIDInDatabase(newTask.getTaskID()));
		
		newSection.setTitle("Blah");
		dbAccess.updateSection(newSection);
		updatedSection = dbAccess.getSection(newSection.getSectionID());
		assertEquals(updatedSection.getTitle(),"Blah");
		
		newTask.setTitle("Make list of items for donation.");
		dbAccess.updateTask(newTask);
		updatedTask = dbAccess.getTask(newTask.getTaskID());
		assertEquals(updatedTask.getTitle(),"Make list of items for donation.");
		
		dbAccess.deleteTask(newTask.getTaskID());
		assertFalse(dbAccess.getAllTasks().contains(newTask));
		
		newTask = new Task("Make list of items to pack for trip.",
				dueDate,"Details","Comments",0);
		taskID = dbAccess.insertTask(newTask, newSection);
		newTask = dbAccess.getTask(taskID);
		assertTrue(dbAccess.isIDInDatabase(newTask.getTaskID()));
		
		newTask.setTitle("Make list of items for donation.");
		dbAccess.updateTask(newTask);
		updatedTask = dbAccess.getTask(newTask.getTaskID());
		assertEquals(updatedTask.getTitle(),"Make list of items for donation.");
		
		newTask.setTitle("Make list of items for charity.");
		dbAccess.updateTask(newTask);
		updatedTask = dbAccess.getTask(newTask.getTaskID());
		assertEquals(updatedTask.getTitle(),"Make list of items for charity.");
		
		dbAccess.deleteTask(newTask.getTaskID());
		assertFalse(dbAccess.isIDInDatabase(newTask.getTaskID()));
		
		dbAccess.deleteBoard(newBoard.getBoardID());
	}
	
	public void testGenerateUUIDThatIsNotInDatabase() {
		UUID newID = UUID.fromString("422ff617-1280-43ab-9641-653d46320555");
		assertFalse(dbAccess.isIDInDatabase(newID));

		newID = UUID.fromString("422ff617-1280-43ab-9641-653d46320444");
		assertFalse(dbAccess.isIDInDatabase(newID));

		newID = UUID.fromString("4aaaa617-1280-43ab-9641-653d46320555");
		assertFalse(dbAccess.isIDInDatabase(newID));
		
		newID = UUID.fromString("422ff617-1280-43ab-9641-653d4abcd555");
		assertFalse(dbAccess.isIDInDatabase(newID));
		
		newID = UUID.fromString("422ff617-1280-43ab-7458-653d46320555");
		assertFalse(dbAccess.isIDInDatabase(newID));
		
		newID = UUID.fromString("422ff617-1562-888b-9641-653d46320555");
		assertFalse(dbAccess.isIDInDatabase(newID));
		
		newID = UUID.fromString("78954235-1280-43ab-9641-653d46320555");
		assertFalse(dbAccess.isIDInDatabase(newID));
		
		newID = UUID.fromString("422ff617-1280-43ab-9641-456212357489");
		assertFalse(dbAccess.isIDInDatabase(newID));
	}
}
