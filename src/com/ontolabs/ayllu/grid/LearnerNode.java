/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.grid;

import com.ontolabs.ayllu.Learner;
import com.ontolabs.ayllu.LearnerException;
import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.data.DataSet;

/**
 * The Interface LearnerNode is node in the 2D Grid. The responsibility of this interface is to
 * manage its learner and have data in two states. The set/get Data is for training time 
 * and setSelected/getSelected is for transition or selection/sampling phase.
 */
public interface LearnerNode {
	
	/**
	 * Sets the learner.
	 *
	 * @param l
	 *            the new learner
	 * @throws ParameterException
	 *             the parameter exception
	 */
	public void setLearner(Learner l) throws ParameterException;
	
	/**
	 * Gets the learner.
	 *
	 * @return the learner
	 */
	public Learner getLearner();
	
	/**
	 * Sets the data.
	 *
	 * @param dataset
	 *            the new data
	 * @throws ParameterException
	 *             the parameter exception
	 */
	public void setData(DataSet dataset) throws ParameterException;
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public DataSet getData();	
	
	/**
	 * Train.
	 *
	 * @throws LearnerException
	 *             the learner exception
	 */
	public void train() throws LearnerException;
	
	/**
	 * Sets the selected data.
	 *
	 * @param dataset
	 *            the new selected data
	 */
	public void setSelectedData(DataSet dataset);
	
	/**
	 * Gets the selected data.
	 *
	 * @return the selected data
	 */
	public DataSet getSelectedData();
	
	
	/**
	 * resets the data to change from training to new selected
	 */
	public void resetData();
}
