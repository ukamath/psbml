/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.weka;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import weka.core.Attribute;

import com.ontolabs.ayllu.data.Feature;

// TODO: Auto-generated Javadoc
/**
 * The Class WekaFeature.
 */
public class WekaFeature implements Feature, Serializable {

	/** The attribute. */
	protected Attribute attribute;

	/**
	 * Instantiates a new weka feature.
	 *
	 * @param a
	 *            the a
	 */
	protected WekaFeature(Attribute a) {
		this.attribute = a;
	}

	public int getIndex() {
		return attribute.index();
	}

	public String getFeatureName() {
		return attribute.name();
	}

	public boolean isNominal() {
		return attribute.isNominal();
	}

	public boolean isNumeric() {
		return attribute.isNumeric();
	}

	public String[] getCategories() {
		List<String> values = new ArrayList<String>();
		Enumeration enumValues = attribute.enumerateValues();
		while (enumValues.hasMoreElements())
			values.add((String) enumValues.nextElement());
		return values.toArray(new String[0]);
	}

	

}
