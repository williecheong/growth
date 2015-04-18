package com.growth.pattern.tracker.main;


import com.growth.common.mongo.PatternsDb;
import com.growth.common.objects.GrowthCandlePoint;
import com.oanda.fxtrade.api.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class IntstrumentHistoryRetriever {

    static String login = "williec";
    static String password = "";

    static API api = new API();
    // Connect to FXServer
    static FXClient fxclient = api.createFXGame();



    public static void main(String[] args) throws OAException, InterruptedException, IOException {

        // start things up
        fxclient.setWithRateThread(true);
        fxclient.login(login, password);


        // inputs:

        // eventually we want to use the smallest interval (5 seconds)

        long interval = FXClient.INTERVAL_1_DAY;
//        int numTicks = 5;
        FXPair fxPair = api.createFXPair("EUR/USD");

        // Something is weird with DateTime and the millis that Oanda uses
        DateTime periodStart = new DateTime(1388534400000l); // 2014
        DateTime periodEnd = new DateTime(1420070400000l); // 2015
        // ends up going 31st of Dec to 31st of Dec

        System.out.println(DateTimeUtils.currentTimeMillis());
        System.out.println(periodStart.toString());
        System.out.println(periodEnd.toString());

        //////

//        Object[] array = fxclient.getRateTable().getHistory(fxPair, interval, numTicks).toArray();

        // getCandles(FXPair pair, long intervalInMillisec, int numberOfCandles, long startTime, long endTime)

        Object[] resultArray = fxclient.getRateTable().getCandles(fxPair, interval, 0,
                periodStart.getMillis() / 1000, periodEnd.getMillis() / 1000).toArray();

        // not accounting for leap years, but fuck it
        // diving by 1000 to convert from millis to seconds (for epoch)

        List<GrowthCandlePoint> candlePoints = new ArrayList<>();

        for (Object o : resultArray) {
            CandlePoint candlePoint = (CandlePoint) o;
            candlePoints.add(new GrowthCandlePoint(candlePoint));
        }
        System.out.println("# of candle points " + candlePoints.size());


        saveYearCandleStickData(periodStart, candlePoints);

        List<GrowthCandlePoint> someCandles = loadCandleStickDataForYear(periodEnd);


        //Done, quit now
        fxclient.logout();
    }


    public static void saveYearCandleStickData(DateTime periodStart, List<GrowthCandlePoint> candlePoints) throws UnknownHostException {
        // open db connection
        PatternsDb patternsDb = new PatternsDb();
        patternsDb.insertYearCandleStickData(periodStart, candlePoints);
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



//        Object[] resultArray = fxclient.getRateTable().getCandles(fxPair, interval, 0,
//                periodStart.getMillis() / 1000, periodEnd.getMillis() / 1000).toArray();

    }


}