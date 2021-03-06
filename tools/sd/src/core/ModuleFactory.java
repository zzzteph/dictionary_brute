package core;

import common.Logger;

import core.interfaces.IModule;

public class ModuleFactory {

	public static IModule getModule(String module) {

		try {
			module = module.toLowerCase();
			module = Character.toUpperCase(module.charAt(0))
					+ module.substring(1);
			module = "core.modules." + module;

			Class<?> cl = Class.forName(module);
			return (IModule) cl.getConstructor().newInstance();

		} catch (Exception ex) {
			Logger.error(ex.getMessage());
		}
		return null;
	}

	public static String checkModule(String module) {
		if (getModule(module) != null)
			return module;
		return null;
	}

}