package com.growth.pattern.tracker.main;

import java.net.UnknownHostException;

import com.growth.common.mongo.PatternsDb;
import com.growth.common.objects.*;

public class MainController {

	public static void main(String[] args) throws UnknownHostException {
		PatternsDb patternsDb = new PatternsDb();
		patternsDb.create();
		
		System.out.println("Terminating patternizer now...");
	}

}
