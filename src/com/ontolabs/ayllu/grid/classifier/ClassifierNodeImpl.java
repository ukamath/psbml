/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.grid.classifier;

import java.util.ArrayList;
import java.util.List;

import com.ontolabs.ayllu.classifier.ClassificationException;
import com.ontolabs.ayllu.classifier.Classifier;
import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.grid.BaseLearnerNode;

/**
 * The Class ClassifierNodeImpl is concrete implementation for Classification.
 * It does classification and uses the confidence measures for sampling needed
 * by the grid's worker.
 */
public class ClassifierNodeImpl extends BaseLearnerNode implements ClassifierNode{
	
	

	@Override
	public List<Double> classify(List<DataVector> instances)
			throws ClassificationException {
		Classifier classifier = (Classifier)learner;
		List<Double> results = new ArrayList<Double>();
		for(DataVector instance: instances){
			results.add(classifier.classify(instance));
		}
		return results;
	}

	@Override
	public List<double[]>  distributionForData(List<DataVector> instances)
			throws ClassificationException {
		Classifier classifier = (Classifier)learner;
		List<double[]> results = new ArrayList<double[]>();		
			for(DataVector instance: instances){
				results.add(classifier.distributionForData(instance));
			
		}
		return results;
	}

	@Override
	public double classify(DataVector d) throws ClassificationException {
		Classifier classifier = (Classifier)learner;
		double maxp = classifier.classify(d);
		return maxp;
	}

	

}
