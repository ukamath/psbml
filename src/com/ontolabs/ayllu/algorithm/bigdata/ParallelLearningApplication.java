/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.algorithm.bigdata;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class ParallelLearningApplication.
 */
public class ParallelLearningApplication {
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args) throws Exception{
		if(args.length < 4)
			System.err.println("ParallelLearningApplication learnerType mode masterSlave onlyMaster propertiesFileLocation");
		String learnerType = args[0];
		String mode = args[1];
		boolean masterSlave = new Boolean(args[2]).booleanValue();
		//get a parallel learner
		ParallelLearner parallelLearner = LearningFactory.createLearner(mode, learnerType, masterSlave);
		Properties learnerProperties = new Properties();
		learnerProperties.load(new FileInputStream(args[3]));
		//initialize the parallel learner
		parallelLearner.initializeLearning(learnerProperties);
		//learn
		parallelLearner.learn();
		//postprocess
		parallelLearner.postLearning();
	}

}
