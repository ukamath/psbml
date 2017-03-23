/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.grid;

import java.util.List;

import com.ontolabs.ayllu.LearnerException;
import com.ontolabs.ayllu.ParameterException;

/**
 * The Interface Grid is the basic topological 2D spatial structure. It has basic
 * concepts of Neighborhood and Nodes embedded in each. Grid initializes each of 
 * the @see LearnerNode with learner and has responsibility of managing their initialization
 * and neighborhood.
 */
public interface Grid{
	
	// Neighborhood definition: arrays of relative positions of neighbors
    /** The Constant NEIGHBORS_L5. */
	public static final int [][]NEIGHBORS_L5 = new int[][]
            {
        new int[]{-1,  0},
        new int[]{ 0, -1},
        new int[]{ 0,  1},
        new int[]{ 1,  0}
            };
    
    /** The Constant NEIGHBORS_L9. */
    public static final int [][]NEIGHBORS_L9 = new int[][]
            {
        new int[]{-2,  0},
        new int[]{-1,  0},
        new int[]{ 0, -2},
        new int[]{ 0, -1},
        new int[]{ 0,  1},
        new int[]{ 0,  2},
        new int[]{ 1,  0},
        new int[]{ 2,  0}
            };
    
    /** The Constant NEIGHBORS_C9. */
    public static final int [][]NEIGHBORS_C9 = new int[][]
            {
        new int[]{-1, -1},
        new int[]{ 0, -1},
        new int[]{ 1, -1},
        new int[]{-1,  0},
        new int[]{ 1,  0},
        new int[]{-1,  1},
        new int[]{ 0,  1},
        new int[]{ 1,  1}
            };
    
    /** The Constant NEIGHBORS_C13. */
    public static final int [][]NEIGHBORS_C13 = new int[][]
            {
        new int[]{-2,  0},
        new int[]{-1,  0},
        new int[]{ 0, -2},
        new int[]{ 0, -1},
        new int[]{ 0,  1},
        new int[]{ 0,  2},
        new int[]{ 1,  0},
        new int[]{ 2,  0},
        new int[]{-1, -1},
        new int[]{-1,  1},
        new int[]{ 1,  1},
        new int[]{ 1, -1}
            };
    
    /**
	 * Initialize learner nodes.
	 *
	 * @throws ParameterException
	 *             the parameter exception
	 */
    public abstract void initializeLearnerNodes() throws ParameterException ;
    
    /**
	 * Sets the neighborhood.
	 *
	 * @param neighborhood
	 *            the new neighborhood
	 * @throws LearnerException
	 *             the learner exception
	 */
    public void setNeighborhood(int[][] neighborhood) throws LearnerException;
    	
	/**
	 * Gets the all nodes.
	 *
	 * @return the all nodes
	 */
	public List<LearnerNode> getAllNodes();
	
	/**
	 * Gets the neighbors.
	 *
	 * @param node
	 *            the node
	 * @return the neighbors
	 */
	public List<? extends LearnerNode> getNeighbors(LearnerNode node);
	
	/**
	 * Gets the node at.
	 *
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * @return the node at
	 */
	public LearnerNode getNodeAt(int i,int j);
	 

}
