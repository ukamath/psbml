/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.data;

import java.util.List;

/**
 * The Interface DataVector is the instance corresponding to single record from data.
 * It has @see Feature as @see List corresponding to attributes and values for each.
 * DataVector can have either Nominal/Categorical values as @see String or numeric
 * as double.
 */
public interface DataVector {	
	
	/**
	 * Gets the features.
	 *
	 * @return the features
	 */
	public List<Feature> getFeatures();
	
	/**
	 * Sets the weight.
	 *
	 * @param weight
	 *            the new weight
	 */
	public void setWeight(double weight);
	
	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public double getWeight();
	
	/**
	 * Gets the value.
	 *
	 * @param f
	 *            the f
	 * @return the value
	 */
	public double getValue(Feature f);
	
	/**
	 * Gets the nominal value.
	 *
	 * @param f
	 *            the f
	 * @return the nominal value
	 */
	public String getNominalValue(Feature f);
	
	/**
	 * Sets the value.
	 *
	 * @param f
	 *            the f
	 * @param v
	 *            the v
	 */
	public void setValue(Feature f, double v);
	
	/**
	 * Sets the nominal value.
	 *
	 * @param f
	 *            the f
	 * @param v
	 *            the v
	 */
	public void setNominalValue(Feature f, String v);
	
	/**
	 * Gets the value.
	 *
	 * @param index
	 *            the index
	 * @return the value
	 */
	public double getValue(int index);
	
	/**
	 * Gets the nominal value.
	 *
	 * @param f
	 *            the f
	 * @return the nominal value
	 */
	public String getNominalValue(int f);
	
	/**
	 * Copy.
	 *
	 * @return the data vector
	 */
	public DataVector copy();
	
	
	public double[] getNumericValues();


}
