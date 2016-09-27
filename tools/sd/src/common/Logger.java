package common;

public class Logger {

	public static void error(String error) {
		System.out.println("ERROR:" + error);
		System.exit(1);
	}

	public static void info(String error) {
		System.out.println("INFO:" + error);
	}

}
