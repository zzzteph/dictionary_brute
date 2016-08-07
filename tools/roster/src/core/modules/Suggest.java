package core.modules;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import common.Logger;
import common.Utils;
import core.GlobalOptions;
import core.beans.Strings;

public class Suggest extends ModuleImpl {
	static String[] passwords = { "admin", "adm", "cisco", "manager", "app", "administrator", "agent", "root", "bank",
			"public", "private", "db", "dba", "oracle", "database", "123456", "123456789", "password", "12345678",
			"qwerty", "1234567", "1234567890", "1234", "12345", "12345", "123456", "2016", "2015", "2014", "2013",
			"2011", "2012", "2017", "2018", "wi-fi", "wpa", "wpa2", "wep", "wifi" };

	static String[] spec = { "!", "@", "#", "$" };
	static HashSet<String> dictionary = new HashSet<String>();

	static void add(String tmp) {

		if (!dictionary.contains(tmp))
			dictionary.add(tmp);

	}

	public List<String> run() {

		List<String> words = new ArrayList<String>();
		List<String> swords = new ArrayList<String>();

		if (GlobalOptions.getInstance().getOption(Strings.SUGGEST) == null)
			return null;
		for (String tmp : options.get(Strings.SUGGEST).split(",")) {
			if (tmp.length() > 1) {
				swords.add(tmp);// test
				swords.add(tmp.toUpperCase());
				swords.add(tmp.substring(0, 1).toUpperCase() + tmp.substring(1));
			}
		}
		for (String password : passwords) {
			words.add(password);
			words.add(password.toUpperCase());
			words.add(password.substring(0, 1).toUpperCase() + password.substring(1));
		}

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

		for (String word : words) {
			for (String word2 : words) {
				for (String sword : swords) {
					add(word + sword + word2);
					add(word2 + sword + word);
					add(word + word2 + sword);
					add(word2 + word + sword);
					add(sword + word2 + word);
					add(sword + word + word2);
					for (String sep : spec) {
						add(word + sword + word2 + sep);
						add(word2 + sword + word + sep);
						add(word + word2 + sword + sep);
						add(word2 + word + sword + sep);
						add(sword + word2 + word + sep);
						add(sword + word + word2 + sep);

					}

				}
			}
		}
		String dictName = Utils.getOutputFile();
		PrintWriter writer = null;
		try {
			 writer = new PrintWriter(dictName, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
				Logger.error(e.getMessage());
		}
		for (String tmp : dictionary) {
			writer.println(tmp);
			if (!this.options.containsKey(Strings.SIMPLE))
				for (Integer i = 0; i < 100; i++)
					writer.println(tmp + i.toString());

		}
		this.options.put(Strings.TAIL, dictName);
		List<String>ret =new ArrayList<String>();
		for (String tmp : super.run()) {
			ret.add(tmp);
		}
		
		
		
	return ret;
	}

}
