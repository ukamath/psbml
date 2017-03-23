package com.ontolabs.ayllu.vendor.weka;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.storage.DataLoader;
import com.ontolabs.ayllu.storage.DataLoaderException;

public class WekaDataStorage implements DataLoader{

	@Override
	public DataSet getDataSet(String file) throws DataLoaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeDataSet(String file, DataSet ds)
			throws DataLoaderException {
		WekaDataSet dataSet = (WekaDataSet)ds;
		Instances wekaInstances = dataSet.getInstances();
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			 writer.write(wekaInstances.toString());
			 writer.flush();
			 writer.close();
		}
		catch(IOException ioe){
			throw new DataLoaderException(ioe);
		}
	}

}
