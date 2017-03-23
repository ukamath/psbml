/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu;

import com.ontolabs.ayllu.data.DataSet;

/**
 * The Interface Learner is the basic interface for batch learning. 
 * It trains on the given dataset whether supervised/unsupervised manner.
 */
public interface Learner extends Cloneable, Parameters{
	
	  /**
	 * Train.
	 *
	 * @param data
	 *            the data
	 * @throws LearnerException
	 *             the learner exception
	 */
  	public  void train(DataSet data) throws LearnerException;
  	
  	/**
  	 * 
  	 * @return deep copy of Learner
  	 * @throws ParameterException
  	 */
  	public Learner copy() throws ParameterException;


}
