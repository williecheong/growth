package com.growth.patternizer.main;

import java.net.UnknownHostException;

import com.growth.common.mongo.PatternsDb;

public class MainController {

	public static void main(String[] args) throws UnknownHostException {
		PatternsDb patternsDb = new PatternsDb();
		patternsDb.create();
		
		System.out.println("Terminating patternizer now...");
	}

}
