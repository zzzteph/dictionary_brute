class Part {
	String password;
	Integer lowercase;
	Integer uppercase;
	Integer digits;
	Integer specchars;

	public Part() {
		password = "";
		this.lowercase = 0;
		this.uppercase = 0;
		this.digits = 0;
		this.specchars = 0;
	}

	public Part(String word) {
		System.out.println(word);
		this.password = word;
		calcStats();
	}

	public void append(Part part) {

		this.password = this.password + part.getPassword();
		this.lowercase += part.getLowercase();
		this.uppercase += part.getUppercase();
		this.digits += part.getDigits();
		this.specchars += part.getSpecchar();
		if (this.lowercase > 1)
			this.lowercase = 1;
		if (this.uppercase > 1)
			this.uppercase = 1;
		if (this.digits > 1)
			this.digits = 1;
		if (this.specchars > 1)
			this.specchars = 1;

	}

	void calcStats() {
		String lcase = "qazwsxedcrfvtgbyhnujmikolp";
		String ucase = "QAZWSXEDCRFVTGBYHNUJMIKOLP";
		String ds = "1234567890";
		this.lowercase = 0;
		this.uppercase = 0;
		this.digits = 0;
		this.specchars = 0;
		for (int i = 0; i < password.length(); i++) {
			if (lcase.indexOf(password.charAt(i)) != -1)
				lowercase = 1;
			else if (ucase.indexOf(password.charAt(i)) != -1)
				uppercase = 1;
			else if (ds.indexOf(password.charAt(i)) != -1)
				digits = 1;
			else
				specchars = 1;
		}

	}

	public boolean policyCheck() {
		if (this.lowercase + this.uppercase + this.digits + this.specchars >= 3)
			return true;
		return false;
	}

	public Integer getLowercase() {
		return this.lowercase;
	}

	public Integer getUppercase() {
		return this.uppercase;

	}

	public Integer getDigits() {
		return this.digits;
	}

	public Integer getSpecchar() {
		return this.specchars;
	}

	public String getPassword() {
		return this.password;
	}
}