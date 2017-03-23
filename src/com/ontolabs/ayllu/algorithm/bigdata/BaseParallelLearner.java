package com.ontolabs.ayllu.algorithm.bigdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontolabs.ayllu.Learner;
import com.ontolabs.ayllu.LearnerException;
import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.algorithm.bigdata.classification.local.NodeWorker;
import com.ontolabs.ayllu.classifier.Classifier;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.grid.Grid;
import com.ontolabs.ayllu.grid.GridFactory;
import com.ontolabs.ayllu.grid.LearnerNode;
import com.ontolabs.ayllu.vendor.VendorAbstractFactory;
import com.ontolabs.ayllu.vendor.VendorFactory;

public abstract class BaseParallelLearner implements ParallelLearner {
	
	 private final Logger logger = LoggerFactory.getLogger(BaseParallelLearner.class);

	// vendor
	protected VendorFactory factory;

	// classifier
	protected Learner baseLearner = null;
	// dataset
	protected DataSet trainingSet = null;
	// label
	protected String labelName;
	// dataset
	protected DataSet validationSet = null;
	// Grid
	protected Grid parallelGrid = null;
	// selection
	protected double replacement;
	// synchronous or asynchronous
	protected boolean learnSynchronously = true;
	// Multi-threading support
	protected ExecutorService executorService;
	
	protected int numberOfThreads;

	// stop criteria
	/** The epoch. */
	protected int epoch = 0;
	protected double minError = 0.0;
	protected int sizeAtMinError = 0;
	protected int epochAtMinError=0;
	protected Learner bestSoFarLearner;
	protected int maxEpochs;

	

	/**
	 * Partition data and distribute.
	 *
	 * @throws LearnerException
	 *             the learner exception
	 */
	protected void partitionDataAndDistribute() throws LearnerException {
		System.out.println("Distributing train dataset over nodes:");
		int numNodes = this.parallelGrid.getAllNodes().size();
		int i = 0;
		for (LearnerNode node : this.parallelGrid.getAllNodes()) {
			DataSet nodedata = this.trainingSet.createPartition(numNodes, i++);
			try {
				node.setData(nodedata);
			} catch (ParameterException pe) {
				throw new LearnerException(pe);
			}

		}
	}

	protected void initializeVendor(Properties l) throws ParameterException {
		// get the vendor
		String vendor = l.getProperty(VENDOR);
		// get the factory of factories from the vendor
		factory = VendorAbstractFactory.getVendorFactory(vendor);
	}

	protected void initializeLearner(Properties l) throws ParameterException {
		// get learner
		String learnerClass = l.getProperty(LEARNER_CLASS);
		String options = l.getProperty(LEARNER_OPTIONS);
		baseLearner = (Classifier) factory.getClassifier(learnerClass,
				options);
		labelName = l.getProperty(LEARNER_GOAL_NAME);
	}

	protected void initializeTrainingData(Properties l)
			throws ParameterException {
		String dataSet = l.getProperty(DATASET_TRAINING);
		String filePath = l.getProperty(DATASET_TRAINING_FILE_PATH);
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(DATASET_TRAINING, DATASET_FILE);
		properties.put(DATASET_TRAINING_FILE_PATH, filePath);
		properties.put(LEARNER_GOAL_NAME, labelName);
		this.trainingSet = factory.getDataSet(properties);
	}

	protected void initializeValidationData(Properties l)
			 {

		String validationDataSet = l.getProperty(DATASET_TRAINING);
		String validationFilePath = l.getProperty(DATASET_VALIDATION_FILE_PATH);
		Map<String, String> validationProperties = new HashMap<String, String>();
		validationProperties.put(DATASET_TRAINING, DATASET_FILE);
		validationProperties
				.put(DATASET_TRAINING_FILE_PATH, validationFilePath);
		validationProperties.put(LEARNER_GOAL_NAME, labelName);
		try {
			this.validationSet = factory.getDataSet(validationProperties);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void initializeGrid(Properties l) throws ParameterException {
		// load the grid stuff
		String gridWidthProperty = l.getProperty(GRID_WIDTH);
		int gridWidth = new Integer(gridWidthProperty).intValue();
		String gridHeightProperty = l.getProperty(GRID_HEIGHT);
		int gridHeight = new Integer(gridHeightProperty).intValue();
		String neighborhood = l.getProperty(GRID_NEIGHBORHOOD);
		int numberOfLearners = gridWidth*gridHeight;
		List<Learner> learnerForNodes = new ArrayList<Learner>();
		for(int i=0; i< numberOfLearners; i++){
			learnerForNodes.add(this.baseLearner.copy());
		}			
		this.parallelGrid = GridFactory.createGrid(gridWidth, gridHeight,
				neighborhood, learnerForNodes);
		String replacementString = l.getProperty(REPLACEMENT_RATIO);
		this.replacement = new Double(replacementString).doubleValue();
	}
	

	protected void initializeParallel(Properties l) throws ParameterException {
		
		String threads = l.getProperty(PARALLEL_THREADS);
		this.numberOfThreads = new Integer(threads).intValue();
		String epochsString = l.getProperty(PARALLEL_EPOCHS);
		this.maxEpochs = new Integer(epochsString).intValue();
	}

	public void initializeLearning(Properties l) throws ParameterException {
		this.initializeVendor(l);

		// get learner
		this.initializeLearner(l);

		// load the training dataset
		this.initializeTrainingData(l);

		// load the validation dataset
		this.initializeValidationData(l);

		

		// initialize grid
		this.initializeGrid(l);
		
		// load parallel stuff
		this.initializeParallel(l);

	}
	
	/**
	 * post-process stuff can be done here, like
	 * saving the margin data or cluster found etc
	 */
	public void postLearning() throws LearnerException{
		
	}

}
