package com.down2thewire;

import java.util.HashMap;

public class Edge<T extends Node> {
    // todo - The Vertex in the Edge Class need to be a <generic Node> rather than Vertex or Waypoint
    private T start;
    private T end;
    private String mode;
    private Integer duration;
    private Double cost;
    private Integer distance;


    public Edge(T s, T e, String mode, Integer duration, Double cost, Integer distance) {
        this.start = s;
        this.end = e;
        this.mode = mode;
        this.duration = duration;
        this.distance = distance;
        this.cost = cost;

        //      if (cost == 0.00d) {
        //          this.cost = estimateCost();
        //     }
    }
    public Edge(){}
    public T getStart() {
        return start;
    }
    public void setStart(T v) {this.start = v;}

    public T getEnd() {
        return end;
    }
    public void setEnd(T v) {this.end = v;}

    public String getMode() {
        return this.mode;
    }
    public void setMode(String mode) { this.mode = mode;}
    static public String getModeFromInt(int i) {
        switch (i) {
            case 0:
                return "WALKING";
            case 1:
                return "BICYCLING";
            case 2:
                return "DRIVING";
            case 3:
                return "TRANSIT";
            case 4:
                return "";
            case 5:
                return "";
            case 6:
                return "";
            case 7:
                return "";
            case 8:
                return "";
            default:
                return "";
        }
    }

    public void setDuration(int duration) {this.duration = duration;}
    public Integer getDuration() {
        return this.duration;
    }

    public void setCost(double cost) {this.cost = cost;}
    public Double getCost() {
        return this.cost;
    }

    public Integer getDistance() { return this.distance;}
    public void setDistance(int distance) { this.distance = distance;}

    // 9/16/23 Created method to estimate metrics.  It only changes the metrics is they are unset or zero
    // This is necessary to estimate the distance, duration, and cost for a single use route request
    public Integer estimateDistance(){

        if (this.distance != null && this.distance != 0L) {
            return this.distance;
        }
        //** note:  Google Distance Value from DistanceMatrix is in meters, so we will match that here **//
        Double latDifferenceInDegrees = Math.abs(this.start.getLatitude() - this.end.getLatitude());  // in degrees
        // one degree lat is about 69 miles, or 111,000 meters and is fairly consistent over the globe.
        Double latDistanceInMeters = latDifferenceInDegrees * 111000.0D;

        // The longitude distance depends on the latitude.  As you go away from the equator, the distance becomes
        // shorter proportionally to cosine(latitude)
        Double lngDifferenceInDegrees = Math.abs(this.start.getLongitude() - this.end.getLongitude());
        Double lngDistanceInMeters = lngDifferenceInDegrees * 111000.0D * Math.cos(Math.toRadians(start.getLatitude()));
        Double distenceEstimate =
                Math.max(latDistanceInMeters, lngDistanceInMeters) +
                Math.min(latDistanceInMeters, lngDistanceInMeters)/1.414D; // estimating based on short leg at 45 degrees
        this.distance =  distenceEstimate.intValue();
        return this.distance;
    }

    public Integer estimateDuration(Integer distance){
        double factor;
        double offset;
        if (this.duration != null && this.duration != 0L) {
            return this.duration;
        }
        switch (this.mode) {
            // Check Google Estimates Duration for Distance give factor of .844 s/m
            case "WALKING":
                factor = .844D;
                offset = 0.0D;
                break;
            // Check Google Estimates Duration for Distance gives factor of .244 s/m
            case "BICYCLING":
                factor = .244D;
                offset = 250.0D;
                break;
            // Rough estimate of driving speed varies significantly with traffic, time of day, road type.
            // This is only a rough guideline and has a standard deviation higher than the value.  .06
            case "DRIVING":
                factor = .06D;
                offset = 420.0D;
                break;
            // Transit can not be predicted due to train/bus schedules, leave time, and all factors from driving
            default:
                factor = 0.0D;
                offset = 0.0D;
                break;
        }
        Double durationEstimateDouble = offset + (distance.doubleValue() * factor);
        Integer durationEstimate = durationEstimateDouble.intValue();
        this.duration = durationEstimate;
        return durationEstimate;
    }

    public double estimateCost(){
        if (this.cost != null && this.cost != 0D) {
            return this.duration;
        }
        if (this.mode.equals("TRANSIT")){
            this.cost = 2.50D;
            return this.cost;
        }
        if (this.mode.equals("DRIVING")){
            // AAA estimates $.64/mile https://newsroom.aaa.com/wp-content/uploads/2021/08/2021-YDC-Brochure-Live.pdf
            // US Tax allows $.655/mile  --> we will use $.65 until updated, or until we find a better way to estimate
            this.cost = .65D * this.distance.doubleValue();
            return this.cost;
        }
        // walking and bicycling have negligible cash cost
        return 0D;
    }


    public Edge clone() {
        Edge tempEdge = new Edge();
        tempEdge.start = this.start;
        tempEdge.end = this.end;
        tempEdge.duration = this.duration;
        tempEdge.distance = this.distance;
        tempEdge.cost = this.cost;
        tempEdge.mode = this.mode;
        return tempEdge;
    }

    public Node getNeighbor() {
        return this.end;
    }



}
