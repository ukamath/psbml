/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.weka;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.algorithm.bigdata.ParallelLearner;
import com.ontolabs.ayllu.classifier.Classifier;
import com.ontolabs.ayllu.classifier.ClassifierEvaluation;
import com.ontolabs.ayllu.clustering.ClusterEvaluation;
import com.ontolabs.ayllu.clustering.Clusterer;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.distance.DistanceMetric;
import com.ontolabs.ayllu.storage.DataLoader;
import com.ontolabs.ayllu.vendor.VendorFactory;

/**
 * A factory for creating Weka objects.
 */
public class WekaFactory implements VendorFactory, Serializable{

	@Override
	public  Classifier getClassifier(String classifierClass,
			String options) throws ParameterException {
		AbstractClassifier classifier;
		try {
			classifier = (AbstractClassifier) Class.forName(classifierClass)
					.newInstance();
		} catch (InstantiationException e) {
			throw new ParameterException(e);
		} catch (IllegalAccessException e) {
			throw new ParameterException(e);
		} catch (ClassNotFoundException e) {
			throw new ParameterException(e);
		}
		// wrap it as WekaClassifier
		WekaClassifier wekaClassifier = new WekaClassifier(classifier);
		wekaClassifier.setOptions(options);
		return wekaClassifier;
	}

	@Override
	public DataSet getDataSet(Map<String,String> params) throws ParameterException {
		DataSource source=null;
		Instances instances=null;
		try {
			source = new DataSource(params.get(ParallelLearner.DATASET_TRAINING_FILE_PATH));
		} catch (Exception e) {
			throw new ParameterException(e);
		}
		try {
			instances = source.getDataSet();
		} catch (Exception e) {
			throw new ParameterException(e);
		}
		//get the goal
		String goal  = params.get(ParallelLearner.LEARNER_GOAL_NAME);
		//create a weka dataset
		return new WekaDataSet(instances,goal);
	}
	
	/**
	 * Get the loader/storer of data
	 */
	public DataLoader getDataStorage(){
		return new WekaDataStorage();
	}
	
	

	@Override
	public ClassifierEvaluation getClassifierEvaluation() {
		return new WekaClassifierEvaluation();
	}

	@Override
	public DataSet createDataSet(Collection<DataVector> dataVectors, String label) {
		ArrayList newDataSet =  new ArrayList<DataVector>(dataVectors.size());
		CollectionUtils.addAll(newDataSet,dataVectors);
		return new WekaDataSet(newDataSet,label);
	}

	@Override
	public Clusterer getClusterer(String clustererClass, Map<String,Object> options)
			throws ParameterException {
		throw new UnsupportedOperationException("Clsutering not supported on weka yet!");
	}

	@Override
	public ClusterEvaluation getClusterEvaluation(String method) {
		throw new UnsupportedOperationException("Clsutering not supported on weka yet!");
	}

	@Override
	public DistanceMetric getDistanceMetric(String metricType) {
		throw new UnsupportedOperationException("Clsutering not supported on weka yet!");
	}
	
}
