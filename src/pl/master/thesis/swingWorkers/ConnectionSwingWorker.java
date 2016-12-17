package pl.master.thesis.swingWorkers;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import pl.master.thesis.database.Database;

public class ConnectionSwingWorker extends SwingWorker<Void,Void> {
	
	private final String propertiesFile = "properties/db properties.xml";
	protected ProgressMonitor p;
			
		@Override
        public Void doInBackground() {
		Connection connection = null;
    		try {
    			connection = createConnection();     			
    			doSqlStatements(connection);
    			doOtherThings();    			
    		} 
    		
    		catch (SQLException e2) {
    			e2.printStackTrace();
    			
    		}
    		
    		finally {
    			tryToCloseConnection(connection);
    		}
            
            return null;
        }
		
		private void tryToCloseConnection(Connection connection){
			try { 
				if (connection!=null){
    				connection.close();
    				connection=null;
    				System.out.println("connection is closed");
				}
			}
			catch (SQLException ex){
				ex.printStackTrace();
			}
		}

        protected void doOtherThings() {
			//override it
		}

		protected void doSqlStatements(Connection connection) throws SQLException {
			//override it
		}

		@Override
        public void done() {
//        	p.setProgress(0); 
        }        
        
        protected Connection createConnection() throws SQLException{
        	Database d = new Database(propertiesFile);    		
    		Connection connection = d.createConnection();     		
    		
    		return connection;
        }
        
}
	

