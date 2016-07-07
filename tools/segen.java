import java.util.HashSet;

public class segen {

	static String[] line_lowercase = { "`1234567890-=", "qwertyuiop[]",
			"asdfghjkl;'", "zxcvbnm,./" };

	static String[] upper_lowercase = { "1qaz", "2wsx", "3edc", "4rfv", "5tgb",
			"6yhn", "7ujm", "8ik,", "9ol.", "0p;/", "-['" };

	static String[] upper_lowercase_backwards = { "]'/", "=[;.", "-pl,",
			"0okm", "9ijn", "8uhb", "7ygv", "6tfc", "5rdx", "4esz", "3wa" };

	static String[] line_uppercase = { "~!@#$%^&*()_+", "QWERTYUIOP{}",
			"ASDFGHJKL:\"", "ZXCVBNM<>?" };

	static String[] upper_uppercase = { "!QAZ", "@WSX", "#EDC", "$RFV", "%TGB",
			"^YHN", "&UJM", "*IK<", "(OL>", ")P:?", "_{\"", };

	static String[] upper_uppercase_backwards = { "}\"?", "+{:>", "_PL<",
			")OKM", "(IJN", "*UHB", "&YGV", "^TFC", "%RDX", "$ESZ", "#WA" };

	static String[] line_capslock = { "`1234567890-=", "QWERTYUIOP[]",
			"ASDFGHJKL;'", "ZXCVBNM,./" };

	static String[] upper_capslock = { "1QAZ", "2WSX", "3EDC", "4RFV", "5TGB",
			"6YHN", "7UJM", "8IK,", "9OL.", "0P;/", "-['" };

	static String[] upper_capslock_backwards = { "]'/", "=[;.", "-PL,", "0OKM",
			"9IJN", "8UHB", "7YGV", "6TFC", "5RDX", "4ESZ", "3WA" };

	private static HashSet<String> merged;

	static void init_line(int size) {

		merged = new HashSet<String>();

		for (String tmp : line_lowercase) {
			for (int i = 0; i < tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}
		for (String tmp : line_uppercase) {
			for (int i = 0; i < tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}
		for (String tmp : line_capslock) {
			for (int i = 0; i < tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}

	}

	static void init_upper(int size) {

		merged = new HashSet<String>();

		for (String tmp : upper_lowercase) {
			for (int i = 0; i < tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}
		for (String tmp : upper_lowercase_backwards) {
			for (int i = 0; i < tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}
		for (String tmp : upper_uppercase) {
			for (int i = 0; i < tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}

		for (String tmp : upper_uppercase_backwards) {
			for (int i = 0; i < tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}

		for (String tmp : upper_capslock) {
			for (int i = 0; i < tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}

		for (String tmp : upper_capslock_backwards) {
			for (int i = 0; i < tmp.length() - size; i++) {
				merged.add(tmp.substring(i, i + size));
			}
		}

	}

	static void generate_3() {
		StringBuilder temp = new StringBuilder();
		for (String part1 : merged)
			for (String part2 : merged)
				for (String part3 : merged) {
					temp.append(part1);
					temp.append(part2);
					temp.append(part3);
					System.out.println(temp.toString());
					temp.setLength(0);
				}
	}

	static void generate_4() {
		StringBuilder temp = new StringBuilder();
		for (String part1 : merged)
			for (String part2 : merged)
				for (String part3 : merged)
					for (String part4 : merged) {
						temp.append(part1);
						temp.append(part2);
						temp.append(part3);
						temp.append(part4);
						System.out.println(temp.toString());
						temp.setLength(0);
					}
	}

	static void generate_5() {
		StringBuilder temp = new StringBuilder();
		for (String part1 : merged)
			for (String part2 : merged)
				for (String part3 : merged)
					for (String part4 : merged)
						for (String part5 : merged) {
							temp.append(part1);
							temp.append(part2);
							temp.append(part3);
							temp.append(part4);
							temp.append(part5);
							System.out.println(temp.toString());
							temp.setLength(0);
						}
	}

	public static void main(String[] args) {
		Boolean line = false;
		if (line)
			init_line(3);
		else
			init_upper(3);

		System.out.println(merged.size());
		generate_5();

	}

}
