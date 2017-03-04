package pl.master.thesis.classifier;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

public class StatisticalClassifier {
	
	private RealMatrix calculateCovariance(RealMatrix entry){
		RealMatrix cov = new Covariance (entry).getCovarianceMatrix();
		System.out.println("covariance: "+cov);
		return cov;
	}
	
	private RealMatrix calculateSquareMatrix(RealMatrix m){
		RealMatrix matrix = new EigenDecomposition (m).getSquareRoot();
		System.out.println("square root: "+matrix);
		return matrix;
	}
	
	private RealMatrix calculateInverseMatrix (RealMatrix m){
		RealMatrix res = new LUDecomposition(m).getSolver().getInverse();
		System.out.println("inversed: "+res);
		return res;
	}
	
	public RealMatrix calculateInverseSquareRootOfCovarianceMatrix(RealMatrix entry){
		return calculateInverseMatrix(calculateSquareMatrix(calculateCovariance(entry)));
	}
	
}
