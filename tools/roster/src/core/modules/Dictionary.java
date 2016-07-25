package core.modules;
import common.Logger;
public class Dictionary extends ModuleImpl{

	final String DICTIONARY="DICTIONARY";
	public void run() {
		if(!this.options.containsKey(this.DICTIONARY))
			Logger.error("NO DICTIONARY SET");
		this.options.put(this.TAIL, options.get(this.DICTIONARY));
		super.run();
	}

}
