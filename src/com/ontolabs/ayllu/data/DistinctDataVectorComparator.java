/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.data;

import java.util.Comparator;


/**
 * The Class DistinctDataVectorComparator.
 */
public class DistinctDataVectorComparator implements Comparator {
	public int compare(Object arg0, Object arg1) {
		if ((arg0 instanceof DataVector) && (arg1 instanceof DataVector)) {
			DataVector a = (DataVector) arg0;
			DataVector b = (DataVector) arg1;
			boolean diff = false;
			int i = 0;
			while (i < a.getFeatures().size() && !diff) {
				if (a.getValue(i) != b.getValue(i))
					diff = true;
				if (!diff)
					i++;
			}
			if (!diff)
				return (0);
			else
				return (new Double(a.getValue(i)).compareTo(new Double(b
						.getValue(i))));
		} else
			return 0;
	}
}
