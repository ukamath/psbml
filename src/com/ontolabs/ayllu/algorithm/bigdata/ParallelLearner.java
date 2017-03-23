/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.algorithm.bigdata;

import java.util.Properties;

import com.ontolabs.ayllu.LearnerException;
import com.ontolabs.ayllu.ParameterException;


/**
 * The Interface ParallelLearner is generic for parallelizing Supervised Classification/Regression
 * or Unsupervised Clustering, Outlier etc. The responsibility of the learner is to initialize
 * from the properties given and run learning.
 */
public interface ParallelLearner {
	
	//vendor 
	/** The vendor. */
	public static String VENDOR="vendor";
	
	/** The learner class. */
	public static String LEARNER_CLASS="learner.class";
	
	/** The learner options. */
	public static String LEARNER_OPTIONS="learner.options";
	
	/** The learner goal name. */
	public static String LEARNER_GOAL_NAME="learner.goal";
	
	//dataset
	/** The dataset training. */
	public static String DATASET_TRAINING="dataset.training";
	
	/** The dataset file. */
	public static String DATASET_FILE="file";
	
	/** The dataset training file path. */
	public static String DATASET_TRAINING_FILE_PATH="dataset.training.file.path";
	
	/** The dataset validation file path. */
	public static String DATASET_VALIDATION_FILE_PATH="dataset.validation.file.path";
	
	//grid
	/** The grid width. */
	public static String GRID_WIDTH="grid.width";
	
	/** The grid height. */
	public static String GRID_HEIGHT="grid.height";
	
	/** The grid neighborhood. */
	public static String GRID_NEIGHBORHOOD="grid.neighborhood";
	
	/** The replacement ratio. */
	public static String REPLACEMENT_RATIO="grid.sample.replacement";
	
	/** The C9. */
	public static String C9="c9";
	
	/** The L5. */
	public static String L5="l5";
	
	/** The L9. */
	public static String L9="l9";
	
	/** The L13. */
	public static String L13="l13";	
	
	/** The grid neighborhood choices. */
	public static String[] GRID_NEIGHBORHOOD_CHOICES = new String [] {C9,L5,L9,L13};
	
	//parallel
	/** The parallel threads. */
	public static String PARALLEL_THREADS="parallel.threads";
	
	/** The parallel epochs. */
	public static String PARALLEL_EPOCHS="parallel.epochs";
	
	public static String EVALUATION_METHOD="classifier.evaluation.method";
	
	/**
	 * post-process stuff can be done here, like
	 * saving the margin data or cluster found etc
	 */
	public void postLearning() throws LearnerException;
	
	/**
	 * Learn.
	 *
	 * @throws LearnerException
	 *             the learner exception
	 */
	public void learn() throws LearnerException;
		
	/**
	 * Initialize learning.
	 *
	 * @param learnerProperties
	 *            the learner properties
	 * @throws ParameterException
	 *             the parameter exception
	 */
	public void initializeLearning(Properties learnerProperties) throws ParameterException;	

}
