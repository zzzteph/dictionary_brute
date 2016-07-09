import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.UUID;

public class segen {

	static String[] line_lowercase = { "`1234567890-=", "qwertyuiop[]", "asdfghjkl;'", "zxcvbnm,./" };

	static String[] upper_lowercase = { "1qaz", "2wsx", "3edc", "4rfv", "5tgb", "6yhn", "7ujm", "8ik,", "9ol.", "0p;/",
			"-['" };

	static String[] upper_lowercase_backwards = { "]'/", "=[;.", "-pl,", "0okm", "9ijn", "8uhb", "7ygv", "6tfc", "5rdx",
			"4esz", "3wa" };

	static String[] line_uppercase = { "~!@#$%^&*()_+", "QWERTYUIOP{}", "ASDFGHJKL:\"", "ZXCVBNM<>?" };

	static String[] upper_uppercase = { "!QAZ", "@WSX", "#EDC", "$RFV", "%TGB", "^YHN", "&UJM", "*IK<", "(OL>", ")P:?",
			"_{\"", };

	static String[] upper_uppercase_backwards = { "}\"?", "+{:>", "_PL<", ")OKM", "(IJN", "*UHB", "&YGV", "^TFC",
			"%RDX", "$ESZ", "#WA" };

	static String[] line_capslock = { "`1234567890-=", "QWERTYUIOP[]", "ASDFGHJKL;'", "ZXCVBNM,./" };

	static String[] upper_capslock = { "1QAZ", "2WSX", "3EDC", "4RFV", "5TGB", "6YHN", "7UJM", "8IK,", "9OL.", "0P;/",
			"-['" };

	static String[] upper_capslock_backwards = { "]'/", "=[;.", "-PL,", "0OKM", "9IJN", "8UHB", "7YGV", "6TFC", "5RDX",
			"4ESZ", "3WA" };

	private static HashSet<String> merged;
	final static int max_size = 10000000;
	static PrintWriter writer;
	static String temp_name = "temp_brute_file";
	static int size = 0;

	static String exec_path = "";
	static Boolean runExternal = false;
	static Boolean debug = false;
	static Boolean policy = false;
	static Boolean reverse = false;

	static String lowercase = "qazwsxedcrfvtgbyhnujmikolp";
	static String uppercase = "QAZWSXEDCRFVTGBYHNUJMIKOLP";
	static String digits = "1234567890";

	static boolean policy_check(String password) {

		int contains_lowercase = 0;
		int contains_uppercase = 0;
		int contains_digits = 0;
		int contains_specchar = 0;
		int policy = 0;
		for (int i = 0; i < password.length(); i++) {
			if (lowercase.indexOf(password.charAt(i)) != -1)
				contains_lowercase = 1;
			else if (uppercase.indexOf(password.charAt(i)) != -1)
				contains_uppercase = 1;
			else if (digits.indexOf(password.charAt(i)) != -1)
				contains_digits = 1;
			else
				contains_specchar = 1;
		}
		policy = contains_lowercase + contains_uppercase + contains_digits + contains_specchar;
		if (policy >= 3)
			return true;
		return false;
	}

