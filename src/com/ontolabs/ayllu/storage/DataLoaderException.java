/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.storage;

import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class DataLoaderException.
 */
public class DataLoaderException extends Exception{
	
	
	/**
	 * Instantiates a new data loader exception.
	 *
	 * @param message
	 *            the message
	 * @param t
	 *            the t
	 */
	public DataLoaderException(String message, Throwable t){
		super(message,t);
	}

	public DataLoaderException(Exception ioe) {
		super(ioe);
	}

}
