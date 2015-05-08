package com.growth.pattern.tracker.main;


import com.growth.common.mongo.PatternsDb;
import com.growth.common.objects.GrowthCandlePoint;
import com.oanda.fxtrade.api.*;

import org.joda.time.DateTime;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class IntstrumentHistoryRetriever {
	
	/************************
	 * Connection parameters */
	static API api = new API();
	static final String login = "williec";
    static final String password = "";
    static FXClient fxclient = api.createFXGame(); // Connect to FXServer
    
	/************************
	 * Retrieval parameters */    
    static final long interval = FXClient.INTERVAL_5_SEC;
    static final FXPair fxPair = api.createFXPair("EUR/USD");
    static final int monthsToGoBack = 1;
    static final int hoursToIncrement = 4;
    static final int minPatternComplexity = 8;
    static final int maxPatternComplexity = 12;
    static final int exceptionSleepInMilliseconds = 10000;
    
    public static void main(String[] args) throws OAException, InterruptedException, IOException {
        DateTime periodStart, periodEnd;
        periodStart = DateTime.now().minusMonths(monthsToGoBack);
        List<GrowthCandlePoint> candlePoints = new ArrayList<>();
        
        int iteration = 0;
        while (periodStart.isBefore(DateTime.now())) {
        	try {
            	System.out.println("Iteration "+iteration+" started...");
                
            	periodEnd = periodStart.plusHours(hoursToIncrement);
                System.out.println("Start: " + periodStart.toString());
                System.out.println("End Date: " + periodEnd.toString());

                Object[] resultArray = fxclient.getRateTable().getCandles(fxPair, interval, 0,
                        periodStart.getMillis() / 1000, periodEnd.getMillis() / 1000).toArray();

                for (Object o : resultArray) {
                    CandlePoint candlePoint = (CandlePoint) o;
                    candlePoints.add(new GrowthCandlePoint(candlePoint));
                }
                
            	System.out.println("A total of "+resultArray.length+" candles were retrieved");
            	System.out.println("Iteration "+iteration+" ended... \n");
                periodStart = periodEnd;
                iteration++;
            } catch (SessionDisconnectedException e) {
                System.out.println("Starting session for oanda api...");
            	fxclient.setWithRateThread(true);
                fxclient.login(login, password);
        	} catch (Exception e) {
        		System.out.println("Not sure what went wrong, retrying in "+exceptionSleepInMilliseconds+" milliseconds...");
        		Thread.sleep(exceptionSleepInMilliseconds);
        	}
        }
        
        System.out.println("Sorting array of "+candlePoints.size()+" candle sticks...");
        Collections.sort(candlePoints, new Comparator<CandlePoint>() {
            @Override public int compare(CandlePoint p1, CandlePoint p2) {
                DateTime p1Timestamp = new DateTime(p1.getTimestamp());
                DateTime p2Timestamp = new DateTime(p2.getTimestamp());
                
                // Sort candlesticks starting with earliest first
                if (p1Timestamp.isBefore(p2Timestamp)) {
                    return -1;
                } else if (p1Timestamp.isAfter(p2Timestamp)) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        System.out.println("End of sorting candle sticks");
        
        System.out.println("Starting pattern matching process...");
        List<String> marketChanges = new ArrayList<>();
        for (int i=0; i < candlePoints.size() ; i++) {        	
			CandlePoint thisCandle = candlePoints.get(i);
        	double open = thisCandle.getOpen();
        	double close = thisCandle.getClose();
        	double percentChange = (close - open) / open ;

			String change = "--";
        	if (percentChange > 0) {
        		change = "U1";
        	}
        	
        	if (percentChange < 0) {
        		change = "D1";
        	}
        	
        	marketChanges.add(change);
        }
        
        HashMap<String, Integer> patterns = new HashMap<>();
        for (int complexity = minPatternComplexity; complexity <= maxPatternComplexity; complexity++) {
        	for (int i=complexity-1; i < marketChanges.size(); i++) {
        		List<String> tempPattern = new ArrayList<>();
        		for (int j=0; j<complexity; j++) {
        			tempPattern.add(marketChanges.get(i-j));
        		}
        		
        		String thisPattern = String.join(",", tempPattern);
        		if (patterns.containsKey(thisPattern)) {
        			patterns.put(thisPattern, patterns.get(thisPattern) + 1);
        		} else {
        			patterns.put(thisPattern, 1);
        		}
        	}
        }
        System.out.println("Successfully found "+patterns.size()+" patterns!");
        savePatterns(patterns);
        
        fxclient.logout();
    }

    public static void savePatterns(HashMap<String, Integer> patterns) throws UnknownHostException {
        PatternsDb patternsDb = new PatternsDb(); //open DB connection
        patternsDb.insertPatternsFromRun(patterns);
        System.out.println("Inserted candle data to: " + patternsDb.getCollectionName());
    }
    
    public static void saveYearCandleStickData(DateTime periodStart, List<GrowthCandlePoint> candlePoints) throws UnknownHostException {
        // open db connection
        PatternsDb patternsDb = new PatternsDb();
        patternsDb.insertCandleStickData(periodStart, candlePoints);
        System.out.println("Inserted candle data to: " + patternsDb.getCollectionName());
    }

    public static List<GrowthCandlePoint> loadCandleStickDataForYear(DateTime yearDate) throws IOException {
        // open db connection
        PatternsDb patternsDb = new PatternsDb();
        List<GrowthCandlePoint> candles = patternsDb.loadCandleStickDataForYear(yearDate);
        System.out.println("Retrieved candle data from: " + patternsDb.getCollectionName());
        return candles;
    }

    // function to retrieve candlestick data between two dates and patch together the info
    // this is to get around Oanda's 5000 data point issue
    public static void retrieveCandleStickDataInChunks(DateTime startPeriod, DateTime endPeriod) throws SessionDisconnectedException {
        // grab date, convert to human readable format
		// Object[] resultArray = fxclient.getRateTable().getCandles(fxPair, interval, 0,
		// periodStart.getMillis() / 1000, periodEnd.getMillis() / 1000).toArray();
    }
}