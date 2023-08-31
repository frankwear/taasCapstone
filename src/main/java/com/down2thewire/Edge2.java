package com.down2thewire;

public class Edge2 <T extends Node> {
    // todo - The Vertex in the Edge Class need to be a <generic Node> rather than Vertex or Waypoint
    T start;
    T end;
    String mode;
    Integer duration;
    Double cost;
    Integer distance;


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

    public T getEnd() {
        return end;
    }

    public String getMode() {
        return mode;
    }

    public Integer getDuration() {
        return duration;
    }

    public double getCost() {
        return cost;
    }



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

    public String getMode() {
        return mode;
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

    static public int getMode(String mode) {
        switch (mode) {
            case "walking":
                return 0;
            case "driving":
                return 1;
            case "rideshare":
                return 2;
            case "carrental":
                return 3;
            case "bicycling":
                return 4;
            case "scooter":
                return 5;
            case "transit":
                return 6;
            case "bus":
                return 7;
            case "airplane":
                return 8;
            default:
                return -1;
        }
    }

    static public String getMode(int i) {
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

    public Vertex getEnd() {
        return end;
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
