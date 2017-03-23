/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.weka;

import java.io.Serializable;
import java.util.Map;

import weka.classifiers.AbstractClassifier;
import weka.core.Instance;
import weka.core.Instances;

import com.ontolabs.ayllu.Learner;
import com.ontolabs.ayllu.LearnerException;
import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.classifier.ClassificationException;
import com.ontolabs.ayllu.classifier.Classifier;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;

/**
 * The Class WekaClassifier.
 */
public class WekaClassifier implements Classifier, Serializable {
	weka.classifiers.AbstractClassifier wekaClassifier;
	
	WekaClassifier(weka.classifiers.AbstractClassifier c){
		this.wekaClassifier =c;
	}

	@Override
	public void train(DataSet data) throws LearnerException {
		Instances wekaInstances = ((WekaDataSet) data).getInstances();
		try {
			synchronized(this.wekaClassifier){
				wekaClassifier.buildClassifier(wekaInstances);
			}
		} catch (Exception e) {
			throw new LearnerException(e);
		}
	}

	@Override
	public double classify(DataVector instance) throws ClassificationException {
		Instance wekaInstance = ((WekaDataVector) instance).getInstance();
		double val = 0.0;
		try {
			synchronized(this.wekaClassifier){
				val = this.wekaClassifier.classifyInstance(wekaInstance);
			}
		} catch (Exception e) {
			throw new ClassificationException(e);
		}
		return val;
	}

	@Override
	public double[] distributionForData(DataVector instance)
			throws ClassificationException {
		Instance wekaInstance = ((WekaDataVector) instance).getInstance();
		double[] results = null;
		try {
			results = this.wekaClassifier
					.distributionForInstance(wekaInstance);
		} catch (Exception e) {
			throw new ClassificationException(e);
		}
		return results;
	}

	@Override
	public void setOptions(Map options) throws ParameterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOptions(String options) throws ParameterException {
		try {
			if(options != null)
			 this.wekaClassifier.setOptions(weka.core.Utils.splitOptions(options));
		} catch (Exception e) {
			throw new ParameterException(e);
		}
	}

	@Override
	public Learner copy() throws ParameterException {
		//make a deep cop
		try {
			return new WekaClassifier((AbstractClassifier) this.wekaClassifier.makeCopy(wekaClassifier));
		} catch (Exception e) {
			throw new ParameterException(e);
		}
	}

}
