package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import common.Utils;

import core.beans.ModuleOptions;
import core.beans.Strings;
import core.interfaces.IModule;

public class ModuleRunner {
	private static ModuleRunner instance;
	private static List<String> results = new ArrayList<String>();

	public static synchronized ModuleRunner getInstance() {
		if (instance == null) {
			instance = new ModuleRunner();
		}
		return instance;
	}

	private void rebuildInputFile() {
		List<String> file = Utils.readFile(GlobalOptions.getInstance()
				.getOption(Strings.INPUT));
		for (String crackedPasswords : results) {
			int i = 0;
			for (String uncrackedPasswords : file) {
				if (crackedPasswords.contains(uncrackedPasswords)) {
					file.remove(i);

					break;
				}
				i++;
			}
		}

		Utils.writeToFile(GlobalOptions.getInstance().getOption(Strings.INPUT),
				file);

	}

	public void runModule(ModuleOptions options) {
		IModule module = ModuleFactory.getModule(options.getModule());
		System.out.println(options.getModule());
		for (Entry<String, String> entry : options.getOptions().entrySet()) {
			module.setValue(entry.getKey(), entry.getValue());
		}

		for (Entry<String, String> entry : GlobalOptions.getInstance()
				.getOptions().entrySet()) {
			module.setValue(entry.getKey(), entry.getValue());
		}

		results.addAll(module.run());
		rebuildInputFile();
	}

}
