/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.classifier;



import com.ontolabs.ayllu.Learner;
import com.ontolabs.ayllu.data.DataVector;



/**
 * The Interface Classifier is abstraction for Machine Learning Classification or Supervised
 * Learning.
 */
public interface Classifier extends Learner{
	
	  
	  /**
	 * Classify.
	 *
	 * @param instance
	 *            the instance
	 * @return the double
	 * @throws ClassificationException
	 *             the classification exception
	 */
  	public double classify(DataVector instance) throws ClassificationException;
	  	  	  
	  /**
	 * Distribution for data.
	 *
	 * @param instance
	 *            the instance
	 * @return the double[]
	 * @throws ClassificationException
	 *             the classification exception
	 */
  	public double[] distributionForData(DataVector instance) throws ClassificationException; 
	  
	 

}
