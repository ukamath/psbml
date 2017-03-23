/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.weka;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import weka.core.Instance;

import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.data.Feature;

// TODO: Auto-generated Javadoc
/**
 * The Class WekaDataVector.
 */
public class WekaDataVector implements DataVector, Serializable {
	List<Feature> features;
	Instance wekaInstance;
	
	/**
	 * Instantiates a new weka data vector.
	 *
	 * @param i
	 *            the i
	 */
	protected WekaDataVector(Instance i){
		this.wekaInstance =i;
	}

	private void initFeatures() {
		features = new ArrayList<Feature>();
		for(int i=0; i < wekaInstance.numAttributes(); i++){
			features.add(new WekaFeature(wekaInstance.attribute(i)));
		}
	}
	
	@Override
	public List<Feature> getFeatures() {
		if (features == null) {
			initFeatures();
		}
		return features;
	}

	@Override
	public void setWeight(double weight) {
		wekaInstance.setWeight(weight);
	}

	@Override
	public double getWeight() {
		return wekaInstance.weight();
	}
	@Override
	public double getValue(Feature f) {
		return wekaInstance.value(f.getIndex());
	}
	@Override
	public String getNominalValue(Feature f){
		return wekaInstance.stringValue(f.getIndex());
	}
	@Override
	public void setValue(Feature f, double v){
		 wekaInstance.setValue(f.getIndex(), v);
	}
	@Override
	public void setNominalValue(Feature f, String v){
		wekaInstance.setValue(f.getIndex(), v);
	}
	@Override
	public double getValue(int index){
		return wekaInstance.value(index);
	}
	@Override
	public String getNominalValue(int f){
		return wekaInstance.stringValue(f);
	}
	
	public DataVector copy(){
		return new WekaDataVector((Instance) this.wekaInstance.copy());
	}
	
	Instance getInstance(){
		return this.wekaInstance;
	}

	@Override
	public double[] getNumericValues() {
		return this.wekaInstance.toDoubleArray();
	}

}
