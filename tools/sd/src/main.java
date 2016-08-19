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
import core.beans.Strings.Common;

public class main {

	static void loadConfig(String configPath) {
		CommandLine global = CommandLine.getInstance();
		
		File config = new File(configPath,"config.txt");

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
			
			if (("-c").equals(args[i]) ||("--config").equals(args[i])) {
			
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
			} else if (("-s").equals(args[i]) || ("--suggest").equals(args[i])) {
				if (i + 1 <= args.length) {
					global.add(Common.SUGGEST, args[i + 1]);
					i++;
				}
			}

			else {
				System.out.println("INPUT FILE:"+args[i]);
				inputFile = args[i];

			}
		}

		loadConfig(global.getOption(Common.CONFIG));
		global.add(Common.INPUT, Utils.getInputFile());
		// make input duplication and copy it
		Utils.cloneFile(inputFile, global.getOption(Common.INPUT));
		Parser parser = Parser.getInstance();

		for (Options ModuleStage : parser.parse("test.xml")) {
			List<String> result = Runner.getInstance().runModule(ModuleStage);
			// if(!global.getOption(Strings.MODULE).equalsIgnoreCase("2500"))
			Utils.rebuildInputFile(result,
					CommandLine.getInstance().getOption(Common.INPUT));

		}

		Utils.deleteFile(global.getOption(Common.INPUT));
	}
}
