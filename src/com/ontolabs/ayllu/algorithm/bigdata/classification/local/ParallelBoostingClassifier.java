/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.algorithm.bigdata.classification.local;


import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.Instances;

import com.ontolabs.ayllu.LearnerException;
import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.algorithm.bigdata.BaseParallelLearner;
import com.ontolabs.ayllu.classifier.Classifier;
import com.ontolabs.ayllu.classifier.ClassifierEvaluation;
import com.ontolabs.ayllu.classifier.EvaluationException;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.data.DistinctDataVectorComparator;
import com.ontolabs.ayllu.grid.GridFactory;
import com.ontolabs.ayllu.grid.LearnerNode;
import com.ontolabs.ayllu.storage.DataLoader;
import com.ontolabs.ayllu.storage.DataLoaderException;
import com.ontolabs.ayllu.vendor.weka.WekaDataSet;
import com.ontolabs.ayllu.vendor.weka.WekaDataStorage;

/**
 * The Class ParallelBoostingClassifier is a generic ParallelLearner for any 
 * Supervised Learning across any vendors.
 */
public class ParallelBoostingClassifier extends BaseParallelLearner {
	 private final Logger logger = LoggerFactory.getLogger(ParallelBoostingClassifier.class);
	
	//Nodeworkers
	protected List<NodeWorker> workers;
	
	//evaluation method
	protected String evaluationMethod;
	
	protected ClassifierEvaluation evaluation;
	
	private DataSet marginData;
	
	private boolean storeMarginData;
	private String marginDataFile;
	
	public static String MARGIN_DATA_STORAGE="learner.storeMarginData";
	public static String MARGIN_DATA_STORAGE_FILE="learner.margin.data.file";

	@Override
	public void learn() throws LearnerException {
		if (learnSynchronously)
			learnSynchronously();
	}

	/**
	 * Learn synchronously.
	 *
	 * @throws LearnerException the learner exception
	 */
	protected void learnSynchronously() throws LearnerException {
		// partitionData and distribute
		partitionDataAndDistribute();

		// create NodeWorkers
		initializeNodeWorkers();
		
		long currentTime = System.currentTimeMillis();
		
		this.minError = Double.MAX_VALUE;
		// start the first cycle
		trainMultiThreaded();
		

		//while the condition is true
		while (testStopCondition())
			trainMultiThreaded();
		
		//shut the executor service
		this.executorService.shutdownNow();
		
		long endTime = System.currentTimeMillis();
		logger.debug("Time taken to learn " + (endTime-currentTime) + " msecs");
		logger.debug("Min Error = " + this.minError + ", Size At Min Error= " + this.sizeAtMinError + ", Epoch At Min Error=" + this.epochAtMinError);
		
		
	}
	
	/**
	 * Initialize node workers.
	 */
	protected void initializeNodeWorkers() {
		// Create multi-threading logic. One worker for each Node.
		this.executorService = Executors.newFixedThreadPool(numberOfThreads);
		workers = new LinkedList<NodeWorker>();
		for (LearnerNode node : this.parallelGrid.getAllNodes())
			workers.add(new NodeWorker(node, this.parallelGrid
					.getNeighbors(node), this.replacement, factory));

	}
	
	/**
	 * Use the evaluation metric to decide stop condition
	 * @param classifier
	 * @param trainset
	 * @return
	 * @throws EvaluationException
	 */
	protected double getEvaluationMetric(Classifier classifier, DataSet trainset) throws EvaluationException{
		//get the accuracy or area under curve and invert it as error is opposite
		return 1- this.evaluation.evaluate(this.evaluationMethod, classifier, trainset, this.validationSet);
	}

	/**
	 * Test stop condition.s
	 *
	 * @return true, if successful
	 * @throws LearnerException the learner exception
	 */
	public boolean testStopCondition() throws LearnerException {
		

		// Find the total number of distinct Instances in the train sets
		TreeSet<DataVector> pruneset = new TreeSet<DataVector>(
				new DistinctDataVectorComparator());

		
		for (LearnerNode node : this.parallelGrid.getAllNodes()) {
			for (DataVector instance : node.getData()) {
				pruneset.add(instance);
			}
		}

		
		
		//create trainset for validation
		DataSet trainset = factory.createDataSet(pruneset, this.labelName );

		LearnerNode testnode = GridFactory.createNode(this.baseLearner);
		try {
			testnode.setLearner(baseLearner);
		} catch (ParameterException e) {
			throw new LearnerException(e);
		}
		try {
			testnode.setData(trainset);
		} catch (ParameterException e) {
			throw new LearnerException(e);
		}
		testnode.train();
		ClassifierEvaluation eval = factory.getClassifierEvaluation();
		double currentError=0;
		try {
			currentError = this.getEvaluationMetric((Classifier)testnode.getLearner(), trainset);
		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("EPCOH = " + this.epoch + ", VALIDATION ERROR= " + currentError
				+ ", SAMPLE SIZE = " + pruneset.size());
		// Remember best performance
		if (currentError <= this.minError) {
			this.minError = currentError;
			this.sizeAtMinError = pruneset.size();
			this.bestSoFarLearner = testnode.getLearner();
			this.epochAtMinError = this.epoch;
			this.marginData = trainset;
		}

		// Move on when the classification error is lower than twice it was it
		// the start of the experiment
		this.epoch++;
		// if (this.firstClassError*2 < errtotaltest) this.epoch = EPOCHS;

		boolean done = this.epoch == maxEpochs;
		

		return (!done);
	}

	

	/**
	 * Train multi threaded.
	 *
	 * @throws LearnerException the learner exception
	 */
	public void trainMultiThreaded() throws LearnerException {
		// First train each node on its dataset
		for (NodeWorker worker : this.workers)
			worker.setWork(NodeWorker.WORK_TRAIN);
		try {
			this.executorService.invokeAll(this.workers);
		} catch (InterruptedException e) {
			throw new LearnerException(e);
		}

		// Then test instances on neighboring nodes and select the ones for the
		// next epoch
		for (NodeWorker worker : this.workers)
			worker.setWork(NodeWorker.WORK_SELECT);
		try {
			this.executorService.invokeAll(this.workers);
		} catch (InterruptedException e) {
			throw new LearnerException(e);
		}

		// Switch over this epoch's instances for the selected ones
		for (LearnerNode node : this.parallelGrid.getAllNodes())
				node.resetData();
	}
	
	/**
	 * post-process stuff can be done here, like
	 * saving the margin data or cluster found etc
	 */
	public void postLearning() throws LearnerException{
		if(this.storeMarginData){
			try {
				this.factory.getDataStorage().storeDataSet(this.marginDataFile, this.marginData);
			} catch (DataLoaderException e) {
				throw new LearnerException(e);
			}
		}
	}

	
	@Override
	public void initializeLearning(Properties l) throws ParameterException {
		super.initializeLearning(l);
		
		if (this.validationSet == null) {
			// need to create from training
			DataSet[] sets = this.trainingSet
					.createTrainingTestingPartitions(0.9);
			this.trainingSet = sets[0];
			this.validationSet = sets[1];
		}
		
		//evaluation method
		evaluationMethod = l.getProperty(EVALUATION_METHOD);
		this.evaluation = factory.getClassifierEvaluation();
		
		//margin data
		String marginDataStorage = l.getProperty(MARGIN_DATA_STORAGE);
		if(marginDataStorage != null){
			this.storeMarginData = new Boolean(marginDataStorage).booleanValue();
			//check for margin data file
			this.marginDataFile = l.getProperty(MARGIN_DATA_STORAGE_FILE);
		}
		else
			this.storeMarginData = false;
	}
}
