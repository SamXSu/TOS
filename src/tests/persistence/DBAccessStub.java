package tests.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.UUID;

import tos.objects.*;
import tos.persistence.DBAccess;

public class DBAccessStub implements DBAccess {
	private boolean isConnectionOpen;
	private ArrayList<Task> taskTable;
	private ArrayList<Section> sectionTable;
	private ArrayList<Board> boardTable;
	private ArrayList<UUID> uuidTable;
	private ArrayList<Tag> tagTable;

	public DBAccessStub(String dbName) {
		isConnectionOpen = false;
		taskTable = new ArrayList<Task>();
		sectionTable = new ArrayList<Section>();
		boardTable = new ArrayList<Board>();
		uuidTable = new ArrayList<UUID>();
		tagTable = new ArrayList<Tag>();
	}

	public boolean connectToDB() {
		isConnectionOpen = true;
		generateData();
		return isConnectionOpen;
	}

	public boolean closeConnectionToDB() {
		isConnectionOpen = false;
		taskTable.clear();
		sectionTable.clear();
		boardTable.clear();
		return !isConnectionOpen;
	}

	private void generateData() {
		Board board = new Board(new UUID(1,0), "Default");
		Section toDoSection = new Section(new UUID(5,0), board.getBoardID(),
				"To-do",false,0);
		Section workInProgressSection = new Section(new UUID(5,1),
				board.getBoardID(), "Work In Progress",true,1);
		Section doneSection = new Section(new UUID(5,2), board.getBoardID(),
				"Done",false,2);
		Section sec1 = new Section(new UUID(5,3), board.getBoardID(),
				"sec1",false,3);
		Section sec2 = new Section(new UUID(5,4), board.getBoardID(),
				"sec2",false,4);
		
		Task task;
		Calendar dueDate = Calendar.getInstance();
		Calendar startTime = Calendar.getInstance();

		sectionTable.add(toDoSection);
		sectionTable.add(workInProgressSection);
		sectionTable.add(doneSection);
		sectionTable.add(sec1);
		sectionTable.add(sec2);

		boardTable.add(board);

		dueDate.set(2014, 6, 23, 5, 10, 55);
		startTime.set(2013, 7, 3, 5, 10, 55);
		task = new Task(new UUID(2,0), toDoSection.getSectionID(),
				"Collapse on ground", dueDate, "Details",
				"Comments", 0, startTime,0,true);
		taskTable.add(task);
		
		dueDate = Calendar.getInstance();
		startTime = Calendar.getInstance();
		dueDate.set(2015, 8, 14, 47, 15, 33);
		startTime.set(2013, 7, 6, 8, 10, 55);
		task = new Task(new UUID(2,1), toDoSection.getSectionID(),
				"Step 3: Profit!", dueDate, "-------------------------------" +
						"-----------------------------------------------------" +
						"----------------------------------", 
						"Comments",1, startTime, 0, true);
		taskTable.add(task);
		tagTable.add(new Tag("gnomes",new UUID(7,1),new UUID(2,1)));

		dueDate = Calendar.getInstance();
		startTime = Calendar.getInstance();
		dueDate.set(2014, 8, 12, 22, 05, 33);
		startTime.set(2013, 7, 5, 5, 10, 14);
		task = new Task(new UUID(2,2), toDoSection.getSectionID(),
				"Takeover Greenland", dueDate, "Details", 
				"Comments",2, startTime, 0, true);
		taskTable.add(task);

		dueDate = Calendar.getInstance();
		startTime = Calendar.getInstance();
		dueDate.set(2015, 3, 14, 47, 15, 33);
		startTime.set(2013, 7, 4, 10, 00, 55);
		task = new Task(new UUID(2,3), workInProgressSection.getSectionID(),
				"Drink a gallon of water", dueDate, "Details", 
				"Comments",0, startTime, 0, true);
		taskTable.add(task);

		dueDate = Calendar.getInstance();
		startTime = Calendar.getInstance();
		dueDate.set(2019, 12, 14, 47, 15, 33);
		startTime.set(2013, 7, 3, 3, 3, 0);
		task = new Task(new UUID(2,4), workInProgressSection.getSectionID(),
				"Build a trebuchet", dueDate, "Details", 
				"Comments",1, startTime, 0, true);
		taskTable.add(task);

		dueDate = Calendar.getInstance();
		startTime = Calendar.getInstance();
		dueDate.set(2018, 8, 14, 47, 15, 33);
		startTime.set(2013, 7, 3, 10, 10, 36);
		task = new Task(new UUID(2,5), workInProgressSection.getSectionID(),
				"Step 2: ??????", dueDate, "Details", 
				"Comments",2, startTime, 0, true);
		taskTable.add(task);
		tagTable.add(new Tag("gnomes",new UUID(7,1),new UUID(2,5)));
		
		dueDate = Calendar.getInstance();
		startTime = Calendar.getInstance();
		dueDate.set(2015, 8, 14, 47, 15, 33);
		startTime.set(2013, 7, 4, 11, 10, 25);
		task = new Task(new UUID(2,6), doneSection.getSectionID(),
				"Run half-marathon", dueDate, "Details", 
				"Comments",0, startTime, 0, true);
		taskTable.add(task);

		dueDate = Calendar.getInstance();
		startTime = Calendar.getInstance();
		dueDate.set(2015, 8, 14, 47, 15, 33);
		startTime.set(2013, 7, 3, 3, 10, 20);
		task = new Task(new UUID(2,7), doneSection.getSectionID(),
				"Design a trebuchet", dueDate, "Details",
				"Comments",1, startTime, 0, true);
		taskTable.add(task);
		
		dueDate = Calendar.getInstance();
		startTime = Calendar.getInstance();
		dueDate.set(2015, 2, 14, 47, 15, 33);		startTime.set(2013, 6, 3, 5, 10, 55);
		task = new Task(new UUID(2,8), doneSection.getSectionID(),
				"Step 1: Steal underpants", dueDate, "Details", 
				"Comments",2, startTime, 0, true);
		taskTable.add(task);
		tagTable.add(new Tag("gnomes",new UUID(7,1),new UUID(2,8)));
		
		uuidTable.add(new UUID(5,0));
		uuidTable.add(new UUID(5,1));
		uuidTable.add(new UUID(5,2));
		uuidTable.add(new UUID(5,3));
		uuidTable.add(new UUID(5,4));
		uuidTable.add(new UUID(2,0));
		uuidTable.add(new UUID(2,1));
		uuidTable.add(new UUID(2,2));
		uuidTable.add(new UUID(2,3));
		uuidTable.add(new UUID(2,4));
		uuidTable.add(new UUID(2,5));
		uuidTable.add(new UUID(2,6));
		uuidTable.add(new UUID(2,7));
		uuidTable.add(new UUID(2,8));
		uuidTable.add(new UUID(1,0));		
		uuidTable.add(new UUID(7,1));
	}

