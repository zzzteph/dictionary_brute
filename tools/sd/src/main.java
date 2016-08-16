import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import common.Logger;
import common.Utils;

import core.Parser;
import core.Runner;
import core.beans.CommandLine;
import core.beans.Options;
import core.beans.Strings;

public class main {

	static void loadConfig() {
		CommandLine global = CommandLine.getInstance();
		String initPath = null;
		try {
			initPath = new File(main.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath())
					.getAbsolutePath();
		} catch (URISyntaxException e) {
			Logger.error(e.getMessage());
		}

		File config = new File(initPath, "../../config.txt");

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(config.getAbsolutePath()));
			String line;
			while ((line = br.readLine()) != null) {

				String[] tmp = line.split("=", 2);
				if (tmp.length == 2) {
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

		CommandLine global = CommandLine.getInstance();
		String inputFile = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-m") || args[i].equals("--module")) {
				if (i + 1 != args.length) {
					global.add(Strings.MODULE, args[i + 1]);
					i++;
				}
			} else if (args[i].equals("-s") || args[i].equals("--suggest")) {
				if (i + 1 != args.length) {
					global.add(Strings.SUGGEST, args[i + 1]);
					i++;
				}
			}

			else {
				System.out.println(args[i]);
				inputFile = args[i];

			}
		}

		loadConfig();
		global.add(Strings.INPUT, Utils.getInputFile());
		// make input duplication and copy it
		Utils.cloneFile(inputFile, global.getOption(Strings.INPUT));
		Parser parser = Parser.getInstance();

		for (Options ModuleStage : parser.parse("test.xml")) {
			List<String> result = Runner.getInstance().runModule(ModuleStage);
			// if(!global.getOption(Strings.MODULE).equalsIgnoreCase("2500"))
			Utils.rebuildInputFile(result,
					CommandLine.getInstance().getOption(Strings.INPUT));

		}

		Utils.deleteFile(global.getOption(Strings.INPUT));
	}
}
