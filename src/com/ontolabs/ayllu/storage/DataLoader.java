/*
 * Copyright (C) Ontolabs Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Uday Kamath <kamathuday@gmail.com> 
 */
package com.ontolabs.ayllu.storage;

import com.ontolabs.ayllu.data.DataSet;

// TODO: Auto-generated Javadoc
/**
 * The Interface DataLoader.
 */
public interface DataLoader {
	
	/**
	 * Gets the data set.
	 *
	 * @param file
	 *            the file
	 * @return the data set
	 * @throws DataLoaderException
	 *             the data loader exception
	 */
	public DataSet getDataSet(String file) throws DataLoaderException;
	
	
	/**
	 * Store data set.
	 *
	 * @param file
	 *            the file
	 * @param ds
	 *            the ds
	 * @throws DataLoaderException
	 *             the data loader exception
	 */
	public void storeDataSet(String file, DataSet ds) throws DataLoaderException;
	

}
