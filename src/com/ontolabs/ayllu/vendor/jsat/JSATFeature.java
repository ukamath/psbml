/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.jsat;


import com.ontolabs.ayllu.data.Feature;

public class JSATFeature implements Feature{
	
	String feature;
	
	/** The index. */
	int index;
	
	protected JSATFeature(String feature, int index){
		this.feature = feature;
		this.index = index;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public String getFeatureName() {
		return this.feature;
	}

	@Override
	public boolean isNominal() {
		return false;
	}

	@Override
	public boolean isNumeric() {
		return true;
	}

	@Override
	public String[] getCategories() {
		throw new UnsupportedOperationException("Not supported categorical for now");
	}

}
