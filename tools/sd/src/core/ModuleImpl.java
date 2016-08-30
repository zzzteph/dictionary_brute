package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import common.Utils;

import core.beans.Strings.Common;
import core.interfaces.IModule;

public abstract class ModuleImpl implements IModule {

	
	public List<String> tail = new ArrayList<String>();
	CommandThread run;
	protected Map<String, String> options = new HashMap<String, String>();

	public void setValue(String key, String value) {
		this.options.put(key, value);
	}

	public String getValue(String key) {
		return this.options.get(key);
	}

	public void init() {

	}

	public List<String> run() {
		List<String> cmd = new ArrayList<String>();
		options.put(Common.OUTPUT, Utils.getOutputFile());
		/*
		cmd.add(options.get(Common.EXEC));
		cmd.add("--potfile-disable");
		cmd.add("-m");
		cmd.add(options.get(Common.MODULE));
		cmd.add("-o");
		cmd.add(options.get(Common.OUTPUT));
		if (options.containsKey(Common.RULES)) {
			cmd.add("-r");
			cmd.add(options.get(Common.RULES));
		}
		cmd.add(options.get(Common.INPUT));
		for (String addOptions : tail) {
			cmd.add(addOptions);
		}
		System.out.println("Command" + cmd.toString());
*/
		cmd.add("C:\\Windows\\notepad.exe");
		run=new CommandThread(cmd);
		run.start();
		Scanner keyboard = new Scanner(System.in);
	
		
		while(run.isAlive())
		{
			System.out.println("enter an integer");
			int myint = keyboard.nextInt();
		}
		System.out.println("asdasd");
		List<String> ret = Utils.readFileUniq(options.get(Common.OUTPUT));
		//Utils.deleteFile(options.get(Common.OUTPUT));

		return ret;
	}
}
