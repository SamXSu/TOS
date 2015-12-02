package tos.objects;
import java.util.UUID;

public class Board {
	private UUID boardID;
	private String name;
	
	public Board(String name) {
		validateNullObject(name);
		this.name = name;
	}

	public Board(UUID boardID,String name){
		validateNullObject(name);
		validateNullObject(boardID);
		this.name = name;
		this.boardID = boardID;
	}

	public UUID getBoardID() {
		return boardID;
	}

	public void setName(String name) {
		validateNullObject(name);
		this.name = name;
	}

	private void validateNullObject(Object o) {
		if (o == null) {
			throw new IllegalArgumentException(
					"Illegal Argument Exception: Argument cannot be null.");
		}
	}

	public String getName() {
		return name;
	}

	public void setBoardID(UUID boardID) {
		validateNullObject(boardID);
		this.boardID = boardID;
	}
	
	public Board deepCopy() {
		return new Board(boardID,name);
	}
}
