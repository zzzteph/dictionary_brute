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
	final int max_size = 10000000;
	PrintWriter writer;
	String temp_name = "temp_brute_file";
	int size = 0;

	String exec_path = "";
	Boolean runExternal = false;
	Boolean debug = false;
	Boolean policy = false;
	Boolean reverse = false;
	Boolean optimization = false;

	Integer count = 0;
	static Map<String, HashSet<String>> links = new HashMap<String, HashSet<String>>();
	List<String> ret = new ArrayList<String>();

	char shift(char c) {
		switch (c) {
		case '1':
			return '!';
		case '2':
			return '@';
		case '3':
			return '#';
		case '4':
			return '$';
		case '5':
			return '%';
		case '6':
			return '^';
		case '7':
			return '&';
		case '8':
			return '*';
		case '9':
			return '(';
		case '0':
			return ')';
		case '-':
			return '_';
		case '=':
			return '+';
		case '\\':
			return '|';
		case '`':
			return '~';
		case '[':
			return '{';
		case ']':
			return '}';
		case ';':
			return ':';
		case '\'':
			return '"';
		case ',':
			return '<';
		case '.':
			return '>';
		case '/':
			return '?';
		default:
			return Character.toUpperCase(c);
		}

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
		value = value + value.toUpperCase();
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

		this.options.put(Common.TAIL, temp_name);
		for (String tmp : super.run()) {
			ret.add(tmp);
		}
	}

	void makeDictionary(String temp) {
		if (size > max_size) {

			writer.close();

			runExternal();
			size = 0;
			Utils.deleteFile(temp_name);
			try {
				writer = new PrintWriter(temp_name, "UTF-8");
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

		(new File(temp_name)).delete();

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

		for (String part : upper) {
			System.out.println(part);
		}
		System.exit(0);
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

		temp_name = Utils.getOutputFile();

		try {

			writer = new PrintWriter(temp_name, "UTF-8");
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
		Utils.deleteFile(temp_name);

		return ret;
	}

	private void init_qwerty() {

		add("`", "`~1!");
		add("~", "`~1!");
		add("0", "_-po90");
		add("1", "12q!@Q");
		add("2", "123wq!@#WQ");
		add("3", "34ew2@#$EW");
		add("4", "345re#$%RE");
		add("5", "56tr4%^TR$");
		add("6", "67yt5%^&YT");
		add("7", "78uy6&*UY^");
		add("8", "89iu7*(IU&");
		add("9", "90oi8()OI*");
		add("0", "0-po9)_PO(");
		add("-", "-=[p0_+{P)");
		add("=", "=][-+}{_");
		add(")", "_-po90");
		add("!", "12q!@Q");
		add("@", "123wq!@#WQ");
		add("#", "34ew2@#$EW");
		add("$", "345re#$%RE");
		add("%", "56tr4%^TR$");
		add("^", "67yt5%^&YT");
		add("&", "78uy6&*UY^");
		add("*", "89iu7*(IU&");
		add("(", "90oi8()OI*");
		add(")", "0-po9)_PO(");
		add("_", "-=[p0_+{P)");
		add("+", "=][-+}{_");
		add("q", "12waq!@WAQ");
		add("w", "23esaqw@#ESAQW");
		add("e", "34rdswe#$RDSWE");
		add("r", "45tfder$%TFDER");
		add("t", "56ygfrt%^YGFRT");
		add("y", "67uhgty^&UHGTY");
		add("u", "78ijhyu&*IJHYU");
		add("i", "89okjui*(OKJUI");
		add("o", "90plkio()PLKIO");
		add("p", "0-[;lop)_{:LOP");
		add("[", "-=]';p[_+}\":P{");
		add("]", "=[']+{\"}");
		add("a", "aAqwszQWSZ");
		add("s", "wedxzasWEDXZAS");
		add("d", "erfcxsdERFCXSD");
		add("f", "rtgvcdfRTGVCDF");
		add("g", "tyhbvfgTYHBVFG");
		add("h", "yujnbghYUJNBGH");
		add("j", "uikmnhjUIKMNHJ");
		add("k", "iol,mjkIOL<MJK");
		add("l", "op;.,klOP:><KL");
		add(";", "p['/.l;P{\"?>L:");
		add("'", "][;/'}{:?\"");
		add("z", "asxzASXZ");
		add("x", "zsdcxZSDCX");
		add("c", "xdfvcXDFVC");
		add("v", "cfgbvCFGBV");
		add("b", "vghnbVGHNB");
		add("n", "bhjmnBHJMN");
		add("m", "njk,mNJK<M");
		add(",", "mkl.,MKL><");
		add(".", ",l;/.<L:?>");
		add("/", "';./\":>?");
		add("{", "-=]';p[_+}\":P{");
		add("}", "=[']+{\"}");
		add("<", "mkl.,MKL><");
		add(">", ",l;/.<L:?>");
		add("?", "';./\":>?");
		add(":", "p['/.l;P{\"?>L:");
		add("\"", "][;/'}{:?\"");
		// line = line_init;
		// upper = upper_init;

	}

}
