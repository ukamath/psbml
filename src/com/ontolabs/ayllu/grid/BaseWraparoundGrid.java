/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.grid;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



import com.ontolabs.ayllu.Learner;
import com.ontolabs.ayllu.LearnerException;

/**
 * The Class BaseWraparoundGrid is base implementation of Grid. 
 */
public abstract class BaseWraparoundGrid implements Grid {

	/** The neighborhood. */
	protected int[][] neighborhood;
	
	/** The nodes. */
	protected LearnerNode[][] nodes;
	
	/** The width. */
	protected int width;
	
	/** The height. */
	protected int height;
	
	/** The learner. */
	protected List<Learner> learner;


	/**
	 * Instantiates a new base wraparound grid.
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
	public BaseWraparoundGrid(int w, int h, int[][] neighborhood, List<Learner> learner) {
		this.width = w;
		this.height = h;
		this.learner = learner;
		this.neighborhood = neighborhood;
	}
	
	

	// ----------
	/** The node pos. */
	protected Map<LearnerNode, int[]> nodePos;

	@Override
	public void setNeighborhood(int[][] n) throws LearnerException {
		this.neighborhood = n;
	}

	@Override
	public List<LearnerNode> getAllNodes() {
		List<LearnerNode> nodes = new LinkedList<LearnerNode>();
		for (int i = 0; i < this.nodes.length; i++)
			for (int j = 0; j < this.nodes[i].length; j++)
				nodes.add(this.nodes[i][j]);
		return (nodes);
	}

	@Override
	public List<LearnerNode> getNeighbors(LearnerNode node) {
		// Find the neighbors in grid. Wraparound in 2 directions.
		int[] pos;
		List<LearnerNode> nei = new LinkedList<LearnerNode>();

		pos = this.nodePos.get(node);
		for (int[] neipos : neighborhood)
			nei.add(getNodeAt(pos[0] + neipos[0], pos[1] + neipos[1]));

		return (nei);
	}

	@Override
	public LearnerNode getNodeAt(int x, int y) {
		if (x < 0)
			x = this.width + x;
		else if (x >= this.width)
			x = x - this.width;

		if (y < 0)
			y = this.height + y;
		else if (y >= this.height)
			y = y - this.height;

		return (this.nodes[x][y]);
	}
	


}
