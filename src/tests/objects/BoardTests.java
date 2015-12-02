package tests.objects;

import java.util.UUID;

import tos.objects.*;
import junit.framework.TestCase;

public class BoardTests extends TestCase {	
	public void testMutatorsValidArguments() {
		Board board = new Board(new UUID(1,1),"Test");
		board.setName("Blah");
		assertEquals(board.getName(),"Blah");
		
		board.setBoardID(new UUID(2,2));
		assertEquals(board.getBoardID(),new UUID(2,2));
	}
	
	public void testMutatorsNullArguments() {
		Board board = new Board(new UUID(1,1),"Test");
		try {
			board.setName(null);
			fail("testMutatorsNullArguments()" +
					" Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			board.setBoardID(null);
			fail("testMutatorsNullArguments()" +
					" Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}
	
	public void testMutatorEmptyString() {
		Board board = new Board("Test");
		board.setName("");
		assertEquals("",board.getName());
	}
	
	public void testConstructorsValidData() {
		Board board = new Board("Test");
		assertEquals(board.getName(),"Test");
		
		board = new Board(new UUID(1,1),"Test");
		assertEquals(board.getName(),"Test");
		assertEquals(board.getBoardID(),new UUID(1,1));
	}
	
	public void testConstructorsEmptyString() {
		assertNotNull(new Board(""));
		assertNotNull(new Board(new UUID(1,1),""));
	}
	
	public void testConstructorNullArguments() {
		try {
			new Board(new UUID(1,1),null);
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Board(null,"blah");
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Board(null,null);
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
		
		try {
			new Board(null);
			fail("Illegal argument exception should have been thrown.");
		}
		catch(IllegalArgumentException iae) {
			
		}
	}

	public void testAccessorsNotNull() {
		Board board = new Board(new UUID(1,1),"Blah");
		assertNotNull(board.getName());
		assertNotNull(board.getBoardID());
	}
}
