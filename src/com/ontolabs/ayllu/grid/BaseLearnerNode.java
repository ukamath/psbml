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
 * The Class BaseLearnerNode is a base implementation of @see LearnerNode.
 */
public class BaseLearnerNode implements LearnerNode{
	
	/** The learner. */
	protected Learner learner;
	
	/** The data set. */
	protected DataSet dataSet;
	
	/** The selected data set. */
	protected DataSet selectedDataSet;
	
	public void setLearner(Learner l) throws ParameterException{
		this.learner = l;
	}
	
	public Learner getLearner(){
		return this.learner;
	}
	
	public void setData(DataSet dataset) throws ParameterException{
		this.dataSet = dataset;
	}
	
	public DataSet getData(){
		return this.dataSet;
	}
	
	public void train() throws LearnerException{
		this.learner.train(dataSet);
	}
	
	public void setSelectedData(DataSet ds){
		this.selectedDataSet =ds;
	}
	
	public DataSet getSelectedData(){
		return this.selectedDataSet;
	}

	@Override
	public void resetData() {
		this.dataSet = this.selectedDataSet;
		this.selectedDataSet = null;
		
	}
}
