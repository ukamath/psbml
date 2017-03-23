/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.weka;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

import com.ontolabs.ayllu.classifier.Classifier;
import com.ontolabs.ayllu.classifier.ClassifierEvaluation;
import com.ontolabs.ayllu.classifier.EvaluationException;
import com.ontolabs.ayllu.data.DataSet;

/**
 * The Class WekaClassifierEvaluation.
 */
public class WekaClassifierEvaluation implements ClassifierEvaluation {

	@Override
	public double evaluate(String type, Classifier c, DataSet training,
			DataSet testing) throws EvaluationException {
		double result = 0;
		Instances train = ((WekaDataSet) training).getInstances();
		Instances test = ((WekaDataSet) testing).getInstances();
		Evaluation eval = null;
		try {
			eval = new Evaluation(train);
		} catch (Exception e1) {
			throw new EvaluationException(e1);
		}
		AbstractClassifier classifier = ((WekaClassifier) c).wekaClassifier;
		try {
			eval.evaluateModel(classifier, test);
		}
		catch(Exception e){
			throw new EvaluationException(e);
		}
		if (type.equals(AREA_UNDER_CURVE)) {			
				result = eval.weightedAreaUnderROC();			
		}
		else if(type.equals(ACCURACY)){
			result = eval.pctCorrect();
		}
		else if(type.equals(PRECISION)){
			result = eval.weightedPrecision();
		}
		else if(type.equals(MEAN_ERROR)){
			result = eval.errorRate();
		}

		return result;
	}

	

}
