/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.classifier;

// TODO: Auto-generated Javadoc
/**
 * The Class ClassificationException.
 */
public class ClassificationException extends Exception{

	/**
	 * Instantiates a new classification exception.
	 *
	 * @param message
	 *            the message
	 * @param e
	 *            the e
	 */
	public ClassificationException(String message, Exception e){
		super(message,e);
	}
	
	/**
	 * Instantiates a new classification exception.
	 *
	 * @param e
	 *            the e
	 */
	public ClassificationException(Exception e){
		super(e);
	}
}
