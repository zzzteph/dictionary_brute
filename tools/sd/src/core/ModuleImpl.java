package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Logger;
import common.Utils;

import core.beans.Strings.Common;
import core.interfaces.IModule;

public abstract class ModuleImpl implements IModule {

	public List<String> tail = new ArrayList<String>();
	protected Process process = null;
	protected Map<String, String> options = new HashMap<String, String>();

	public void setValue(String key, String value) {
		this.options.put(key, value);
	}

	public String getValue(String key) {
		return this.options.get(key);
	}

	public void init() {

	}

	public final boolean checkOptions() {
		// check exec file
		if (!Utils.checkFileExist(options.get(Common.EXEC))) {
			Logger.warning(Common.EXEC + " not exists");
			return false;
		}
		if (!Utils.checkFileExist(options.get(Common.WORKFILE))) {
			Logger.warning(Common.WORKFILE + " not exists");
			return false;
		}

		// check needfull data

		if (options.containsKey(Common.RULES)) {
			if (!Utils.checkFileExist(options.get(Common.RULES))) {
				Logger.warning(Common.RULES + " not exists");
				return false;
			}
		}
		//check dictionary
		if (options.containsKey(Common.DICTIONARY)) {
			if (!Utils.checkFileExist(options.get(Common.DICTIONARY))) {
				Logger.warning(Common.DICTIONARY + " not exists");
				return false;
			}
		}
		
		

		return true;
	}

	public List<String> run() {
		ProcessBuilder command = new ProcessBuilder();
		List<String> cmd = new ArrayList<String>();
		options.put(Common.OUTPUT, Utils.getOutputFile());

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
		cmd.add(options.get(Common.WORKFILE));
		for (String addOptions : tail) {
			cmd.add(addOptions);
		}
		checkOptions();
		Logger.debug("Command" + cmd.toString());

		try {
			command = new ProcessBuilder(cmd);
			process = command.start();

			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				Logger.debug(line);
			}
			int exitVal = process.waitFor();

			in.close();
			Logger.debug("Process exited with value:" + exitVal);
		} catch (IOException e) {
			Logger.error(e.getMessage());
		} catch (InterruptedException e) {
			Logger.error(e.getMessage());
		}

		List<String> ret = Utils.readFileUniq(options.get(Common.OUTPUT));
		Utils.deleteFile(options.get(Common.OUTPUT));

		return ret;
	}
}
