package core.modules;

import java.util.List;

import common.Logger;

import core.ModuleImpl;
import core.beans.Strings.Common;

public class Mask extends ModuleImpl {
	public List<String> run() {
		if (!this.options.containsKey(Common.MASK))
			Logger.error("NO CHARSET|BRUTEFORCE OPTIONS SET");

		this.tail.add("-a");
		this.tail.add("3");
		if (options.containsKey(Common.CHARSET_1)) {
			tail.add("-1");
			tail.add(options.get(Common.CHARSET_1));

		}
		if (options.containsKey(Common.CHARSET_2)) {
			tail.add("-2");
			tail.add(options.get(Common.CHARSET_2));
		}
		if (options.containsKey(Common.CHARSET_3)) {
			tail.add("-3");
			tail.add(options.get(Common.CHARSET_3));

		}
		tail.add(options.get(Common.MASK));
		return super.run();
	}

}
