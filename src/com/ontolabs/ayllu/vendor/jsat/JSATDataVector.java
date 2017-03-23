/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.jsat;

import java.util.List;

import jsat.classifiers.DataPoint;

import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.data.Feature;

public class JSATDataVector extends JSATBaseVector implements DataVector{
	DataPoint dataPoint;

	protected JSATDataVector(DataPoint dataPoint, List<Feature> jsatFeatures) {
		this.dataPoint = dataPoint;
		if(features == null){
			//do it once only for now as it should be static
			features = jsatFeatures;
		}
	}
	
	/**
	 * Gets the data point.
	 *
	 * @return the data point
	 */
	protected DataPoint getDataPoint(){
		return this.dataPoint;
	}

	@Override
	public List<Feature> getFeatures() {
		return this.features;
	}

	@Override
	public void setWeight(double weight) {
		this.dataPoint.setWeight(weight);		
	}

	@Override
	public double getWeight() {
		return this.dataPoint.getWeight();
	}

	@Override
	public double getValue(Feature f) {
		return this.dataPoint.getNumericalValues().get(f.getIndex());
	}

	@Override
	public String getNominalValue(Feature f) {
		throw new UnsupportedOperationException("Not Supported in JSAT for now");
	}

	@Override
	public void setValue(Feature f, double v) {
		this.dataPoint.getNumericalValues().set(f.getIndex(), v);
	}

	@Override
	public void setNominalValue(Feature f, String v) {
		throw new UnsupportedOperationException("Not Supported in JSAT for now");		
	}

	@Override
	public double getValue(int index) {
		return this.dataPoint.getNumericalValues().get(index);
	}

	@Override
	public String getNominalValue(int f) {
		throw new UnsupportedOperationException("Not Supported in JSAT for now");
	}

	@Override
	public DataVector copy() {
		return  new JSATDataVector(this.dataPoint.clone(), this.features);
	}

	@Override
	public double[] getNumericValues() {
		return this.dataPoint.getNumericalValues().arrayCopy();
	}

}
