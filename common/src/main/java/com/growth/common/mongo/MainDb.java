package com.growth.common.mongo;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MainDb {

	final protected String dbName;
	final protected String hostname;
	final protected int portNumber;
	final protected DB db;
	
	public MainDb() throws UnknownHostException {
		this.dbName = "peanut";		
		this.hostname = "localhost";
		this.portNumber = 27017;
		this.db = new MongoClient(hostname, portNumber).getDB(dbName);
	}
	
}
