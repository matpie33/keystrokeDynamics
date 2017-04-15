package pl.master.thesis.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
	private String driverName;
	private Properties properties;

	public Database(String file) {
		prop = new Properties();
		try {
			FileInputStream stream = new FileInputStream(file);
			prop.loadFromXML(stream);
			dbms = prop.getProperty("dbms");
			jarFile = prop.getProperty("jar_file");
			driverName = prop.getProperty("driver");
			dbName = prop.getProperty("database_name");
			serverName = prop.getProperty("server_name");
			userName = prop.getProperty("user_name");
			password = prop.getProperty("password");
			portNumber = Integer.parseInt(prop.getProperty("port_number"));
			Class.forName(driverName);

		}
		catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

	}

	public Connection createConnection() throws SQLException {
		Connection connection = null;
		properties = new Properties();
		properties.put("user", userName);
		properties.put("password", password);

		String connectionUrl = "";

		if (dbms.equals("mysql")) {
			connectionUrl = "jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/";
			connection = DriverManager.getConnection(connectionUrl, properties);

			urlString = connectionUrl + this.dbName;
			connection.setCatalog(this.dbName);
		}
		else if (dbms.equals("derby")) {

			urlString = "jdbc:" + dbms + "://localhost:1527/" + dbName;
			connection = DriverManager.getConnection(this.urlString + ";create=true", properties);

		}
		return connection;
	}

	public void closeConnection() throws SQLException {
		DriverManager.getConnection(this.urlString + ";shutdown=true", properties);

	}

}
