package com.ontolabs.ayllu.examples.classification;

import java.util.Iterator;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
/**
 * Large Margin Circle Classifier, that estimates the distance between the two classes
 * based on the radius. 
 * @author uday
 *
 */
public class CircleFitClassifier extends AbstractClassifier implements Classifier
{
    private static final long serialVersionUID = 1L;
    
    private double r;
    
   
    public void buildClassifier(Instances data) throws Exception
    {
        Iterator<Instance> itinstance;
        Instance instance;
        double rposmax, rnegmin, rnow;
        
        rposmax = Double.NEGATIVE_INFINITY;
        rnegmin = Double.POSITIVE_INFINITY;
        
        itinstance = data.iterator();
        while(itinstance.hasNext())
        {
            instance = itinstance.next();
            rnow = getRadius(instance);
            if (getLabel(instance) == 1.0)
            {
                if (rnow > rposmax) rposmax = rnow;
            }
            else
            {
                if (rnow < rnegmin) rnegmin = rnow;
            }
        }
        if (rposmax == Double.NEGATIVE_INFINITY) rposmax = rnegmin;
        if (rnegmin == Double.POSITIVE_INFINITY) rnegmin = rposmax;
        
        // Radius is the middle between the largest positive and smallest negative radius from the training set.
        this.r = (rposmax + rnegmin) / 2;
    }
    
    public double getR() { return this.r; }
    
    public double[] distributionForInstance(Instance instance) throws Exception
    {
        double   r, d;
        double []conf;
        
        conf = new double[2];
        
        // Set instance label to positive when point falls within circle
        r = getRadius(instance);
        d = Math.abs(r - this.r);
        if (r <= this.r) { conf[0] = d; conf[1] = 0; }
        else             { conf[1] = d; conf[0] = 0; }
        
        // confidence = shortest distance to radius
        return(conf);
    }
    
    private double getLabel(Instance instance)
    {
    	int totalDimensions = instance.numAttributes();
        return Integer.parseInt(instance.stringValue(totalDimensions-1));
    }
    
    public static double getRadius(Instance instance)
    {
        double x,y,r;
        int totalDimensions = instance.numAttributes();
        double sumOfSquares =0;
        for(int dim=0; dim< totalDimensions-1; dim++){  
        	double val = instance.value(dim);
   		 	sumOfSquares = val*val + sumOfSquares;
   	 	}
        r = Math.sqrt(sumOfSquares);
        
        return(r);
    }
}
