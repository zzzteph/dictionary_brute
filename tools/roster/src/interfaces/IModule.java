package interfaces;

import java.util.List;

import beans.ModuleOptions;
public interface IModule {
    
	public void run();
	public void help();
    public boolean setValue(String key, String value);
    public List<ModuleOptions> getValues();
    public String getValue(String name);
    public void start();
    public boolean status();
    public void interrupt();

}
