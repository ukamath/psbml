/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.data;

import java.util.List;


/**
 * The Interface DataSet is the abstraction of Collection of @see DataVector.
 * This is used in all the algorithms as sample for training, pruning sets, 
 * testing etc. It hides the implementation of vendor specific collection.
 * 
 */
public interface DataSet extends List<DataVector>{	
	
	
	/**
	 * Creates the training testing partitions.
	 *
	 * @param trainingPortion
	 *            the training portion
	 * @return the data set[]
	 */
	public DataSet[] createTrainingTestingPartitions(double trainingPortion);
	
	/**
	 * Creates the partition.
	 *
	 * @param partitionSize
	 *            the partition size
	 * @param partitionNumber
	 *            the partition number
	 * @return the data set
	 */
	public DataSet createPartition(int partitionSize, int partitionNumber);
	
	/**
	 * Gets the goal.
	 *
	 * @return the goal
	 */
	public String getGoal();
	
	/**
	 * Gets the goal. Indexs
	 *
	 * @return the goal
	 */
	public int getGoalIndex();
	
	
}
