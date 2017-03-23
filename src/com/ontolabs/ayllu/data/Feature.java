/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.data;

/**
 * The Interface Feature is an abstraction for data typing in machine learning. Each
 * vendor has his own representation and this abstraction hides the implementation.
 * Feature has index, name and categorical/numeric nature of it. In case of categorical
 * all values possible are available from the interface.
 */
public interface Feature {
	
	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public int getIndex();
	
	/**
	 * Gets the feature name.
	 *
	 * @return the feature name
	 */
	public String getFeatureName();
	
	/**
	 * Checks if is nominal.
	 *
	 * @return true, if is nominal
	 */
	public boolean isNominal();
	
	/**
	 * Checks if is numeric.
	 *
	 * @return true, if is numeric
	 */
	public boolean isNumeric();
	
	/**
	 * Gets the categories.
	 *
	 * @return the categories
	 */
	public String[] getCategories();	
	
}
