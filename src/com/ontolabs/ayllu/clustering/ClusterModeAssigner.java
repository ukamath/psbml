/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.clustering;

import java.util.List;

import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;

public interface ClusterModeAssigner {
	

	/**
	 * Assign cluster.
	 *
	 * @param ds
	 *            the ds
	 * @param clusters
	 *            the clusters
	 * @return the int[]
	 */
	int[] assignCluster(DataSet ds, List<List<DataVector>> clusters);

}
