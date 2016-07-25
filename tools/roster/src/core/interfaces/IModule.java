package core.interfaces;

public interface IModule {
    
	public void run();
    public void setValue(String key, String value);
    public String getValue(String key);

}
