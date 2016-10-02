package pl.master.thesis.main;
import javax.swing.SwingUtilities;

import pl.master.thesis.frame.MainWindow;


public class Master {
	
	
	public static void main (String arg[]){
		SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
            	MainWindow w = new MainWindow();
//        		w.setResizable(false);
        		w.setVisible(true);
            }
        });
		
	}

}
