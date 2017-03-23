/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.grid.clustering.density;

import java.util.List;

import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.grid.clustering.ClustererNode;


/**
 * The Interface DensityBasedClusterNode.
 */
public interface DensityBasedClusterNode extends ClustererNode{
	
	/**
	 * Gets the modes.
	 *
	 * @return the modes
	 */
	public List<DataVector> getModes();

}
