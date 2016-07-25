package core.modules;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import common.Logger;
import core.interfaces.IModule;

public abstract class ModuleImpl implements IModule {
	
	    protected Process process = null;
	    protected ProcessBuilder builder = null;
	    Map<String,String> options;
	    
	    final String RULES = "RULES";
	    final String MODULE = "MODULE";
	    final String EXEC = "EXEC";
	    final String INPUT = "INPUT";
	    final String OUTPUT = "OUTPUT";
	    final String TAIL = "TAIL";
	    
	    public void setValue(String key, String value)
	    {
	    	this.options.put(key, value);
	    }

	    public String getValue(String key)
	    {
	    	return this.options.get(key);
	    }
		 public void run() {
			StringBuffer cmd=new StringBuffer();
			cmd.append(options.get(this.EXEC));
			cmd.append(" ");
			cmd.append("--potfile-disable");
			cmd.append(" ");
			cmd.append("-m");
			cmd.append(" ");
			cmd.append(options.get(this.MODULE));
			cmd.append(" ");
			cmd.append("-o");
			cmd.append(" ");
			cmd.append(options.get(this.OUTPUT));
			if(options.containsKey(this.RULES))
			{
				cmd.append(" ");
				cmd.append("-r");
				cmd.append(" ");
				cmd.append(options.get(this.RULES));
			}
			cmd.append(" ");
			cmd.append(options.get(this.INPUT));
			cmd.append(" ");
			cmd.append(options.get(this.TAIL));
			Process p;
			try {
				p = Runtime.getRuntime().exec(cmd.toString());
		
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));

			while ((stdOut.readLine()) != null)
				;

			System.out.println("Batch file done.");
			} catch (IOException e) {
				
				Logger.error(e.getMessage());
			}
		}
}
