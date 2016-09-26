package core.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SessionHandler {
	private static SessionHandler instance;
	private Connection dbConnection;
	private Statement statement;
	private Integer currentSession;
	
	public SessionHandler(){
		try {
			Class.forName("org.sqlite.JDBC");
			dbConnection = DriverManager.getConnection("jdbc:sqlite:session.db");
			statement = dbConnection.createStatement();
			createSessionDB();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("something wrong with session.db");
			e.printStackTrace();
		}
	}
	
	public static synchronized SessionHandler getInstance() {
		if (instance == null) {
			instance = new SessionHandler();
		}
		return instance;
	}
	
	private Boolean createSessionDB() throws SQLException {
		return statement.execute("CREATE TABLE IF NOT EXISTS 'session' ("
				+ "'id' INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "'started' DATETIME,"
				+ "'finished' DATETIME,"
				+ "'cmd_options' TEXT,"
				+ "'options' TEXT);");
	}
	
	private String optionsToString(Map<String, String> options){
		String result = "";
		for(Entry<String,String> option : options.entrySet()) {
			result += option.getKey() + "=" + option.getValue() + ";";
		}
		return result;
	}
	
	private String optionsToString(List<String> options){
		String result = "";
		for(String option : options) {
			result += option + ";";
		}
		return result;
	}
	
	public Boolean checkPreviousSession(){
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM 'session' WHERE `finished` == NULL");
			while(rs.next()){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean cleanOldSessions(){
		try {
			statement.execute("DROP TABLE 'session'");
			return createSessionDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean startSession(Map<String, String> options){
		try {
			statement.execute("INSERT INTO 'session' ('started', 'options') VALUES (CURRENT_TIMESTAMP, '"+optionsToString(options)+"'); ");
			ResultSet key = statement.getGeneratedKeys();
			if (key.next()) {
				currentSession = key.getInt(1);
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean finishSession(){
		try {
			statement.executeUpdate("UPDATE 'session' set 'finished' = CURRENT_TIMESTAMP WHERE id = "+currentSession+"; ");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean addCommand(List<String> cmd){
		try {
			statement.executeUpdate("UPDATE 'session' set 'cmd_options' = '"+optionsToString(cmd)+"' WHERE id = "+currentSession+"; ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
