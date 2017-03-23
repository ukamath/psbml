/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.grid.clustering.density;

import java.util.ArrayList;
import java.util.List;

import com.ontolabs.ayllu.Learner;
import com.ontolabs.ayllu.LearnerException;
import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.clustering.MeanShift;
import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;

public class DensityBasedClusterNodeImpl implements DensityBasedClusterNode {

	protected MeanShift clusterer;
	protected DataSet dataSet;
	protected DataSet replacementSet;

	protected List<double[]> weightsCollectedFromNeighbors;

	protected double[] weightsFromNode;

	// modes for the nodes;
	protected List<DataVector> modes;

	@Override
	public void addWeightsFromNeighbors(double[] weights) {
		synchronized (weightsCollectedFromNeighbors) {
			weightsCollectedFromNeighbors.add(weights);
		}
	}
	
	private void reInitiatialize(){			
		weightsCollectedFromNeighbors = new ArrayList<double[]>();
		weightsFromNode = null;
	}

	@Override
	public List<double[]> getAllWeights() {
		// now it has been synchronized and we have all data
		// we will add ours to all
		synchronized (weightsCollectedFromNeighbors) {
			weightsCollectedFromNeighbors.add(weightsFromNode);
			return weightsCollectedFromNeighbors;
		}
	}

	@Override
	public void setWeightsFromNode(double[] weights) {
		this.weightsFromNode = weights;
	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.grid.clustering.density.DensityBasedClusterNode#getModes()
	 */
	@Override
	public List<DataVector> getModes() {
		return this.clusterer.getModes();
	}

	@Override
	public void setLearner(Learner l) throws ParameterException {
		this.clusterer = (MeanShift)l;
	}

	@Override
	public Learner getLearner() {
		return this.clusterer;
	}

	@Override
	public void setData(DataSet dataset) throws ParameterException {
		this.dataSet = dataset;
		//needed for first time
		if(this.weightsCollectedFromNeighbors == null){
			this.reInitiatialize();
		}
	}

	@Override
	public DataSet getData() {
		return this.dataSet;
	}

	@Override
	public void train() throws LearnerException {
		this.clusterer.train(dataSet);
	}

	@Override
	public void setSelectedData(DataSet dataset) {
		this.replacementSet = dataset;
		
	}

	@Override
	public DataSet getSelectedData() {
		return this.replacementSet;
	}

	@Override
	public void resetData() {
		this.dataSet = this.replacementSet;
		this.replacementSet = null;
		this.reInitiatialize();
		
	}

}
