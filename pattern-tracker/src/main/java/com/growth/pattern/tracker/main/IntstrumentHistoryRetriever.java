package com.growth.pattern.tracker.main;


import com.oanda.fxtrade.api.*;

public class IntstrumentHistoryRetriever {

    public static void main(String[] args) throws OAException, InterruptedException {

        API api = new API();

        String login = "williec";
        String password = "";

        // Connect to FXServer
        FXClient fxclient = api.createFXGame();

        fxclient.setWithRateThread(true);

        fxclient.login(login, password);


        // inputs:

        long interval = FXClient.INTERVAL_1_DAY;
        int numTicks = 5;
        FXPair fxPair = api.createFXPair("EUR/USD");

        //////

        Object[] array = fxclient.getRateTable().getHistory(fxPair, interval, numTicks).toArray();

        for (Object o : array) {
            System.out.println(o);
        }

        //Done, quit now
        fxclient.logout();
    }
}