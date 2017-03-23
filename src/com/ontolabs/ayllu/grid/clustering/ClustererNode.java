/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.grid.clustering;

import java.util.List;

import com.ontolabs.ayllu.grid.LearnerNode;

/**
 * The Interface ClustererNode.
 */
public interface ClustererNode extends LearnerNode{
	
		/**
		 * Adds the weights from neighbors.
		 *
		 * @param weights
		 *            the weights
		 */
		public void addWeightsFromNeighbors(double[] weights) ;

		/**
		 * Gets the all weights.
		 *
		 * @return the all weights
		 */
		public List<double[]> getAllWeights();

		// this doesnt need synchronization
		/**
		 * Sets the weights from node.
		 *
		 * @param weights
		 *            the new weights from node
		 */
		public void setWeightsFromNode(double[] weights) ;

}
