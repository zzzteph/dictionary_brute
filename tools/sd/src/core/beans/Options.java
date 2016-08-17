package core.beans;

import java.util.HashMap;
import java.util.Map;

import common.Logger;
import core.ModuleFactory;
import core.beans.Strings.Common;

public class Options {
	String module;
	Map<String, String> options;

	public Options(String module) {
		options = new HashMap<String, String>();
		if ((this.module = ModuleFactory.checkModule(module)) == null) {
			Logger.error("Module not exist");
		}

	}

	public void add(String key, String value) {
		key = key.toUpperCase();
		value = value.trim();
		value = value.replace("$MAINDIR", CommandLine.getInstance()
				.getOption(Common.MAINDIR));
		options.put(key, value);
	}

	public String getModule() {
		return this.module;
	}

	public Map<String, String> getOptions() {
		return this.options;
	}

}