	static void runExternal() throws IOException, InterruptedException {
		System.out.println(exec_path + " " + temp_name);
		// hacked external program run
		Process p = Runtime.getRuntime().exec(exec_path + " " + temp_name);
		System.out.println("Waiting for batch file ...");
		BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));

		while ((stdOut.readLine()) != null)
			;

		System.out.println("Batch file done.");
	}

	static void write_to_file(String temp) {
		if (size > max_size) {
			System.out.println("Created");
			writer.close();

			try {
				runExternal();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			size = 0;
			(new File(temp_name)).delete();
			try {
				writer = new PrintWriter(temp_name, "UTF-8");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			size++;
			writer.println(temp);
		}
	}

	static void checkFileEnd() {

		try {
			runExternal();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		(new File(temp_name)).delete();

	}

	static void reverse() {

		HashSet<String> rev = new HashSet<String>();
		for (String tmp : merged) {
			rev.add(new StringBuilder(tmp).reverse().toString());
		}
		for (String tmp : rev) {
			merged.add(tmp);
		}
	}

	static void init_line(int size) {

		merged = new HashSet<String>();

		for (String tmp : line_lowercase) {
			for (int i = 0; i <= tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}
		for (String tmp : line_uppercase) {
			for (int i = 0; i <= tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}
		for (String tmp : line_capslock) {
			for (int i = 0; i <= tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}
		if (reverse == true)
			reverse();
	}

	static void init_upper(int size) {

		merged = new HashSet<String>();

		for (String tmp : upper_lowercase) {
			for (int i = 0; i <= tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}
		for (String tmp : upper_lowercase_backwards) {
			for (int i = 0; i <= tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}
		for (String tmp : upper_uppercase) {
			for (int i = 0; i <= tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}

		for (String tmp : upper_uppercase_backwards) {
			for (int i = 0; i <= tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}

		for (String tmp : upper_capslock) {
			for (int i = 0; i <= tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}

		for (String tmp : upper_capslock_backwards) {
			for (int i = 0; i <= tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}

		// reverse strings
		if (reverse == true)
			reverse();
	}

	static void generate(HashSet<String> iterate, String start, int depth) {
		StringBuilder temp = new StringBuilder();

		for (String tmp : iterate) {
			temp.append(start);
			temp.append(tmp);

			if (depth != 0) {

				generate(iterate, temp.toString(), depth - 1);

			} else {

				if (policy == true) {
					if (policy_check(temp.toString()) == true) {

						if (runExternal == true) {
							write_to_file(temp.toString());
						} else {
							System.out.println(temp.toString());

						}
					}
				} else {
					if (runExternal == true) {
						write_to_file(temp.toString());
					} else {
						System.out.println(temp.toString());

					}
				}
			}
			temp.setLength(0);
		}

	}

	static void help()
	{
		System.out.println("-e , --exec  executable path with options");
		System.out.println("Sample: -e \"D:\\hashcat\\hashcat-cli64.exe -m 0 test.txt\"");
		System.out.println("-t , --type 0|1");
		System.out.println("0 - upper");
		System.out.println("1 - line(default)");
		System.out.println("-p , --parts number");
		System.out.println("Sample qwe,asd,zxc, qwer...");
		System.out.println("-l , --length number");
		System.out.println("Sample qweasd,zxcqwe...");
		System.out.println("--policy use windows policy checker (false)");
		System.out.println("--reverse add reverse parts (false)");
	}
	public static void main(String[] args) {

		Integer type = 1;
		Integer parts = 4;
		Integer length = 4;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-e") || args[i].equals("--exec"))
				if (i + 1 < args.length) {
					exec_path = args[i + 1];
					runExternal = true;
				}
			if (args[i].equals("-d") || args[i].equals("--debug"))
				if (i + 1 < args.length) {
					debug = Boolean.valueOf(args[i + 1]);
				}
			if (args[i].equals("-t") || args[i].equals("--type"))
				if (i + 1 < args.length)
					type = Integer.parseInt(args[i + 1]);
			if (args[i].equals("-p") || args[i].equals("--parts"))
				if (i + 1 < args.length)
					parts = Integer.parseInt(args[i + 1]);
			if (args[i].equals("-l") || args[i].equals("--length"))
				if (i + 1 < args.length)
					length = Integer.parseInt(args[i + 1]);
			if (args[i].equals("--policy"))
				policy = true;
			if (args[i].equals("--reverse"))
				reverse = true;

			if (args[i].equals("-h") || args[i].equals("--help"))
					help();
			
			
			
		}

		temp_name = UUID.randomUUID().toString().replace("-", "");
		try {
			writer = new PrintWriter(temp_name, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (type == 1)
			init_line(parts);
		else
			init_upper(parts);
		System.out.println(merged.size());
		length = length - 1;// Human readable
		generate(merged, "", length);
		if (runExternal == true)
			checkFileEnd();

	}
}
