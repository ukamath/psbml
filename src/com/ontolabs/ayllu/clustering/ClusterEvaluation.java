/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.clustering;


import com.ontolabs.ayllu.data.DataSet;


/**
 * The Interface ClusterEvaluation.
 */
public interface ClusterEvaluation {
	
	/** The rand index evaluation. */
	public static String RAND_INDEX_EVALUATION="randIndex";
	
	/** 
	 * evaluates clustering based on designation in dataset
	 * @param designation
	 * @param dataSet
	 * @return
	 */
    public double evaluate(int[] designation, DataSet dataSet);

}
