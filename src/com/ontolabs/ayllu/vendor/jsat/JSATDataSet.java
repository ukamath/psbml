/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.jsat;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;

import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.data.Feature;

public class JSATDataSet extends AbstractList<DataVector>  implements DataSet{
	jsat.DataSet jsatDataSet;
	String goalName;
	List<DataVector> dataVectors;
	protected static List<Feature> features;
	Random r ;
	
	public JSATDataSet(jsat.DataSet ds, String goalName){
		this.jsatDataSet =ds;
		this.r = new Random();
		this.goalName = goalName;
	}
	
	protected List<Feature> getJSATFeatures(){
		return features;
	}
	
	protected jsat.DataSet getDataSet(){
		if(this.jsatDataSet == null && this.dataVectors != null){
			this.jsatDataSet =JSATUtility.getDataPoints(dataVectors);
		}
		return this.jsatDataSet;
	}
	
	protected  List<DataVector> getDataVectors(){
		if(this.jsatDataSet != null){
			if(features == null){
				int numberOfNumerics = jsatDataSet.getNumNumericalVars();
				features = new ArrayList<Feature>(numberOfNumerics);
				for(int i =0; i<numberOfNumerics;i++){
					Feature f = new JSATFeature(jsatDataSet.getNumericName(i),i);
					features.add(f);
				}
			}
			this.dataVectors = JSATUtility.getDataVectors(jsatDataSet, features);
		}
		return this.dataVectors;
	}

	@Override
	public DataSet[] createTrainingTestingPartitions(double trainingPortion) {
		throw new UnsupportedOperationException("Not Supported in JSAT for now");
	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.data.DataSet#createPartition(int, int)
	 */
	@Override
	public DataSet createPartition(int partitionSize, int partitionNumber) {
		return new JSATDataSet(this.jsatDataSet.cvSet(partitionSize).get(partitionNumber), this.goalName);
	}

	@Override
	public String getGoal() {
		return this.goalName;
	}

	@Override
	public DataVector get(int index) {
		return this.getDataVectors().get(index);
	}

	@Override
	public int size() {
		int size =0;
		if(this.jsatDataSet != null)
			size = this.jsatDataSet.getSampleSize();
		else
			size = this.dataVectors.size();
		return size;
	}

	@Override
	public int getGoalIndex() {
		//TODO not done classification stuff yet on JSAT
		return 0;
	}

}
