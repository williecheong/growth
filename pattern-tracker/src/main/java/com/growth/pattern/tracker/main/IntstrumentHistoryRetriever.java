package com.growth.pattern.tracker.main;


import com.growth.common.mongo.PatternsDb;
import com.growth.common.objects.GrowthCandlePoint;
import com.oanda.fxtrade.api.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import java.net.UnknownHostException;

public class IntstrumentHistoryRetriever {

    public static void main(String[] args) throws OAException, InterruptedException, UnknownHostException {

        API api = new API();

        String login = "williec";
        String password = "";

        // Connect to FXServer
        FXClient fxclient = api.createFXGame();

        fxclient.setWithRateThread(true);

        fxclient.login(login, password);


        // inputs:

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

        GrowthCandlePoint[] candlePoints = new GrowthCandlePoint[314];

        int count= 0;
        for (Object o : resultArray) {
            CandlePoint candlePoint = (CandlePoint) o;
            candlePoints[count] = new GrowthCandlePoint(candlePoint);
            count++;
        }
        System.out.println(count);


        System.out.println(candlePoints[0]);

        // open db connection
        PatternsDb patternsDb = new PatternsDb();
        patternsDb.insertYearCandleStickData(periodStart, candlePoints);
        System.out.println("Inserted Candle Data to: " + patternsDb.getCollectionName());


        //Done, quit now
        fxclient.logout();
    }
}