package com.growth.rulegenerator.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.growth.common.models.MathModel;

public class MainController {
	public static void main(String[] args) {
		final ScheduledExecutorService runner = Executors.newScheduledThreadPool(1);
		runner.scheduleAtFixedRate(() -> MathModel.printStuff(), 0, 300, TimeUnit.MILLISECONDS);

//		while ( true ) {
//			final Data data = oandaApi.receive();
//			if ( data == null ) {
//				TimeUnit.MILLISECONDS.sleep(10);
//			}
//			compute(data);
//		}
	}
}
