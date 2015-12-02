package tests.business;

import java.util.ArrayList;
import java.util.Calendar;

import tests.persistence.DBAccessStub;
import tos.application.DBService;
import tos.business.AccessBoards;
import tos.business.AccessSections;
import tos.business.AccessTasks;
import tos.objects.Board;
import tos.objects.Section;
import tos.objects.Task;
import tos.objects.Tag;
import tos.persistence.DBAccess;
import junit.framework.TestCase;

public class AccessTasksTests extends TestCase{
	private AccessSections accessSections;
	private AccessBoards accessBoards;
	private AccessTasks accessTasks;
	private Calendar dueDate;
	
	public void setUp() {
		try {
			DBService.createDataAccess(new DBAccessStub("DB"));
		} catch (Exception e) {
			
		}
		dueDate = Calendar.getInstance();
		dueDate.set(2016,10,23,13,50,45);
		accessBoards = new AccessBoards();
		accessSections = new AccessSections();
		accessTasks = new AccessTasks();
	}
	
	public void tearDown() {
		try {
			DBService.closeDataAccess();
		}
		catch (Exception e) {
			
		}
	}
	
	public void testNullArguments() {		
		try {
			accessTasks.getTasksInSection(null);
			fail("acessTasks.getTaskInSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessTasks.setTaskSection(0, 0, 0, null);
			fail("acessTasks.setTaskSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessTasks.updateTask(null);
			accessTasks.updateTask(null, null);
			fail("acessTaks.getTaskInSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessTasks.deleteTask(null, null);
			fail("accessTasks.deleteTask()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessTasks.createTask(null,new Section("Test Section"), null);
			fail("accessTasks.createTask()" +
					" Should have thrown null pointer exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessTasks.createTask(new Task("Test Task",
					dueDate,"Details","Comments",0), null, null);
			fail("accessTasks.createTask()" +
					" Should have thrown illegal argument exception.");
		}
		catch(NullPointerException npe) {
			
		}
		
		try {
			accessTasks.swapTaskPriority(null, 1);
			fail("accessTasks.swapTaskPriority()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		try {
			accessTasks.swapTaskPriority(new Task("Test Task",
					dueDate,"Details","Comments",0), 0);
			fail("accessTasks.swapTaskPriority()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		
		
		
		try {
			accessTasks.getTask(null);
			fail("accessTasks.getTask()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testUpdateDeleteMoveGetNonExistingTask() {
		Task newTask = new Task("Test Task",dueDate,"Details","Comments",0);
		Section newSection = new Section("Test Section");
		Board newBoard = new Board("Test Board");
		ArrayList<Tag> tags = new ArrayList<Tag>();
		
		accessBoards.createBoard(newBoard);
		accessSections.createSection(newSection, newBoard);
		
		try {
			accessTasks.updateTask(newTask);
			fail("Should have thrown illegal argument exception.");
		}
		catch (IllegalArgumentException iae) {
			
		}
		
		try {
			accessTasks.deleteTask(newTask.getTaskID(), tags);
			fail("accessTasks.deleteTask()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessTasks.swapTaskPriority(newTask, 1);
			fail("accessTasks.swapTaskPriority()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessTasks.getTags(newTask.getTaskID());
			fail("Should have thrown illegal argument exception.");
		}
		catch (IllegalArgumentException iae) {
			
		}
		
		try {
			accessBoards.deleteBoard(newBoard.getBoardID());
			accessSections.deleteSection(newSection.getSectionID());
			fail("Should have thrown illegal argument exception.");
		}
		catch (IllegalArgumentException iae) {
			
		}
		
	}
	
	public void testInsertUpdateDeleteMoveOneExistingTask() {
		Task newTask = new Task("Test Task",dueDate,"Details","Comments",0);
		Section newSection = new Section("Test Section");
		Section newSection2 = new Section("Test Section 2");
		Board newBoard = new Board("Test Board");
		ArrayList<Tag> tags = new ArrayList<Tag>();
		accessBoards.createBoard(newBoard);
		accessSections.createSection(newSection, newBoard);
		accessSections.createSection(newSection2, newBoard);
		
		accessTasks.createTask(newTask, newSection, new ArrayList<Tag>());
		assertTrue(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(newTask));
		
		newTask.setTitle("Blah");
		newTask.setDetail("DVNO");
		accessTasks.updateTask(newTask);
		Task updatedTask = accessTasks.getTask(newTask.getTaskID());
		assertEquals(updatedTask.getTitle(),"Blah");
		updatedTask = accessTasks.getTasksInSection(
				newSection.getSectionID()).get(0);
		assertEquals(updatedTask.getDetail(), "DVNO");
		
		tags.add(new Tag("#2012"));
		accessTasks.updateTask(newTask, tags);
		assertEquals(tags.get(0).getTag(), accessTasks.getTags(
				newTask.getTaskID()).get(0).getTag());
		
		accessTasks.setTaskSection(0, 0, 0, newSection2.getSectionID());
		assertFalse(accessTasks.getTasksInSection(
				newSection2.getSectionID()).isEmpty());
		accessTasks.setTaskSection(0, 1, 0, newSection.getSectionID());
		accessTasks.deleteTask(newTask.getTaskID(), new ArrayList<Tag>());
		assertFalse(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(newTask));
		accessBoards.deleteBoard(newBoard.getBoardID());
	}
	
	public void testInsertUpdateDeleteMoveManyExistingTasks() {
		Task[] tasks = new Task[8];
		Task updatedTask = null;
		Section newSection = new Section("Test Section");
		Board newBoard = new Board("Test Board");
		ArrayList<Tag> tags = new ArrayList<Tag>();
		accessBoards.createBoard(newBoard);
		accessSections.createSection(newSection, newBoard);
		
		tasks[0] = new Task("Test Task 1",dueDate,"Details","Comments",0);
		accessTasks.createTask(tasks[0], newSection, tags);
		assertTrue(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[0]));
		tasks[0].setTitle("Test Blah 1");
		accessTasks.updateTask(tasks[0]);
		updatedTask = accessTasks.getTask(tasks[0].getTaskID());
		assertEquals(updatedTask.getTitle(),"Test Blah 1");
		assertFalse(accessTasks.swapTaskPriority(tasks[0], 1));
		
		tasks[1] = new Task("Test Task 2",dueDate,"Details","Comments",0);
		accessTasks.createTask(tasks[1], newSection, tags);
		assertTrue(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[1]));
		tasks[1].setTitle("Test Blah 2");
		accessTasks.updateTask(tasks[1]);
		updatedTask = accessTasks.getTask(tasks[1].getTaskID());
		assertEquals(updatedTask.getTitle(),"Test Blah 2");
		assertFalse(accessTasks.swapTaskPriority(tasks[0], -1));
		assertTrue(accessTasks.swapTaskPriority(tasks[0], 1));
		assertTrue(accessTasks.swapTaskPriority(tasks[1], 1));
		
		tasks[2] = new Task("Test Task 3",dueDate,"Details","Comments",0);
		accessTasks.createTask(tasks[2], newSection, tags);
		assertTrue(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[2]));
		tasks[2].setTitle("Test Blah 3");
		accessTasks.updateTask(tasks[2]);
		updatedTask = accessTasks.getTask(tasks[2].getTaskID());
		assertEquals(updatedTask.getTitle(),"Test Blah 3");
		
		tasks[3] = new Task("Test Task 4",dueDate,"Details","Comments",0);
		accessTasks.createTask(tasks[3], newSection, tags);
		assertTrue(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[3]));
		tasks[3].setTitle("Test Blah 4");
		accessTasks.updateTask(tasks[3]);
		updatedTask = accessTasks.getTask(tasks[3].getTaskID());
		assertEquals(updatedTask.getTitle(),"Test Blah 4");
		
		tasks[4] = new Task("Test Task 5",dueDate,"Details","Comments",0);
		accessTasks.createTask(tasks[4], newSection, tags);
		assertTrue(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[4]));
		tasks[4].setTitle("Test Blah 5");
		accessTasks.updateTask(tasks[4]);
		updatedTask = accessTasks.getTask(tasks[4].getTaskID());
		assertEquals(updatedTask.getTitle(),"Test Blah 5");
		
		tasks[5] = new Task("Test Task 6",dueDate,"Details","Comments",0);
		accessTasks.createTask(tasks[5], newSection, tags);
		assertTrue(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[5]));
		tasks[5].setTitle("Test Blah 6");
		accessTasks.updateTask(tasks[5]);
		updatedTask = accessTasks.getTask(tasks[5].getTaskID());
		assertEquals(updatedTask.getTitle(),"Test Blah 6");
		
		tasks[6] = new Task("Test Task 7",dueDate,"Details","Comments",0);
		accessTasks.createTask(tasks[6], newSection, tags);
		assertTrue(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[6]));
		tasks[6].setTitle("Test Blah 7");
		accessTasks.updateTask(tasks[6]);
		updatedTask = accessTasks.getTask(tasks[6].getTaskID());
		assertEquals(updatedTask.getTitle(),"Test Blah 7");
		
		tasks[7] = new Task("Test Task 8",dueDate,"Details","Comments",0);
		accessTasks.createTask(tasks[7], newSection, tags);
		assertTrue(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[7]));
		tasks[7].setTitle("Test Blah 8");
		accessTasks.updateTask(tasks[7]);
		updatedTask = accessTasks.getTask(tasks[7].getTaskID());
		assertEquals(updatedTask.getTitle(),"Test Blah 8");
		
		accessTasks.deleteTask(tasks[0].getTaskID(), tags);
		assertFalse(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[0]));
		accessTasks.deleteTask(tasks[1].getTaskID(), tags);
		assertFalse(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[1]));
		accessTasks.deleteTask(tasks[2].getTaskID(), tags);
		assertFalse(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[2]));
		accessTasks.deleteTask(tasks[3].getTaskID(), tags);
		assertFalse(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[3]));
		accessTasks.deleteTask(tasks[4].getTaskID(), tags);
		assertFalse(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[4]));
		accessTasks.deleteTask(tasks[5].getTaskID(), tags);
		assertFalse(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[5]));
		accessTasks.deleteTask(tasks[6].getTaskID(), tags);
		assertFalse(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[6]));
		accessTasks.deleteTask(tasks[7].getTaskID(), tags);
		assertFalse(accessTasks.getTasksInSection(
				newSection.getSectionID()).contains(tasks[7]));
		accessBoards.deleteBoard(newBoard.getBoardID());
	}
	
	public void testInsertOrMoveTaskIntoNonExistingSection() {
		Task newTask = new Task("Test Task",dueDate,"Details","Comments",0);
		Section newSection = new Section("Test Section");
		
		try {
			accessTasks.createTask(newTask, newSection, new ArrayList<Tag>());
			fail("accessTasks() Should have returned illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testGetTaskFromNonExistingSection() {
		Board newBoard = new Board("Test Board");
		accessBoards.createBoard(newBoard);
		
		try {
			accessTasks.getTask(accessBoards.getAllBoards().size()-1,0,0);
			fail("accessTaks.getTask()" +
					" Should have thrown Index Out of Bounds exception.");
		}
		catch(IndexOutOfBoundsException ioub) {
			
		}
	}
	
	public void testGetNonExistingTaskFromSection() {
		Board newBoard = new Board("Test Board");
		accessBoards.createBoard(newBoard);
		
		try {
			accessTasks.getTask(accessBoards.getAllBoards().size()-1,
								accessSections.getSectionsOnBoard(
										newBoard.getBoardID()).size()-1,0);
			fail("accessTasks.getTask()" +
					" Should have thrown Index Out of Bounds exception.");
		}
		catch(IndexOutOfBoundsException ioub) {
			
		}
	}
	
	public void testGetTaskFromNonExistingBoard() {		
		try {
			accessTasks.getTask(accessBoards.getAllBoards().size(),0,0);
			fail("accessTaks.getTask()" +
					" Should have thrown Index Out of Bounds exception.");
		}
		catch(IndexOutOfBoundsException ioub) {
			
		}
	}
	
	public void testMethodsOnNullConnection() {
		DBAccess dbAccess = null;
		Section section = new Section("blah");
		Task task = new Task("blah",dueDate,"blah","blah",0);
		
		try {
			DBService.createDataAccess(dbAccess);
			accessTasks.createTask(task, section, new ArrayList<Tag>());
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
		
		try {
			DBService.createDataAccess(dbAccess);
			accessTasks.deleteTask(task.getTaskID(), new ArrayList<Tag>());
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
		
		try {
			DBService.createDataAccess(dbAccess);
			accessTasks.getTask(0, 0, 0);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
		
		try {
			DBService.createDataAccess(dbAccess);
			accessTasks.getTasksInSection(section.getSectionID());
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
		
		try {
			DBService.createDataAccess(dbAccess);
			accessTasks.setTaskSection(0, 0, 0, section.getSectionID());
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
		
		try {
			DBService.createDataAccess(dbAccess);
			accessTasks.updateTask(task);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
		
		try {
			DBService.createDataAccess(dbAccess);
			accessTasks.swapTaskPriority(task, 1);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
	}
}
