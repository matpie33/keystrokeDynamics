package pl.master.thesis.database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import pl.master.thesis.dialogs.WaitingDialog;

public class ConnectionSwingWorker extends SwingWorker<Void,Void> {
	
	private final String propertiesFile = "properties/db properties.xml";
	protected ProgressMonitor p;
		
		@Override
        public Void doInBackground() {
		Connection connection = null;
    		try {
    			System.out.println("tried");
    			connection = createConnection();  
    			
    			doSqlStatements(connection);
    			doOtherThings();    			
    		} 
    		
    		catch (SQLException e2) {
    			e2.printStackTrace();
    		}
    		
    		finally {
    			try { 
    				if (connection!=null){
	    				connection.close();
	    				connection=null;
    				}
    			}
    			catch (SQLException ex){
    				ex.printStackTrace();
    			}
    		}
            
            return null;
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
	

