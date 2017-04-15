package pl.master.thesis.swingWorkers;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import pl.master.thesis.database.Database;

public abstract class ConnectionSwingWorker extends SwingWorker<Void, Void> {

	private final String propertiesFile = "properties/db properties.xml";
	protected ProgressMonitor p;
	protected Database database;
	{
		database = new Database(propertiesFile);
	}

	@Override
	public Void doInBackground() throws SQLException {
		Connection connection = null;
		try {
			connection = createConnection();
			doSqlStatements(connection);
			doOtherThings();
		}
		//
		catch (SQLException e2) {
			e2.printStackTrace();

		}

		// finally {
		// tryToCloseConnection(connection);
		// }

		return null;
	}

	private void tryToCloseConnection(Connection connection) {
		// try {
		// if (connection!=null){
		// connection.close();
		// connection=null;
		// }
		// }
		// catch (SQLException ex){
		// ex.printStackTrace();
		// }
	}

	protected void doOtherThings() {
	};

	protected abstract void doSqlStatements(Connection connection) throws SQLException;

	@Override
	public void done() {
		// p.setProgress(0);
	}

	protected Connection createConnection() throws SQLException {

		Connection connection = database.createConnection();

		return connection;
	}

}
