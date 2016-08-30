package core;

import java.io.IOException;
import java.util.List;

import common.Logger;

public class CommandThread extends Thread{

	protected Process process = null;
	ProcessBuilder command = new ProcessBuilder();
	
	@Override
	public void run()
	{
		try {
	
			process = command.start();
			int exitVal = process.waitFor();
			System.out.println("Process exited with value:" + exitVal);
		} catch (IOException e) {

			Logger.error(e.getMessage());
		} catch (InterruptedException e) {
			Logger.error(e.getMessage());
		}
		
	}
	
	public CommandThread(List<String> commandOptions)
	{
		
		
		command = new ProcessBuilder(commandOptions);
	}
	
}
