/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.weka;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.data.Feature;

// TODO: Auto-generated Javadoc
/**
 * The Class WekaDataSet.
 */
public class WekaDataSet extends AbstractList<DataVector> implements DataSet, Serializable {
	Instances wekaDataSet;
	List<DataVector> dataVectors;
	String labelAttributeName;
	Random random;
	ArrayList<Attribute> features;

	WekaDataSet(Instances in, String label) {
		this.wekaDataSet = in;
		this.labelAttributeName = label;
		this.random = new Random();
	}

	WekaDataSet(List<DataVector> vectors, String label) {
		this.dataVectors = vectors;
		this.labelAttributeName = label;

	}

	/**
	 * Gets the data vectors.
	 *
	 * @return the data vectors
	 */
	protected List<DataVector> getDataVectors() {
		if (dataVectors == null) {
			dataVectors = new ArrayList<DataVector>();
			for (int i = 0; i < wekaDataSet.numInstances(); i++)
				dataVectors.add(new WekaDataVector(wekaDataSet.get(i)));
		}
		return this.dataVectors;
	}

	@Override
	public DataVector get(int index) {
		return this.getDataVectors().get(index);
	}

	@Override
	public DataVector set(int index, DataVector v) {
		return this.getDataVectors().set(index, v);
	}

	@Override
	public boolean add(DataVector v) {
		return this.getDataVectors().add(v);
	}

	@Override
	public DataSet[] createTrainingTestingPartitions(double trainingPortion) {
		DataSet[] partition = new DataSet[2];
		// Select out a fold
		int stratify = (int) (trainingPortion * 10);
		// set the label
		this.wekaDataSet.setClass(this.wekaDataSet
				.attribute(labelAttributeName));

		// create training and testing set
		this.wekaDataSet.randomize(random);

		this.wekaDataSet.stratify(10);
		int splits = new Double(this.wekaDataSet.numInstances()
				* trainingPortion).intValue();
		partition[0] = new WekaDataSet(new Instances(this.wekaDataSet, 0,
				splits), this.labelAttributeName);
		partition[1] = new WekaDataSet(new Instances(this.wekaDataSet, splits,
				this.wekaDataSet.numInstances() - splits),
				this.labelAttributeName);
		System.out.println("Building model on training split (" + splits
				+ " instances)...");

		return partition;
	}

	public DataSet createPartition(int partitionSize, int partitionNumber) {
		DataSet partition = null;
		partition = new WekaDataSet(this.wekaDataSet.testCV(partitionSize,
				partitionNumber), this.labelAttributeName);
		return partition;
	}

	public int size() {
		int size = 0;
		if (this.wekaDataSet != null)
			size = this.wekaDataSet.size();
		else
			size = this.dataVectors.size();
		return size;
	}

	Instances getInstances() {
		if (this.wekaDataSet == null && this.dataVectors != null) {
			List<Feature> features = this.dataVectors.get(0).getFeatures();
			ArrayList<Attribute> allAttributes = new ArrayList<Attribute>(
					features.size());
			for (Feature f : features) {
				Attribute attribute = ((WekaFeature) f).attribute;
				allAttributes.add(attribute);
			}
			this.wekaDataSet = new Instances("temp"
					+ System.currentTimeMillis(), allAttributes,
					this.dataVectors.size());
			// add instances
			for (DataVector dataVector : this.dataVectors) {
				Instance instance = ((WekaDataVector) dataVector).wekaInstance;
				this.wekaDataSet.add(instance);
			}
			// set the class variable
			this.wekaDataSet.setClass(this.wekaDataSet
					.attribute(labelAttributeName));
		}
		return this.wekaDataSet;
	}

	@Override
	public String getGoal() {
		return this.labelAttributeName;
	}

	@Override
	public int getGoalIndex() {
		return this.getInstances().classIndex();
	}

}
