package pl.master.thesis.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlStatements {
	
	public static void addUser (Connection connection, String username, String password,
			String question, String answer) throws SQLException{
		Statement s = null;
		String query = "INSERT INTO Users (Username,Password, Question, Answer) "
				+ "VALUES ('"+username+"','"+password+"','"+question+"','"+answer+"')";
		
		s=connection.createStatement();
		s.executeUpdate(query);		
	}
	
	
	public static boolean userExists(Connection connection, String username, char [] password) throws SQLException{
		String passwordd = new String(password);
		Statement s = null;
		String query = "SELECT * "
				+ "FROM Users "
				+ "WHERE username='"+username+"' AND password='"+passwordd+"'";
		
		s=connection.createStatement();
		
		
		ResultSet r = s.executeQuery(query);
		return r.next();
	}

}
