package com.down2thewire;

public class Edge2 <T extends Node> {
    // todo - The Vertex in the Edge Class need to be a <generic Node> rather than Vertex or Waypoint
    private T start;
    private T end;
    private String mode;
    private Integer duration;
    private Double cost;
    private Integer distance;


    public Edge2(T s, T e, String mode, Integer duration, Double cost, Integer distance) {
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
    public Edge2(){}
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
                return "walking";
            case 1:
                return "driving";
            case 2:
                return "rideshare";
            case 3:
                return "carrental";
            case 4:
                return "bicycling";
            case 5:
                return "scooter";
            case 6:
                return "transit";
            case 7:
                return "bus";
            case 8:
                return "airplane";
            default:
                return " ";
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

    /*
    private Double estimateCost() {
        // Implement your cost estimation logic here
        return 0.0d;
    }

}


    /*    public <T> Edge2(T start, T end, GNode<T> s, GNode e, String mode, Integer duration, Double cost, Integer distance) {
        T start1 = new GNode<T>;
  //      T end = new GNode<T>()
        this.start = s;
        this.start = start;
        this.end = end;
        this.mode = mode;
        this.duration = duration;
        this.distance = distance;
        this.cost = cost;
        if (cost == 0.00d) {
            this.cost = estimateCost(this);
        }
    }
    */
    /*
    public <T> GNode getStart() {

        return this.start;

    }

    public T getEnd() {

        return end;

    }
    public Edge2(){}

    public Vertex getStart() {
        return start;
    }



    public double getCost() {
        return cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public static double estimateCost(Edge e) {
        switch (e.mode) {
            case "walking":
            case "bicycling":
                return 0.00d;
            case "driving":
                return (.90d * e.distance);
            case "rideshare":
                return (10.0d + 1.60d * e.distance);
            case "carrental":
                return (90.0d + .20 * e.distance);
            case "scooter":
                return 1.0d + .39d * e.duration;
            case "transit":
            case "bus":
                return 2.50d;
            default:
                return 0.00d;
        }
    }


*/


    public Edge2 clone() {
        Edge2 tempEdge = new Edge2();
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
