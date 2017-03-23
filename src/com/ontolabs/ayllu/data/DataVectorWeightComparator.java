/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.data;

import java.util.Comparator;

/**
 * The Class DataVectorWeightComparator.
 */
public class DataVectorWeightComparator implements Comparator {

	// Order from highest to lowest Instance getWeight. Break ties with the
	// hashCode of the Instances.
	public int compare(Object arg0, Object arg1) {
		DataVector a = (DataVector) arg0;
		DataVector b = (DataVector) arg1;
		if (b.getWeight() > a.getWeight())
			return (1);
		else if (b.getWeight() < a.getWeight())
			return (-1);
		else
			return new Integer(b.hashCode())
					.compareTo(new Integer(a.hashCode()));
	}

}
