package core.modules;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Logger;
import common.Utils;

import core.ModuleImpl;
import core.beans.CommandLine;
import core.beans.Strings.Common;

public class Ntlmsuggest extends ModuleImpl {

	private Map<String, String> parseNTLMFile(String filePath) {
		Map<String, String> ret = new HashMap<String, String>();
		List<String> hashes = Utils.readFileUniq(CommandLine.getInstance().getOption(Common.WORKFILE));
		for (String hash : hashes) {
			String[] parts = hash.split(":");
			if (parts.length > 2) {
				if (parts[0].length() > 2) {
					ret.put(hash, parts[0]);
				}
			}
		}

		return ret;
	}

	private void NTLMDictionary(String user) {
		PrintWriter writer = null;

		try {
			writer = new PrintWriter(Utils.getTMPFile(), "UTF-8");
		} catch (FileNotFoundException e) {
			Logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			Logger.error(e.getMessage());
		}

		List<String> dictionary = new ArrayList<String>();
		dictionary.add(user);
		dictionary.add(user.toLowerCase());
		dictionary.add(user.toUpperCase());
		dictionary.add(user.substring(0, 1).toUpperCase() + user.substring(1));
		dictionary.add(user.replaceAll("[^a-zA-Z0-9]", ""));
		for (String tmp : dictionary) {
			writer.println(tmp.concat("!"));
			writer.println(tmp.concat("@"));
			writer.println(tmp.concat("#"));
			writer.println(tmp.concat("$"));
			writer.println(tmp.concat("!@"));
			writer.println(tmp.concat("!@#"));
			writer.println(tmp.concat("!@#$"));

			for (Integer j = 0; j < 10000; j++) {
				writer.println(tmp.concat(j.toString()));
				writer.println(tmp.concat(j.toString()).concat("!"));
				writer.println(tmp.concat(j.toString()).concat("@"));
				writer.println(tmp.concat(j.toString()).concat("#"));
				writer.println(tmp.concat(j.toString()).concat("$"));
				writer.println(tmp.concat(j.toString()).concat("!@"));
				writer.println(tmp.concat(j.toString()).concat("!@#"));
				writer.println(tmp.concat(j.toString()).concat("!@#$"));

			}

		}

		writer.close();

	}

	private void makeTEMPNTLM(String key) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(CommandLine.getInstance().getOption(Common.WORKFILE), "UTF-8");
			writer.println(key);
			writer.close();
		} catch (FileNotFoundException e) {
			Logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			Logger.error(e.getMessage());
		}

	}

	public List<String> run() {

		Logger.info("NTLMSuggest working...");
		List<String> ret = new ArrayList<String>();
		this.tail.add(Utils.getTMPFile());
		Utils.cloneFile(CommandLine.getInstance().getOption(Common.WORKFILE),
				CommandLine.getInstance().getOption(Common.WORKFILE).concat("_temp"));
		for (Map.Entry<String, String> entry : this.parseNTLMFile(CommandLine.getInstance().getOption(Common.WORKFILE))
				.entrySet()) {
			NTLMDictionary(entry.getValue());
			makeTEMPNTLM(entry.getKey());
			Logger.debug(entry.getValue());

			for (String tmp : super.run()) {
				ret.add(tmp);
				Logger.info("TempCrack:" + tmp);
			}
			Logger.debug("RETSIZE:" + ret.size());

		}
		Utils.cloneFile(CommandLine.getInstance().getOption(Common.WORKFILE).concat("_temp"),
				CommandLine.getInstance().getOption(Common.WORKFILE));
		Utils.deleteFile(CommandLine.getInstance().getOption(Common.WORKFILE).concat("_temp"));
		Utils.deleteFile(Utils.getTMPFile());
		return ret;
	}

}
