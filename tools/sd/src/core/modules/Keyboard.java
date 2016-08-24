package core.modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import common.Logger;
import common.Utils;

import core.ModuleImpl;
import core.beans.CommandLine;
import core.beans.Strings.Common;
import core.modules.helpers.Part;

public class Keyboard extends ModuleImpl {

	HashSet<String> line = new HashSet<String>();
	HashSet<String> upper = new HashSet<String>();

	HashSet<String> merged;
	HashSet<Part> passwords;

	PrintWriter writer;
	String tempName = "temp_brute_file";
	int size = 0;

	Boolean policy = false;
	Boolean reverse = false;
	Boolean optimization = false;

	static Map<String, HashSet<String>> links = new HashMap<String, HashSet<String>>();
	List<String> ret = new ArrayList<String>();

	char shift(char c) {

		return 'c';

	}

	String capsed(String part) {

		return part.toUpperCase();
	}

	String shifted(String part) {

		StringBuilder str = new StringBuilder();
		for (int i = 0; i < part.length(); i++) {
			str.append(shift(part.charAt(i)));
		}
		return str.toString();
	}

	String upperfirst(String part) {
		if (part.length() == 0)
			return "";
		StringBuilder str = new StringBuilder();
		str.append(shifted(part.substring(0, 1)));
		str.append(part.substring(1));
		return str.toString();
	}

	boolean canBeCombined(String combination, String part) {

		String start = part.substring(0, 1);
		String end = part.substring(part.length() - 1, part.length());

		for (int i = 0; i < combination.length(); i++) {

			if (links.get(combination.substring(i, i + 1)).contains(start)
					|| links.get(combination.substring(i, i + 1)).contains(end))
				return true;

		}

		return false;
	}

	void add(String key, String value) {

		String tmp;
		for (int i = 0; i < value.length(); i++) {
			tmp = value.substring(i, i + 1);
			if (!links.containsKey(key))
				links.put(key, new HashSet<String>());
			if (!links.containsKey(key.toUpperCase()))
				links.put(key.toUpperCase(), new HashSet<String>());

			links.get(key).add(tmp);
			links.get(key.toUpperCase()).add(tmp);

		}

	}

	void init(int type, int size) {

		List<String> init = new ArrayList<String>();

		if (type == 1) {
			for (String tmp : line)
				init.add(tmp);
		}

		if (type == 0) {
			for (String tmp : upper)
				init.add(tmp);
		}
		if (type == 2) {
			for (String tmp : upper)
				init.add(tmp);
			for (String tmp : line)
				init.add(tmp);
		}

		for (String tmp : init) {
			for (int i = 0; i <= tmp.length() - size; i++) {

				merged.add(tmp.substring(i, i + size));
			}
		}
		for (String tmp : init) {
			for (int i = 0; i <= tmp.length() - size; i++) {
				merged.add(capsed(tmp.substring(i, i + size)));// capslock
			}
		}
		for (String tmp : init) {
			for (int i = 0; i <= tmp.length() - size; i++) {
				merged.add(shifted(tmp.substring(i, i + size)));// shift
																// pressed
			}
		}

		if (reverse == true)
			reverse();

		for (String tmp : init) {
			for (int i = 0; i <= tmp.length() - size; i++) {

				merged.add(upperfirst(tmp.substring(i, i + size)));// upperfirts
			}
		}

	}

	void runExternal() {

		this.options.put(Common.TAIL, tempName);
		for (String tmp : super.run()) {
			ret.add(tmp);
		}
	}

	void makeDictionary(String temp) {
		if (size > 10000000) {

			writer.close();

			runExternal();
			size = 0;
			Utils.deleteFile(tempName);
			try {
				writer = new PrintWriter(tempName, "UTF-8");
			} catch (FileNotFoundException e) {
				Logger.error(e.getMessage());
			} catch (UnsupportedEncodingException e) {
				Logger.error(e.getMessage());
			}
		} else {
			size++;
			writer.println(temp);
		}
	}

	void checkFileEnd() {

		runExternal();

		(new File(tempName)).delete();

	}

	void reverse() {

		HashSet<String> rev = new HashSet<String>();
		for (String tmp : merged) {
			rev.add(new StringBuilder(tmp).reverse().toString());
		}
		for (String tmp : merged) {

			rev.add(upperfirst(new StringBuilder(tmp).reverse().toString()));// upperfirts
		}

		for (String tmp : rev) {
			merged.add(tmp);
		}
	}

	void generate(HashSet<Part> iterate, Part start, int depth) {

		for (Part pass : iterate) {
			Part tmp = new Part();

			if (optimization == true)
				if (start.getPassword().length() > 0)
					if (!canBeCombined(start.getPassword(), pass.getPassword()))
						continue;
			tmp.append(start);
			tmp.append(pass);

			if (depth != 0) {
				generate(iterate, tmp, depth - 1);
			} else {

				if (policy == true)
					if (tmp.policyCheck() != false)
						continue;

				makeDictionary(tmp.getPassword());

			}

		}

	}

