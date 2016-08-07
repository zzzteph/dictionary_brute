package core.modules;

import java.util.List;

import common.Logger;
import core.beans.Strings;

public class Mask extends ModuleImpl{
	public List<String> run() {
		if (!this.options.containsKey(Strings.MASK))
			Logger.error("NO CHARSET|BRUTEFORCE OPTIONS SET");

		
		StringBuffer tail=new StringBuffer();
		tail.append("-a 3");
		tail.append(" ");
		if(options.containsKey(Strings.CHARSET_1))
		{
			tail.append("-1 ");
			tail.append(options.get(Strings.CHARSET_1));
			tail.append(" ");
		}
		if(options.containsKey(Strings.CHARSET_2))
		{
			tail.append("-2 ");
			tail.append(options.get(Strings.CHARSET_2));
			tail.append(" ");
		}
		if(options.containsKey(Strings.CHARSET_3))
		{
			tail.append("-3 ");
			tail.append(options.get(Strings.CHARSET_3));
			tail.append(" ");
		}
		tail.append(options.get(Strings.MASK));

		this.options.put(Strings.TAIL, tail.toString());
		return super.run();
	}

}
