package core.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import common.Logger;
import common.Utils;

import core.ModuleImpl;
import core.beans.CommandLine;
import core.beans.Strings.Common;
import core.beans.Strings.SuggestConfig;

public class Essidsuggest extends ModuleImpl {
	List<String> passwords = new ArrayList<String>();

	List<String> spec = new ArrayList<String>();
	HashSet<String> dictionary = new HashSet<String>();

	String parseHCCAPFile(String filePath) {
		StringBuilder ret = new StringBuilder();
		File hccapFile = new File(filePath);
		byte[] bFile = new byte[(int) hccapFile.length()];
		try {
			FileInputStream fileInputStream = new FileInputStream(hccapFile);
			fileInputStream.read(bFile);
			fileInputStream.close();

			if (bFile.length > 36) {
				for (int i = 0; i < 36; i++) {
					if (bFile[i] == 0)
						break;
					ret.append((char) bFile[i]);
				}

			} else {
				Logger.error("File less than 36 bytes");
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}

		return ret.toString();
	}

	void add(String tmp) {

		if (!dictionary.contains(tmp))
			dictionary.add(tmp);

	}

	public void init() {

		passwords = Utils.readFileUniq(Paths.get(
				CommandLine.getInstance().getOption(Common.CONFIG),
				this.getClass().getSimpleName().toLowerCase(),
				SuggestConfig.WIFI).toString());
		spec = Utils.readFileUniq(Paths.get(
				CommandLine.getInstance().getOption(Common.CONFIG),
				this.getClass().getSimpleName().toLowerCase(),
				SuggestConfig.SPEC).toString());

	}

	public List<String> run() {
		System.out.println("ESSID Suggester run");
		init();
		HashSet<String> words = new HashSet<String>();
		HashSet<String> swords = new HashSet<String>();
		String ESSID = this.parseHCCAPFile(CommandLine.getInstance().getOption(
				Common.INPUT));

		add(ESSID);
		add(ESSID.toUpperCase());
		add(ESSID.substring(0, 1).toUpperCase() + ESSID.substring(1));

		for (String word : words) {
			for (String sword : swords) {
				add(word + sword);
				add(sword + word);
				for (String sep : spec) {
					add(word + sword + sep);
					add(sword + word + sep);

				}

			}
		}
		String dictName = Utils.getOutputFile();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(dictName, "UTF-8");
		} catch (FileNotFoundException e) {
			Logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			Logger.error(e.getMessage());
		}
		for (String tmp : dictionary) {
			writer.println(tmp);

			for (Integer i = 0; i < 100; i++)
				writer.println(tmp + i.toString());

		}
		writer.close();
		this.tail.add(dictName);
		List<String> ret = new ArrayList<String>();
		for (String tmp : super.run()) {
			ret.add(tmp);
		}
		Utils.deleteFile(dictName);
		return ret;
	}

}
