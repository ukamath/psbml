package com.ontolabs.ayllu.vendor.weka;

import weka.core.DistanceFunction;

import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.distance.DistanceMetric;

public class WekaDistanceMetric implements DistanceMetric {
	
	protected DistanceFunction distanceFunction;

	@Override
	public double distance(DataVector from, DataVector to) {
		WekaDataVector point1 = (WekaDataVector)from;
		WekaDataVector point2 = (WekaDataVector)to;
		return this.distanceFunction.distance(point1.wekaInstance, point2.wekaInstance);		
	}

	

}
