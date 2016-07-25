package modules;

import interfaces.IModule;

public abstract class ModuleImpl extends Thread implements IModule {
	
	    protected Process process = null;
	    protected ProcessBuilder builder = null;
	    protected String execPath = "";
	    protected String moduleType = "";
	    protected String inputFile = "";
	    protected String outputFile = "";

	    @Override
	    public void run() {
	        while (true) {
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException ex) {
	                process.destroy();
	                Thread.currentThread().interrupt();
	                return;
	            }
	        }
	    }

	    boolean check(String value) {
	        File f = new File(value);
	        return !(!f.exists() || f.isDirectory());
	    }

	    void printCommandLine(List<String> params) {
	        params.stream().map((tmp) -> {
	            System.out.print(tmp);
	            return tmp;
	        }).forEach((_item) -> {
	            System.out.print(" ");
	        });
	        System.out.println("");
	    }

	    //@Override
	    //   abstract void stop();
	    public ModuleImpl() {
	        values = new HashMap();
	        values.put(Params.HASHCAT, new Settings(false));
	        values.put(Params.OCLHASHCAT, new Settings(false));
	        values.put(Params.DESCRIPTION, new Settings(false));
	        values.put(Params.NAME, new Settings(false));
	        values.put(Params.WORKDIR, new Settings(false));
	        values.put(Params.MODULE, new Settings(false));
	        values.put(Params.MODULE_PATH, new Settings(false));
	        values.put(Params.NAME, new Settings(false));

	    }

	    @Override
	    public boolean setValue(String key, String value) {
	        key = key.toUpperCase();
	        if (values.containsKey(key)) {
	            values.put(key, new Settings(values.get(key).getSettable(), value));
	            return true;
	        }
	        return false;
	    }

	    @Override
	    public boolean setValue(String key, boolean settable, String value) {
	        key = key.toUpperCase();
	        if (values.containsKey(key)) {
	            values.put(key, new Settings(settable, value));
	            return true;
	        }
	        return false;
	    }

	    @Override
	    public List<Settings> getOptions() {
	        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public void help() {
	        System.out.println("This is an abstract module");

	    }

	    @Override
	    public List<String> getStringFields() {
	        List<String> ret = new ArrayList();

	        values.entrySet().stream().forEach((entry) -> {
	            ret.add(entry.getKey());
	        });

	        return ret;
	    }

	    @Override
	    public String getValue(String name) {
	        return values.get(name).getValue();
	    }

	    @Override
	    public Map<String, Settings> getSettings() {

	        return this.values;
	    }

	    @Override
	    public Result check() {
	        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public void clear() {
	        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	    }

	    @Override
	    public Object clone() {
	        try {
	            return super.clone();
	        } catch (Exception e) {
	            return null;
	        }
	    }

	    @Override
	    public void clone(Map<String, Settings> values) {
	        this.values = values;
	    }

	    @Override
	    public boolean status() {

	        if (this.process != null) {
	            return this.process.isAlive();
	        }
	        return false;
	    }

	    @Override
	    public void setRuntime(Map<String, Settings> runtimeParams) {

	        if (runtimeParams.get(GlobalParams.EXEC).getValue().equalsIgnoreCase(GlobalParams.HASHCAT)) {
	            this.execPath = Global.getInstance().getValue(GlobalParams.HASHCAT);
	        }
	        if (runtimeParams.get(GlobalParams.EXEC).getValue().equalsIgnoreCase(GlobalParams.OCLHASHCAT)) {
	            this.execPath = Global.getInstance().getValue(GlobalParams.OCLHASHCAT);
	        }

	        this.moduleType = runtimeParams.get(GlobalParams.MODULE).getValue();

	    }

	    @Override
	    public void setOutputFile(String outputFile) {
	        this.outputFile = outputFile + "_" + this.values.get(Params.NAME).getValue().replace("\\", "_").replace("//", "_");

	    }

	    @Override
	    public void setInputFile(String inputFile) {
	        this.inputFile = inputFile;

	    }

}
