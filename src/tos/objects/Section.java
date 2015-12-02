package tos.objects;
import java.util.UUID;

public class Section implements Comparable<Section> {
	private UUID sectionID;
	private UUID boardID;
	private String title;
	private boolean workInProgress;
	private int sortOrder;
	
	public Section(UUID sectionID, UUID boardID, String title
			, boolean workInProgress, int sortOrder) {
		init(sectionID,boardID,title,workInProgress,sortOrder);
	}

	public Section(String title) {
		// set default parameters
		init(new UUID(100,2),new UUID(200,3),title,false,100);
	}

	private void init(UUID sectionID, UUID boardID, String title,
			boolean workInProgress, int sortOrder){
		validateNullObject(sectionID);
		validateNullObject(boardID);
		validateNullObject(title);
		validateIntegers(sortOrder);
		
		this.sectionID = sectionID;
		this.boardID = boardID;
		this.title = title;
		this.workInProgress = workInProgress;
		this.sortOrder = sortOrder;
	}
	
	public boolean isWIP(){
		return workInProgress;
	}
	
	private void validateIntegers(int value) {
		if (value < 0) {
			throw new IllegalArgumentException(
					"Illegal Argument Exception:" +
						" Integer value must be greater than 0.");
		}
	}
	private void validateNullObject(Object o) {
		if (o == null) {
			throw new IllegalArgumentException(
					"Illegal Argument Exception: Argument cannot be null.");
		}
	}

	public void setSectionID(UUID sectionID) {
		validateNullObject(sectionID);
		this.sectionID = sectionID;
	}

	public void setBoardID(UUID boardID) {
		validateNullObject(boardID);
		this.boardID = boardID;
	}
	
	public void setTitle(String title) {
		validateNullObject(title);
		this.title = title;
	}
	
	public void setSortOrder(int order) {
		validateIntegers(order);
		this.sortOrder = order;
	}

	public UUID getBoardID() {
		return boardID;
	}

	public UUID getSectionID() {
		return sectionID;
	}

	public String getTitle() {
		return title;
	}	
	
	public int getSortOrder() {
		return sortOrder;
	}
	
	public Section deepCopy() {
		return new Section(sectionID, boardID, title, workInProgress, sortOrder);
	}

	public int compareTo(Section otherSection) {
		return Integer.valueOf(this.getSortOrder()).compareTo(
				otherSection.getSortOrder());
	}
}
