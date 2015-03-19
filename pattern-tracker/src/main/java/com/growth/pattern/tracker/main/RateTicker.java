package com.growth.pattern.tracker.main;

import com.oanda.fxtrade.api.*;


/**
 * A simple event model example which prints all incoming rate ticks, and
 * watches a specific pair for tick changes
 *
 * @author Chris MacGregor
 */
public class RateTicker {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: RateTicker [username] [password]");
            System.exit(1);
        }
        // Connect to FXServer
        FXClient fxclient = new FXGame();
        FXClient fxclient = new API.createFXTrade();

        System.out.print("Logging in as \"" + args[0] + "\"...");
        fxclient.setWithRateThread(true);
        try {
            fxclient.login(args[0], args[1]);
        } catch (SessionException e) {
            System.exit(1);
        } catch (InvalidUserException e) {
            System.exit(1);
        } catch (InvalidPasswordException e) {
            System.exit(1);
        }

        //Register rate ticker event
        System.out.print("login complete. Registering listeners...");
        Ticker t = new Ticker();
        try {
            fxclient.getRateTable().getEventManager().add(t);
        } catch (SessionException e) {
            fxclient.logout();
            System.exit(1);
        }

        //Register pair watcher event
        PairWatch pw = new PairWatch("EUR/USD");
        try {
            fxclient.getRateTable().getEventManager().add(pw);
        } catch (SessionException e) {
            fxclient.logout();
            System.exit(1);
        }

        //Sleep for a minute, let it run
        System.out.println("done. Printing rate data for one minute...");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
        } //Interrupted, just keep running

        System.out.println("One minute elapsed.  Logging out and exiting.");
        //Done, quit now
        fxclient.logout();
    }
}

class Ticker extends FXRateEvent {
    // No key set and no match implemented; this event matches all RateEventInfos

    public void handle(FXEventInfo EI, FXEventManager EM) {
        //Just print the tick
        FXRateEventInfo REI = (FXRateEventInfo) EI;
        System.out.println(REI.getPair() + ":" + REI.getTick());
    }
}

class PairWatch extends FXRateEvent {
    public PairWatch(String s) {
        super(s);
    } //Watch for rates for the given pair

    public void handle(FXEventInfo EI, FXEventManager EM) {
        FXRateEventInfo REI = (FXRateEventInfo) EI;
        FXTick currTick = REI.getTick();
        if (lastTick == null) // Init the tick value if no previous one is available
        {
            lastTick = REI.getTick();
            return;
        } else //Compare the current and previous tick values
        {
            if (currTick.getBid() > lastTick.getBid())
                System.out.println(REI.getPair() + " has gone up");
            else if (currTick.getBid() < lastTick.getBid())
                System.out.println(REI.getPair() + " has gone down");
            else
                System.out.println(REI.getPair() + " hasn't changed");
            lastTick = currTick;
        }
    }

    FXTick lastTick;
}