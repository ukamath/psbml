/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.jsat;

import java.util.List;
import java.util.Map;
import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;
import jsat.clustering.Clusterer;







import com.ontolabs.ayllu.Learner;
import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.clustering.MeanShift;
import com.ontolabs.ayllu.data.DataVector;

/**
 * The Class JSATMeanShiftClustering.
 */
public class JSATMeanShiftClustering extends JSATClusterer implements MeanShift {
	
	/** The max iterations. */
	int maxIterations;
	
	/** The bandwith. */
	double bandwith;

	/**
	 * Instantiates a new JSAT mean shift clustering.
	 *
	 * @param clusterer
	 *            the clusterer
	 */
	protected JSATMeanShiftClustering(Clusterer clusterer) {
		super(clusterer);
	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.vendor.jsat.JSATClusterer#setOptions(java.util.Map)
	 */
	@Override
	public void setOptions(Map options) throws ParameterException {
		//save max iterations
		String maxIterationsProperty = (String) options.get(MAX_ITERATIONS);
		this.maxIterations = new Integer(maxIterationsProperty).intValue();
		//save bandwith
		String bandwidthProperty = (String)options.get(SCALE_BANDWIDTH);
		this.bandwith = new Double(bandwidthProperty).doubleValue();		
	}
	
	/**
  	 * 
  	 * @return deep copy of Learner
  	 * @throws ParameterException
  	 */
  	public Learner copy() throws ParameterException{
  		jsat.clustering.MeanShift ms = new jsat.clustering.MeanShift();
  		JSATMeanShiftClustering copy = new JSATMeanShiftClustering(ms);
  		copy.bandwith = this.bandwith;
  		copy.maxIterations = this.maxIterations;
  		return copy;
  	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.clustering.MeanShift#getModes()
	 */
	@Override
	public List<DataVector> getModes() {
		jsat.clustering.MeanShift ms = (jsat.clustering.MeanShift)this.clusterer;
		List<DataPoint> points = ms.getModes();
		SimpleDataSet sds = new SimpleDataSet(points);
		return JSATUtility.getDataVectors(sds, null);
	}

	/* (non-Javadoc)
	 * @see com.ontolabs.ayllu.Parameters#setOptions(java.lang.String)
	 */
	@Override
	public void setOptions(String options) throws ParameterException {
		// TODO Auto-generated method stub
		
	}
}
