package core.beans;

import java.util.HashMap;
import java.util.Map;

public class CommandLine {
	private static CommandLine instance;
	Map<String, String> options = new HashMap<String, String>();

	public static synchronized CommandLine getInstance() {
		if (instance == null) {
			instance = new CommandLine();
		}
		return instance;
	}

	public void add(String key, String value) {
		key = key.toUpperCase();
		value = value.trim();
		options.put(key, value);

	}

	public Map<String, String> getOptions() {
		return this.options;
	}

	public String getOption(String key) {
		return this.options.get(key);

	}

}
