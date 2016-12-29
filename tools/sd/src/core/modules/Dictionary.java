package core.modules;

import java.io.File;
import java.io.IOException;
import java.util.List;

import common.Logger;

import core.ModuleImpl;
import core.beans.Strings.Common;

public class Dictionary extends ModuleImpl {

	public List<String> run() {
		if (!this.options.containsKey(Common.DICTIONARY))
			Logger.error("NO DICTIONARY SET");

		try {
			tail.add(new File(options.get(Common.DICTIONARY))
					.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage());
		}
		return super.run();
	}

}
