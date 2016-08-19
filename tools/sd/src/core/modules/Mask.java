package core.modules;

import java.util.List;

import common.Logger;
import core.ModuleImpl;
import core.beans.Strings.Common;

public class Mask extends ModuleImpl{
	public List<String> run() {
		if (!this.options.containsKey(Common.MASK))
			Logger.error("NO CHARSET|BRUTEFORCE OPTIONS SET");

		
		StringBuffer tail=new StringBuffer();
		tail.append("-a 3");
		tail.append(" ");
		if(options.containsKey(Common.CHARSET_1))
		{
			tail.append("-1 ");
			tail.append(options.get(Common.CHARSET_1));
			tail.append(" ");
		}
		if(options.containsKey(Common.CHARSET_2))
		{
			tail.append("-2 ");
			tail.append(options.get(Common.CHARSET_2));
			tail.append(" ");
		}
		if(options.containsKey(Common.CHARSET_3))
		{
			tail.append("-3 ");
			tail.append(options.get(Common.CHARSET_3));
			tail.append(" ");
		}
		tail.append(options.get(Common.MASK));

		this.options.put(Common.TAIL, tail.toString());
		return super.run();
	}

}
