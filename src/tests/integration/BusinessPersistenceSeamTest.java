package tests.integration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import tos.application.DBService;
import tos.business.AccessBoards;
import tos.business.AccessSections;
import tos.business.AccessTasks;
import tos.objects.Board;
import tos.objects.Section;
import tos.objects.Tag;
import tos.objects.Task;
import tos.persistence.DBAccessHSQL;
import junit.framework.TestCase;

public class BusinessPersistenceSeamTest extends TestCase {
	private AccessSections accessSections;
	private AccessBoards accessBoards;
	private AccessTasks accessTasks;
	private Calendar dueDate;
	
	public void setUp() {
	  	DBService.createDataAccess(new DBAccessHSQL("TOS","HSQL"));
		dueDate = Calendar.getInstance();
		dueDate.set(2016,10,23,13,50,45);
		accessBoards = new AccessBoards();
		accessSections = new AccessSections();
		accessTasks = new AccessTasks();
		System.out.println("Integration Test Using HSQL Database");
	}
	
	public void tearDown() {
		DBService.closeDataAccess();
	}
	
	public void testInterleavingBoardOperations() {
		Board[] boards = new Board[3];
		boards[0] = new Board("Board 1");
		boards[1] = new Board("Board 2");
		boards[2] = new Board("Board 3");
		UUID boardID = accessBoards.createBoard(boards[0]);
		boards[0] = accessBoards.getBoard(boardID);
		boardID = accessBoards.createBoard(boards[1]);
		boards[1] = accessBoards.getBoard(boardID);
		boardID = accessBoards.createBoard(boards[2]);
		assertTrue(accessBoards.isBoardInDatabase(boards[1].getBoardID()));
		accessBoards.deleteBoard(boards[1].getBoardID());
		assertFalse(accessBoards.isBoardInDatabase(boards[1].getBoardID()));	
		
		boards[2] = accessBoards.getBoard(boardID);
		assertTrue(accessBoards.isBoardInDatabase(boards[0].getBoardID()));
		assertTrue(accessBoards.isBoardInDatabase(boards[2].getBoardID()));
		
		accessBoards.deleteBoard(boards[0].getBoardID());
		assertFalse(accessBoards.isBoardInDatabase(boards[0].getBoardID()));
		accessBoards.deleteBoard(boards[2].getBoardID());
		assertFalse(accessBoards.isBoardInDatabase(boards[2].getBoardID()));
	}
	
	public void testInterleavingSectionOperations() {
		Board newBoard = new Board("Test Board");
		Section[] newSection = new Section[8];
		ArrayList<Section> sectionsOnBoard;
		UUID boardID = accessBoards.createBoard(newBoard);
		newBoard = accessBoards.getBoard(boardID);
		
		newSection[0] = new Section("Test Section 1");
		newSection[0].setSortOrder(0);
		UUID sectionID = accessSections.createSection(newSection[0], newBoard);
		newSection[0] = accessSections.getSection(sectionID);
		assertEquals(1,accessSections.getSectionsOnBoard(
		newBoard.getBoardID()).size());
		sectionsOnBoard = accessSections.getSectionsOnBoard(newBoard.getBoardID()); 
		assertEquals(sectionsOnBoard.get(0).compareTo(newSection[0]),0);
		
		newSection[1] = new Section("Test Section 2");
		newSection[1].setSortOrder(1);
		sectionID = accessSections.createSection(newSection[1], newBoard);
		newSection[1] = accessSections.getSection(sectionID);
		assertEquals(2,accessSections.getSectionsOnBoard(
		newBoard.getBoardID()).size());
		sectionsOnBoard = accessSections.getSectionsOnBoard(newBoard.getBoardID()); 
		assertEquals(sectionsOnBoard.get(1).compareTo(newSection[1]),0);
		
		int s1_so = newSection[0].getSortOrder();
		int s2_so = newSection[1].getSortOrder();
		accessSections.switchSections(newSection[0].getSectionID(), newSection[1].getSectionID());
		newSection[0] = accessSections.getSection(newSection[0].getSectionID());
		newSection[1] = accessSections.getSection(newSection[1].getSectionID());
		assertEquals(newSection[0].getSortOrder(),s2_so);
		assertEquals(newSection[1].getSortOrder(),s1_so);		
		
		accessSections.deleteSection(newSection[0].getSectionID());
		assertFalse(accessSections.getSectionsOnBoard(
		newBoard.getBoardID()).contains(newSection[0]));
		
		newSection[1].setTitle("Updated Section 1");
		accessSections.updateSection(newSection[1]);
		newSection[1] = accessSections.getSection(newSection[1].getSectionID());
		assertEquals(newSection[1].getTitle(),"Updated Section 1");
		
		newSection[2] = new Section("Test Section 3");
		newSection[2].setSortOrder(2);
		sectionID = accessSections.createSection(newSection[2], newBoard);
		newSection[2] = accessSections.getSection(sectionID);
		assertEquals(2,accessSections.getSectionsOnBoard(
		newBoard.getBoardID()).size());
		sectionsOnBoard = accessSections.getSectionsOnBoard(newBoard.getBoardID()); 
		assertEquals(sectionsOnBoard.get(1).compareTo(newSection[2]),0);
		
		accessSections.deleteSection(newSection[1].getSectionID());
		assertFalse(accessSections.getSectionsOnBoard(
		newBoard.getBoardID()).contains(newSection[1]));
		accessSections.deleteSection(newSection[2].getSectionID());
		assertFalse(accessSections.getSectionsOnBoard(
		newBoard.getBoardID()).contains(newSection[2]));
		
		accessBoards.deleteBoard(newBoard.getBoardID());				
	}
	