	List<String> buildUpper(List<String> keyboard) {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < keyboard.get(0).length(); i++) {
			StringBuffer temp = new StringBuffer();
			for (int j = 0; j < keyboard.size(); j++) {
				temp.append(keyboard.get(j).charAt(i));
			}
			if (temp.toString().contains(" "))
				continue;
			ret.add(temp.toString());
		}

		for (int i = keyboard.get(0).length() - 1; i > 0; i--) {
			StringBuffer temp = new StringBuffer();
			for (int j = 0; j < keyboard.size(); j++) {
				if (i - j < 0)
					break;
				temp.append(keyboard.get(j).charAt(i - j));
			}
			if (temp.toString().contains(" "))
				continue;
			if (temp.toString().length() != keyboard.size())
				continue;

			ret.add(temp.toString());
		}
		return ret;
	}

	public void init(String keyboard) {
		List<String> keyBoards = Utils.readFile(Paths.get(
				CommandLine.getInstance().getOption(Common.CONFIG),
				this.getClass().getSimpleName().toLowerCase(),
				keyboard + ".txt").toString());

		List<String> lowerCase = new ArrayList<String>();
		List<String> upperCase = new ArrayList<String>();
		List<String> capslockCase = new ArrayList<String>();
		int keyBoardType = 0;
		for (String tmp : keyBoards) {
			if (tmp.length() <= 1) {
				keyBoardType++;
			} else {
				if (keyBoardType == 0)
					lowerCase.add(tmp);
				if (keyBoardType == 1)
					upperCase.add(tmp);
				if (keyBoardType == 2)
					capslockCase.add(tmp);
			}
		}

		for (String part : buildUpper(lowerCase)) {
			upper.add(part);
		}
		for (String part : buildUpper(upperCase)) {
			upper.add(part);
		}
		for (String part : buildUpper(capslockCase)) {
			upper.add(part);
		}

		// building chains lowercase

		for (int i = 0; i < lowerCase.get(0).length(); i++) {
			for (int j = 0; j < lowerCase.size(); j++) {
				if (lowerCase.get(j).charAt(i) != ' ') {

					StringBuffer temp = new StringBuffer();
					temp.append(buildLinks(lowerCase, i, j));
					temp.append(buildLinks(upperCase, i, j));
					temp.append(buildLinks(capslockCase, i, j));

					add(String.valueOf(lowerCase.get(j).charAt(i)),
							Utils.cleanStringDuplicateChars(temp.toString()));
				}
			}
		}

		System.exit(0);
	}

	private String buildLinks(List<String> keyBoard, int i, int j) {
		StringBuffer ret = new StringBuffer();
		if (j - 1 >= 0)
			if (i - 1 >= 0)
				ret.append(keyBoard.get(j - 1).charAt(i - 1));
		if (i - 1 >= 0)
			ret.append(keyBoard.get(j).charAt(i - 1));

		if (j + 1 < keyBoard.size())
			if (i - 1 >= 0)
				ret.append(keyBoard.get(j + 1).charAt(i - 1));

		if (j - 1 >= 0)
			ret.append(keyBoard.get(j - 1).charAt(i));

		ret.append(keyBoard.get(j).charAt(i));

		if (j + 1 < keyBoard.size())
			ret.append(keyBoard.get(j + 1).charAt(i));

		if (j - 1 >= 0)
			if (i + 1 <= keyBoard.get(0).length())
				ret.append(keyBoard.get(j - 1).charAt(i + 1));
		if (i + 1 < keyBoard.get(0).length())
			ret.append(keyBoard.get(j).charAt(i + 1));

		if (j + 1 < keyBoard.size())
			if (i + 1 < keyBoard.get(0).length())
				ret.append(keyBoard.get(j + 1).charAt(i + 1));

		return ret.toString().replace(" ", "");
	}

	public List<String> run() {

		Integer type = 0;
		Integer parts = 3;
		Integer length = 3;

		merged = new HashSet<String>();

		init(options.get(Common.KEYBOARD));

		if (options.get(Common.TYPE) != null)
			type = Integer.parseInt(options.get(Common.TYPE));
		if (options.get(Common.PARTS) != null)
			parts = Integer.parseInt(options.get(Common.PARTS));
		if (options.get(Common.LENGTH) != null)
			length = Integer.parseInt(options.get(Common.LENGTH));

		if (Boolean.parseBoolean(options.get(Common.POLICY)))
			policy = true;
		if (Boolean.parseBoolean(options.get(Common.REVERSE)))
			reverse = true;
		if (Boolean.parseBoolean(options.get(Common.OPTIMIZATION)))
			optimization = true;

		tempName = Utils.getOutputFile();

		try {

			writer = new PrintWriter(tempName, "UTF-8");
		} catch (FileNotFoundException e) {
			Logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			Logger.error(e.getMessage());
		}

		init(type, parts);

		length = length - 1;// Human readable
		passwords = new HashSet<Part>();
		for (String tmp : merged) {
			passwords.add(new Part(tmp));
		}
		merged.clear();
		System.out.println(Double.valueOf(
				Math.pow(passwords.size(), length + 1)).intValue());
		generate(passwords, new Part(), length);
		runExternal();
		writer.close();
		Utils.deleteFile(tempName);

		return ret;
	}

}
