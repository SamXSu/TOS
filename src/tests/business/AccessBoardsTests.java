package tests.business;

import java.util.UUID;

import tests.persistence.DBAccessStub;
import tos.business.AccessBoards;
import tos.application.DBService;
import tos.objects.Board;
import tos.persistence.DBAccess;
import junit.framework.TestCase;

public class AccessBoardsTests extends TestCase  {
	private AccessBoards accessBoards;
	
	public void setUp() {
		try {
			DBService.createDataAccess(new DBAccessStub("DB"));
		} catch (Exception e) {
			
		}
		accessBoards = new AccessBoards();
	}
	
	public void tearDown() {
		try {
			DBService.closeDataAccess();
		}
		catch (Exception e) {
			
		}
	}
	
	public void testRetriveNonExistantData() {
		try {
			accessBoards.getBoard(new UUID(9,9));
		}
		catch (Exception e) {
			
		}
	}
	
	public void testNeverReturnNull() {
		assertNotNull(accessBoards);
	}
	
	public void testInsertDeleteValidArgument() {
		Board board = new Board("blah");
		accessBoards.createBoard(board);
		assertTrue(accessBoards.isBoardInDatabase(board.getBoardID()));
		
		accessBoards.deleteBoard(board.getBoardID());
		assertFalse(accessBoards.isBoardInDatabase(board.getBoardID()));
	}
	
	public void testNullArguments() {
		try {
			accessBoards.createBoard(null);
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessBoards.deleteBoard(null);
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessBoards.getBoard(null);
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessBoards.isBoardInDatabase(null);
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testMethodsOnNullConnection() {
		DBAccess dbAccess = null;
		Board board = new Board("blah");
		
		try {
			DBService.createDataAccess(dbAccess);
			accessBoards.createBoard(board);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
		
		try {
			DBService.createDataAccess(dbAccess);
			accessBoards.deleteBoard(board.getBoardID());
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
		
		try {
			DBService.createDataAccess(dbAccess);
			accessBoards.getAllBoards();
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
		
		try {
			DBService.createDataAccess(dbAccess);
			accessBoards.getTagList();
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
		
		try {
			DBService.createDataAccess(dbAccess);
			accessBoards.isBoardInDatabase(new UUID(1,1));
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
	}
}
