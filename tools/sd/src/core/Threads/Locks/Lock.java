package core.Threads.Locks;

public class Lock {
	private static Lock instance;

	public static synchronized Lock getInstance() {
	    if (instance == null) {
	        instance = new Lock();
	    }
	    return instance;
	}
}
