import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import common.Logger;
import common.Utils;

import core.Parser;
import core.Runner;
import core.beans.CommandLine;
import core.beans.Options;
import core.beans.Strings.Common;

public class main {
	static CommandLine global;

	static void printOptions() {
		global = CommandLine.getInstance();
		System.out.println("INPUT:" + global.getOption(Common.INPUT));
		System.out.println("OUTPUT:" + global.getOption(Common.OUTPUT));
		System.out.println("TMP:" + global.getOption(Common.TMP));
		System.out.println("CONFIG:" + global.getOption(Common.CONFIG));
		System.out.println("MODULE:" + global.getOption(Common.MODULE));
		System.out.println("EXEC_STAGE:" + global.getOption(Common.EXEC_STAGE));
		System.out.println("INPUT_WORK_FILE:"
				+ global.getOption(Common.WORKFILE));
	}

	static void initProjectOptions() {
		global = CommandLine.getInstance();

		if (global.getOption(Common.INPUT).isEmpty())
			Logger.error("No input file set");
		if (global.getOption(Common.MODULE).isEmpty())
			Logger.error("No module with stage rules set");
		if (global.getOption(Common.CONFIG).isEmpty())
			Logger.error("Configuration file not set");

		// rebuilding full pathe's
		try {
			global.add(Common.INPUT, new File(global.getOption(Common.INPUT))
					.getCanonicalPath().toString());
			global.add(Common.WORKFILE,
					new File(global.getOption(Common.INPUT)).getCanonicalPath()
							.toString() + "_input");
			global.add(Common.TMP, new File(global.getOption(Common.INPUT))
					.getCanonicalPath().toString() + "_tmp");
			global.add(Common.OUTPUT, new File(global.getOption(Common.INPUT))
					.getCanonicalPath().toString() + "_out");
			global.add(Common.RESULT, new File(global.getOption(Common.INPUT))
					.getCanonicalPath().toString() + "_result");
			global.add(Common.EXEC_STAGE,
					new File(global.getOption(Common.EXEC_STAGE))
							.getCanonicalPath().toString());
			global.add(Common.CONFIG, new File(global.getOption(Common.CONFIG))
					.getCanonicalPath().toString());

		} catch (IOException e) {

			Logger.error(e.getMessage());
		}
		loadConfig(global.getOption(Common.CONFIG));

		// make input duplication and copy it
		Utils.cloneFile(global.getOption(Common.INPUT),
				global.getOption(Common.WORKFILE));
	}

	static void loadConfig(String configPath) {

		System.out.println(configPath);
		global = CommandLine.getInstance();
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

		global = CommandLine.getInstance();
		PrintWriter out = null;
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < args.length; i++) {

			if (("-c").equals(args[i]) || ("--config").equals(args[i])) {

				if (i + 1 <= args.length) {
					global.add(Common.CONFIG, args[i + 1]);
					i++;
				}
			}

			else if (("-m").equals(args[i]) || ("--module").equals(args[i])) {
				if (i + 1 <= args.length) {
					global.add(Common.MODULE, args[i + 1]);
					i++;
				}
			}

			else if (("-s").equals(args[i]) || ("--suggest").equals(args[i])) {
				if (i + 1 <= args.length) {
					global.add(Common.SUGGEST, args[i + 1]);
					i++;
				}
			}

			else {
				if (global.getOption(Common.INPUT) == null) {

					global.add(Common.INPUT, args[i]);
				} else {

					global.add(Common.EXEC_STAGE, args[i]);
				}
			}
		}
		initProjectOptions();
		printOptions();

		for (Options ModuleStage : Parser.getInstance().parse(
				global.getOption(Common.EXEC_STAGE))) {
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(
						global.getOption(Common.RESULT), true)));
			} catch (IOException e) {
				Logger.error(e.getMessage());
			}

			for (String cracked : Runner.getInstance().runModule(ModuleStage)) {
				System.out.println(cracked);
				result.add(cracked);
				out.println(cracked);

			}
			out.close();

			// file can't be rebuild if it is binary
			if (CommandLine.getInstance().getOption(Common.MODULE)
					.equalsIgnoreCase("2500")
					|| CommandLine.getInstance().getOption(Common.MODULE)
							.equalsIgnoreCase("5200")
					|| CommandLine.getInstance().getOption(Common.MODULE)
							.equalsIgnoreCase("5300")
					|| CommandLine.getInstance().getOption(Common.MODULE)
							.equalsIgnoreCase("5400")
					|| CommandLine.getInstance().getOption(Common.MODULE)
							.startsWith("62")
					|| CommandLine.getInstance().getOption(Common.MODULE)
							.equalsIgnoreCase("6600")
					|| CommandLine.getInstance().getOption(Common.MODULE)
							.equalsIgnoreCase("8200")
					|| CommandLine.getInstance().getOption(Common.MODULE)
							.equalsIgnoreCase("8800")
					|| CommandLine.getInstance().getOption(Common.MODULE)
							.equalsIgnoreCase("9000")) {
				// nothing to do
			} else {
				Utils.rebuildInputFile(result, CommandLine.getInstance()
						.getOption(Common.WORKFILE));
			}
			System.out.println("REBUILDED");

		}
		System.out.println("Cleared");
		Utils.deleteFile(global.getOption(Common.WORKFILE));
		Utils.deleteFile(global.getOption(Common.TMP));

	}
}
