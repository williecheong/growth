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
        try {
            fxclient.login(login, password);
        } catch (SessionException e) {
            System.exit(1);
        } catch (InvalidUserException e) {
            System.exit(1);
        } catch (InvalidPasswordException e) {
            System.exit(1);
        } catch (MultiFactorAuthenticationException e) {
            e.printStackTrace();
        }


        //Register pair watcher event


//        ArrayList<Object> array = new ArrayList<>();

        long interval = FXClient.INTERVAL_1_DAY;
        int numTicks = 5;


        FXPair fxPair = api.createFXPair("EUR/USD");

        Object[] array;


        array = fxclient.getRateTable().getHistory(fxPair, interval, numTicks).toArray();


        System.out.println(array.length);

        for (Object o : array) {
            System.out.println(o);
        }

        //Done, quit now
        fxclient.logout();
    }


}