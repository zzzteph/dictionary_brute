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
		init();
		HashSet<String> words = new HashSet<String>();
		HashSet<String> swords = new HashSet<String>();

		if (CommandLine.getInstance().getOption(Common.SUGGEST) == null)
			return new ArrayList<String>();
		System.out.println(options.get(Common.SUGGEST));
		for (String tmp : options.get(Common.SUGGEST).split(",")) {
			if (tmp.length() > 1) {
				swords.add(tmp);// test
				swords.add(tmp.toUpperCase());
				swords.add(tmp.substring(0, 1).toUpperCase() + tmp.substring(1));

				String leet = tmp.replace("o", "0").replace("O", "0");
				swords.add(leet);
				swords.add(leet.toUpperCase());
				swords.add(leet.substring(0, 1).toUpperCase()
						+ tmp.substring(1));
				leet = tmp.replace("a", "4").replace("A", "4");
				swords.add(leet);
				swords.add(leet.toUpperCase());
				swords.add(leet.substring(0, 1).toUpperCase()
						+ tmp.substring(1));
				leet = tmp.replace("a", "@").replace("a", "@");
				swords.add(leet);
				swords.add(leet.toUpperCase());
				swords.add(leet.substring(0, 1).toUpperCase()
						+ tmp.substring(1));
				leet = tmp.replace("s", "$").replace("S", "$");
				swords.add(leet);
				swords.add(leet.toUpperCase());
				swords.add(leet.substring(0, 1).toUpperCase()
						+ tmp.substring(1));

				leet = tmp.replace("o", "0").replace("O", "0")
						.replace("a", "4").replace("A", "4").replace("s", "$")
						.replace("S", "$");

				swords.add(leet);// test
				swords.add(leet.toUpperCase());
				swords.add(leet.substring(0, 1).toUpperCase()
						+ tmp.substring(1));
				leet = tmp.replace("o", "0").replace("O", "0")
						.replace("a", "@").replace("A", "@").replace("s", "$")
						.replace("S", "$");

				swords.add(leet);// test
				swords.add(leet.toUpperCase());
				swords.add(leet.substring(0, 1).toUpperCase()
						+ tmp.substring(1));
			}
		}

		for (String word : swords)
			add(word);
		for (String password : passwords) {
			words.add(password);
			words.add(password.toUpperCase());
			words.add(password.substring(0, 1).toUpperCase()
					+ password.substring(1));
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
