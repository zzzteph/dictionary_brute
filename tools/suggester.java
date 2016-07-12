import java.util.ArrayList;
import java.util.List;

public class suggester {

	static String[] passwords = { "admin", "adm", "cisco", "manager", "app",
			"administrator", "agent", "root", "bank", "public", "private",
			"db", "dba", "oracle", "mysql", "database", "123456", "123456789",
			"password", "12345678", "qwerty", "1234567", "111111", "123123",
			"1234567890", "1234", "654321", "12345", "666666", "123321",
			"112233", "987654321", "123456", "2016", "2015", "2014", "2013",
			"2011", "2012", "2017", "2018", };
	static String[] wifi = { "wi-fi", "wpa", "wpa2", "wep", "wifi" };
	static String[] separators = { "_", "-" };
	static String[] ending = { "!", "@", "#", "$" };
	static List<String> dictionary = new ArrayList<String>();
	static boolean wifi_suggest = false;
	static boolean password_policy = true;
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
		policy = contains_lowercase + contains_uppercase + contains_digits
				+ contains_specchar;
		if (policy >= 3)
			return true;
		return false;
	}

	static void add(String tmp) {
		if (password_policy == true) {
			if (policy_check(tmp))
				dictionary.add(tmp);
		} else
			dictionary.add(tmp);
	}

	public static void main(String[] args) {

		List<String> words = new ArrayList<String>();
		List<String> suggested_words = new ArrayList<String>();

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--wifi")) {
				wifi_suggest = true;
				continue;
			}
			if (args[i].equals("--no_policy")) {
				password_policy = false;
				continue;
			}
			suggested_words.add(args[i]);
			suggested_words.add(args[i].toUpperCase());
			suggested_words.add(args[i].substring(0, 1).toUpperCase()
					+ args[i].substring(1));
		}
		for (String password : passwords) {
			words.add(password);
			words.add(password.toUpperCase());
			words.add(password.substring(0, 1).toUpperCase()
					+ password.substring(1));
		}
		// adding passwords
		if (wifi_suggest == true) {
			for (String password : wifi) {
				words.add(password);
				words.add(password.toUpperCase());
				words.add(password.substring(0, 1).toUpperCase()
						+ password.substring(1));
			}
		}

		for (String word : suggested_words) {
			add(word);
		}
		// separated words
		for (String word1 : words) {
			for (String sword : suggested_words) {
				add(sword + word1);
				add(word1 + sword);
				for (String sep : separators) {
					add(sword + sep + word1);
					add(word1 + sep + sword);
				}

			}
		}

		for (int i = 0; i < words.size(); i++) {
			for (int j = i; j < words.size(); j++) {
				for (String sword : suggested_words) {
					add(words.get(j) + sword + words.get(i));
					add(words.get(i) + sword + words.get(j));
				}
			}
		}

		// policy_check
		// adding dates
		words.clear();
		suggested_words.clear();
		for (String tmp : dictionary) {
			System.out.println(tmp);
			for (String sep : ending)
				System.out.println(tmp + sep);

			for (Integer i = 0; i < 100; i++)
				System.out.println(tmp + i.toString());

		}
	}

}

