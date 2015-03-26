package com.growth.pattern.tracker.main;

import com.growth.common.mongo.PatternsDb;
import org.joda.time.LocalDate;

import java.net.UnknownHostException;

public class MainController {

	public static void main(String[] args) throws UnknownHostException {
		PatternsDb patternsDb = new PatternsDb();
//		patternsDb.create();

        LocalDate date = LocalDate.now();
        Integer price = 10;

        patternsDb.addDataPoint(date, price);


		System.out.println("Terminating patternizer now...");
	}

}
