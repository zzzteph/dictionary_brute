package core.beans.Strings;

public interface Common {

	public static final String DEBUG = "DEBUG";// is debug enabled
	
	
	public static final String CONFIG = "CONFIG";// path to config folder

	public static final String MODULE = "MODULE";
	public static final String EXEC = "EXEC";
	public static final String INPUT = "INPUT"; // original file
	public static final String OUTPUT = "OUTPUT";// temp output file
	public static final String TMP = "TMP";// working tmp file
	public static final String WORKFILE = "WORKFILE";// temp_input file
	public static final String RESULT = "RESULT";// result file with cracked
													// passwords

	public static final String EXEC_STAGE = "EXEC_STAGE";

	
	
	public static final String RULES = "RULES";
	// dictionary
	public static final String DICTIONARY = "DICTIONARY";

	// Keyboard
	public static final String TYPE = "TYPE";
	public static final String SEGMENT_LENGTH = "SEGMENT_LENGTH";
	public static final String SEGMENTS_COUNT = "SEGMENTS_COUNT";
	public static final String MAX_PARTS = "MAX_PARTS";
	public static final String POLICY = "POLICY";
	public static final String REVERSE = "REVERSE";
	public static final String OPTIMIZATION = "OPTIMIZATION";
	public static final String KEYBOARD = "KEYBOARD";

	// Bruteforce
	public static final String BRUTEFORCE = "BRUTEFORCE";
	public static final String ATTACK_TYPE = "ATTACK_TYPE";
	public static final String MASK = "MASK";
	public static final String CHARSET_1 = "CHARSET_1";
	public static final String CHARSET_2 = "CHARSET_2";
	public static final String CHARSET_3 = "CHARSET_3";

	// Custom
	public static final String CUSTOM = "CUSTOM";

	// Suggester
	public static final String SUGGEST = "SUGGEST";
	public static final String LEET = "LEET";

}
