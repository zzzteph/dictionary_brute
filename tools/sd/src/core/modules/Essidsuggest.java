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
import java.util.Set;

import common.Logger;
import common.Utils;

import core.ModuleImpl;
import core.beans.CommandLine;
import core.beans.Strings.Common;
import core.beans.Strings.SuggestConfig;

public class Essidsuggest extends ModuleImpl {
	List<String> passwords = new ArrayList<String>();
	List<String> wifi = new ArrayList<String>();
	List<String> spec = new ArrayList<String>();
	Set<String> dictionary = new HashSet<String>();

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
		Logger.info("ESSID Suggester working...");
		init();
		HashSet<String> words = new HashSet<String>();
		String ESSID = this.parseHCCAPFile(CommandLine.getInstance().getOption(
				Common.WORKFILE));

		wifi.add(ESSID);
		wifi.add(ESSID.toLowerCase());
		wifi.add(ESSID.toUpperCase());
		wifi.add(ESSID.substring(0, 1).toUpperCase() + ESSID.substring(1));

		// replace all
		ESSID = ESSID.replaceAll("[^a-zA-Z0-9]", "");
		wifi.add(ESSID);
		// leet add
		if (options.get(Common.LEET) != null) {
			for (int i = 0; i < wifi.size(); i++) {
				for (String leet : Utils.leeter(wifi.get(i))) {
					if (!wifi.contains(leet))
						wifi.add(leet);
				}
			}
		}
		for (String tmp : passwords) {
			words.add(tmp);
			words.add(tmp.toUpperCase());
			if (tmp.length() > 1)
				words.add(tmp.substring(0, 1).toUpperCase() + tmp.substring(1));
		}

		// passwords leet
		List<String> tmpPasswords = new ArrayList<String>(words);
		if (options.get(Common.LEET) != null) {
			for (int i = 0; i < tmpPasswords.size(); i++) {

				for (String leet : Utils.leeter(tmpPasswords.get(i))) {
					words.add(leet);
				}
			}
		}
		tmpPasswords.clear();

		for (String word : passwords) {
			for (String ESSIDPass : wifi) {
				dictionary.add(ESSIDPass.concat(word));
				dictionary.add(word.concat(ESSIDPass));
			}
		}

		for (String word : passwords) {
			for (String ESSIDPass : wifi) {
				for (String password2 : passwords) {
					// spec+word+WIFI
					dictionary.add(password2.concat(word.concat(ESSIDPass)));
					// word+WIFI+spec
					dictionary.add(word.concat(ESSIDPass).concat(password2));
					// word+spec+WIFI
					dictionary.add(word.concat(password2).concat(ESSIDPass));
				}
			}

		}

		// add dates
		tmpPasswords = new ArrayList<String>(dictionary);
		for (Integer i = 1950; i < 2100; i++) {
			for (String tmp : wifi) {
				dictionary.add(tmp.concat(i.toString()));
			}
		}
		tmpPasswords.clear();

		// add numbers
		tmpPasswords = new ArrayList<String>(dictionary);
		for (Integer i = 0; i < 100; i++) {
			for (String tmp : wifi) {
				dictionary.add(tmp.concat(i.toString()));
			}
		}
		tmpPasswords.clear();

		// add special chars
		tmpPasswords = new ArrayList<String>(dictionary);
		for (String word : tmpPasswords) {
			for (String special : spec) {
				dictionary.add(word.concat(special));
			}
		}
		tmpPasswords.clear();

		// remove passwords more 20 chars
		tmpPasswords = new ArrayList<String>(dictionary);
		dictionary.clear();
		for (String tmp : tmpPasswords) {
			if (tmp.length() < 21 && tmp.length() >= 8)
				dictionary.add(tmp);
		}

	
		PrintWriter writer = null;
		try {
			writer = new PrintWriter( Utils.getTMPFile(), "UTF-8");
		} catch (FileNotFoundException e) {
			Logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			Logger.error(e.getMessage());
		}
		for (String tmp : dictionary) {
			writer.println(tmp);
		}
		writer.close();
		this.tail.add( Utils.getTMPFile());
		List<String> ret = new ArrayList<String>();
		for (String tmp : super.run()) {
			ret.add(tmp);
		}

		Utils.deleteFile( Utils.getTMPFile());
		return ret;
	}
}
