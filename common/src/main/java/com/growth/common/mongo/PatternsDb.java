package com.growth.common.mongo;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class PatternsDb extends MainDb {
	
	final protected String collectionName;
	final protected DBCollection collection;
	
	public PatternsDb() throws UnknownHostException {
		collectionName = "patterns";
		if (!super.db.collectionExists(collectionName)) {
	        super.db.createCollection(collectionName, null);
	    }
		
		collection = super.db.getCollection(collectionName);
	}
	
	public void create() {
		BasicDBObject document = new BasicDBObject("name", "MongoDB")
						        .append("type", "database")
						        .append("count", 1)
						        .append("info", new BasicDBObject("x", 203).append("y", 102));
		collection.insert(document);
	}
	
}
