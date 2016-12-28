package core.modules;

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

public class Suggest extends ModuleImpl {
	List<String> passwords = new ArrayList<String>();
	List<String> suggestWords = new ArrayList<String>();
	List<String> spec = new ArrayList<String>();
	HashSet<String> dictionary = new HashSet<String>();

	void add(String tmp) {

		if (!dictionary.contains(tmp))
			dictionary.add(tmp);

	}

	public void init() {

		passwords = Utils.readFileUniq(Paths.get(
				CommandLine.getInstance().getOption(Common.CONFIG),
				this.getClass().getSimpleName().toLowerCase(),
				SuggestConfig.WORDS).toString());
		spec = Utils.readFileUniq(Paths.get(
				CommandLine.getInstance().getOption(Common.CONFIG),
				this.getClass().getSimpleName().toLowerCase(),
				SuggestConfig.SPEC).toString());

	}

	public List<String> run() {
		System.out.println("Suggester run");
		init();
		HashSet<String> words = new HashSet<String>();
		for (String tmp : options.get(Common.SUGGEST).split(",")) {
			suggestWords.add(tmp);
			suggestWords.add(tmp.toLowerCase());
			suggestWords.add(tmp.toUpperCase());
			suggestWords.add(tmp.substring(0, 1).toUpperCase()
					+ tmp.substring(1));
		}

		// replace all
		if (options.get(Common.LEET) != null) {
			// leet add
			for (int i = 0; i < suggestWords.size(); i++) {
				for (String leet : Utils.leeter(suggestWords.get(i))) {
					if (!suggestWords.contains(leet))
						suggestWords.add(leet);
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
			for (String suggest : suggestWords) {
				dictionary.add(suggest.concat(word));
				dictionary.add(word.concat(suggest));
			}
		}

		for (String word : passwords) {
			for (String suggest : suggestWords) {
				for (String password2 : passwords) {
					// spec+word+WIFI
					dictionary.add(password2.concat(word.concat(suggest)));
					// word+WIFI+spec
					dictionary.add(word.concat(suggest).concat(password2));
					// word+spec+WIFI
					dictionary.add(word.concat(password2).concat(suggest));
				}
			}

		}

		// add dates
		tmpPasswords = new ArrayList<String>(dictionary);
		for (Integer i = 1950; i < 2100; i++) {
			for (String suggest : suggestWords) {
				dictionary.add(suggest.concat(i.toString()));
			}
		}
		tmpPasswords.clear();

		// add numbers
		tmpPasswords = new ArrayList<String>(dictionary);
		for (Integer i = 0; i < 100; i++) {
			for (String suggest : suggestWords) {
				dictionary.add(suggest.concat(i.toString()));
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
