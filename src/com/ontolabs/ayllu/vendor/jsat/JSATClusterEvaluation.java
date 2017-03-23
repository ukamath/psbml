/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.jsat;

import java.util.List;

import jsat.SimpleDataSet;
import jsat.classifiers.ClassificationDataSet;
import jsat.clustering.evaluation.RandIndex;

import com.ontolabs.ayllu.clustering.ClusterEvaluation;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;

public class JSATClusterEvaluation implements ClusterEvaluation{
	protected String method;
	
	public JSATClusterEvaluation(String method){
		this.method = method;
	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.clustering.ClusterEvaluation#evaluate(int[], com.ontolabs.ayllu.data.DataSet)
	 */
	@Override
	public double evaluate(int[] designations, DataSet dataSet) {
		double score =0.0;
		if(this.method.equals(RAND_INDEX_EVALUATION)){
			//get jsat
			JSATDataSet jsatDataSet = (JSATDataSet)dataSet;
			//if its simple dataset
			if(jsatDataSet.getDataSet() instanceof SimpleDataSet){
				//create classification dataset
				ClassificationDataSet classificationDataSet = new ClassificationDataSet(jsatDataSet.jsatDataSet.getDataPoints(),0);
				//now call RI
				RandIndex ri = new RandIndex();
				score =ri.evaluate(designations, classificationDataSet);			
			}
		}
		return score;
	}

	
	

}
