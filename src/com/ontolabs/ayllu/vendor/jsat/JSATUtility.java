/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.jsat;

import java.util.ArrayList;
import java.util.List;

import jsat.DataSet;
import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;

import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.data.Feature;

/**
 * The Class JSATUtility.
 */
public class JSATUtility {

	/**
	 * Gets the data vectors.
	 *
	 * @param jsatDataSet
	 *            the jsat data set
	 * @param features
	 *            the features
	 * @return the data vectors
	 */
	protected static List<DataVector> getDataVectors(DataSet jsatDataSet,
			List<Feature> features) {
		List<DataVector> dataVectors = new ArrayList<DataVector>(
				jsatDataSet.getSampleSize());
		for (int j = 0; j < jsatDataSet.getSampleSize(); j++) {
			DataVector vector = new JSATDataVector(jsatDataSet.getDataPoint(j),
					features);
			dataVectors.add(vector);
		}
		return dataVectors;
	}
	
	
	/**
	 * Gets the data points.
	 *
	 * @param dataVectors
	 *            the data vectors
	 * @return the data points
	 */
	protected static DataSet getDataPoints(List<DataVector> dataVectors){
		List<DataPoint> dataPoints = new ArrayList<DataPoint>(dataVectors.size());
		//lets create the reverse
		for (int j=0; j < dataVectors.size(); j++){
			JSATDataVector vector = (JSATDataVector) dataVectors.get(j);
			DataPoint point = vector.getDataPoint();
			dataPoints.add(point);	
		}
		DataSet jsatDataSet = new SimpleDataSet(dataPoints);
		return jsatDataSet;
	}

}
