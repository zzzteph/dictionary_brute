package core.interfaces;

import java.util.List;

public interface IModule {

	public void init();

	public List<String> run();

	public void setValue(String key, String value);

	public String getValue(String key);

	boolean checkOptions();
}
