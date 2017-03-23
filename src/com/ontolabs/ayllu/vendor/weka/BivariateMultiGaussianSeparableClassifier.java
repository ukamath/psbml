package com.ontolabs.ayllu.vendor.weka;

import java.util.Iterator;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
/**
 * A simulated Large Marging Classifier that estimates the distance
 * between two classes. 
 * It uses the point-distance equation for the hidden margin of line x=20
 * for generating the confidence.
 * 
 * @author uday
 *
 */
public class BivariateMultiGaussianSeparableClassifier extends AbstractClassifier implements Classifier {
	private static final long serialVersionUID = 1L;

	private double r;
	double[] bottom;
	double[] top;

	public void buildClassifier(Instances data) throws Exception {
		Iterator<Instance> itinstance;
		Instance instance;
		double rposmax, rnegmin, rnow;

		rposmax = Double.NEGATIVE_INFINITY;
		rnegmin = Double.POSITIVE_INFINITY;

		itinstance = data.iterator();
		while (itinstance.hasNext()) {
			instance = itinstance.next();
			rnow = instance.value(0);
			if (getLabel(instance) == 1.0) {
				if (rnow > rposmax)
					rposmax = rnow;
			} else {
				if (rnow < rnegmin)
					rnegmin = rnow;
			}
		}
		if (rposmax == Double.NEGATIVE_INFINITY)
			rposmax = rnegmin;
		if (rnegmin == Double.POSITIVE_INFINITY)
			rnegmin = rposmax;

		// boundary is the middle between the largest positive and smallest
		// negative value from the training set.
		this.r = (rposmax + rnegmin) / 2;
		// points for drawing a line and getting distance
		bottom = new double[] { this.r, 0 };
		top = new double[] { this.r, 20 };
	}
	
	 public double[] distributionForInstance(Instance instance) throws Exception
	    {
	        double   r, d;
	        double []conf;
	        
	        conf = new double[2];
	        
	        // Set instance label to positive when point falls within 2d plane
	        d = getDistanceFromBoundary(instance);
	        if (d >= this.r) { conf[0] = d; conf[1] = 0; }
	        else             { conf[1] = d; conf[0] = 0; }
	        
//	        Utils.normalize(conf);
	        // confidence = shortest distance to edge
	        return(conf);
	    }

	private double getLabel(Instance instance) {
		int totalDimensions = instance.numAttributes();
		return Integer.parseInt(instance.stringValue(totalDimensions - 1));
	}

	public double getDistanceFromBoundary(Instance instance) {
		double distance = 0;
		distance = pointToLineDistance(new double[]{instance.value(0), instance.value(1)}, this.bottom, this.top);
		return distance;
	}

	/**
	 * 
	 * @param p given instance
	 * @param A data on the Edge of X axis
	 * @param B data on the Edge on top of the line
	 * compute a normal and then use point line distance formula
	 * @return
	 */public  double pointToLineDistance(double[] p,double[] A, double[] B) {
		double normalLength = Math.sqrt((B[0] - A[0]) * (B[0] - A[0])
				+ (B[1] - A[1]) * (B[1] - A[1]));
		return Math.abs((p[0] - A[0]) * (B[1] - A[1]) - (p[1] - A[1])
				* (B[0] - A[0]))
				/ normalLength;
	}
}
