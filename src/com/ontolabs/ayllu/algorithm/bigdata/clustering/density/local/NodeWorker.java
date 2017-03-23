/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.algorithm.bigdata.clustering.density.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.data.DataVectorWeightComparator;
import com.ontolabs.ayllu.distance.DistanceMetric;
import com.ontolabs.ayllu.grid.LearnerNode;
import com.ontolabs.ayllu.grid.clustering.ClustererNode;
import com.ontolabs.ayllu.grid.clustering.density.DensityBasedClusterNode;
import com.ontolabs.ayllu.vendor.VendorFactory;



public class NodeWorker implements Callable<NodeWorker> {
	 private final Logger logger = LoggerFactory.getLogger(NodeWorker.class);


	public static final int WORK_TRAIN = 1; // Train the instances
	public static final int WORK_TEST = 2;// Test instances on neighbors.
	public static final int WORK_WEIGHT_ENSEMBLE = 3; // combining weights in ensemble
	public static final int WORK_SELECT = 4; // Select instances for next epoch.
	public static final int WORK_NEXT_EPOCH = 5;

	private int work;
	private DensityBasedClusterNode node;
	private List<? extends LearnerNode> neighbors;
	private VendorFactory factory;
	
	
	
	
	// ------
	private Random random;
	private long duration;
	private double replacementRatio;

	public NodeWorker(LearnerNode node, List<? extends LearnerNode> neighbors,
			double replacementRatio, VendorFactory factory) {
		this.node = (DensityBasedClusterNode)node;
		this.neighbors = neighbors;
		this.random = new Random(System.currentTimeMillis() % this.hashCode());
		this.replacementRatio = replacementRatio;
		this.factory = factory;
	}

	