	public ArrayList<Task> getAllTasks() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		for (int index = 0; index < taskTable.size(); index++) {
			tasks.add(taskTable.get(index).deepCopy());
		}
		return tasks;
	}

	public ArrayList<Section> getAllSections() {
		ArrayList<Section> sections = new ArrayList<Section>();
		for (int index = 0; index < sectionTable.size(); index++) {
			sections.add(sectionTable.get(index).deepCopy());
		}
		return sections;
	}

	public ArrayList<Board> getAllBoards() {
		ArrayList<Board> boards = new ArrayList<Board>();
		for (int index = 0; index < boardTable.size(); index++) {
			boards.add(boardTable.get(index).deepCopy());
		}
		return boards;
	}

	public ArrayList<Task> getTasksInSectionSorted(UUID sectionID) {
		ArrayList<Task> tasksInSection = new ArrayList<Task>();
		validateUUID(sectionID);

		for (int index = 0; index < taskTable.size(); index++) {
			if (sectionID == taskTable.get(index).getSectionID()) {
				tasksInSection.add(taskTable.get(index));
			}
		}
		Collections.sort(tasksInSection);
		return tasksInSection;
	}

	public ArrayList<Section> getSectionsInBoardSorted(UUID boardID) {
		ArrayList<Section> sectionsInBoard = new ArrayList<Section>();
		validateUUID(boardID);

		for (int index = 0; index < sectionTable.size(); index++) {
			if (boardID == sectionTable.get(index).getBoardID()) {
				sectionsInBoard.add(sectionTable.get(index));
			}
		}
		Collections.sort(sectionsInBoard);
		return sectionsInBoard;
	}
	
	public Task getTask(UUID taskID) {
		Task foundTask = null;
		validateUUID(taskID);
		
		for (int index=0; index < taskTable.size(); index++) {
			Task currentTask = taskTable.get(index);
			UUID currentID = currentTask.getTaskID();
			if (currentID.equals(taskID)) {
				foundTask = currentTask;
			}
		}
		
		return foundTask;
	}
	
	public Section getSection(UUID sectionID) {
		Section foundSection = null;
		validateUUID(sectionID);
		
		for (int index=0; index < sectionTable.size(); index++) {
			Section currentSection = sectionTable.get(index);
			UUID currentID = currentSection.getSectionID();
			if (currentID.equals(sectionID)) {
				foundSection = currentSection;
			}
		}
		
		return foundSection;
	}
	
	public Board getBoard(UUID boardID) {
		Board foundBoard = null;
		validateUUID(boardID);
		
		for (int index=0; index < boardTable.size(); index++) {
			Board currentBoard = boardTable.get(index);
			UUID currentID = currentBoard.getBoardID();
			if (currentID.equals(boardID)) {
				foundBoard = currentBoard;
			}
		}
		
		return foundBoard;
	}
	
	public ArrayList<Tag> getTagsForTask(UUID taskID) {
		validateUUID(taskID);
		ArrayList<Tag> tagsForTask = new ArrayList<Tag>();
		
		for (int index=0; index < tagTable.size(); index++) {
			Tag currTag = tagTable.get(index);
			if (currTag.getTaskID().equals(taskID)) {
				tagsForTask.add(currTag);
			}
		}
		
		return tagsForTask;
	}

	public UUID insertTask(Task task, Section section) {
		validateSectionObject(section);
		UUID taskID = generateUUID();
		task.setSectionID(section.getSectionID());
		task.setTaskID(taskID);
		taskTable.add(task);
		uuidTable.add(task.getTaskID());
		return taskID;
	}

	public void deleteTask(UUID taskID) {
		validateUUID(taskID);
		ArrayList<Tag> tags;
		
		for (int index=0; index < taskTable.size(); index++) {
			if (taskID == taskTable.get(index).getTaskID()) {
				tags = getTagsForTask(taskID);	
				for (int i = 0; i < tags.size(); i++) {
					deleteTagForTask(tags.get(i).getTagID(), taskID);
				}
				uuidTable.remove(taskTable.get(index).getTaskID());
				taskTable.remove(taskTable.get(index));
				index--;
				
			}
		}
	}

	private void validateTaskObject(Task task) {
		validateNullObject(task);
		if (!isIDInDatabase(task.getTaskID())) {
			throw new IllegalArgumentException(
					"Illegal Argument Exception: Task doesn't exist in database.");
		}
	}
	
	private void validateNullObject(Object o) {
		if (o == null) {
			throw new IllegalArgumentException(
					"Illegal Argument Exception: Argument cannot be null.");
		}
	}
	
	private void validateStringObject(String str) {
		validateNullObject(str);
		if (str == "") {
			throw new IllegalArgumentException("String argument cannot be blank.");
		}
	}

	private void validateSectionObject(Section section) {
		validateNullObject(section);
		if (!isIDInDatabase(section.getSectionID())) {
			throw new IllegalArgumentException(
					"Illegal Argument Exception: Section doesn't exist in database.");
		}
	}

	private void validateBoardObject(Board board) {
		validateNullObject(board);
		if (!isIDInDatabase(board.getBoardID())) {
			throw new IllegalArgumentException(
					"Illegal Argument Exception: Board doesn't exist in database.");
		}
	}
	
	private void validateUUID(UUID uuid) {
		validateNullObject(uuid);
		if (!isIDInDatabase(uuid)) {
			throw new IllegalArgumentException(
					"Illegal Argument Exception: UUID doesn't exist in database.");
		}
	}

	public void updateTask(Task updatedTask) {
		validateTaskObject(updatedTask);
		int index = taskTable.indexOf(updatedTask);
		if (index >= 0) {
			taskTable.set(index, updatedTask);
		}
		else {
			System.out.println("updateTask(Task): cannot find index for task: "
					+ updatedTask.toString());
		}
	}

	public UUID insertSection(Section section, Board board) {
		validateBoardObject(board);
		UUID sectionID = generateUUID();
		section.setBoardID(board.getBoardID());
		section.setSectionID(sectionID);
		sectionTable.add(section);
		uuidTable.add(section.getSectionID());
		return sectionID;
	}

	public boolean deleteSection(UUID sectionID) {
		validateUUID(sectionID);
		ArrayList<Task> tasksInSection = getTasksInSectionSorted(sectionID);
		boolean removed = false;
		for (int index=0; index < sectionTable.size(); index++) {
			if (sectionID == sectionTable.get(index).getSectionID()) {
				if(!sectionTable.get(index).isWIP()){
					uuidTable.remove(sectionTable.get(index).getSectionID());
					sectionTable.remove(sectionTable.get(index));
					removed = true;
					// on delete cascade, delete all tasks in section
					for(int i=0; i < tasksInSection.size(); i++) {
						deleteTask(tasksInSection.get(i).getTaskID());
					}
				}
			}
		}	
		return removed;
	}

	public void updateSection(Section updatedSection) {
		validateSectionObject(updatedSection);
		int index = sectionTable.indexOf(updatedSection);
		if (index >= 0) {
			sectionTable.set(index, updatedSection);
		}
	}

	public UUID insertBoard(Board board) {
		UUID boardID = generateUUID();
		board.setBoardID(boardID);
		boardTable.add(board);
		uuidTable.add(board.getBoardID());
		return boardID;
	}

	public void deleteBoard(UUID boardID) {
		validateUUID(boardID);
		ArrayList<Section> sectionsInBoard = getSectionsInBoardSorted(boardID);
		
		for (int index=0; index < boardTable.size(); index++) {
			if (boardID == boardTable.get(index).getBoardID()) {
				uuidTable.remove(boardTable.get(index).getBoardID());
				boardTable.remove(index);
			}
		}
		// on delete cascade, delete all sections in board
		for (int index=0; index < sectionsInBoard.size(); index++) {
			deleteSection(sectionsInBoard.get(index).getSectionID());
		}
	}

	public void updateBoard(Board updatedBoard) {
		validateBoardObject(updatedBoard);
		int index = boardTable.indexOf(updatedBoard);
		if (index >= 0) {
			boardTable.set(index, updatedBoard);
		}
	}
	
	public boolean isIDInDatabase(UUID uuid) {
		return uuidTable.contains(uuid);
	}

	public UUID generateUUID() {
		UUID newUUID = UUID.randomUUID();
		
		while (isIDInDatabase(newUUID)) {
			newUUID = UUID.randomUUID();
		}
		
		return newUUID;
	}
	
	public void deleteTagForTask(UUID tagID, UUID taskID) {
		validateUUID(taskID);
		validateUUID(tagID);
		
		for (int index=0; index < tagTable.size(); index++) {
			Tag currTag = tagTable.get(index);
			if (currTag.getTagID().equals(tagID) && currTag.getTaskID()
					.equals(taskID)) {
				tagTable.remove(index);
			}
		}
		
	}
	
	public UUID insertTagForTask(UUID taskID, String tag) {
		validateUUID(taskID);
		validateStringObject(tag);
		UUID tagID = null;
		ArrayList<Tag> tagsForTask = getTagsForTask(taskID);
		if (!isTagInList(tag,tagsForTask)) {
			for (int index=0; index < tagTable.size(); index++) {
				Tag currTag = tagTable.get(index);
				if (currTag.getTag().equals(tag)) {
					tagID = currTag.getTagID();
				}
			}
			if (tagID == null) {
				tagID = generateUUID();
			}
			
			tagTable.add(new Tag(tag,tagID,taskID));
			uuidTable.add(tagID);
		}
		return tagID;
	}
	
	private boolean isTagInList(String tag, ArrayList<Tag> tags) {
		boolean isFound = false;
		
		for (int i=0 ; i < tags.size(); i++){
			if(tags.get(i).getTag().equals(tag)) {
				isFound = true;
			}
		}		
		
		return isFound;
	}

	public ArrayList<Tag> getAllTags() {
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (int index = 0; index < tagTable.size(); index++) {
			Tag currTag = tagTable.get(index);
			if (!isTagInList(currTag.getTag(),tags)) {
				tags.add(currTag);
			}
		}
		return tags;
	}
}