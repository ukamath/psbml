/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.algorithm.bigdata.clustering.density.local;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;




import com.ontolabs.ayllu.LearnerException;
import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.algorithm.bigdata.BaseParallelLearner;
import com.ontolabs.ayllu.clustering.ClusterEvaluation;
import com.ontolabs.ayllu.clustering.ClusterModeAssigner;
import com.ontolabs.ayllu.clustering.Clusterer;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.grid.LearnerNode;
import com.ontolabs.ayllu.grid.clustering.density.DensityBasedClusterNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ParallelBoostingMeanShiftClustering.
 */
public class ParallelBoostingMeanShiftClustering extends BaseParallelLearner {
	
	 /** The logger. */
 	private final Logger logger = LoggerFactory.getLogger(ParallelBoostingMeanShiftClustering.class);

	// mode assigner
	/** The mode assigner. */
	public static String MODE_ASSIGNER = "clustering.mode.assigner";

	// cluster evaluation method
	/** The cluster evaluation. */
	public static String CLUSTER_EVALUATION = "clustering.evaluation.method";

	/** The cluster evaluation. */
	protected ClusterEvaluation clusterEvaluation;
	
	/** The workers. */
	protected List<NodeWorker> workers;

	// all modes
	/** The entire set. */
	private Set<DataVector> entireSet;

	// dbscan clusterer
	/** The dbscan. */
	Clusterer dbscan;

