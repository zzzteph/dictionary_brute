
public class utils {
	static char shift(char c) {
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

	static String capsed(String part) {
		
		return part.toUpperCase();
	}
	
	
	static String shifted(String part) {
	
		StringBuilder str=new StringBuilder();
		for(int i=0;i<part.length();i++)
		{
			str.append(shift(part.charAt(i)));
		}
		return str.toString();
	}
	static String upperfirst(String part) {
		if(part.length()==0)return "";
		StringBuilder str=new StringBuilder();
		str.append(shifted(part.substring(0, 1)));
		str.append(part.substring(1));
		return str.toString();
	}
}
