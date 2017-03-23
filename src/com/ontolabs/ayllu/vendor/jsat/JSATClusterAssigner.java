/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.jsat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jsat.classifiers.DataPoint;
import jsat.linear.Vec;
import jsat.linear.VecPaired;
import jsat.linear.distancemetrics.EuclideanDistance;
import jsat.linear.vectorcollection.VPTreeMV;
import jsat.linear.vectorcollection.VPTree.VPSelection;

import com.ontolabs.ayllu.clustering.ClusterModeAssigner;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;

public class JSATClusterAssigner implements ClusterModeAssigner{
	
	// Multi-threading support
	protected ExecutorService executorService;
	
	
	public JSATClusterAssigner(int threads){
		this.executorService = Executors.newFixedThreadPool(threads);
	}
	

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.clustering.ClusterModeAssigner#assignCluster(com.ontolabs.ayllu.data.DataSet, java.util.List)
	 */
	@Override
	public int[] assignCluster(DataSet ds,  List<List<DataVector>> clusterVectors) {
		int[] designations = new int[ds.size()];
		int currentClusterId=0;
		List<VecPaired<Vec, Integer>> allModes = new ArrayList<VecPaired<Vec, Integer>>();
		jsat.DataSet dataSet = ((JSATDataSet)ds).getDataSet();
		// create a vec pair of all modes
		for (List<DataVector> cluster : clusterVectors) {
			for (DataVector point : cluster) {
				JSATDataVector dataPoint = (JSATDataVector)point;
				VecPaired<Vec, Integer> pair = new VecPaired<Vec, Integer>(
						dataPoint.getDataPoint().getNumericalValues(), currentClusterId);
				allModes.add(pair);
			}
			currentClusterId++;
		}
		// now create a VPTrees
		EuclideanDistance distanceMetric = new EuclideanDistance();
		VPTreeMV<VecPaired<Vec, Integer>> collection = new VPTreeMV<VecPaired<Vec, Integer>>(
				allModes, distanceMetric, VPSelection.Random, new Random(), 80,
				40, this.executorService);
		// now search for every data point
		designations = new int[dataSet.getSampleSize()];
		for (int j = 0; j < designations.length; j++) {
			// find where this data point is close to
			// collection.se
			// let me do linear search
			DataPoint currentDataPoint = dataSet.getDataPoint(j);
			// find the shortest match
			List<? extends VecPaired<VecPaired<Vec, Integer>, Double>> searchForClossest = collection
					.search(currentDataPoint.getNumericalValues(), 1);// search
																		// for
																		// the 1
																		// nearest
																		// neighbor
																		// to v
			VecPaired<VecPaired<Vec, Integer>, Double> closestMode = searchForClossest
					.get(0);
			designations[j] = closestMode.getVector().getPair();
		}
		this.executorService.shutdownNow();		
		return designations;
	}

}
