package tos.application;

import tos.presentation.*;

public class Main {
	public static final String dbName = "TOS";

	public static void main(String[] args) throws Exception {
		new SplashScreen();
		startUp();
		new MainWindow();
		shutDown();
		System.out.println("Exiting.");
	}

	public static void startUp() throws Exception {
		DBService.createDataAccess(dbName,"HSQL");
	}

	public static void shutDown() throws Exception {
		DBService.closeDataAccess();
	}
}
