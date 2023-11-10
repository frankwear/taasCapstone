package com.down2thewire;

import java.util.LinkedList;

public class LinearProposedRoute {
    private LinkedList<LinearWayPoint> waypoints;
    private LinkedList<Edge> edges;

    public LinearProposedRoute(LinearWayPoint origin, LinearWayPoint change, LinearWayPoint destination) {
        waypoints = new LinkedList<>();
        edges = new LinkedList<>();

        waypoints.add(origin); // Add origin
        waypoints.add(change); // Add waypoint where mode change occurs
        waypoints.add(destination); // Add destination

        // Assuming the edge is between origin and change, and change and destination
        edges.add(origin.getEdge()); // Add edge from origin to change
        edges.add(change.getEdge()); // Add edge from change to destination
    }

    // Getters and setters for waypoints and edges
    public LinkedList<LinearWayPoint> getWaypoints() {
        return waypoints;
    }

    public LinkedList<Edge> getEdges() {
        return edges;
    }
}
