/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu;

// TODO: Auto-generated Javadoc
/**
 * The Class LearnerException.
 */
public class LearnerException extends Exception{
	
	/**
	 * Instantiates a new learner exception.
	 *
	 * @param message
	 *            the message
	 * @param t
	 *            the t
	 */
	public LearnerException(String message, Throwable t){
		super(message,t);
	}
	
	/**
	 * Instantiates a new learner exception.
	 *
	 * @param e
	 *            the e
	 */
	public LearnerException(Exception e){
		super(e);
	}

}
