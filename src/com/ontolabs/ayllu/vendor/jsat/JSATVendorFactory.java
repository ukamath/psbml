/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.jsat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import jsat.ARFFLoader;
import jsat.SimpleDataSet;
import jsat.clustering.DBSCAN;
import jsat.clustering.MeanShift;

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

public class JSATVendorFactory implements VendorFactory {
	
	/**
	 * Get the loader/storer of data
	 */
	public DataLoader getDataStorage(){
		return null;
	}

	@Override
	public Classifier getClassifier(String classifierClass, String options)
			throws ParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataSet getDataSet(Map<String, String> params)
			throws ParameterException {
		jsat.DataSet ds = ARFFLoader.loadArffFile(new File(params
				.get(ParallelLearner.DATASET_TRAINING_FILE_PATH)));
		// get the goal
		String goal = params.get(ParallelLearner.LEARNER_GOAL_NAME);
		return new JSATDataSet(ds, goal);
	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.vendor.VendorFactory#createDataSet(java.util.Collection, java.lang.String)
	 */
	@Override
	public DataSet createDataSet(Collection<DataVector> dataVectors,
			String label) {
		ArrayList newDataSet = new ArrayList<DataVector>(dataVectors.size());
		CollectionUtils.addAll(newDataSet, dataVectors);
		return new JSATDataSet(JSATUtility.getDataPoints(newDataSet), label);
	}

	@Override
	public ClassifierEvaluation getClassifierEvaluation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clusterer getClusterer(String clustererClass,
			Map<String, Object> options) throws ParameterException {
		Clusterer clusterer = null;
		if (clustererClass.contains("MeanShift")) {
			jsat.clustering.Clusterer meanShift = new MeanShift();
			clusterer = new JSATMeanShiftClustering(meanShift);
		} else if (clustererClass.contains("DBSCAN")) {
			jsat.clustering.Clusterer dbscan = new DBSCAN();
			clusterer = new JSATDBScanClustering(dbscan);
		}
		clusterer.setOptions(options);
		return clusterer;
	}

	@Override
	public ClusterEvaluation getClusterEvaluation(String method) {
		return new JSATClusterEvaluation(method);
	}

	@Override
	public DistanceMetric getDistanceMetric(String metricType) {
		return new JSATDistanceMetric();
	}

}
