package core.modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import common.Logger;
import common.Utils;

import core.beans.Strings;
import core.modules.helpers.Part;

public class Keybordcomb extends ModuleImpl {

	String[] line = {};
	String[] upper = {};

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
	String lowercase;
	String uppercase;
	String digits = "1234567890";
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

		this.options.put(Strings.TAIL, temp_name);
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

	public List<String> run() {

		Integer type = 0;
		Integer parts = 3;
		Integer length = 3;
		Integer partsmax = 0;
		merged = new HashSet<String>();
		if (options.get(Strings.KEYBOARD).equalsIgnoreCase("qwerty")) {
			init_qwerty();
		} else {
			Logger.error("No such keyboard");
		}

		if (options.get(Strings.TYPE) != null)
			type = Integer.parseInt(options.get(Strings.TYPE));
		if (options.get(Strings.PARTS) != null)
			parts = Integer.parseInt(options.get(Strings.PARTS));
		if (options.get(Strings.LENGTH) != null)
			length = Integer.parseInt(options.get(Strings.LENGTH));
		if (options.get(Strings.MAX_PARTS) != null)
			partsmax = Integer.parseInt(options.get(Strings.MAX_PARTS));
		if (Boolean.parseBoolean(options.get(Strings.POLICY)))
			policy = true;
		if (Boolean.parseBoolean(options.get(Strings.REVERSE)))
			reverse = true;
		if (Boolean.parseBoolean(options.get(Strings.OPTIMIZATION)))
			optimization = true;

		temp_name = Utils.getOutputFile();

		try {

			writer = new PrintWriter(temp_name, "UTF-8");
		} catch (FileNotFoundException e) {
			Logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			Logger.error(e.getMessage());
		}
		if (partsmax != 0) {

			for (int i = parts; i <= partsmax; i++) {
				init(type, i);
			}
		} else {
			init(type, parts);
		}

		length = length - 1;// Human readable
		passwords = new HashSet<Part>();
		for (String tmp : merged) {
			passwords.add(new Part(tmp));
		}
		merged.clear();
		generate(passwords, new Part(), length);
		runExternal();
		writer.close();
		Utils.deleteFile(temp_name);

		return ret;
	}

	private void init_qwerty() {
		String[] line_init = { "`1234567890-=", "qwertyuiop[]", "asdfghjkl;'", "zxcvbnm,./" };

		String[] upper_init = { "1qaz", "2wsx", "3edc", "4rfv", "5tgb", "6yhn", "7ujm", "8ik,", "9ol.", "0p;/", "-['",
				"2qaz", "3wsx", "4edc", "5rfv", "6tgb", "7yhn", "8ujm", "9ik,", "0ol.", "-p;/", "=]'/", "-[;.", "0pl,",
				"9okm", "8ijn", "7uhb", "6ygv", "5tfc", "4rdx", "3esz" };

		lowercase = "qazwsxedcrfvtgbyhnujmikolp";
		uppercase = "QAZWSXEDCRFVTGBYHNUJMIKOLP";
		digits = "1234567890";
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
		line=line_init;
		upper=upper_init;
		
	}

}
