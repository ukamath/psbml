/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu;

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Interface Parameters.
 */
public interface Parameters {

	 /**
	 * Sets the options.
	 *
	 * @param options
	 *            the new options
	 * @throws ParameterException
	 *             the parameter exception
	 */
 	public void setOptions(Map options) throws ParameterException;
	  
	 /**
	 * Sets the options.
	 *
	 * @param options
	 *            the new options
	 * @throws ParameterException
	 *             the parameter exception
	 */
 	public void setOptions(String options) throws ParameterException;	
}
