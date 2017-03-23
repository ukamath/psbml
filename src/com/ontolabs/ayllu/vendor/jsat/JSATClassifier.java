/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.jsat;

import java.util.Map;

import com.ontolabs.ayllu.Learner;
import com.ontolabs.ayllu.LearnerException;
import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.classifier.ClassificationException;
import com.ontolabs.ayllu.classifier.Classifier;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;

/**
 * The Class JSATClassifier.
 */
public class JSATClassifier implements Classifier{
	
	/** The jsat classifier. */
	jsat.classifiers.Classifier jsatClassifier;

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.Learner#train(com.ontolabs.ayllu.data.DataSet)
	 */
	@Override
	public void train(DataSet data) throws LearnerException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.Parameters#setOptions(java.util.Map)
	 */
	@Override
	public void setOptions(Map options) throws ParameterException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.Parameters#setOptions(java.lang.String)
	 */
	@Override
	public void setOptions(String options) throws ParameterException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.classifier.Classifier#classify(com.ontolabs.ayllu.data.DataVector)
	 */
	@Override
	public double classify(DataVector instance) throws ClassificationException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.classifier.Classifier#distributionForData(com.ontolabs.ayllu.data.DataVector)
	 */
	@Override
	public double[] distributionForData(DataVector instance)
			throws ClassificationException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.Learner#copy()
	 */
	@Override
	public Learner copy() throws ParameterException {
		// TODO Auto-generated method stub
		return null;
	}

}
