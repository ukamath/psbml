/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.grid;

import java.util.List;

import com.ontolabs.ayllu.Learner;
import com.ontolabs.ayllu.LearnerException;
import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.classifier.Classifier;
import com.ontolabs.ayllu.clustering.MeanShift;
import com.ontolabs.ayllu.grid.classifier.ClassifierNodeImpl;
import com.ontolabs.ayllu.grid.classifier.ClassifierWrapAroundGrid;
import com.ontolabs.ayllu.grid.clustering.density.ClusterWrapAroundGrid;

/**
 * A factory for creating Grid objects given the parameters.
 * 
 */
public class GridFactory {

	/**
	 * Creates a new Grid object.
	 *
	 * @param gridWidth
	 *            the grid width
	 * @param gridHeight
	 *            the grid height
	 * @param neighborhood
	 *            the neighborhood
	 * @param learner
	 *            the learner
	 * @return the grid
	 * @throws ParameterException
	 *             the parameter exception
	 */
	public static Grid createGrid(int gridWidth, int gridHeight,
			String neighborhood, List<Learner> learners) throws ParameterException {
		Grid grid= null;
		int[][] neighborhoodVal = getNeighborhood(neighborhood);
		if(learners.get(0) instanceof Classifier){
			grid = new ClassifierWrapAroundGrid(gridWidth,gridHeight,neighborhoodVal,learners);
		} 
		else if(learners.get(0) instanceof MeanShift){
			grid = new ClusterWrapAroundGrid(gridWidth,gridHeight,neighborhoodVal,learners);
		}
		grid.initializeLearnerNodes();
		return grid;
	}
	
	private static int[][] getNeighborhood(String n){
		int[][] val = Grid.NEIGHBORS_L5;
		if(n.equalsIgnoreCase("c9"))
			val = Grid.NEIGHBORS_C9;
		else if(n.equalsIgnoreCase("c13"))
			val = Grid.NEIGHBORS_C13;
		else if(n.equalsIgnoreCase("l9"))
			val = Grid.NEIGHBORS_L9;
		return val;
			
			
		 
	}
	
	/**
	 * Creates a new LearnerNode.
	 *
	 * @param learner
	 *            the learner
	 * @return the learner node
	 */
	public static LearnerNode createNode(Learner learner){
		LearnerNode node = null;
		if(learner instanceof Classifier){
			node = new ClassifierNodeImpl();
		}		
		return node;
	}

}
