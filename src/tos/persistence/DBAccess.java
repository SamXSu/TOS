package tos.persistence;

import java.util.ArrayList;
import java.util.UUID;

import tos.objects.*;

public interface DBAccess {
	public boolean connectToDB();
	public boolean closeConnectionToDB();
	public ArrayList<Task> getAllTasks();
	public ArrayList<Section> getAllSections();
	public ArrayList<Board> getAllBoards();
	public ArrayList<Task> getTasksInSectionSorted(UUID sectionID);
	public ArrayList<Section> getSectionsInBoardSorted(UUID boardID);
	public Task getTask(UUID taskID);
	public Section getSection(UUID sectionID);
	public Board getBoard(UUID boardID);
	public UUID insertTask(Task task, Section section);
	public void deleteTask(UUID taskID);
	public void updateTask(Task updatedTask);
	public UUID insertSection(Section section, Board board);
	public boolean deleteSection(UUID sectionID);
	public void updateSection(Section updatedSection);
	public UUID insertBoard(Board board);
	public void deleteBoard(UUID boardID);
	public void updateBoard(Board updatedBoard);
	public boolean isIDInDatabase(UUID uuid);
	public UUID generateUUID();
	public ArrayList<Tag> getAllTags();
	public ArrayList<Tag> getTagsForTask(UUID taskID);
	public void deleteTagForTask(UUID tagID, UUID taskID);
	public UUID insertTagForTask(UUID taskID, String tag);
}
