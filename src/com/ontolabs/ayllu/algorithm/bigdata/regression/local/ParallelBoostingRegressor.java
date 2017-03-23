package com.ontolabs.ayllu.algorithm.bigdata.regression.local;

import java.util.LinkedList;
import java.util.concurrent.Executors;

import com.ontolabs.ayllu.algorithm.bigdata.classification.local.NodeWorker;
import com.ontolabs.ayllu.algorithm.bigdata.classification.local.ParallelBoostingClassifier;
import com.ontolabs.ayllu.classifier.Classifier;
import com.ontolabs.ayllu.classifier.EvaluationException;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.grid.LearnerNode;

public class ParallelBoostingRegressor extends ParallelBoostingClassifier{
	
	/**
	 * Initialize node workers.
	 */
	protected void initializeNodeWorkers() {
		// Create multi-threading logic. One worker for each Regression Node.
		this.executorService = Executors.newFixedThreadPool(numberOfThreads);
		this.workers = new LinkedList<NodeWorker>();
		for (LearnerNode node : this.parallelGrid.getAllNodes())
			this.workers.add(new RegressionNodeWorker(node, this.parallelGrid
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
		//get the mean error
		return this.evaluation.evaluate(this.evaluationMethod, classifier, trainset, this.validationSet);
	}
}
