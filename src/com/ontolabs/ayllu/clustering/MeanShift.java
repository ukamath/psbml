/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.clustering;

import java.util.List;

import com.ontolabs.ayllu.data.DataVector;

/**
 * The Interface MeanShift algorithm
 */
public interface MeanShift extends Clusterer{
	
	/**
	 * Gets the modes.
	 *
	 * @return the modes
	 */
	List<DataVector> getModes();
}
