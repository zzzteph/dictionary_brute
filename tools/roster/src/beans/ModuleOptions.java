package beans;

import java.util.Map;

import common.Error;
import core.*;
public class ModuleOptions {
	String module;
	Map<String,String> options;
	public ModuleOptions(String module)
	{
		if((this.module=ModuleFactory.checkModule(module))!=null)
		{
			Error.error("Module not exist");
		}
	}
}
