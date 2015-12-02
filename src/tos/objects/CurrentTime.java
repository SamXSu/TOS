package tos.objects;

import java.util.Calendar;

public class CurrentTime {
	private static Calendar currentTime = Calendar.getInstance();
	private static boolean isCurrentTime = false;
	
	public static boolean createCurrentTime() {
		isCurrentTime = true;
		return isCurrentTime;
	}
	
	public static boolean createCurrentTime(Calendar time) {
		if (time == null)
			throw new IllegalArgumentException("Illegal Argument Exception: Time cannot be null.");
		currentTime.setTime(time.getTime());
		isCurrentTime = false;
		return isCurrentTime;
	}
	
	public static Calendar getCurrentTime() {
		if (isCurrentTime) {
			currentTime = Calendar.getInstance();
		}
		return currentTime;
	}
}
