package tests.objects;

import java.util.Calendar;

import tos.objects.CurrentTime;
import junit.framework.TestCase;

public class CurrentTimeTests extends TestCase {
	
	public void setup() {
		System.out.println("Test using CurrentTime Injection");
	}
	
	public void testCreateDefaultTime() {
		assertTrue(CurrentTime.createCurrentTime());
	}
	
	public void testCreateInjectedTime() {
		Calendar currentTime = Calendar.getInstance();
		currentTime.set(2013,07,04,12,18,00);
		CurrentTime.createCurrentTime(currentTime);
		assertEquals(currentTime,CurrentTime.getCurrentTime());
	}
	
	public void testCreateInjectedNullTime() {
		try {
			CurrentTime.createCurrentTime(null);
		}
		catch (IllegalArgumentException e) {
			
		}
	}
}
