package tos.business;

import java.util.ArrayList;
import java.util.UUID;

import tos.application.DBService;
import tos.objects.*;
import tos.persistence.DBAccess;

public class AccessBoards {
	private DBAccess dbAccess;

	public AccessBoards() {
		dbAccess = DBService.getDataAccess();
	}

	public ArrayList<Board> getAllBoards() {
		dbAccess = DBService.getDataAccess();
		return dbAccess.getAllBoards();
	}

	public Board getBoard(UUID boardID) {
		dbAccess = DBService.getDataAccess();
		return dbAccess.getBoard(boardID);
	}

	public UUID createBoard(Board board) {
		validateNullObject(board);
		dbAccess = DBService.getDataAccess();
		return dbAccess.insertBoard(board);
	}

	public void deleteBoard(UUID boardID) {
		dbAccess = DBService.getDataAccess();
		dbAccess.deleteBoard(boardID);
	}

	private void validateNullObject(Object o) {
		if (o == null) {
			throw new IllegalArgumentException(
					"ValidaNullObject():Illegal Argument Exception: " +
						"Argument cannot be null.");
		}
	}

	public ArrayList<Tag> getTagList() {
		dbAccess = DBService.getDataAccess();
		return dbAccess.getAllTags();
	}

	public boolean isBoardInDatabase(UUID boardID) {
		return dbAccess.isIDInDatabase(boardID);
	}
}