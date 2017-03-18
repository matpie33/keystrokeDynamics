package pl.master.thesis.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlStatements {
	
	public static int addUser (Connection connection, String username, String password,
			String question, String answer) throws SQLException{
		PreparedStatement s = null;
		String query = "INSERT INTO Users (Username,Password, Question, Answer) "
				+ "VALUES ('"+username+"','"+password+"','"+question+"','"+answer+"')";
		s=connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		s.executeUpdate();		
		ResultSet generatedKeys = s.getGeneratedKeys();
		int userId = 0;
		if (generatedKeys.next()){
			  userId = generatedKeys.getInt(1);
		}
		
		s.close();
		connection.close();
		return userId;
	}
	
	
	public static boolean userExists(Connection connection, String username, char [] password) throws SQLException{
		String passwordd = new String(password);
		Statement s = null;
		String query = "SELECT * "
				+ "FROM Users "
				+ "WHERE username='"+username+"' AND password='"+passwordd+"'";
		
		s=connection.createStatement();
		
		
		ResultSet r = s.executeQuery(query);
//		connection.close();
		return r.next();
	}

}
