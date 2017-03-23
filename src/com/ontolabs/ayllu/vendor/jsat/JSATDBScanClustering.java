/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.jsat;



import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;

import jsat.classifiers.DataPoint;
import jsat.clustering.Clusterer;

public class JSATDBScanClustering extends JSATClusterer{
	double epsilon;
	int minPoints;
	int threads;

	protected JSATDBScanClustering(Clusterer clusterer) {
		super(clusterer);
	}
	
	@Override
	public void setOptions(Map options) throws ParameterException {
		String epsilonProperty = (String)options.get(EPSILON);
		this.epsilon = new Double(epsilonProperty).doubleValue();
		String minPointsProperty = (String)options.get(MIN_POINTS);
		this.minPoints = new Integer(minPointsProperty).intValue();
		String threadsProperty = (String)options.get(THREADS);
		this.threads = new Integer(threadsProperty).intValue();
	}
	
	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.vendor.jsat.JSATClusterer#cluster(com.ontolabs.ayllu.data.DataSet)
	 */
	@Override
	public List<List<DataVector>> cluster(DataSet data) {
		List<List<DataVector>> clusterToDataVector = null;
		JSATDataSet dataset = (JSATDataSet) data;
		ExecutorService service = Executors
				.newFixedThreadPool(this.threads);
		List<List<DataPoint>> dataPoints =((jsat.clustering.DBSCAN)this.clusterer).cluster(dataset.getDataSet(), this.epsilon, this.minPoints,
				service);	
		service.shutdown();
		clusterToDataVector = transform(dataPoints, dataset.getJSATFeatures());
		return clusterToDataVector;
	}

	@Override
	public void setOptions(String options) throws ParameterException {
		// TODO Auto-generated method stub
		
	}

}
