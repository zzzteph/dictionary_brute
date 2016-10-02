import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.Logger;
import common.Utils;

import core.Parser;
import core.Runner;
import core.beans.CommandLine;
import core.beans.Options;
import core.beans.SessionHandler;
import core.beans.Strings.Common;

public class main {

	static void loadConfig(String configPath) {

		System.out.println(configPath);
		CommandLine global = CommandLine.getInstance();
		if (configPath == null)
			configPath = "config";
		File config = new File(configPath, "config.txt");

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(config.getAbsolutePath()));

			String line;
			while ((line = br.readLine()) != null) {

				String[] tmp = line.split("=", 2);
				if (tmp.length == 2 && !tmp[0].startsWith("#")) {
					tmp[0] = tmp[0].toUpperCase();
					global.add(tmp[0], tmp[1]);

				}
			}
		} catch (FileNotFoundException e) {
			Logger.error(e.getMessage());
		} catch (IOException e) {
			Logger.error(e.getMessage());
		}

	}

	void isDone() {

	}

	public static void main(String[] args) {
		List<String> hashFile = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		SessionHandler session = SessionHandler.getInstance();
		boolean founded = false;
		CommandLine global = CommandLine.getInstance();
		String inputFile = null;
		String execModule = null;
		Boolean runPrevSession = false;
		PrintWriter out = null;
		hashFile.add("2500");// WPA/WPA2
		hashFile.add("5200");// Password Safe v3
		hashFile.add("5300");// IKE-PSK MD5
		hashFile.add("5400");// IKE-PSK SHA1
		hashFile.add("6600");// 1Password, Agile Keychain
		hashFile.add("8200");// 1Password, Cloud Keychain
		hashFile.add("8800");// Android FDE <= 4.3
		hashFile.add("9000");// Password Safe v2
		hashFile.add("6211");// TrueCrypt 5.0+
		hashFile.add("6212");// TrueCrypt 5.0+
		hashFile.add("6213");// TrueCrypt 5.0+
		hashFile.add("6221");// TrueCrypt 5.0+
		hashFile.add("6222");// TrueCrypt 5.0+
		hashFile.add("6223");// TrueCrypt 5.0+
		hashFile.add("6231");// TrueCrypt 5.0+
		hashFile.add("6232");// TrueCrypt 5.0+
		hashFile.add("6233");// TrueCrypt 5.0+
		hashFile.add("6241");// TrueCrypt 5.0+
		hashFile.add("6242");// TrueCrypt 5.0+
		hashFile.add("6243");// TrueCrypt 5.0+
		
		for (int i = 0; i < args.length; i++) {

			if (("-c").equals(args[i]) || ("--config").equals(args[i])) {

				if (i + 1 <= args.length) {
					global.add(Common.CONFIG, args[i + 1]);
					i++;
				}
			}

			else if (("-o").equals(args[i]) || ("--output").equals(args[i])) {

				if (i + 1 <= args.length) {
					global.add(Common.PROJECT, args[i + 1]);
					i++;
				}
			}

			else if (("-m").equals(args[i]) || ("--module").equals(args[i])) {
				if (i + 1 <= args.length) {
					global.add(Common.MODULE, args[i + 1]);
					i++;
				}
			} 
			
			else if (("-p").equals(args[i]) || ("--project").equals(args[i])) {
				if (i + 1 <= args.length) {
					global.add(Common.PROJECT, args[i + 1]);
					i++;
				}
			} 
			
			else if (("-s").equals(args[i]) || ("--suggest").equals(args[i])) {
				if (i + 1 <= args.length) {
					global.add(Common.SUGGEST, args[i + 1]);
					i++;
				}
			} 
			
			else if (("--continue").equals(args[i])) {
				runPrevSession = true;
			} 
			
			else {
				if (inputFile == null) {
					System.out.println("INPUT FILE:" + args[i]);
					inputFile = args[i];
				} else {
					System.out.println("EXEC MODULE:" + args[i]);
					execModule = args[i];
				}
			}
		}


		// check if input and exec module were set
		if (inputFile == null)
			Logger.error("No input file set");
		if (execModule == null)
			Logger.error("No module with stage rules set");
		if (global.getOption(Common.CONFIG).isEmpty())
			Logger.error("Configuration file not set");

		loadConfig(global.getOption(Common.CONFIG));	
		global.add(Common.INPUT, Utils.getInputFile());
		
		// make input duplication and copy it
		Utils.cloneFile(inputFile, global.getOption(Common.INPUT));
		
		Parser parser = Parser.getInstance();

		// create output folder
		if (global.getOption(Common.PROJECT) == null) {

			global.add(Common.PROJECT, new File(inputFile).getName()
					+ "_output");
		}
		if (Utils.createFolder(global.getOption(Common.OUTPUT),
				global.getOption(Common.PROJECT)) == false)
			Logger.error("Can't create output folder:"
					+ global.getOption(Common.PROJECT));
		// copy input file

		File projectFolder = new File(global.getOption(Common.OUTPUT),
				global.getOption(Common.PROJECT));
		File projectFile = new File(projectFolder,
				new File(inputFile).getName());
		File outputFile = new File(projectFolder, "output.txt");
		Utils.cloneFile(inputFile, projectFile.getAbsolutePath());
		
		System.out.println(""+projectFolder.getAbsolutePath()+" | "+global.getOption(Common.PROJECT)+" | "+inputFile);
		
		Integer prevSession = session.checkPreviousSession(projectFolder.getAbsolutePath(),global.getOption(Common.PROJECT),inputFile);
		if(prevSession != null) {
			Console console = System.console();
			if (console != null) {
				System.out.println("you have unfinished session, do you want to continue it ? (Y/n)");
				String answer = console.readLine();
				runPrevSession = answer.toLowerCase().equals("y");
			}
		}
		
		if (runPrevSession) {
			if (prevSession != null) {
				Utils.rebuildInputFile(outputFile.getAbsolutePath(),global.getOption(Common.INPUT));
			} else {
				System.out.println("no unfinished sessions found");
			}
		}

		if(session.startSession(prevSession, global.getOption(Common.PROJECT),projectFolder.getAbsolutePath(),inputFile,outputFile.getAbsolutePath())) {
			for (Options ModuleStage : parser.parse("test.xml")) {
				try {
					out = new PrintWriter(new BufferedWriter(new FileWriter(
							outputFile.getAbsolutePath(), true)));
				} catch (IOException e) {
					Logger.error(e.getMessage());
				}
	
				for (String cracked : Runner.getInstance().runModule(ModuleStage)) {
					if (!result.contains(cracked)) {
						result.add(cracked);
						out.println(cracked);
					}
				}
				out.close();
				// append to outputfile
				// check if only one hash per file
				for (String oneSample : hashFile) {
					if (global.getOption(Common.MODULE).equalsIgnoreCase(oneSample)) {
						if (result.size() > 0)// if founded for files with only one
							founded = true;
					}
				}
				if (founded)
					break;
	
				// oneline hashes cant be rebuild
	
				if (!hashFile.contains(global.getOption(Common.MODULE)))
					Utils.rebuildInputFile(result, CommandLine.getInstance()
							.getOption(Common.INPUT));
	
			}
			for (String tmp : result) {
				System.out.println(tmp);
			}
			session.finishSession();
		}
		Utils.deleteFile(global.getOption(Common.INPUT));
	}
}
