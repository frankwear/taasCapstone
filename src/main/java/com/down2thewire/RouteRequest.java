package com.down2thewire;

import java.util.LinkedList;

public class RouteRequest {
    // preference is 0-4 as:
    // 0 means can't - imperative not to use this mode
    // 1 means can if necessary
    // 2 means prefer is possible
    // 3 means must - imperative that this mode be used
    // initialize all preferences to "preferred", 2
    int[] modePref = {2,2,2,2,2,2,2,2,0,0};
    String origin;
    String destination;
    Vertex originVertex;
    Vertex destinationVertex;

    String priority;  //QUICK, CHEAP, FREE, COMFORTABLE, PERSONALIZED, EASY, EXERCISE
    public RouteRequest() {
    }
    public void setPriority(String p) {
        this.priority = validatePriority(p);
    }
    public static String validatePriority (String p) {
        if (p == "QUICK" || p == "CHEAP" || p == "FREE" || p == "COMFORTABLE" || p == "PERSONALIZED" || p == "EASY" || p == "EXERCISE") {
            return p;
        } else {
            System.out.println("Priority not set");
            return " ";
        }
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
    public int getDrivePref() {
        return modePref[1];
    }
    public void setDrivePref(int p) {
        this.modePref[1] = p;
    }
    public int getRidesharePref() {
        return modePref[2];
    }
    public void setRidesharePref(int p) {
        this.modePref[2] = p;
    }
    public int getCarRentalPref() {
        return modePref[3];
    }
    public void setCarRentalPref(int p) {
        this.modePref[3] = p;
    }
    public int getBikePref() {
        return modePref[4];
    }
    public void setBikePref(int p) {
        this.modePref[4] = p;
    }
    public int getScooterPref() {
        return modePref[5];
    }
    public void setScooterPref(int p) {
        this.modePref[5] = p;
    }
    public int getTransitPref() {
        return modePref[6];
    }
    public void setTransitPref(int p) {
        this.modePref[6] = p;
    }
    public int getBusPref() {
        return modePref[7];
    }
    public void setBusPref(int p) {
        this.modePref[7] = p;
    }
    public void setOriginVertex (Vertex v) {this.originVertex = v;}
    public Vertex getOriginVertex() {return this.originVertex;}
    public void setDestinationVertex (Vertex v) {this.destinationVertex = v;}
    public Vertex getDestinationVertex() {return this.destinationVertex;}

    public LinkedList<String> getModePrefAsList(){

        LinkedList<String> preferredModes = new LinkedList<>();
        for (int i = 0; i < modePref.length; i++){
            if (modePref[i] != 0){
                preferredModes.addLast(Edge.getMode(i));
            }
        }
        return preferredModes;
    }
    public String getOrigin(){
        return this.origin;
    }
    public void setOrigin(String o){
        this.origin = o;
    }
    public String getDestination(){
        return this.destination;
    }
    public void setDestination(String d){
        this.destination = d;
    }


    public void generateTransitRequestTest2() {
        UserAccount userT = new UserAccount("Thomas");
        userT.initializeTransitUser();
        setOrigin("Macys Perimeter Mall Dunwoody");
        setDestination("Piedmont Atlanta Hospital");
        setBikePref(1);  setBusPref(2);  setDrivePref(0);  setTransitPref(3);  setScooterPref(1);
        setCarRentalPref(0);  setRidesharePref(0); setWalkPref(1);
        setPriority("CHEAP");
    }
    public void setEndsDunwoodyToAmtrak(){
        setOrigin("Memphis BBQ Co., Dunwoody");
        setDestination("Amtrak Atlanta");
    }
    public void setEndsKennesawToHartsfield() {
        setOrigin("Marietta Square Marietta, GA");
        setDestination("Hartsfield International Airport");
    }
    public void setEndsPigNChikToBurlingtonCoatFac() {
        setOrigin("Pig-N-Chik BBQ, Chamblee, GA 30341");
        setDestination("Burlington, 5766 Buford Hwy, Doraville, GA 30340");
    }
    public void setEndsOfficeBarToKingQueen() {
        setOrigin("The Office Bar, 1105 W. Peachtree St, Atlanta");
        setDestination("King Queen Building, 5 Concourse Pkwy, Sandy Springs, GA 30328");
    }

    public WeightedGraph getAPIWeightedGraph (String origin, String destination, String mode) {
        // todo - complete getAPIWeightedGraph() method
        // create connector
        ApiConnector googleConnector = new ApiConnector(origin, destination, mode);
        String jsonOutput = googleConnector.saveJsonToString();
        WeightedGraph weightedGraph;
        weightedGraph = googleConnector.constructWeightedGraph(jsonOutput);
        return weightedGraph;
    }

    public WeightedGraph getAPIRouteFromVertices (Vertex origin, Vertex destination, String mode) {
        // todo - complete getAPIRouteFromVertices
        // create connector
        ApiConnector googleConnector = new ApiConnector(origin, destination, mode);
        String jsonOutput = googleConnector.saveJsonToString();
        WeightedGraph weightedGraph;
        weightedGraph = googleConnector.constructWeightedGraph(jsonOutput);
        return weightedGraph;
    }



    public void setModePrefFromAccount(UserAccount uAcct) {
        this.modePref = uAcct.modePref;
    }

}
