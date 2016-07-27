package core.modules;

import java.util.List;

import common.Logger;

import core.beans.Strings;

public class Dictionary extends ModuleImpl {

	public List<String> run() {
		if (!this.options.containsKey(Strings.DICTIONARY))
			Logger.error("NO DICTIONARY SET");
		this.options.put(Strings.TAIL, options.get(Strings.DICTIONARY));
		return super.run();
	}

}
