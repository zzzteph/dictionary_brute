package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Utils {

	public static List<String> readFile(String file) {
		List<String> ret = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!ret.contains(line))
					ret.add(line);
			}
		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return ret;
	}

	public static void writeToFile(String filename, List<String> data) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			for (String tmp : data) {
				writer.println(tmp);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			Logger.error(e.getMessage());
		}

	}

	public static String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String getOutputFile() {

		File outFile = new File(System.getProperty("java.io.tmpdir"),
				getRandomString());
		return outFile.getAbsolutePath();
	}

	public static boolean deleteFile(String filename) {
		File file = new File(filename);

		if (file.delete()) {
			return true;
		}
		return false;

	}

	public static List<String> scanDirectory(String path) {

		File dir = new File(path);
		List<String> ret = new ArrayList<String>();
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					ret.addAll(scanDirectory(file.getAbsolutePath()));
				} else {
					ret.add(file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static DocumentBuilder getDocumentBuilder() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			dbFactory.setFeature(
					"http://apache.org/xml/features/disallow-doctype-decl",
					true);
			dbFactory.setFeature(
					"http://xml.org/sax/features/external-general-entities",
					false);
			dbFactory.setFeature(
					"http://xml.org/sax/features/external-parameter-entities",
					false);
			dbFactory
					.setFeature(
							"http://apache.org/xml/features/nonvalidating/load-external-dtd",
							false);
			dbFactory.setXIncludeAware(false);
			dbFactory.setExpandEntityReferences(false);
			return dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			Logger.error(ex.getMessage());
		}

		return null;
	}

}