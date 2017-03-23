/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu;

// TODO: Auto-generated Javadoc
/**
 * The Class ParameterException.
 */
public class ParameterException extends Exception{

	/**
	 * Instantiates a new parameter exception.
	 *
	 * @param message
	 *            the message
	 * @param t
	 *            the t
	 */
	public ParameterException(String message, Throwable t){
		super(message,t);
	}
	
	/**
	 * Instantiates a new parameter exception.
	 *
	 * @param e
	 *            the e
	 */
	public ParameterException(Exception e){
		super(e);
	}
}
