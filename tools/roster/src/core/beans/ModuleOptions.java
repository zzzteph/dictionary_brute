package core.beans;

import java.util.Map;

import common.Logger;
import core.*;
public class ModuleOptions {
	String module;
	Map<String,String> options;
	public ModuleOptions(String module)
	{
		if((this.module=ModuleFactory.checkModule(module))!=null)
		{
			Logger.error("Module not exist");
		}
	}
}
