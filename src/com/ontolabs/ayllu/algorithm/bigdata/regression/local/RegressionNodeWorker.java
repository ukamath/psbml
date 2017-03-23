package com.ontolabs.ayllu.algorithm.bigdata.regression.local;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;


import com.ontolabs.ayllu.algorithm.bigdata.classification.local.NodeWorker;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.grid.LearnerNode;
import com.ontolabs.ayllu.grid.classifier.ClassifierNode;
import com.ontolabs.ayllu.vendor.VendorFactory;

/**
 * The Class NodeWorker is a @see Callable implementation for parallelizing
 * learning of the @see LearnerNode either in training or selection phase.
 */
public class RegressionNodeWorker extends NodeWorker {
	

	public RegressionNodeWorker(LearnerNode node,
			List<? extends LearnerNode> neighbors,
			double replacementPercentage, VendorFactory factory) {
		super(node, neighbors, replacementPercentage, factory);
		
	}

	protected void testInstancesOnNeighbors() throws Exception {
		DataSet nodeset;
		int i;
		double[] weight;
		double conf, minconf;

		nodeset = node.getData();
		weight = new double[nodeset.size()];
		int goalIndex = nodeset.getGoalIndex();
		// For all instance in this node's dataset
		i = 0;
		for (DataVector instance : nodeset) {
			// Test on neighbors.closest to boundary
			minconf = Double.MAX_VALUE;
			for (LearnerNode neighbor : neighbors) {
				// cast to classifier
				ClassifierNode classifierNode = (ClassifierNode) neighbor;
				double regressedValue = classifierNode.classify(instance);
				double actualLabelValue = instance.getValue(goalIndex);
				conf= Math.abs(actualLabelValue - regressedValue);
				if (conf < minconf)
					minconf = conf;
			}
			// ********** DISABLE WEIGHTING *******
			// minconf = 1.0;
			// ************************************
			weight[i++] = minconf;
		}

		double wdiff, wmax, wmin;

		// Normalize weight with the highest weight is the most difficult
		// instance
		wmax = Double.NEGATIVE_INFINITY;
		wmin = Double.POSITIVE_INFINITY;
		for (i = 0; i < weight.length; i++)
			if (weight[i] > wmax)
				wmax = weight[i];
		for (i = 0; i < weight.length; i++)
			if (weight[i] < wmin)
				wmin = weight[i];
		wdiff = (wmax - wmin);
		if (wdiff != 0) {
			for (i = 0; i < weight.length; i++) {
				// Normalize between 0 and 1
				weight[i] = 1.0 - ((weight[i] - wmin) / wdiff);

			}
		} else {
			for (i = 0; i < weight.length; i++)
				weight[i] = 1.0;
		}

		// Update the Instances of this Node with their new weights
		i = 0;
		for (DataVector ins : nodeset) {
			// TODO: Add momentum as suggested by Uday
			ins.setWeight(weight[i++]);
		}
	}

}
