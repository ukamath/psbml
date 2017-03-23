/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.classifier;

import com.ontolabs.ayllu.data.DataSet;

// TODO: Auto-generated Javadoc
/**
 * The Interface ClassifierEvaluation.
 */
public interface ClassifierEvaluation {
	
	public static String AREA_UNDER_CURVE="areaUnderCurve";
	
	public static String ACCURACY="accuracy";
	
	public static String PRECISION="precision";
	
	public static String MEAN_ERROR="meanError";
	
	
	/**
	 * Evaluate based on choice, i.e area under curve, accuracy, precision etc etc
	 *
	 * @param c
	 *            the c
	 * @param training
	 *            the training
	 * @param testing
	 *            the testing
	 * @return the double
	 * @throws EvaluationException
	 *             the evaluation exception
	 */
	public double evaluate(String type, Classifier c, DataSet training, DataSet testing) throws EvaluationException;
	
	
}