	public void testInterleavingTaskSectionBoardOperations() {
		Task[] tasks = new Task[8];
		Task updatedTask = null;
		Section newSection = new Section("Test Section");
		Section newSection2 = new Section("Test Section");
		Board newBoard = new Board("Test Board");
		ArrayList<Tag> tags = new ArrayList<Tag>();
		ArrayList<Task> tasksInSection;
		UUID boardID = accessBoards.createBoard(newBoard);		
		newBoard = accessBoards.getBoard(boardID);
		UUID sectionID = accessSections.createSection(newSection, newBoard);
		newSection = accessSections.getSection(sectionID);
		sectionID = accessSections.createSection(newSection2, newBoard);
		newSection2 = accessSections.getSection(sectionID);
		
		tasks[0] = new Task("Test Task 1",dueDate,"Details","Comments",0);
		UUID taskID = accessTasks.createTask(tasks[0], newSection, tags);
		tasks[0] = accessTasks.getTask(taskID);
		tasksInSection = accessTasks.getTasksInSection(newSection.getSectionID());
		assertEquals(tasksInSection.get(0).compareTo(tasks[0]),0);
		tasks[0].setTitle("Test Blah 1");
		accessTasks.updateTask(tasks[0]);
		updatedTask = accessTasks.getTask(tasks[0].getTaskID());
		assertEquals(updatedTask.getTitle(),"Test Blah 1");
		
		tags.add(new Tag("#2012"));
		accessTasks.updateTask(tasks[0], tags);
		assertEquals("#2012", accessTasks.getTags(
				tasks[0].getTaskID()).get(0).getTag());
		
		accessTasks.setTaskSection(tasks[0], newSection2);
		tasksInSection = accessTasks.getTasksInSection(newSection2.getSectionID());
		assertEquals(tasksInSection.get(0).compareTo(tasks[0]),0);
		
		accessTasks.setTaskSection(tasks[0], newSection);
		tasksInSection = accessTasks.getTasksInSection(newSection.getSectionID());
		assertEquals(tasksInSection.get(0).compareTo(tasks[0]),0);
		
		tasks[1] = new Task("Test Task 2",dueDate,"Details","Comments",0);
		taskID = accessTasks.createTask(tasks[1], newSection, tags);
		tasks[1] = accessTasks.getTask(taskID);
		tasksInSection = accessTasks.getTasksInSection(newSection.getSectionID());
		assertEquals(tasksInSection.get(1).compareTo(tasks[1]),0);
		
		accessTasks.setFilterRule(tags);
		tasksInSection = accessTasks.getTasksInSection(newSection.getSectionID());
		assertEquals(2,tasksInSection.size());
		accessTasks.setFilterRule(null);
		
		assertFalse(accessTasks.swapTaskPriority(tasks[0], -1));
		assertTrue(accessTasks.swapTaskPriority(tasks[0], 1));
		assertTrue(accessTasks.swapTaskPriority(tasks[1], 1));
		
		newSection.setTitle("Updated Section 1");
		accessSections.updateSection(newSection);
		newSection = accessSections.getSection(newSection.getSectionID());
		assertEquals(newSection.getTitle(),"Updated Section 1");
		
		accessTasks.deleteTask(tasks[0].getTaskID(), new ArrayList<Tag>());
		assertFalse(accessTasks.getTasksInSection(
		newSection.getSectionID()).contains(tasks[0]));
		
		tasks[1].setTitle("Test Blah 2");
		accessTasks.updateTask(tasks[1]);
		updatedTask = accessTasks.getTask(tasks[1].getTaskID());
		assertEquals(updatedTask.getTitle(),"Test Blah 2");
		
		tasks[2] = new Task("Test Task 3",dueDate,"Details","Comments",0);
		taskID = accessTasks.createTask(tasks[2], newSection2, tags);
		tasks[2] = accessTasks.getTask(taskID);
		tasksInSection = accessTasks.getTasksInSection(newSection2.getSectionID());
		assertEquals(tasksInSection.get(0).compareTo(tasks[2]),0);
		tasks[2].setTitle("Test Blah 3");
		accessTasks.updateTask(tasks[2]);
		updatedTask = accessTasks.getTask(tasks[2].getTaskID());
		assertEquals(updatedTask.getTitle(),"Test Blah 3");
		
		int s1_so = newSection.getSortOrder();
		int s2_so = newSection2.getSortOrder();
		accessSections.switchSections(newSection.getSectionID(), newSection2.getSectionID());
		newSection = accessSections.getSection(newSection.getSectionID());
		newSection2 = accessSections.getSection(newSection2.getSectionID());
		assertEquals(newSection.getSortOrder(),s2_so);
		assertEquals(newSection2.getSortOrder(),s1_so);			

		accessTasks.setTaskSection(tasks[1], newSection2);
		tasksInSection = accessTasks.getTasksInSection(newSection2.getSectionID());
		assertEquals(tasksInSection.get(1).compareTo(tasks[1]),0);
		
		accessTasks.setTaskSection(tasks[2], newSection);
		tasksInSection = accessTasks.getTasksInSection(newSection.getSectionID());
		assertEquals(tasksInSection.get(0).compareTo(tasks[2]),0);
		
		accessTasks.deleteTask(tasks[1].getTaskID(), new ArrayList<Tag>());
		assertFalse(accessTasks.getTasksInSection(
		newSection.getSectionID()).contains(tasks[1]));
		accessTasks.deleteTask(tasks[2].getTaskID(), new ArrayList<Tag>());
		assertFalse(accessTasks.getTasksInSection(
		newSection.getSectionID()).contains(tasks[2]));	
		
		accessBoards.deleteBoard(newBoard.getBoardID());
	}
}
