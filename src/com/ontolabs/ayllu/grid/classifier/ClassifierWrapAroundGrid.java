/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.grid.classifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ontolabs.ayllu.Learner;
import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.grid.BaseWraparoundGrid;
import com.ontolabs.ayllu.grid.LearnerNode;

/**
 * The Class ClassifierWrapAroundGrid is for classification semantics in the Grid.
 */
public class ClassifierWrapAroundGrid extends BaseWraparoundGrid {

	/**
	 * Instantiates a new classifier wrap around grid.
	 *
	 * @param w
	 *            the w
	 * @param h
	 *            the h
	 * @param neighborhood
	 *            the neighborhood
	 * @param learner
	 *            the learner
	 */
	public ClassifierWrapAroundGrid(int w, int h, int[][] neighborhood,
			List<Learner> learner) {
		super(w, h, neighborhood, learner);
	}

	public void initializeLearnerNodes() throws ParameterException {
		this.nodes = new LearnerNode[this.width][this.height];
		this.nodePos = new HashMap<LearnerNode, int[]>();
		int learnerCount=0;
		for (int i = 0; i < this.nodes.length; i++)
			for (int j = 0; j < this.nodes[i].length; j++) {
				this.nodes[i][j] = new ClassifierNodeImpl();
				this.nodes[i][j].setLearner(this.learner.get(learnerCount));
				this.nodePos.put( this.nodes[i][j], new int[] {
						i, j });
				learnerCount++;
			}
	}

}
