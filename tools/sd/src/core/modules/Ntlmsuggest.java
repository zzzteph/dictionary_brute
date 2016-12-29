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
		List<String> hashes = Utils.readFileUniq(CommandLine.getInstance()
				.getOption(Common.WORKFILE));
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
		List<String> dictionary = new ArrayList<String>();
		dictionary.clear();
		List<String> users = new ArrayList<String>();
		dictionary.add(user);
		dictionary.add(user.toLowerCase());
		dictionary.add(user.toUpperCase());
		dictionary.add(user.substring(0, 1).toUpperCase() + user.substring(1));
		dictionary.add(user.replaceAll("[^a-zA-Z0-9]", ""));
		for (String tmp : dictionary) {
			for (Integer j = 0; j < 100; j++) {
				users.add(tmp.concat(j.toString()));
			}
			for (Integer j = 1900; j < 2100; j++) {
				users.add(tmp.concat(j.toString()));
			}
		}
		for (String tmp : users) {
			dictionary.add(tmp);
		}
		users.clear();

		for (String tmp : dictionary) {

			users.add(tmp.concat("!"));
			users.add(tmp.concat("@"));
			users.add(tmp.concat("#"));
			users.add(tmp.concat("$"));
			users.add(tmp.concat("!@"));
			users.add(tmp.concat("!@#"));
			users.add(tmp.concat("!@#$"));

		}

		for (String tmp : users) {
			dictionary.add(tmp);
		}
		users.clear();

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(Utils.getTMPFile(), "UTF-8");
		} catch (FileNotFoundException e) {
			Logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			Logger.error(e.getMessage());
		}
		for (String tmp : dictionary) {
			writer.println(tmp);

		}
		writer.close();

	}

	private void makeTEMPNTLM(String key) {
		writer = new PrintWriter(filename, "UTF-8");
		for (String tmp : data) {
			writer.println(tmp);
		}
		writer.close();

	}

	public List<String> run() {

		System.out.println("NTLM");
		List<String> ret = new ArrayList<String>();
		this.tail.add(Utils.getTMPFile());
		// make temp file
		Utils.cloneFile(
				CommandLine.getInstance().getOption(Common.WORKFILE),
				CommandLine.getInstance().getOption(Common.WORKFILE)
						.concat("_temp"));
		for (Map.Entry<String, String> entry : this.parseNTLMFile(
				CommandLine.getInstance().getOption(Common.WORKFILE))
				.entrySet()) {
			NTLMDictionary(entry.getValue());
			makeTEMPNTLM(entry.getKey());
			for (String tmp : super.run()) {
				ret.add(tmp);
			}

		}
		Utils.cloneFile(CommandLine.getInstance().getOption(Common.WORKFILE)
				.concat("_temp"),
				CommandLine.getInstance().getOption(Common.WORKFILE));
		Utils.deleteFile(CommandLine.getInstance().getOption(Common.WORKFILE)
				.concat("_temp"));
		Utils.deleteFile(Utils.getTMPFile());
		return ret;
	}

}
