package tests.business;

import java.util.UUID;

import tests.persistence.DBAccessStub;
import tos.application.DBService;
import tos.business.AccessBoards;
import tos.business.AccessSections;
import tos.objects.Board;
import tos.objects.Section;
import tos.persistence.DBAccess;
import junit.framework.TestCase;

public class AccessSectionsTests extends TestCase  {
	private AccessSections accessSections;
	private AccessBoards accessBoards;
	
	public void setUp() {
		try {
			DBService.createDataAccess(new DBAccessStub("DB"));
		} catch (Exception e) {
			
		}
		accessBoards = new AccessBoards();
		accessSections = new AccessSections();
	}
	
	public void tearDown() {
		try {
			DBService.closeDataAccess();
		}
		catch (Exception e) {
			
		}
	}
	
	public void testNullArguments() {
		try {
			accessSections.getSectionsOnBoard(null);
			fail("accessSections.getSectiononBoard()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessSections.createSection(null, new Board("test"));
			fail("accessSections.createSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessSections.createSection(new Section("test"), null);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessSections.createSection(null, null);
			fail("accreSections.createSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessSections.deleteSection(null);
			fail("accessSections.deleteSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessSections.getSection(null);
			fail("accessSections.deleteSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessSections.switchSections(null, new UUID(2,1));
			fail("accessSections.deleteSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessSections.switchSections(new UUID(2,1),null);
			fail("accessSections.deleteSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testSwitchSectionsValidData() {
		Section section1 = accessSections.getSection(new UUID(5,0));
		Section section2 = accessSections.getSection(new UUID(5,1));
		int s1_so = section1.getSortOrder();
		int s2_so = section2.getSortOrder();
		accessSections.switchSections(new UUID(5,0), new UUID(5,1));
		section1 = accessSections.getSection(new UUID(5,0));
		section2 = accessSections.getSection(new UUID(5,1));
		assertEquals(section1.getSortOrder(),s2_so);
		assertEquals(section2.getSortOrder(),s1_so);
	}
	
	public void testArgumentNotInDatabase() {
		try {
			accessSections.createSection(new Section("Test Section"),
					new Board("Test Board"));
			fail("accessSections.createSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessSections.deleteSection(new UUID(1,1));
			fail("accessSections.deleteSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessSections.switchSections(new UUID(9,9),new UUID(8,8));
			fail("accessSections.deleteSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			accessSections.switchSections(new UUID(9,9),new UUID(5,1));
			fail("accessSections.deleteSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}	
		
		try {
			accessSections.switchSections(new UUID(5,1),new UUID(8,8));
			fail("accessSections.deleteSection()" +
					" Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException iae) {
			
		}	
	}
	
	public void testNoSectionsOnBoard() {
		Board newBoard = new Board("Test Board");
		accessBoards.createBoard(newBoard);
		assertTrue(accessSections.getSectionsOnBoard
				(newBoard.getBoardID()).isEmpty());
		accessBoards.deleteBoard(newBoard.getBoardID());
	}
	
	public void testInsertDeleteManySectionsOnBoard() {
		Board newBoard = new Board("Test Board");
		Section[] newSection = new Section[8];
		accessBoards.createBoard(newBoard);
		
		newSection[0] = new Section("Test Section 1");
		accessSections.createSection(newSection[0], newBoard);
		assertEquals(1,accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).size());
		assertTrue(accessSections.getSectionsOnBoard
				(newBoard.getBoardID()).contains(newSection[0]));
		
		newSection[1] = new Section("Test Section 2");
		accessSections.createSection(newSection[1], newBoard);
		assertEquals(2,accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).size());
		assertTrue(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[1]));
		
		newSection[2] = new Section("Test Section 3");
		accessSections.createSection(newSection[2], newBoard);
		assertEquals(3,accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).size());
		assertTrue(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[2]));
		
		newSection[3] = new Section("Test Section 4");
		accessSections.createSection(newSection[3], newBoard);
		assertEquals(4,accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).size());
		assertTrue(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[3]));
		
		newSection[4] = new Section("Test Section 5");
		accessSections.createSection(newSection[4], newBoard);
		assertEquals(5,accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).size());
		assertTrue(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[4]));
		
		newSection[5] = new Section("Test Section 6");
		accessSections.createSection(newSection[5], newBoard);
		assertEquals(6,accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).size());
		assertTrue(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[5]));
		
		newSection[6] = new Section("Test Section 7");
		accessSections.createSection(newSection[6], newBoard);
		assertEquals(7,accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).size());
		assertTrue(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[6]));
		
		newSection[7] = new Section("Test Section 8");
		accessSections.createSection(newSection[7], newBoard);
		assertEquals(8,accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).size());
		assertTrue(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[7]));
		
		accessSections.deleteSection(newSection[0].getSectionID());
		assertFalse(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[0]));
		accessSections.deleteSection(newSection[1].getSectionID());
		assertFalse(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[1]));
		accessSections.deleteSection(newSection[2].getSectionID());
		assertFalse(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[2]));
		accessSections.deleteSection(newSection[3].getSectionID());
		assertFalse(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[3]));
		accessSections.deleteSection(newSection[4].getSectionID());
		assertFalse(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[4]));
		accessSections.deleteSection(newSection[5].getSectionID());
		assertFalse(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[5]));
		accessSections.deleteSection(newSection[6].getSectionID());
		assertFalse(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[6]));
		accessSections.deleteSection(newSection[7].getSectionID());
		assertFalse(accessSections.getSectionsOnBoard(
				newBoard.getBoardID()).contains(newSection[7]));
		
		accessBoards.deleteBoard(newBoard.getBoardID());		
	}
	
	public void testMethodsOnNullConnection() {
		DBAccess dbAccess = null;
		Board board = new Board("blah");
		Section section = new Section("blah");
		
		try {
			DBService.createDataAccess(dbAccess);
			accessSections.createSection(section, board);
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
		
		try {
			DBService.createDataAccess(dbAccess);
			accessSections.deleteSection(section.getSectionID());
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
		
		try {
			DBService.createDataAccess(dbAccess);
			accessSections.getSectionsOnBoard(board.getBoardID());
			fail("Should have thrown illegal argument exception.");
		}
		catch(IllegalArgumentException npe) {
			
		}
	}	
	
	public void testNeverReturnNull() {
		assertNotNull(accessBoards);
	}
}
