package tos.objects;

import java.util.UUID;

public class Tag implements Comparable<Tag>{
	private String tag;
	private UUID tagID;
	private UUID taskID;
	
	private final UUID UNDEFINED = new UUID(0, 0);
	
	public Tag(String tag, UUID tagID, UUID taskID) {
		init(tag, tagID, taskID);
	}
	
	public Tag(String tag, UUID taskID) {
		init(tag, UNDEFINED, taskID);
	}
	
	public Tag(String tag){
		init(tag, UNDEFINED, UNDEFINED);
	}
	
	private void init(String tag, UUID tagID, UUID taskID) {
		validateNullObject(tagID);
		validateNullObject(taskID);
		validateString(tag);
		this.tag = tag;
		this.tagID = tagID;
		this.taskID = taskID;
	}
	
	public UUID getTagID(){
		return tagID;
	}
	
	public UUID getTaskID(){
		return taskID;
	}
	
	public String toString(){
		return tag;
	}
	
	public String getTag(){
		return tag;
	}
	
	public int compareTo(Tag tag){
		return this.tag.compareTo(tag.getTag());
	}
	
	private void validateString(String str) {
		validateNullObject(str);
		if (str.equals("")) {
			throw new IllegalArgumentException(
					"IllegalArgumentException: String cannot be empty.");
		}
	}

	private void validateNullObject(Object o) {
		if (o == null) {
			throw new IllegalArgumentException(
					"Illegal Argument Exception: Argument cannot be null.");
		}
	}
}
