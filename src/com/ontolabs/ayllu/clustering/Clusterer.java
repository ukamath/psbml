/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.clustering;

import java.util.List;

import com.ontolabs.ayllu.Learner;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;

public interface Clusterer extends Learner {

	// dbscan properties
	public static String DBSCAN = "clustering.dbscan";
	public static String EPSILON = "epsilon";
	public static String MIN_POINTS = "minPoints";

	// mean shift properties
	public static String MAX_ITERATIONS = "MaxIterations";
	public static String SCALE_BANDWIDTH = "ScaleBandwidthFactor";
	public static String MEAN_SHIFT = "clustering.meanShift";
	
	
	//threads 
	/** The threads. */
	public static String THREADS="threads";

	/**
	 * Performs clustering on the given data set. Parameters may be estimated by
	 * the method, or other heuristics performed.
	 * 
	 * @param dataSet
	 *            the data set to perform clustering on
	 * @return A list of clusters found by this method.
	 */
	public List<List<DataVector>> cluster(DataSet dataSet);

}
