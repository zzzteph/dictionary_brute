package core.modules;

import java.util.List;

import common.Logger;
import core.beans.Strings.Common;

public class Custom  extends ModuleImpl {
	public List<String> run() {
		if (!this.options.containsKey(Common.CUSTOM))
			Logger.error("NO CUSTOM SET");
		this.options.put(Common.TAIL, options.get(Common.CUSTOM));
		return super.run();
	}

}
