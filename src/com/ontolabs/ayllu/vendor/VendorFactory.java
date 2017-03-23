/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor;

import java.util.Collection;
import java.util.Map;

import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.classifier.Classifier;
import com.ontolabs.ayllu.classifier.ClassifierEvaluation;
import com.ontolabs.ayllu.clustering.ClusterEvaluation;
import com.ontolabs.ayllu.clustering.Clusterer;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.distance.DistanceMetric;
import com.ontolabs.ayllu.storage.DataLoader;

/**
 * A factory for creating Vendor specific implementation of @see DataSet, Classifier, ClassifierEvaluation etc.
 */
public interface VendorFactory {
	
	

	/**
	 * Gets the classifier.
	 *
	 * @param classifierClass
	 *            the classifier class
	 * @param options
	 *            the options
	 * @return the classifier
	 * @throws ParameterException
	 *             the parameter exception
	 */
	public Classifier getClassifier(String classifierClass, String options)
			throws ParameterException;
	
	/**
	 * Gets the classifier.
	 *
	 * @param clustererClass
	 *            the cluster class
	 * @param options
	 *            the options
	 * @return the classifier
	 * @throws ParameterException
	 *             the parameter exception
	 */
	public Clusterer getClusterer(String clustererClass, Map<String,Object> options)
			throws ParameterException;

	/**
	 * Gets the data set.
	 *
	 * @param params
	 *            the params
	 * @return the data set
	 * @throws ParameterException
	 *             the parameter exception
	 */
	public DataSet getDataSet(Map<String,String> params) throws ParameterException;
	
	/**
	 * Creates a new Vendor object.
	 *
	 * @param dataVectors
	 *            the data vectors
	 * @param label
	 *            the label
	 * @return the data set
	 */
	public DataSet createDataSet(Collection<DataVector> dataVectors, String label);
	
	/**
	 * Gets the classifier evaluation.
	 *
	 * @return the classifier evaluation
	 */
	public ClassifierEvaluation getClassifierEvaluation();
	
	/**
	 * Gets the cluster evaluation.
	 * 
	 * @param  cluster evaluation method name
	 * @return the cluster evaluation
	 * 
	 */
	public ClusterEvaluation getClusterEvaluation(String method);
	
	
	/**
	 * Get a distance metric given the type
	 */
	public DistanceMetric getDistanceMetric(String metricType);
	
	/**
	 * Get the loader/storer of data
	 */
	public DataLoader getDataStorage();
	

}
