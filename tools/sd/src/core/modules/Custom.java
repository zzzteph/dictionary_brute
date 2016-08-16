package core.modules;

import java.util.List;

import common.Logger;
import core.beans.Strings;

public class Custom  extends ModuleImpl {
	public List<String> run() {
		if (!this.options.containsKey(Strings.CUSTOM))
			Logger.error("NO CUSTOM SET");
		this.options.put(Strings.TAIL, options.get(Strings.CUSTOM));
		return super.run();
	}

}
