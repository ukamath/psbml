/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.algorithm.bigdata;

import com.ontolabs.ayllu.LearnerException;
import com.ontolabs.ayllu.algorithm.bigdata.classification.local.ParallelBoostingClassifier;
import com.ontolabs.ayllu.algorithm.bigdata.clustering.density.local.ParallelBoostingMeanShiftClustering;
import com.ontolabs.ayllu.algorithm.bigdata.regression.local.ParallelBoostingRegressor;

/**
 * A factory for creating ParallelLearner objects.
 */
public class LearningFactory {
	
	/** The local. */
	public static String LOCAL ="local";
	
	/** The distributed. */
	public static String DISTRIBUTED="distribtued";
	
	/** The learner type classifier. */
	public static String LEARNER_TYPE_CLASSIFIER="classifier";
	
	/** The learner type clustering density */
	public static String LEARNER_TYPE_CLUSTERING_DENSITY="clustering.density";
	
	/** The learner type regression */
	public static String LEARNER_TYPE_REGRESSION="regression";
	
	/**
	 * Creates a new Learning object.
	 *
	 * @param mode
	 *            the mode
	 * @param learner
	 *            the learner
	 * @param masterAndSlave
	 *            the master and slave
	 * @return the parallel learner
	 * @throws LearnerException
	 *             the learner exception
	 */
	public static ParallelLearner createLearner(String mode, String learner, boolean masterAndSlave) throws LearnerException{
		ParallelLearner parallelLearner= null;
		if(mode.equals(LOCAL) && learner.equals(LEARNER_TYPE_CLASSIFIER) && masterAndSlave)
			parallelLearner= new ParallelBoostingClassifier();
		else if(mode.equals(LOCAL) && learner.equals(LEARNER_TYPE_CLUSTERING_DENSITY) && masterAndSlave)
			parallelLearner= new ParallelBoostingMeanShiftClustering();
		else if(mode.equals(LOCAL) && learner.equals(LEARNER_TYPE_REGRESSION) && masterAndSlave)
			parallelLearner = new ParallelBoostingRegressor();
		
		return parallelLearner;
	}

}
