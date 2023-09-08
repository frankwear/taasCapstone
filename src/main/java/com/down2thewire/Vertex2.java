









package com.down2thewire;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Vertex2 extends Node<Vertex2> implements Comparator<Vertex2> {


    Location location;
    String description;
    Long id;

    LinkedList<Edge2> outgoingEdges = new LinkedList<>();

    // 0-walk, 1-drive, 2-rideshare, 3-carRental, 4-bicycle, 5-scooter, 6-transit, 7-bus, 8-airplane, 9-unused
    /*
     * Walking is allowed in most places with stops. It is not allowed on the Interstate
     * Changing modes to/from driving requires a parking lot, and the cost of parking should be added to the final driven leg
     * Rideshare is allowed almost anywhere. In some places like the airport, there are specific pickup areas.
     * Car Rental locations - where you can rent a car, for instance, the airport car rental lots
     * Bicycle is allowed in most places with stops, but changing to/from bike means bike rental or locking rack.
     * Biking is not allowed on the Interstate
     * Scooter Rental Locations - where you can rent a scooter.
     */

    private double tentativeDistance;

    public Vertex2(Location location, Long uniqueNameId) {
        super();
        this.location = location;
        this.id = uniqueNameId;
        //       this.tentativeDistance = Double.POSITIVE_INFINITY;
    }
    public Vertex2(Location location) {
        this.location = location;
        this.id = location.generateUniqueID();
        super.location = location;
    }

    public Location getLocation (){
        return this.location;
    }
    public Vertex2(WayPoint wayPoint){
        Vertex2 vertex = new Vertex2(wayPoint.getLocation());
        vertex.id = wayPoint.getId();
        vertex.outgoingEdges.add(wayPoint.getEdge());
        vertex.description = wayPoint.getDescription();
    }
    static Vertex2 waypointToVertex(WayPoint wayPoint){
        Vertex2 vertex = new Vertex2(wayPoint.getLocation());
        vertex.id = wayPoint.getId();
 //       vertex.outgoingEdges.add(wayPoint.getEdge());
        vertex.description = wayPoint.getDescription();
        return vertex;
    }
//    public Vertex2() {
//        super();
//    }

    public void addEdge(Edge2<Vertex2> e) {
        addEdge(e.getStart(), e.getEnd(), e.getMode(), e.getDuration(), e.getCost(), e.distance);
    }

    public void addEdge(Vertex2 start, Vertex2 end, String mode, Integer duration, double cost, Integer distance) {
        Edge2<Vertex2> tempEdge = new Edge2<>();
        tempEdge.start = this;
        tempEdge.end = end;
        tempEdge.mode = mode;
        tempEdge.duration = duration;
        tempEdge.cost = cost;
        tempEdge.distance = distance;
        outgoingEdges.add(tempEdge);

        // note see next few lines for code from Route
    }
    @Override
    public int compare(Vertex2 v1, Vertex2 v2) {
        Long v1Id = v1.getId();
        Long v2Id = v2.getId();

        if (v1Id > v2Id) {
            return 1;
        } else if (v1Id < v2Id) {
            return -1;
        } else {
            return 0;
        }
    }
/*
    public Edge2 addEdge(WayPoint start, WayPoint end, String mode, Integer duration, Double cost, Integer distance)
    {

        if (isUnique(start)){
            start = addWaypoint(start);
        } else {
            start = this.wayPointLinkedList.get(findMatch(start));
        }
        if (isUnique(end)){
            end = addWaypoint(end);
        } else {
            end = this.wayPointLinkedList.get(findMatch(end));
        }
        this.edgeList.addLast(new Edge2(start, end, mode, duration, cost, distance));

        // make Vertex mode true at source and destination of the edge
        // todo - alter vertex mode list when edge is added.  Resolve
/*        int sIndex = getVertexIndex(start.vertexName);
        this.vertexList.get(sIndex).modes[Edge.getMode(mode)] = true;
        int dIndex = getVertexIndex(end.vertexName);
        this.vertexList.get(dIndex).modes[Edge.getMode(mode)] = true;
        return this.edgeList.getLast();*/

    //
}


    /*

        public Double getLongitude() {
            return this.location.getLongitude();
        }

        public Double getLatitude() {
            return this.location.getLatitude();
        }

        public void autoVertexName() {
            // Get Location Name from GoogleLocation API
            // ToDo
        }

        public void setWalk(boolean tf) {
            modes[0] = tf;
        }

        public void setCarPark(boolean tf) {
            modes[1] = tf;
        }

        public void setRideShare(boolean tf) {
            modes[2] = tf;
        }

        public void setCarRental(boolean tf) {
            modes[3] = tf;
        }

        public void setBikeRack(boolean tf) {
            modes[4] = tf;
        }

        public void setScooterRental(boolean tf) {
            modes[5] = tf;
        }

        public void setTransit(boolean tf) {
            modes[6] = tf;
        }

        public void setBusStop(boolean tf) {
            modes[7] = tf;
        }

        public boolean getWalk() {
            return modes[0];
        }

        public boolean getCarPark() {
            return modes[1];
        }

        public boolean getRideShare() {
            return modes[2];
        }

        public boolean getCarRental() {
            return modes[3];
        }

        public boolean getBikeRack() {
            return modes[4];
        }

        public boolean getScooterRental() {
            return modes[5];
        }

        public boolean getTransit() {
            return modes[6];
        }

        public boolean getBusStop() {
            return modes[7];
        }

        public double getTentativeDistance() {
            return tentativeDistance;
        }

        public void setTentativeDistance(double tentativeDistance) {
            this.tentativeDistance = tentativeDistance;
        }

        public List<Edge2> getOutgoingEdges(List<Edge2> edgeList) {

            return this.outgoingEdges;
        }

        public void addEdge(Edge2 edge){

        }

        public boolean isMatch(com.down2thewire.Vertex tempVer) {
            // match rounded to 4 decimal places - about 10 meters, so 20-meter square box around location
            if (this.location.isMatch(tempVer.location)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

    public static void main (String[] Args){
        Location l = new Location(34.5567898, -89.346566);
        Vertex2 v = new Vertex2<>(l, l.generateUniqueID());
        Location l2 = new Location(35.5567898, -88.346566);
        Vertex2 v2 = new Vertex2<>(l2, l2.generateUniqueID());
        Edge2 e = new Edge2(v, v2, "My Walk", 300, 5.00, 234);
        System.out.println("  ");
    }
*/
