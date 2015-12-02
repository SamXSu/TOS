package tos.objects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.Date;

public class Task implements Comparable<Task> {
	private UUID taskID;
	private UUID sectionID;
	private String title;
	private Calendar dueDate;
	private String detail;
	private String comment;
	private int priority;
	private Calendar startingTime;
	private long runningTime;
	private boolean running;
	private static Calendar currentTime;
	
	private final UUID UNDEFINED = new UUID(0, 0);

	public Task() {
		init(UNDEFINED,UNDEFINED," ",Calendar.getInstance()," "," ",100,
				Calendar.getInstance(),0,true);
	}

	public Task(String title, Calendar dueDate, String detail,
			String comment,int priority) {
		init(UNDEFINED,UNDEFINED,title,dueDate,detail,comment, priority,
				Calendar.getInstance(),0,true);
	}

	public Task(UUID taskID, UUID sectionID, String title,
			Calendar dueDate, String detail, String comment, int priority,
			Calendar startingTime, long runningTime, boolean running) {
		init(taskID,sectionID,title,dueDate,detail,comment, priority,
				startingTime, runningTime, running);
	}
	
	private void init(UUID taskID, UUID sectionID, String title,
			Calendar dueDate, String detail, String comment, int priority,
			Calendar startingTime, long runningTime, boolean running){
		validateNullObject(taskID);
		validateNullObject(sectionID);
		validateNullObject(title);
		validateNullObject(detail);
		validateNullObject(comment);
		validateNullObject(dueDate);
		validateIntegers(priority);
		
		CurrentTime.createCurrentTime();
		this.taskID = taskID;
		this.sectionID = sectionID;
		this.title = title;
		this.dueDate = dueDate;
		this.detail = detail;
		this.comment = comment;
		this.priority = priority;
		this.startingTime = startingTime;
		this.runningTime = runningTime;
		this.running = running;
	}
	
	public void setTitle(String title) {
		validateNullObject(title);
		this.title = title;
	}

	public void setDueDate(Calendar dueDate) {
		validateNullObject(dueDate);
		this.dueDate = dueDate;
	}

	public void setDetail(String detail) {
		validateNullObject(detail);
		this.detail = detail;
	}

	public void setComment(String comment) {
		validateNullObject(comment);
		this.comment = comment;
	}

	public void setTags(ArrayList<Tag> tags) {
		validateNullObject(tags);
	}

	public void setSectionID(UUID sectionID) {
		validateNullObject(sectionID);
		this.sectionID = sectionID;
	}

	public void setTaskID(UUID taskID) {
		validateNullObject(taskID);
		this.taskID = taskID;
	}

	public void setPriority(int priority) {
		validateNullObject(priority);
		validateIntegers(priority);
		this.priority = priority;
	}

	public String getTitle() {
		return title;
	}

	public UUID getSectionID() {
		return sectionID;
	}

	public UUID getTaskID() {
		return taskID;
	}

	public String getComment() {
		return comment;
	}

	public String getDetail() {
		return detail;
	}

	public Date getDueDate() {
		return dueDate.getTime();
	}

	public int getPriority() {
		return priority;
	}

	public int compareTo(Task otherTask) {
		return Integer.valueOf(this.getPriority()).compareTo(
				otherTask.getPriority());
	}

	private void validateIntegers(int value) {
		if (value < 0) {
			throw new IllegalArgumentException(
					"Illegal Argument Exception: Integer value must be greater than 0.");
		}
	}

	private void validateNullObject(Object o) {
		if (o == null) {
			throw new IllegalArgumentException(
					"Illegal Argument Exception: Argument cannot be null.");
		}
	}
	
	public Task deepCopy() {
		return new Task(taskID, sectionID, title, dueDate, detail, comment, priority,
							startingTime, runningTime, running);
	}

	public Date getStartTime(){
		return this.startingTime.getTime();
	}
	
	public long getRunningTime(){
		return this.runningTime;
	}
	
	public long getRuntime(){
		currentTime = CurrentTime.getCurrentTime();
		long l = this.runningTime;
		if(running){
			l += currentTime.getTime().getTime() - startingTime.getTime().getTime();
		}
		return l;
	}
	
	public boolean isRunning(){
		return this.running;
	}
	
	public void switchState(){
		currentTime = CurrentTime.getCurrentTime();
		if(this.running){
			this.running = false;
			this.runningTime += (currentTime.getTime().getTime() - startingTime.getTime().getTime());
		}else{
			this.running = true;
			startingTime.setTime(currentTime.getTime());
		}
	}

}
