/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.distance;

import com.ontolabs.ayllu.data.DataVector;

/**
 * The Interface DistanceMetric.
 */
public interface DistanceMetric {
	
	/** The euclidean distance. */
	public static String EUCLIDEAN="euclidean";
	
	/**
	 * Distance.
	 *
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return the double
	 */
	double distance(DataVector from, DataVector to);

}
