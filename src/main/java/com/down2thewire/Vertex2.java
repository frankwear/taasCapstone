









package com.down2thewire;

import java.util.Iterator;
import java.util.LinkedList;

public class Vertex2 extends Node {
    private Location location;
    private String description;
    private Long id;
    private LinkedList<Edge2> outgoingEdges = new LinkedList<>();
    private double tentativeDistance;

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



    public Vertex2(Location location, Long uniqueNameId) {
        super();
        this.location = location;
        this.id = uniqueNameId;
        //       this.tentativeDistance = Double.POSITIVE_INFINITY;
    }

    public Vertex2(Location location) {
        this.location = location;
        this.id = location.generateUniqueID();
//        super.location = location;
    }
    public Vertex2(WayPoint wayPoint) {
        Vertex2 vertex = new Vertex2(wayPoint.getLocation());
        vertex.id = wayPoint.getId();
        vertex.outgoingEdges.add(wayPoint.getEdge());
        vertex.description = wayPoint.getDescription();
    }

    public Location getLocation() {
        return this.location;
    }
    public Double getLongitude() {
        return this.location.getLongitude();
    }
    public Double getLatitude() {
        return this.location.getLatitude();
    }


    public void setId() {
        this.id = location.generateUniqueID();
    }
    @Override
    public Long getId() { return this.id;}



    static Vertex2 waypointToVertex(WayPoint wayPoint) {
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
        addEdge(e.getStart(), e.getEnd(), e.getMode(), e.getDuration(), e.getCost(), e.getDistance());
    }

    public void addEdge(Vertex2 start, Vertex2 end, String mode, Integer duration, double cost, Integer distance) {
        Edge2<Vertex2> tempEdge = new Edge2<>();
        tempEdge.setStart(this);
        tempEdge.setEnd(end);
        tempEdge.setMode(mode);
        tempEdge.setDuration(duration);
        tempEdge.setCost(cost);
        tempEdge.setDistance(distance);
        outgoingEdges.add(tempEdge);

        // note see next few lines for code from Route
    }
    public Edge2 getEdge(Integer index) {
        return outgoingEdges.get(index);
    }
    public LinkedList<Edge2> getOutgoingEdges(){
        return outgoingEdges;
    }

    public void printEdges(){
        Iterator<Edge2> edge2Iterator = outgoingEdges.iterator();
        for (Edge2 edge : outgoingEdges ){
            System.out.println("Destination: " + edge.getEnd().getId().toString() + "\nMode: " + edge.getMode() +
                    "\nDistance: " + edge.getDistance());
        }
//        while (edge2Iterator.hasNext()){
//            Edge2<Vertex2> tempEdge = edge2Iterator.next();
//            System.out.println("Destination: " + tempEdge.getEnd().getId().toString() + "\nMode: " + tempEdge.getMode() +
//                    "\nDistance: " + tempEdge.distance);
//        }
    }

    public int getEdgeListSize() {
        return outgoingEdges.size();
    }

    // This method is normally in preparation for deleting this vertex, so oldNeighbor would be this Vertex2
    public void updateNeighborsEdges(Vertex2 oldNeighbor, Vertex2 newNeighbor) {
        for (Edge2<Vertex2> edge  : outgoingEdges){
            if (edge.getEnd().getId().equals(oldNeighbor.getId())){
                edge.setEnd(newNeighbor);
            }
        }
    }
}

