package core.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Logger;
import common.Utils;

import core.beans.Strings;
import core.interfaces.IModule;

public abstract class ModuleImpl implements IModule {

	protected Process process = null;
	protected ProcessBuilder builder = null;
	protected Map<String, String> options = new HashMap<String, String>();

	public void setValue(String key, String value) {
		this.options.put(key, value);
	}

	public String getValue(String key) {
		return this.options.get(key);
	}

	public List<String> run() {

		StringBuffer cmd = new StringBuffer();
		options.put(Strings.OUTPUT, Utils.getOutputFile());
		cmd.append(options.get(Strings.EXEC));
		cmd.append(" ");
		cmd.append("--potfile-disable");
		cmd.append(" ");
		cmd.append("-m");
		cmd.append(" ");
		cmd.append(options.get(Strings.MODULE));
		cmd.append(" ");
		cmd.append("-o");
		cmd.append(" ");

		cmd.append(options.get(Strings.OUTPUT));
		if (options.containsKey(Strings.RULES)) {
			cmd.append(" ");
			cmd.append("-r");
			cmd.append(" ");
			cmd.append(options.get(Strings.RULES));
		}
		cmd.append(" ");
		cmd.append(options.get(Strings.INPUT));
		cmd.append(" ");
		cmd.append(options.get(Strings.TAIL));
		// System.out.println(cmd.toString());
		Process p;
		try {
			p = Runtime.getRuntime().exec(cmd.toString());

			BufferedReader stdOut = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			while ((stdOut.readLine()) != null)
				;

		} catch (IOException e) {

			Logger.error(e.getMessage());
		}
		List<String> ret = Utils.readFile(options.get(Strings.OUTPUT));
		Utils.deleteFile(options.get(Strings.OUTPUT));
		for (String temp : ret) {
			System.out.println(temp);
		}
		return ret;
	}
}