	public void setWork(int work) {
		this.work = work;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public NodeWorker call() throws Exception {

		try {
			// Execute either the Classification training or Instance selection
			// step
			long start = System.currentTimeMillis();
			if (this.work == WORK_TRAIN)
				train();
			if (this.work == WORK_TEST)
				testInstancesOnNeighbors();
			if (this.work == WORK_WEIGHT_ENSEMBLE)
				setWeightCombining();
			if (this.work == WORK_SELECT)
				selectSpatialProportional();
			else if(this.work == WORK_NEXT_EPOCH)
				setDataForNextEpoch();
			long end = System.currentTimeMillis();
			
			logger.info("Node = " + this.hashCode() + ", Work = " + this.work + ", timeSpent= " + (end-start) + " msec");
				
			
		} catch (Exception ex) {
			// Debug... Multi-threading issues are tricky.
			ex.printStackTrace();

			// ? Re-throw to make sure the Threadpool kills off the failed
			// thread.
			// throw(ex);
		}
		return this;
	}

	public void train() throws Exception {
		this.node.train();
	}

	public long getTimeSpent() {
		Long spent = this.duration;
		this.duration = 0l;

		return (spent);
	}

	public ClustererNode getNode() {
		return this.node;
	}
	
	
	public void setDataForNextEpoch(){
		this.node.resetData();
	}

	public void setWeightCombining() throws Exception {
		// first we need to find the largest weight to the data point from
		// the ensemble from all my neighbors and my
		List<double[]> weightList = this.node.getAllWeights();
		for (int i = 0; i < this.node.getData().size(); i++) {
			DataVector currentDataPoint = this.node.getData().get(i);
			double weightToSet = 0.0;
			for (int j = 0; j < weightList.size(); j++) {
				double[] weights = weightList.get(j);
				// get the weight for the ith point
				if (weights[i] > weightToSet) {
					weightToSet = weights[i];
				}
			}
			// we found the best weight from ensemble
			currentDataPoint.setWeight(weightToSet);
		}
	}

	public void selectSpatialProportional() throws Exception {
		 TreeSet<DataVector>    weightOrderInstances;
	        TreeMap<Double, DataVector> sampleInstance;
	        List<DataVector>    zeroWeightInstances;
	        double            weightSum;
	        List<DataVector>    replaceSelect;

	        // Test the instances of this Node on the neighboring classifiers. Weight them accordingly.
	        testInstancesOnNeighbors();

	        // Collect all Instances from this Node and its neighboring nodes, ordered from high to low weight.
	        weightOrderInstances = new TreeSet<DataVector>(new DataVectorWeightComparator());
	        for(DataVector instance: node.getData())
	            weightOrderInstances.add(instance);
	        for(LearnerNode neighbor: neighbors)
	            for(DataVector instance: neighbor.getData())
	                weightOrderInstances.add(instance);

	        // Put instances in a TreeMap with as key the cumulative weight starting with highest weight instances
	        sampleInstance      = new TreeMap<Double, DataVector>();
	        zeroWeightInstances = new LinkedList<DataVector>();
	        weightSum = 0;
	        for(DataVector instance: weightOrderInstances)
	        {
	            if (instance.getWeight() > 0)
	            {
	                weightSum += instance.getWeight();
	                sampleInstance.put(weightSum, instance);
	            }
	            else
	            {
	                // Keep the (worst) Instances with weight 0 in a separate list... So they don't overwrite the sampleInstance of the last instances with weight > 0.
	                zeroWeightInstances.add(instance);
	            }
	        }

	        DataVector selectedInstance;
	        DataSet selectedInstances;

	        // Replace the given fraction of instances
	        replaceSelect = new LinkedList<DataVector>();
	        for(DataVector instance: node.getData())
	        {
	            // Replace with instance chosen with weight-proportionate selection.
	            if (this.random.nextDouble() < this.replacementRatio)
	                selectedInstance = selectWeightedInstance(sampleInstance, zeroWeightInstances);
	            else 
	            	selectedInstance = instance;

	            // Make a copy with reset weight 
	            selectedInstance = selectedInstance.copy();
	            selectedInstance.setWeight(1.0);

	            replaceSelect.add(selectedInstance);
	        }
	        
	        //use the factory
	        selectedInstances= factory.createDataSet(replaceSelect, this.node.getData().getGoal());
	        
	        // Remember replacements for this Node. Don't replace yet because this Node is a neighbor for other Nodes and these still need the weight of this epoch.
	        node.setSelectedData(selectedInstances);

	}
	
	 private DataVector selectWeightedInstance(TreeMap<Double, DataVector> instances, List<DataVector> zeroWeightInstances)
	    {
	        double   ran;
	        Double   wkey;

	        // Pick a random number in [0, sum of instance weights]
	        ran  = this.random.nextDouble()*instances.lastKey();

	        // Find the Instance for which the sum of weight of all previous instances is closest
	        wkey = instances.higherKey(ran);

	        // When beyond the highest weight, pick the last one when available. Or one of the zero-weight instances when available.
	        if (wkey == null)
	        {
	            if (zeroWeightInstances.size() == 0) return instances.get(instances.lastKey());
	            else                                 return zeroWeightInstances.get(this.random.nextInt(zeroWeightInstances.size()));
	        }
	        // Return instance selected by weight-proportionate selection.
	        else return instances.get(wkey);
	    }

	public void testInstancesOnNeighbors() throws Exception {
		DistanceMetric metric = this.factory.getDistanceMetric(DistanceMetric.EUCLIDEAN);
		// get modes from this
		List<DataVector>modes = this.node.getModes();
		List<Double>[] modesDistanceList = new List[modes.size()];

		List<List<DataVector>> nodeAndNeighborsData = new ArrayList<List<DataVector>>();
		// weights of node and neighbors
		List<double[]> weightsOfNodeAndNeighbors = new ArrayList<double[]>();
		// mode assigned to node and neighbors
		List<int[]> modesOfNodeAndNeighbors = new ArrayList<int[]>();
		// add my data points
		nodeAndNeighborsData.add(this.node.getData());

		for (LearnerNode node : neighbors) {
			nodeAndNeighborsData.add(node.getData());
		}

		for (List<DataVector> listOfDataPoints : nodeAndNeighborsData) {
			int currentVector = 0;
			// mode initialization for nodes
			int[] currentModes = new int[listOfDataPoints.size()];
			modesOfNodeAndNeighbors.add(currentModes);
			// weight initialization for nodes
			// mode initialization for nodes
			double[] currentWeights = new double[listOfDataPoints.size()];
			weightsOfNodeAndNeighbors.add(currentWeights);
			// iterate the dataset to find the shortest distance to modes
			for (DataVector dataPoint : listOfDataPoints) {
				// the data
				// shortes distance yet for the point
				double shortestDistanceToMode = Double.MAX_VALUE;
				// calculate the mode distance
				int currentMode = 0;
				for (DataVector modeVector : modes) {
					double distance = metric.distance(dataPoint,
							modeVector);
					if (distance < shortestDistanceToMode) {
						shortestDistanceToMode = distance;
						currentModes[currentVector] = currentMode;
					}
					currentMode++;
				}
				currentWeights[currentVector] = shortestDistanceToMode;
				// get the List for this mode
				List distancesToModes = modesDistanceList[currentModes[currentVector]];
				if (distancesToModes == null) {
					// create a list
					distancesToModes = new ArrayList<Double>();
					// add it
					modesDistanceList[currentModes[currentVector]] = distancesToModes;
				}
				distancesToModes.add(currentWeights[currentVector]);
				currentVector++;
			}
		}

		// confidence assignment
		int listCount = 0;
		for (List<DataVector> listOfDataPoints : nodeAndNeighborsData) {

			boolean[] allModesSorted = new boolean[modesDistanceList.length];
			int[] modeAssigned = modesOfNodeAndNeighbors.get(listCount);
			double[] weights = weightsOfNodeAndNeighbors.get(listCount);
			int currentVector = 0;
			for (DataVector dataPoint : listOfDataPoints) {
				// find the mode assigned
				int modeAssignedtoVector = modeAssigned[currentVector];
				// get all distances to
				List<Double> allDistances = modesDistanceList[modeAssignedtoVector];
				if (allModesSorted[modeAssignedtoVector] == false) {
					// sort the distances
					Collections.sort(allDistances);
					allModesSorted[modeAssignedtoVector] = true;
				}
				// the max is the first
				double distanceMax = allDistances.get(allDistances.size() - 1);
				// the min
				double distanceMin = allDistances.get(0);
				// confidence assignment
				weights[currentVector] = 1 - ((weights[currentVector] - distanceMin) / (distanceMax - distanceMin));
				currentVector++;
			}
			listCount++;
		}
		// now i can set the weights on neighbors
		for (int i = 1; i < weightsOfNodeAndNeighbors.size(); i++) {
			double[] weightsForNeighbor = weightsOfNodeAndNeighbors.get(i);
			// set it on the neighbor
			((ClustererNode) this.neighbors.get(i - 1)).addWeightsFromNeighbors(
					weightsForNeighbor);
		}
		// set the weights on the node itself
		this.node.setWeightsFromNode(weightsOfNodeAndNeighbors.get(0));

		// lets assist in some garbage collection
		for(int i=0 ; i < modesDistanceList.length; i++){
			modesDistanceList[i].clear();
		}
		modesDistanceList = null;
		nodeAndNeighborsData.clear();
		nodeAndNeighborsData = null;
		weightsOfNodeAndNeighbors.clear();
		weightsOfNodeAndNeighbors = null;
		modesOfNodeAndNeighbors.clear();
		modesOfNodeAndNeighbors = null;

	}

	
}
