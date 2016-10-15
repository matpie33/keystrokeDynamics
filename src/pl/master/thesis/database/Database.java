package pl.master.thesis.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Database {

	  public String dbms;
	  public String jarFile;
	  public String dbName; 
	  public String userName;
	  public String password;
	  public String urlString;
	  
	  private String serverName;
	  private int portNumber;
	  private Properties prop;
	  
	  public Database (String file){
		  prop = new Properties();
		  try {
			  
			FileInputStream stream = new FileInputStream(file);
			prop.loadFromXML(stream);
			dbms=prop.getProperty("dbms");
			jarFile = prop.getProperty("jar_file");
			prop.getProperty("driver");
			dbName = prop.getProperty("database_name");
			serverName = prop.getProperty("server_name");
			portNumber = Integer.parseInt(prop.getProperty("port_number"));					
			
		  } 
		  catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  
	  public Connection createConnection() throws SQLException{
		  Connection connection = null;
		  Properties properties = new Properties();
		  properties.put("user", "");
		  properties.put("password", "");
		  
		  String connectionUrl = "";
		  
		  if (dbms.equals("mysql")) {
		      connectionUrl= "jdbc:" + dbms + "://" + serverName +
		                                      ":" + portNumber + "/";
		      connection =DriverManager.getConnection(connectionUrl, properties);
		          		      
		      urlString = connectionUrl + this.dbName;
		      connection.setCatalog(this.dbName);
		  } 
		  else if (dbms.equals("derby")) {
			  
		      urlString = "jdbc:" + dbms + ":" + dbName;		      
		      connection =DriverManager.getConnection(this.urlString + ";create=true", properties);
		          	                      
		  }
	      return connection;
		  
	  }
}
