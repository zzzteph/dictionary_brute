import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import common.Logger;
import common.Utils;

import core.GlobalOptions;
import core.ModuleParser;
import core.beans.Strings;

public class main {

	static void loadConfig() {
		GlobalOptions global = GlobalOptions.getInstance();
		String initPath = null;
		try {
			initPath = new File(main.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath())
					.getAbsolutePath();
		} catch (URISyntaxException e) {
			Logger.error(e.getMessage());
		}

		File config = new File(initPath, "../config.txt");

		try (BufferedReader br = new BufferedReader(new FileReader(
				config.getAbsolutePath()))) {
			String line;
			while ((line = br.readLine()) != null) {

				String[] tmp = line.split("=", 2);
				if (tmp.length == 2) {
					tmp[0] = tmp[0].toUpperCase();
					switch (tmp[0]) {
					case Strings.EXEC:
					case Strings.MAINDIR:
						global.add(tmp[0], tmp[1]);

						break;
					}
				}

			}
		} catch (FileNotFoundException e) {
			Logger.error(e.getMessage());
		} catch (IOException e) {
			Logger.error(e.getMessage());
		}

	}

	static void cloneInput(String source, String dest) {
		InputStream is = null;
		OutputStream os = null;
		try {
			try {
				is = new FileInputStream(source);
				os = new FileOutputStream(dest);
			} catch (FileNotFoundException e) {
				Logger.error(e.getMessage());
			}
			byte[] buffer = new byte[1024];
			int length;
			try {
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
			} catch (IOException e) {
				Logger.error(e.getMessage());
			}
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				Logger.error(e.getMessage());
			}

		}
	}

	public static void main(String[] args) {
		GlobalOptions global = GlobalOptions.getInstance();
		String inputFile = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-m") || args[i].equals("--module")) {
				if (i + 1 != args.length) {
					global.add(Strings.MODULE, args[i + 1]);
					i++;
				}
			} else {
				System.out.println(args[i]);
				inputFile = args[i];

			}
		}

		loadConfig();
		global.add(Strings.INPUT, Utils.getOutputFile());
		cloneInput(inputFile, global.getOption(Strings.INPUT));
		ModuleParser parser = ModuleParser.getInstance();
		parser.parse("test.xml");
		System.out.println("END");
		Utils.deleteFile(global.getOption(Strings.INPUT));
	}
}
