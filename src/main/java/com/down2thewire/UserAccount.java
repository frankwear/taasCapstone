package com.down2thewire;

public class UserAccount {

    // modePref key:
    // 0 - walk
    // 1 - bike
    // 2 - drive
    // 3 - transit

    public String accountID = new String();
    Integer[] modePref = new Integer[4];
    String priority;  //QUICK, CHEAP, FREE, COMFORTABLE, PERSONALIZED, EASY, EXERCISE

    public UserAccount(String id) {
        this.accountID = id;
    }
    public UserAccount(){

    }

    public Integer[] getModePref(){ return this.modePref;};

    public void setUID(String id){
        this.accountID = id;
    }
    public String getUID(){
        return this.accountID;
    }
    public void setPriority(String p) {
        this.priority = UserRouteRequest.validatePriority(p);
    }
    public String getPriority(){
        return this.priority;
    }
    public int getWalkPref() {
        return modePref[0];
    }
    public void setWalkPref(int p) {
        this.modePref[0] = p;
    }
    public int getBikePref() {
        return modePref[1];
    }
    public void setBikePref(int p) {
        this.modePref[1] = p;
    }
    public int getDrivePref() {
        return modePref[2];
    }
    public void setDrivePref(int p) {
        this.modePref[2] = p;
    }
    public int getTransitPref() {
        return modePref[3];
    }
    public void setTransitPref(int p) {
        this.modePref[3] = p;
    }

//    public int getRidesharePref() {
//        return modePref[2];
//    }
//    public void setRidesharePref(int p) {
//        this.modePref[2] = p;
//    }
//    public int getCarRentalPref() {
//        return modePref[3];
//    }
//    public void setCarRentalPref(int p) {
//        this.modePref[3] = p;
//    }
//
//    public int getScooterPref() {
//        return modePref[5];
//    }
//    public void setScooterPref(int p) {
//        this.modePref[5] = p;
//    }
//
//    public Integer getBusPref() {
//        return modePref[7];
//    }
//    public void setBusPref(int p) {
//        this.modePref[7] = p;
//    }
//    public int getFlightPref() {return modePref[8];}
//    public void setFlightPref(int p) {this.modePref[8] = p;}
    public void initializeTransitUser(){
        setBikePref(0);
        setDrivePref(0);
        setTransitPref(3);
        setWalkPref(1);
        setPriority("CHEAP");
    }
    public void initializeFastUser() {
        setBikePref(1);
        setDrivePref(2);
        setTransitPref(1);
        setWalkPref(0);
        setPriority("QUICK");
    }
    public void initializeWalkBikeUser() {
        setBikePref(2);
        setDrivePref(0);
        setTransitPref(0);
        setWalkPref(2);
        setPriority("EXERCISE");
    }
    public void initializeRideshareUser() {
        setBikePref(0);
        setDrivePref(0);
        setTransitPref(2);
        setWalkPref(0);
        setPriority("QUICK");
    }
    public void initializeNoPreference(){
        setBikePref(0);
        setDrivePref(0);
        setTransitPref(0);
        setWalkPref(0);
        setPriority(" ");
    }


    public void initializeDriver() {
        setBikePref(0);
        setDrivePref(3);
        setTransitPref(0);
        setWalkPref(0);
        setPriority("EASY");
    }
}
