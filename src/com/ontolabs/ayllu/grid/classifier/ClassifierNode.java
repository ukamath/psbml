/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.grid.classifier;

import java.util.List;

import com.ontolabs.ayllu.classifier.ClassificationException;
import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.grid.LearnerNode;

/**
 * The Interface ClassifierNode is extension of @see LearnerNode for Classification
 * purposes.
 */
public interface ClassifierNode extends LearnerNode{
	
	/**
	 * Classify.
	 *
	 * @param dataVector
	 *            the d
	 * @return the double corresponding to confidence in prediction
	 * @throws ClassificationException
	 *             the classification exception
	 */
	public double classify(DataVector d) throws ClassificationException;

	/**
	 * Classify.
	 *
	 * @param instances
	 *            the instances
	 * @return the list of the double corresponding to confidence in prediction
	 * @throws ClassificationException
	 *             the classification exception
	 */
	public List<Double> classify(List<DataVector> instances) throws ClassificationException;
	
	/**
	 * Distribution for data.
	 *
	 * @param instances
	 *            the instances
	 * @return the list the double corresponding to confidence in prediction
	 * @throws ClassificationException
	 *             the classification exception
	 */
	public List<double[]> distributionForData(List<DataVector> instances) throws ClassificationException;
}
