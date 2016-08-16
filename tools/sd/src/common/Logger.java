package common;

public class Logger {

	public static void error(String error)
	{
		System.out.println(error);
		System.exit(1);
	}
}
