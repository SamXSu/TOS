package tos.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import tos.application.DBService;
import tos.objects.*;
import tos.persistence.DBAccess;

public class AccessTasks {
	private DBAccess dbAccess;
	private ArrayList<Tag> filterRules;

	public AccessTasks() {
		dbAccess = DBService.getDataAccess();
	}
	
	public ArrayList<Task> getTasksInSection(UUID sectionID) {
		ArrayList<Task> resultTasks = new ArrayList<Task>();
		dbAccess = DBService.getDataAccess();
		fixPriorities(sectionID);
		ArrayList<Task> tasks = dbAccess.getTasksInSectionSorted(sectionID);
		if (filterRules == null)
			resultTasks = tasks;
		else {
			for (int i = 0; i < tasks.size(); i++) {
				ArrayList<Tag> tags = dbAccess.getTagsForTask(
						tasks.get(i).getTaskID());
				Task currTask = tasks.get(i);
				if (tags != null) {
					for (int j = 0; j < tags.size(); j++) {
						if (isTagInList(tags.get(j),filterRules)) {
							resultTasks.add(currTask);
							break;
						}
					}
				}
			}
		}
		Collections.sort(resultTasks);
		return resultTasks;
	}
	
	private boolean isTagInList(Tag tag, ArrayList<Tag> list) {
		boolean isFound = false;
		
		for (int index = 0; index < list.size(); index++) {
			if (list.get(index).getTag().equals(tag.getTag())) {
				isFound = true;
			}
		}
		
		return isFound;
	}

	public void updateTask(Task task, ArrayList<Tag> tags) {
		ArrayList<Tag> tagsInDB = dbAccess.getTagsForTask(task.getTaskID());
		dbAccess = DBService.getDataAccess();
		dbAccess.updateTask(task);
		if (tags != null) {
			for(int i = 0; i < tagsInDB.size(); i++){
				Tag currTag = tagsInDB.get(i);
				if (!isTagInList(currTag,tags)) {
					dbAccess.deleteTagForTask(currTag.getTagID(), task.getTaskID());
				}
			}
			for(int i = 0; i < tags.size(); i++) {
				dbAccess.insertTagForTask(task.getTaskID(), tags.get(i).getTag());
			}
		}
		else {
			for (int index = 0; index < tagsInDB.size(); index++) {
				dbAccess.deleteTagForTask(tagsInDB.get(
						index).getTagID(), task.getTaskID());
			
			}
		}
	}
	
	public void updateTask(Task task) {
		dbAccess = DBService.getDataAccess();
		dbAccess.updateTask(task);
	}

	public void setTaskSection(Task task, Section newSection) {
		dbAccess = DBService.getDataAccess();
		ArrayList<Task> tasks = getTasksInSection(newSection.getSectionID());
		task.setPriority(tasks.size());
		task.setSectionID(newSection.getSectionID());
		dbAccess.updateTask(task);
	}

	public void setTaskSection(int boardIndex, int sectionIndex, int taskIndex,
			UUID newSectionID) {
		final int MAX_IN_PROGRESS = 6;
		dbAccess = DBService.getDataAccess();
		Section section = dbAccess.getSection(newSectionID);
		Task task = getTask(boardIndex, sectionIndex, taskIndex);
		if (section.isWIP()) {
			ArrayList<Task> taskList = getTasksInSection(section.getSectionID());
			if (taskList.size() < MAX_IN_PROGRESS) {
				setTaskSection(task, section);
			}
		} else {
			setTaskSection(task, section);
		}
	}

	public void deleteTask(UUID taskID, ArrayList<Tag> tags) {
		dbAccess = DBService.getDataAccess();
		if(tags != null){
			if(!tags.isEmpty()){
				int size = tags.size();
				for(int i = 0; i < size; i++){
					dbAccess.deleteTagForTask(tags.get(i).getTagID(), taskID);
				}
			}
		}
		dbAccess.deleteTask(taskID);
	}

	public UUID createTask(Task task, Section section, ArrayList<Tag> tags) {
		dbAccess = DBService.getDataAccess();
		ArrayList<Task> tasks = dbAccess.getTasksInSectionSorted(section
				.getSectionID());
		task.setPriority(tasks.size());
		UUID taskID = dbAccess.insertTask(task, section);
		
		if(tags != null){
			if(!tags.isEmpty()){
				int size = tags.size();
				for(int i = 0; i < size; i++){
					dbAccess.insertTagForTask(taskID, 
							tags.get(i).getTag());
				}
			}
		}
		
		return taskID;
	}

	public Task getTask(int boardIndex, int sectionIndex, int taskIndex) {
		dbAccess = DBService.getDataAccess();
		Board board = dbAccess.getAllBoards().get(boardIndex);
		Section section = dbAccess.getSectionsInBoardSorted(
				board.getBoardID()).get(
				sectionIndex);
		return getTasksInSection(section.getSectionID()).get(taskIndex);
	}

	public Task getTask(UUID taskID) {
		return dbAccess.getTask(taskID);
	}

	public void fixPriorities(UUID sectionID) {
		dbAccess = DBService.getDataAccess();
		ArrayList<Task> tasks = dbAccess.getTasksInSectionSorted(sectionID);
		Collections.sort(tasks);
		for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
			tasks.get(taskIndex).setPriority(taskIndex);
			updateTask(tasks.get(taskIndex));
		}
	}

	public boolean swapTaskPriority(Task task, int direction) {
		Task otherTask;
		int tempPriority;
		boolean successfulSwap = false;
		if(task !=null){
			dbAccess = DBService.getDataAccess();
			ArrayList<Task> tasks = getTasksInSection(task.getSectionID());
			int taskIndex = 0;
			while(!tasks.get(taskIndex).getTaskID().equals(task.getTaskID())) {
				taskIndex++;
			}
		
			if(direction == 1 || direction == -1) {
				taskIndex = taskIndex + direction;
				if (taskIndex >= 0 && taskIndex < tasks.size()) {
					otherTask = tasks.get(taskIndex);

					tempPriority = task.getPriority();
					task.setPriority(otherTask.getPriority());
					otherTask.setPriority(tempPriority);

					updateTask(task,null);
					updateTask(otherTask,null);

					successfulSwap = true;
				}
			}
			else{
				throw new IllegalArgumentException(
						"swapTaskPriority():Illegal Argument Exception: " +
							"must be 1 or -1");
			}
		}
		else{
			throw new IllegalArgumentException(
					"swapTaskPriority():Illegal Argument Exception: " +
						"Argument must not be null.");
		}
		return successfulSwap;
	}

	public void setFilterRule(ArrayList<Tag> filterRules) {
		this.filterRules = filterRules;
	}
	
	public ArrayList<Tag> getTags(UUID taskID){
		return dbAccess.getTagsForTask(taskID);
	}
}
