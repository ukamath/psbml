/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.vendor;

import com.ontolabs.ayllu.ParameterException;
import com.ontolabs.ayllu.vendor.jsat.JSATVendorFactory;
import com.ontolabs.ayllu.vendor.weka.WekaFactory;

/**
 * A factory for creating VendorFactory objects, following the AbstractFactory design
 * or Factory of Factories. 
 */
public class VendorAbstractFactory {
	
	/** The weka. */
	public static String WEKA="weka";
	
	/** The jsat. */
	public static String JSAT="jsat";
	
	/** The rapid miner. */
	public static String RAPID_MINER="rapidminer";

	
	/**
	 * Gets the vendor factory.
	 *
	 * @param vendor
	 *            the vendor
	 * @return the vendor factory
	 * @throws ParameterException
	 *             the parameter exception
	 */
	public static VendorFactory getVendorFactory(String vendor) throws ParameterException{
		VendorFactory factory = null;
		if(vendor.equals(WEKA))
			factory = new WekaFactory();
		else if(vendor.equals(JSAT))
			factory = new JSATVendorFactory();
		return factory;
	}

}
