package core.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
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
				+ "'project' TEXT,"
				+ "'project_folder' TEXT,"
				+ "'hashfile' TEXT,"
				+ "'outputfile' TEXT);");
	}
	
	public Integer checkPreviousSession(String projectFolder, String project, String hashfile){
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM `session` "
					+ "WHERE `finished` IS NULL AND `project_folder` = '"+projectFolder+"' AND `project` = '"+project+"' AND `hashfile` = '"+hashfile+"'"
					+ "ORDER BY `started` DESC "
					+ "LIMIT 1");
			if(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
	
	public Boolean startSession(Integer previousSession, String project, String projectFolder, String hashfile, String outputfile){
		if (previousSession != null) {
			finishSession(previousSession);
		}
		try {
			statement.execute("INSERT INTO 'session' ('project', 'project_folder', 'hashfile', 'outputfile', 'started') VALUES ('"+project+"', '"+projectFolder+"','"+hashfile+"', '"+outputfile+"' ,CURRENT_TIMESTAMP); ");
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
		return finishSession(currentSession);
	}
	
	public Boolean finishSession(Integer sessionId){
		try {
			statement.executeUpdate("UPDATE 'session' set 'finished' = CURRENT_TIMESTAMP WHERE id = "+sessionId+"; ");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
