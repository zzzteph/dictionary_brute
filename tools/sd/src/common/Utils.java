package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Utils {
	public static void cloneFile(String source, String dest) {
		InputStream is = null;
		OutputStream os = null;
		try {
			try {
				is = new FileInputStream(source);
				os = new FileOutputStream(dest);
			} catch (FileNotFoundException e) {
				Logger.error(e.getMessage());
			}
			byte[] buffer = new byte[1024];
			int length;
			try {
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
			} catch (IOException e) {
				Logger.error(e.getMessage());
			}
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				Logger.error(e.getMessage());
			}

		}
	}

	public static String cleanStringDuplicateChars(String str) {

		String ret = "";

		Boolean exist = false;
		for (int i = 0; i < str.length(); i++) {

			for (int j = 0; j < ret.length(); j++) {

				if (ret.charAt(j) == str.charAt(i)) {
					exist = true;
					break;
				}

			}
			if (exist == false) {
				ret = ret + str.charAt(i);
			}

		}
		return ret.toString();

	}

	public static void rebuildInputFile(List<String> results, String InputFile) {

		List<String> file = readFileUniq(InputFile);
		for (String crackedPasswords : results) {
			int i = 0;
			for (String uncrackedPasswords : file) {
				if (crackedPasswords.contains(uncrackedPasswords)) {
					System.out.println(crackedPasswords);
					file.remove(i);
					break;
				}
				i++;
			}
		}

		writeToFile(InputFile, file);

	}

	public static List<String> readFileUniq(String file) {
		List<String> ret = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {

				if (!ret.contains(line))
					ret.add(line);
			}
			br.close();
		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

		return ret;
	}

	public static List<String> readFile(String file) {
		List<String> ret = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {

				ret.add(line);
			}
			br.close();
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
		} catch (FileNotFoundException e) {
			Logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
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

	public static String getInputFile() {
		return getOutputFile();
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