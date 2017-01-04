package core;
import java.util.List;
import java.util.Map.Entry;

import core.beans.CommandLine;
import core.beans.Options;

import core.interfaces.IModule;

public class Runner {
	private static Runner instance;
	public static synchronized Runner getInstance() {
		if (instance == null) {
			instance = new Runner();
		}
		return instance;
	}



	public List<String> runModule(Options options) {
		IModule module = ModuleFactory.getModule(options.getModule());
		
		for (Entry<String, String> entry : options.getOptions().entrySet()) {
			module.setValue(entry.getKey(), entry.getValue());
		}

		for (Entry<String, String> entry : CommandLine.getInstance()
				.getOptions().entrySet()) {
			module.setValue(entry.getKey(), entry.getValue());
		}

		return module.run();


	}

}