	// cluster mode assigner
	/** The mode assigner. */
	ClusterModeAssigner modeAssigner;

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.algorithm.bigdata.ParallelLearner#learn()
	 */
	@Override
	public void learn() throws LearnerException {
		if (this.learnSynchronously)
			learnSynchronously();
	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.algorithm.bigdata.BaseParallelLearner#initializeValidationData(java.util.Properties)
	 */
	protected void initializeValidationData(Properties l) {
	}

	/**
	 * Learn synchronously.
	 *
	 * @throws LearnerException
	 *             the learner exception
	 */
	protected void learnSynchronously() throws LearnerException {
		boolean keepRunning = true;
		List<List<DataVector>> clusters = null;
		// initialize a counter
		int runCounter = 0;
		int lastNumberOfClustersFound = 0;

		// partitionData and distribute
		partitionDataAndDistribute();

		// create NodeWorkers
		initializeNodeWorkers();
		this.entireSet = new HashSet<DataVector>();

		while (keepRunning) {
			long epochStart = System.currentTimeMillis();
			// train in multi threaded way
			this.trainMultiThreaded();
			long epochEnd = System.currentTimeMillis();
			// now we have the intermediate modes for this epoch
			List<DataVector> allModesAsList = new ArrayList<DataVector>(
					this.entireSet);
			// create a dataset
			DataSet modesAsSet = this.factory.createDataSet(allModesAsList,
					this.labelName);

			// cluster the modes
			clusters = dbscan.cluster(modesAsSet);
			int currentNumberOfClustersFound = clusters.size();
			logger.info("Epoch = " + this.epoch + ", Cluster Size = " + currentNumberOfClustersFound + ", EpochDuration = " + (epochEnd-epochStart) + " msecs" + ", Modes Size = " + entireSet.size());
			// if we are finding same clusers?
			if (currentNumberOfClustersFound == lastNumberOfClustersFound) {
				runCounter++;
			}
			// reset the counting if finding new
			else {
				runCounter = 0;
			}
			lastNumberOfClustersFound = currentNumberOfClustersFound;
			// if we see that there has been no progress
			// stop it, this needs to be parameterized too
			// TODO change this to a property in future
			if (runCounter == 2)
				keepRunning = false;
			this.epoch++;
		}
		logger.debug("Completed parallel training, now using assignmentS");
		this.executorService.shutdownNow();
		// now that we are done we can assign designations
		int[] designations =modeAssigner.assignCluster(this.trainingSet, clusters);
		logger.debug("Completed parallel cluster assignments");
		//perform evaluation
		double evaluation = this.clusterEvaluation.evaluate(designations, trainingSet);
		logger.info("Cluster Evaluation Score = " + evaluation);
	}

	/**
	 * Train multi threaded.
	 *
	 * @throws LearnerException
	 *             the learner exception
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

		// collect all modes from all the nodes
		for (LearnerNode node : this.parallelGrid.getAllNodes()){
			List<DataVector> modes = ((DensityBasedClusterNode) node).getModes();
			logger.info("Node = " + node.hashCode() + ", Modes = " + modes.size());
			this.entireSet.addAll(modes);
		}
		

		// Next Test on neighboring nodes
		for (NodeWorker worker : this.workers)
			worker.setWork(NodeWorker.WORK_TEST);
		try {
			this.executorService.invokeAll(this.workers);
		} catch (InterruptedException e) {
			throw new LearnerException(e);
		}

		// select the best weights using ensemble
		for (NodeWorker worker : this.workers)
			worker.setWork(NodeWorker.WORK_WEIGHT_ENSEMBLE);
		try {
			this.executorService.invokeAll(this.workers);
		} catch (InterruptedException e) {
			throw new LearnerException(e);
		}

		// Then select the ones for the next epoch
		for (NodeWorker worker : this.workers)
			worker.setWork(NodeWorker.WORK_SELECT);
		try {
			this.executorService.invokeAll(this.workers);
		} catch (InterruptedException e) {
			throw new LearnerException(e);
		}

		// Then set data for next
		for (NodeWorker worker : this.workers)
			worker.setWork(NodeWorker.WORK_NEXT_EPOCH);
		try {
			this.executorService.invokeAll(this.workers);
		} catch (InterruptedException e) {
			throw new LearnerException(e);
		}

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

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.algorithm.bigdata.BaseParallelLearner#initializeLearner(java.util.Properties)
	 */
	@Override
	protected void initializeLearner(Properties l) throws ParameterException {
		// get learner
		String learnerClass = l.getProperty(LEARNER_CLASS);
		// get density based options
		Map meanShiftOptions = new HashMap();
		String maxIterations = l.getProperty(Clusterer.MEAN_SHIFT + "."
				+ Clusterer.MAX_ITERATIONS);
		meanShiftOptions.put(Clusterer.MAX_ITERATIONS, maxIterations);
		String bandwidth = l.getProperty(Clusterer.MEAN_SHIFT + "."
				+ Clusterer.SCALE_BANDWIDTH);
		meanShiftOptions.put(Clusterer.SCALE_BANDWIDTH, bandwidth);
		this.baseLearner = this.factory.getClusterer(learnerClass,
				meanShiftOptions);

		// get the dbscan learner
		String dbScanLearner = l.getProperty(Clusterer.DBSCAN);
		// get density based options
		Map dbscanOptions = new HashMap();
		// get dbscan properties
		String epsilon = l.getProperty(Clusterer.DBSCAN + "."
				+ Clusterer.EPSILON);
		dbscanOptions.put(Clusterer.EPSILON, epsilon);
		String minPoints = l.getProperty(Clusterer.DBSCAN + "."
				+ Clusterer.MIN_POINTS);
		dbscanOptions.put(Clusterer.MIN_POINTS, minPoints);
		String dbscanProcessingThreads = l.getProperty(Clusterer.DBSCAN + "."
				+ Clusterer.THREADS);
		dbscanOptions.put(Clusterer.THREADS, dbscanProcessingThreads);
		this.dbscan = this.factory.getClusterer(dbScanLearner, dbscanOptions);

		// get the cluster mode assigner
		String modeAssigner = l.getProperty(MODE_ASSIGNER);
		Class modeAssignerClass = null;
		try {
			modeAssignerClass = Class.forName(modeAssigner);
		} catch (ClassNotFoundException e2) {
			throw new ParameterException(e2);
		}
		Constructor ctor = null;
		try {
			ctor = modeAssignerClass.getConstructor(int.class);
		} catch (NoSuchMethodException e1) {
			throw new ParameterException(e1);
		} catch (SecurityException e1) {
			throw new ParameterException(e1);
		}
		try {
			this.modeAssigner = (ClusterModeAssigner) ctor.newInstance(4);
		} catch (InstantiationException e) {
			throw new ParameterException(e);
		} catch (IllegalAccessException e) {
			throw new ParameterException(e);
		} catch (IllegalArgumentException e) {
			throw new ParameterException(e);
		} catch (InvocationTargetException e) {
			throw new ParameterException(e);
		}

	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.algorithm.bigdata.BaseParallelLearner#initializeLearning(java.util.Properties)
	 */
	@Override
	public void initializeLearning(Properties l) throws ParameterException {
		super.initializeLearning(l);

		// get the cluster evaluation
		String clusterEvaluationMethod = l.getProperty(CLUSTER_EVALUATION);
		clusterEvaluation = this.factory
				.getClusterEvaluation(clusterEvaluationMethod);

	}

}
