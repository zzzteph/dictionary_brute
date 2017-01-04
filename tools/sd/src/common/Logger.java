package common;

import core.beans.CommandLine;
import core.beans.Strings.Common;

public class Logger {

	public static void error(String error) {
		System.out.println("ERROR:" + error);
		System.exit(1);
	}

	public static void info(String info) {
		System.out.println(info);
	}
	public static void warning(String info) {
		System.out.println(info);
	}

	public static void debug(String info) {
		if(CommandLine.getInstance().getOption(Common.DEBUG)!=null)
		System.out.println("DEBUG:" + info);
	}

}
