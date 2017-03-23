/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.jsat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import jsat.classifiers.DataPoint;
import jsat.clustering.MeanShift;

import com.ontolabs.ayllu.Learner;
import com.ontolabs.ayllu.LearnerException;
import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.clustering.Clusterer;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.data.Feature;

public abstract class JSATClusterer implements Clusterer{
	
	jsat.clustering.Clusterer clusterer;
	public static String SET ="set";
	
	protected JSATClusterer(jsat.clustering.Clusterer clusterer){
		this.clusterer = clusterer;
	}

	@Override
	public void train(DataSet data) throws LearnerException {
		JSATDataSet dataset = (JSATDataSet) data;
		ExecutorService service = null;
		this.clusterer.cluster(dataset.getDataSet(), service);		
	}

	@Override
	public void setOptions(Map options) throws ParameterException {
		//something specific, can be changed later to be subclasses
		if(this.clusterer instanceof MeanShift){
			Set keys = options.keySet();
			Iterator keyIterator = keys.iterator();
			while (keyIterator.hasNext()){
				String nextKey = (String)keyIterator.next();
				//concatenate "set" to the string
				String methodName = SET+ nextKey;
				//get the value as double
				double val = new Double((String) options.get(nextKey)).doubleValue();
				//invoke using reflection
				Method m;
				try {
					m = this.clusterer.getClass().getMethod(methodName, double.class);
				} catch (NoSuchMethodException e) {
					throw new ParameterException(e);
				} catch (SecurityException e) {
					throw new ParameterException(e);
				}
				try {
					m.invoke(this.clusterer, val);
				} catch (IllegalAccessException e) {
					throw new ParameterException(e);
				} catch (IllegalArgumentException e) {
					throw new ParameterException(e);
				} catch (InvocationTargetException e) {
					throw new ParameterException(e);
				}
				
			}
		}
	}

	
	
	/**
	 * Transform.
	 *
	 * @param clusterTopoints
	 *            the cluster topoints
	 * @param features
	 *            the features
	 * @return the list
	 */
	protected List<List<DataVector>> transform(List<List<DataPoint>> clusterTopoints, List<Feature> features){
		ArrayList<List<DataVector>> clusterList = new ArrayList<List<DataVector>>(clusterTopoints.size());
		for(int i =0 ; i < clusterTopoints.size(); i++){
			List<DataPoint> dataPoints = clusterTopoints.get(i);
			ArrayList<DataVector> vectors = new ArrayList<DataVector>(dataPoints.size());
			for (int j=0; j < dataPoints.size(); j++){
				DataVector vector = new JSATDataVector(dataPoints.get(j), features);
				vectors.add(vector);
			}			
			clusterList.add(vectors);
		}
		
		return clusterList;
	}

	@Override
	public List<List<DataVector>> cluster(DataSet data) {
		List<List<DataVector>> clusterToDataVector = null;
		JSATDataSet dataset = (JSATDataSet) data;
		ExecutorService service = null;
		List<List<DataPoint>> dataPoints = this.clusterer.cluster(dataset.getDataSet(), service);	
		clusterToDataVector = transform(dataPoints, dataset.getJSATFeatures());
		return clusterToDataVector;
	}

	@Override
	public Learner copy() throws ParameterException {
		// TODO Auto-generated method stub
		return null;
	}
	
	 

}
