package tos.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.UUID;

import tos.objects.Board;
import tos.objects.Section;
import tos.objects.Tag;
import tos.objects.Task;

public class DBAccessHSQL implements DBAccess {
	private String dbName;
	private String dbType;
	private String cmdString;
	private Connection connection;
	private Statement queryStatement;
	private PreparedStatement preparedStatement;
	private PreparedStatement preparedStatement2;
	private ResultSet resultSet;
	private ResultSet resultSet2;
	private int updateCount;
	
	public DBAccessHSQL(String dbName, String dbType) {
		this.dbName = dbName;
		this.dbType = dbType;
	}
	
	public boolean connectToDB() {
		try {
			Class.forName("org.hsqldb.jdbcDriver").newInstance();
			this.connection = DriverManager.getConnection(
					"jdbc:hsqldb:database/" + dbName, "sa", "");
			queryStatement = connection.createStatement();
			updateCount = 0;
		} 
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		System.out.println("Connection created to " + dbType + " DB named " + dbName);
		return true;
	}

	public boolean closeConnectionToDB() {
		try
		{
			cmdString = "shutdown compact";
			resultSet = queryStatement.executeQuery(cmdString);
			connection.close();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		System.out.println("Connection closed to " +dbType +" DB named " +dbName);
		return true;
	}

	public ArrayList<Task> getAllTasks() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		try {
			cmdString = "Select * from TASKS";
			resultSet = queryStatement.executeQuery(cmdString);
			
			while (resultSet.next()) {
				Calendar dueDate = Calendar.getInstance();
				Calendar startingTime = Calendar.getInstance();
				UUID taskID = UUID.fromString(resultSet.getString("TASKID"));
				UUID sectionID = UUID.fromString(resultSet.getString("SECTIONID"));
				String title = resultSet.getString("TITLE");
				String details = resultSet.getString("DETAILS");
				String comments = resultSet.getString("COMMENTS");
				int priority = resultSet.getInt("PRIORITY");
				dueDate.setTime(resultSet.getTimestamp("DUEDATE"));
				startingTime.setTime(resultSet.getTimestamp("STARTINGTIME"));
				long runningTime = resultSet.getLong("RUNNINGTIME");
				boolean running = resultSet.getBoolean("RUNNING");
				
				tasks.add(new Task(taskID,sectionID,title,dueDate,details,comments,priority,
										startingTime,runningTime,running));
			}
			resultSet.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return tasks;
	}

	public ArrayList<Section> getAllSections() {
		ArrayList<Section> sections = new ArrayList<Section>();
		
		try {
			cmdString = "Select * from SECTIONS";
			resultSet = queryStatement.executeQuery(cmdString);
			
			while (resultSet.next()) {
				UUID sectionID = UUID.fromString(resultSet.getString("SECTIONID"));
				UUID boardID = UUID.fromString(resultSet.getString("BOARDID"));
				String title = resultSet.getString("TITLE");
				boolean isWIP = resultSet.getBoolean("ISWIP");
				int sortOrder = resultSet.getInt("SORT_ORDER");
				sections.add(new Section(sectionID,boardID,title,isWIP,sortOrder));
			}
			resultSet.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return sections;
	}

	public ArrayList<Board> getAllBoards() {
		ArrayList<Board> boards = new ArrayList<Board>();
		
		try {
			cmdString = "SELECT * from BOARDS";
			resultSet = queryStatement.executeQuery(cmdString);
			
			while (resultSet.next()) {
				UUID boardID = UUID.fromString(resultSet.getString("BOARDID"));
				String name = resultSet.getString("TITLE");
				
				boards.add(new Board(boardID,name));
			}
			resultSet.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return boards;
	}

	public ArrayList<Task> getTasksInSectionSorted(UUID sectionID) {
		validateUUID(sectionID);
		ArrayList<Task> tasksInSection = new ArrayList<Task>();
		try {
			cmdString = "SELECT * FROM TASKS WHERE sectionID = '"
					+ sectionID + "' order by priority";
			resultSet = queryStatement.executeQuery(cmdString);
			
			while (resultSet.next()) {
				Calendar dueDate = Calendar.getInstance();
				Calendar startingTime = Calendar.getInstance();
				UUID taskID = UUID.fromString(resultSet.getString("TASKID"));
				String title = resultSet.getString("TITLE");
				String details = resultSet.getString("DETAILS");
				String comments = resultSet.getString("COMMENTS");
				int priority = resultSet.getInt("PRIORITY");
				dueDate.setTime(resultSet.getTimestamp("DUEDATE"));
				startingTime.setTime(resultSet.getTimestamp("STARTINGTIME"));
				long runningTime = resultSet.getLong("RUNNINGTIME");
				boolean running = resultSet.getBoolean("RUNNING");
				
				tasksInSection.add(new Task(taskID,sectionID,title,dueDate,details,comments,priority,
												startingTime,runningTime,running));				
			}
			resultSet.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		Collections.sort(tasksInSection);
		return tasksInSection;
	}

	public ArrayList<Section> getSectionsInBoardSorted(UUID boardID) {
		validateUUID(boardID);
		ArrayList<Section> sectionsInBoard = new ArrayList<Section>();
		try {
			cmdString = "SELECT * FROM SECTIONS WHERE boardID = '" 
					+ boardID + "' order by sort_order";
			resultSet = queryStatement.executeQuery(cmdString);
			
			while (resultSet.next()) {
				UUID sectionID = UUID.fromString(resultSet.getString("SECTIONID"));
				String title = resultSet.getString("TITLE");
				boolean isWIP = resultSet.getBoolean("ISWIP");
				int sortOrder = resultSet.getInt("SORT_ORDER");
				sectionsInBoard.add(
						new Section(sectionID,boardID,title,isWIP,sortOrder));				
			}
			resultSet.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		Collections.sort(sectionsInBoard);
		return sectionsInBoard;
	}
	
	public Section getSection(UUID sectionID) {
		validateUUID(sectionID);
		Section resultSection = null;
		
		try {
			cmdString = "SELECT * FROM SECTIONS WHERE sectionID = '" + sectionID + "'";
			resultSet = queryStatement.executeQuery(cmdString);
			
			while(resultSet.next()) {
				UUID boardID = UUID.fromString(resultSet.getString("BOARDID"));
				String title = resultSet.getString("TITLE");
				boolean isWIP = resultSet.getBoolean("ISWIP");
				int sortOrder = resultSet.getInt("SORT_ORDER");
				resultSection = new Section(sectionID,boardID,title,isWIP,sortOrder);				
			}

			resultSet.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return resultSection;
	}
	
	public Board getBoard(UUID boardID) {
		validateUUID(boardID);
		Board resultBoard = null;
		try {
			cmdString = "SELECT * FROM BOARDS WHERE boardID = '" + boardID + "'";
			resultSet = queryStatement.executeQuery(cmdString);
			
			while (resultSet.next()) {
				String name = resultSet.getString("TITLE");
				resultBoard = new Board(boardID,name);
			}
			resultSet.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return resultBoard;
	}
	
	public Task getTask(UUID taskID) {
		validateUUID(taskID);
		Task resultTask = null;
		try {
			cmdString = "SELECT * FROM TASKS WHERE taskID = '" + taskID + "'";
			resultSet = queryStatement.executeQuery(cmdString);

			while (resultSet.next()) {
				Calendar dueDate = Calendar.getInstance();
				Calendar startingTime = Calendar.getInstance();
				UUID sectionID = UUID.fromString(resultSet.getString("SECTIONID"));
				String title = resultSet.getString("TITLE");
				String details = resultSet.getString("DETAILS");
				String comments = resultSet.getString("COMMENTS");
				int priority = resultSet.getInt("PRIORITY");
				dueDate.setTime(resultSet.getTimestamp("DUEDATE"));
				startingTime.setTime(resultSet.getTimestamp("STARTINGTIME"));
				long runningTime = resultSet.getLong("RUNNINGTIME");
				boolean running = resultSet.getBoolean("RUNNING");
				
				resultTask = new Task(taskID,sectionID,title,dueDate,details,comments,priority,
											startingTime,runningTime,running);
			}
			
			resultSet.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return resultTask;
	}

	public UUID insertTask(Task task, Section section) {
		validateSectionObject(section);
		UUID taskID = generateUUID();
		cmdString = "INSERT INTO TASKS (taskID, sectionID, title," +
				" duedate, details, comments, priority, startingtime, runningtime, running) ";
		cmdString += "VALUES (?,?,?,?,?,?,?,?,?,?)";
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(cmdString);
			preparedStatement.setString(1,taskID.toString());
			preparedStatement.setString(2, section.getSectionID().toString());
			preparedStatement.setString(3, task.getTitle());
			preparedStatement.setString(4, formatDateForDB(task.getDueDate()));
			preparedStatement.setString(5, task.getDetail());
			preparedStatement.setString(6, task.getComment());
			preparedStatement.setInt(7, task.getPriority());
			preparedStatement.setString(8, formatDateForDB(task.getStartTime()));
			preparedStatement.setLong(9, task.getRunningTime());
			preparedStatement.setBoolean(10, task.isRunning());
			preparedStatement.executeUpdate();
			connection.commit();
			
		} catch (SQLException e) {
			System.out.println("Error Inserting Task: " + e);
		}
		return taskID;
	}

	public void deleteTask(UUID taskID) {
		validateUUID(taskID);
		cmdString = "DELETE FROM TASKS WHERE taskID = '" + taskID + "'";
		executeUpdateQuery(cmdString);
		deleteUUID(taskID);
	}

	public void updateTask(Task updatedTask) {
		validateTaskObject(updatedTask);
		cmdString = "UPDATE TASKS";
		cmdString += " SET title = ?, sectionID = ?, duedate = ?, details = ?,";
		cmdString += " comments = ?, priority = ?, startingtime = ?, runningtime = ?,";
		cmdString += " running = ?";
		cmdString += " WHERE taskID = '" + updatedTask.getTaskID() + "'";
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(cmdString);
			preparedStatement.setString(1, updatedTask.getTitle());
			preparedStatement.setString(2, updatedTask.getSectionID().toString());
			preparedStatement.setString(3, formatDateForDB(updatedTask.getDueDate()));
			preparedStatement.setString(4, updatedTask.getDetail());
			preparedStatement.setString(5, updatedTask.getComment());
			preparedStatement.setInt(6, updatedTask.getPriority());
			preparedStatement.setString(7, formatDateForDB(updatedTask.getStartTime()));
			preparedStatement.setLong(8, updatedTask.getRunningTime());
			preparedStatement.setBoolean(9, updatedTask.isRunning());
			preparedStatement.executeUpdate();
			connection.commit();		
		} catch (SQLException e) {
			System.out.println("Error Updating Task: " + e);
		}
	}

	public UUID insertSection(Section section, Board board) {
		validateBoardObject(board);
		UUID sectionID = generateUUID();
		cmdString = "INSERT INTO SECTIONS (sectionID, boardID, title," +
				" sort_order, iswip) VALUES(?,?,?,?,?)";
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(cmdString);
			preparedStatement.setString(1, sectionID.toString());
			preparedStatement.setString(2, board.getBoardID().toString());
			preparedStatement.setString(3, section.getTitle());
			preparedStatement.setInt(4, section.getSortOrder());
			preparedStatement.setBoolean(5, section.isWIP());
			preparedStatement.executeUpdate();
			connection.commit();
			
		} catch (SQLException e) {
			System.out.println("Error Inserting Section: " + e);
		}
		return sectionID;
	}

	public boolean deleteSection(UUID sectionID) {
		validateUUID(sectionID);
		cmdString = "DELETE FROM SECTIONS WHERE sectionID = '" + sectionID + "'";
		executeUpdateQuery(cmdString);
		deleteUUID(sectionID);
		return true;
	}

	public void updateSection(Section updatedSection) {
		validateSectionObject(updatedSection);
		cmdString = "UPDATE SECTIONS";
		cmdString += " SET title = ?, sort_order = ?";
		cmdString += " WHERE sectionID = '" + updatedSection.getSectionID() + "'";
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(cmdString);
			preparedStatement.setString(1, updatedSection.getTitle());
			preparedStatement.setInt(2, updatedSection.getSortOrder());
			preparedStatement.executeUpdate();
			connection.commit();
			
		} catch (SQLException e) {
			System.out.println("Error Updating Section: " + e);
		}
	}

	public UUID insertBoard(Board board) {
		UUID boardID = generateUUID();
		cmdString = "INSERT INTO BOARDS (boardID, title) VALUES(?,?)";
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(cmdString);
			preparedStatement.setString(1, boardID.toString());
			preparedStatement.setString(2, board.getName());
			preparedStatement.executeUpdate();
			connection.commit();
			
		} catch (SQLException e) {
			System.out.println("Error Updating Section: " + e);
		}
		return boardID;
	}

	public void deleteBoard(UUID boardID) {
		validateUUID(boardID);
		cmdString = "DELETE FROM BOARDS WHERE boardID = '" + boardID + "'";
		executeUpdateQuery(cmdString);
		deleteUUID(boardID);
	}

	public void updateBoard(Board updatedBoard) {
		validateBoardObject(updatedBoard);
		cmdString = "UPDATE BOARDS ";
		cmdString += "SET title = ?"; 
		cmdString += " WHERE boardID = '" + updatedBoard.getBoardID() + "'";
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(cmdString);
			preparedStatement.setString(1, updatedBoard.getName());
			preparedStatement.executeUpdate();
			connection.commit();
			
		} catch (SQLException e) {
			System.out.println("Error Updating Section: " + e);
		}
	}
	
	public ArrayList<Tag> getAllTags() {
		ArrayList<Tag> tags = new ArrayList<Tag>();
		try {
			cmdString = "select distinct tagid,tag from tasktag";
			resultSet2 = queryStatement.executeQuery(cmdString);
			
			while (resultSet2.next()) {
				UUID tagID = UUID.fromString(resultSet2.getString("TAGID"));
				String tag = resultSet2.getString("TAG");
				tags.add(new Tag(tag,tagID,generateUUID()));
			}
			resultSet2.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return tags;
	}
	
	public ArrayList<Tag> getTagsForTask(UUID taskID) {
		validateUUID(taskID);
		ArrayList<Tag> tagList = new ArrayList<Tag>();
		try {
			cmdString = "SELECT * FROM TASKTAG WHERE TASKID = '" + taskID + "'";
			resultSet = queryStatement.executeQuery(cmdString);
			
			while (resultSet.next()) {
				String tag = resultSet.getString("TAG");
				UUID tagID = UUID.fromString(resultSet.getString("TAGID"));
				tagList.add(new Tag(tag,tagID,taskID));
			}
			resultSet.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return tagList;
	}
	
	public UUID insertTagForTask(UUID taskID, String tag) {
		validateStringObject(tag);
		UUID tagID = null;
		try {			
			cmdString = "select tagid from tasktag where taskid = ? and tag = ?";
			connection.setAutoCommit(false);
			preparedStatement2 = connection.prepareStatement(cmdString);
			preparedStatement2.setString(1, taskID.toString());
			preparedStatement2.setString(2, tag);
			resultSet2 = preparedStatement2.executeQuery();
			
			if(!resultSet2.next()) { // tag doesn't exist for this task in the database
				cmdString = "select tagid from tasktag where tag = ? GROUP BY TAGID";
				preparedStatement = connection.prepareStatement(cmdString);
				preparedStatement.setString(1, tag);
				resultSet = preparedStatement.executeQuery();
				
				if (!resultSet.next()) { // tag doesn't exist in db at all
					tagID = generateUUID();
				}
				else { // tag does exist in db for another task
					tagID = UUID.fromString(resultSet.getString("TAGID"));
				}
				resultSet.close();
				
				cmdString = "INSERT INTO TASKTAG(TAGID,TASKID,TAG) VALUES(?,?,?)";
				preparedStatement = connection.prepareStatement(cmdString);
				preparedStatement.setString(1, tagID.toString());
				preparedStatement.setString(2, taskID.toString());
				preparedStatement.setString(3, tag);
				preparedStatement.executeUpdate();
				connection.commit();
			}
			resultSet2.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return tagID;
	}
	
	public void deleteTagForTask(UUID tagID, UUID taskID) {
		validateNullObject(tagID);
		validateUUID(taskID);
		cmdString = "DELETE FROM TASKTAG WHERE TAGID = '" + tagID +
				"' AND TASKID = '" + taskID + "'";
		executeUpdateQuery(cmdString);
	}

	public boolean isIDInDatabase(UUID uuid) {
		Boolean isIDInDB = false;
		
		try {
			cmdString = "SELECT * FROM UUID WHERE uniqueidentifier = '" +
					uuid + "'";
			resultSet = queryStatement.executeQuery(cmdString);
			
			while (resultSet.next()) {
				isIDInDB = true;
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return isIDInDB;
	}
	
	private void validateUpdateCount(int updateCount) {
		if (updateCount <= 0) {
			System.out.println("Tuple not updated correctly.");
		}
	}
	
	private void executeUpdateQuery(String cmdString) {
		try {
			connection.setAutoCommit(true);
			updateCount = queryStatement.executeUpdate(cmdString);
			validateUpdateCount(updateCount);
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private String formatDateForDB(java.util.Date date) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public UUID generateUUID() {
		UUID newUUID = UUID.randomUUID();
		
		while (isIDInDatabase(newUUID)) {
			newUUID = UUID.randomUUID();
		}
		insertUUID(newUUID);
		
		return newUUID;
	}
	
	private void insertUUID(UUID uuid) {
		cmdString = "INSERT INTO UUID (UNIQUEIDENTIFIER) VALUES('" + uuid.toString() + "')";
		executeUpdateQuery(cmdString);
	}
	
	private void deleteUUID(UUID uuid) {
		cmdString = "DELETE FROM UUID WHERE UNIQUEIDENTIFIER = '" + uuid.toString() + "'";
		executeUpdateQuery(cmdString);
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
}
