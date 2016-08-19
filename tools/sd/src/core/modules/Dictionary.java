package core.modules;

import java.util.List;

import common.Logger;

import core.ModuleImpl;
import core.beans.Strings.Common;

public class Dictionary extends ModuleImpl {

	public List<String> run() {
		if (!this.options.containsKey(Common.DICTIONARY))
			Logger.error("NO DICTIONARY SET");
		tail.add(options.get(Common.DICTIONARY));
		return super.run();
	}

}
