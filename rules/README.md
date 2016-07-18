Rules info
===================


All the rules are divided into four categories,  depending on what is happening with the password.

 - Lowercase or nothing (lower): **password**
 -  Capitalized first char (cap):   **Password**
 -  Uppercase all characters (upper): **PASSWORD**
 -  Lowercase first, uppercase rest chars (rcap): **pASSWORD**


Rule names
----------

 1. d - digits from 0 to 9
 2. sc - popular special chars: **!@#$_**
 3. day - digits from 00 to 31
 4. mon - digits from 00 to 12
 5. year - digits from 1900 to 2030
 6. pd - popular digits like 



###Samples

 - dd_Word_sc.rule
	 - 01Password! 
	 - 02Password! 
	 - ...
	 -  99Password_
 - Word_day_mon.rule
	 - Password0101
	 - Password0201
	 - ...
	 - Password3112
 - Word_day_mon_year.rule
	 - Password01011950
	 - Password02011950
	 - ....
	 - Password31122030
	 - 

