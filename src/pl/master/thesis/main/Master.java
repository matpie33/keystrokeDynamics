package pl.master.thesis.main;
import javax.swing.SwingUtilities;

import pl.master.thesis.frame.MainWindow;


public class Master {
		
	public static void main (String arg[]){
//		System.out.println("equals: "+new Digraph("a","c").equals(new Digraph("a","c")));
//		RealMatrix mx = MatrixUtils.createRealMatrix(new double [][]{
//			{1,2,3,4,5},
//			{2,3,4,5,6},
//			{3,4,5,6,7}
//		});
//		RealMatrix cov = inverseSquareRootOfCovarianceMatrix(mx);
////		testSquareMatrix(cov);
//		testInverseMatrix( MatrixUtils.createRealMatrix(new double [][]{
//			{1,4,5},
//			{2,12,4},
//			{3,22,5}
//		}));
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
