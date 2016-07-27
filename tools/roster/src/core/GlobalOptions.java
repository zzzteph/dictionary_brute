package core;

import java.util.HashMap;
import java.util.Map;

public class GlobalOptions {
	private static GlobalOptions instance;
	Map<String, String> options = new HashMap<String, String>();

	public static synchronized GlobalOptions getInstance() {
		if (instance == null) {
			instance = new GlobalOptions();
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
