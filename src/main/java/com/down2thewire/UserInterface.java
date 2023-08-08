package com.down2thewire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInterface {

    public UserInterface(){

        // from local/frank on a second commit
    }
    public RouteRequest getRequest() {
        RouteRequest sessionRequest = new RouteRequest();
        System.out.println("Welcome to TaaS" +
                "\nTransportation as a Service helps you find new ways to get where you want to go.\n\n");
        int userResponse = menu1Input();
        int userProfileChoice;
        UserAccount acct = new UserAccount();
        switch(userResponse) {
            case 1:
                acct.setUID("Sally");
                acct.initializeWalkBikeUser();
                sessionRequest.modePref = acct.getModePref();
                sessionRequest.setEndsDunwoodyToAmtrak();
                break;
            case 2:
                acct.setUID("Edward");
                acct.initializeRideshareUser();
                sessionRequest.modePref = acct.getModePref();
                sessionRequest.setEndsKennesawToHartsfield();
                break;
            case 3:
                acct.setUID("Fred");
                acct.initializeWalkBikeUser();
                sessionRequest.modePref = acct.getModePref();
                sessionRequest.setEndsPigNChikToBurlingtonCoatFac();
                break;
            case 4:
                acct.setUID("Missy");
                acct.initializeTransitUser();
                sessionRequest.modePref = acct.getModePref();
                sessionRequest.setEndsOfficeBarToKingQueen();
                break;
            case 5:
                System.out.println("What is your name?\n");
                acct.setUID(getStringFromConsole());
                userProfileChoice = menu2Input();
                sessionRequest.modePref = loadPreferences(userProfileChoice);
                System.out.println("\n\nWhere are you now?  (Any Google Searchable Address or location)\n");
                sessionRequest.setOrigin(getStringFromConsole());
                System.out.println("Where do you want to go?  (Destination)");
                sessionRequest.setDestination(getStringFromConsole());
                break;
            case 6:
                System.out.println("What is your name?\n");
                acct.setUID(getStringFromConsole());

                // SECTION - set mode preferences
                System.out.println("OK, " + acct.getUID() + " for each option decide which fits best for you:\n" +
                        "0 - I can't do that.  For example you can't drive without a license.\n" +
                        "1 - I guess I could do that if necessary.\n" +
                        "2 - This is really the best way to get there.\n" +
                        "3 - Just use this one.  Other options don't appeal to me.\n\n");
                System.out.println("Walking");
                acct.setWalkPref(getIntFromConsole());
                System.out.println("\nDriving");
                acct.setDrivePref(getIntFromConsole());
//                System.out.println("\nRideshare");
//                acct.setRidesharePref(getIntFromConsole());
                acct.setRidesharePref(0); // todo
//                System.out.println("\nCar Rental");
//                acct.setCarRentalPref(getIntFromConsole());
                acct.setCarRentalPref(0); // todo
                System.out.println("\nBicycling");
                acct.setBikePref(getIntFromConsole());
//                System.out.println("\nScooter");
//                acct.setScooterPref(getIntFromConsole());
                acct.setScooterPref(0); // todo
                System.out.println("\nTransit");
                acct.setTransitPref(getIntFromConsole());
//                System.out.println("\nBus");
//                acct.setBusPref(getIntFromConsole());
                acct.setBusPref(0); // todo
//                System.out.println("\nAirplane");
//                acct.setFlightPref(getIntFromConsole());
                acct.setFlightPref(0); // todo
                System.out.println("\n\n");
                sessionRequest.setModePrefFromAccount(acct);

                // SECTION - Set Enbpoints
                sessionRequest.setPriority("CHEAP");
                System.out.println("\n\nWhere are you now?  (Any Google Searchable Address or location)\n");
                sessionRequest.setOrigin(getStringFromConsole());
                System.out.println("Where do you want to go?  (Destination)");
                sessionRequest.setDestination(getStringFromConsole());
        }
        System.out.println("Ok " + acct.getUID() + ", this is your request:" +
                "\nOrigin:       " + sessionRequest.getOrigin() +
                "\nDestination:  " + sessionRequest.getDestination() +
                "\nPreferences:  " + sessionRequest.getModePrefAsList());
//todo - fix priority null
        return sessionRequest;
    }
    public int menu1Input(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int userResponse = 0;
        System.out.println("For demonstration, you have the option to select some predefined setting" +
                "\nor to enter all the settings yourself.\n\n" +
                "Please select\n");
        System.out.println("Options:\n" +
                "1 - Sally wants to go downtown and likes riding her bike but doesn't drive\n" +
                "2 - Ed just wants an Uber to the airport\n" +
                "3 - Fred wants to get some exercise biking to the grocery store\n" +
                "4 - Missy wants to the bus home after a long night of drinking\n" +
                "5 - You want to learn your own options, but don't want to spend too long setting up\n" +
                "6 - You really want to see what you can do - gonna take a minute to mock an account\n" +
                "ENTER YOUR CHOICE (1-5):  ");
        try {
            userResponse = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid Number.  Enter 1-5");
        }

        return userResponse;
    }
    public int menu2Input(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int userResponse = 0;
        System.out.println("\n\nSelect the one that best fits your travel interests:");
        System.out.println("Options:\n" +
                "1 - I just want to drive\n" +
                "2 - I need something cheap, but I don't drive\n" +
                "3 - I would love to get some exercise\n" +
                "4 - I need something quick, but I don't drive\n" +
                "ENTER YOUR CHOICE (1-5):  ");
        try {
            userResponse = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid Number.  Enter 1-5");
        }

        return userResponse;
    }
    public String getStringFromConsole(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String userAnswer;
        try {
            userAnswer = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return userAnswer;
    }
    public int[] loadPreferences(int userOption){
        UserAccount tempUA = new UserAccount();
        switch(userOption) {
            case 1:
                tempUA.initializeDriver();
                return tempUA.getModePref();
            case 2:
                tempUA.initializeTransitUser();
                return tempUA.getModePref();

            case 3:
                tempUA.initializeWalkBikeUser();
                return tempUA.getModePref();

            case 4:
                tempUA.initializeRideshareUser();
                return tempUA.getModePref();

            default:
                tempUA.initializeNoPreference();
                return tempUA.getModePref();
        }
    }
    public int getIntFromConsole(){
        int response = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            response = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid Number.  Enter 1-5");
        }

        return response;
    }
    public static void main(String[] args) {
        UserInterface tempUI = new UserInterface();
        tempUI.getRequest();
    }
}
