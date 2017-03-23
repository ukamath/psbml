/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor.jsat;

import jsat.linear.Vec;
import jsat.linear.distancemetrics.EuclideanDistance;

import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.distance.DistanceMetric;

public class JSATDistanceMetric implements DistanceMetric{
	
	protected jsat.linear.distancemetrics.DistanceMetric dm;
	
	/**
	 * Instantiates a new JSAT distance metric.
	 */
	public JSATDistanceMetric(){
		this.dm = new EuclideanDistance();
	}

	@Override
	public double distance(DataVector from, DataVector to) {
		JSATDataVector pointa = (JSATDataVector)from;
		JSATDataVector pointb = (JSATDataVector)to;
		Vec pointaNumericValues = pointa.getDataPoint().getNumericalValues();
		Vec pointbNumericValues = pointb.getDataPoint().getNumericalValues();
		return dm.dist(pointaNumericValues, pointbNumericValues);
	}
		

}
