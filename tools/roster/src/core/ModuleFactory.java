package core;

import interfaces.IModule;

public class ModuleFactory {

    public static IModule getModule(String module) {

        try {
            module=Character.toUpperCase(module.charAt(0)) + module.substring(1);
            module="modules."+module;
            Class<?> cl = Class.forName(module);
            return (IModule) cl.getConstructor().newInstance();
           
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
         return null;
    }

    public static String checkModule(String module)
    {
        if(getModule(module)!=null)
        	return null;
         return module;
    }

}