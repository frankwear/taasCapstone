package com.down2thewire;

import java.util.LinkedList;

public class RouteIdentifier {
    private BranchGeoModel  geoModel;
    private UserRouteRequest routeRequest;

    // Store and manage the set of Dijkstra graphs

    private DijkstraGraph dijkstraGraph;

    private LinkedList<String> operationsList;


    public RouteIdentifier(BranchGeoModel  geoModel, UserRouteRequest routeRequest) {
        this.geoModel = geoModel;
        this.routeRequest = routeRequest;
        // Initialize Dijkstra graphs here
        this.dijkstraGraph = new DijkstraGraph(geoModel);
        this.operationsList = new LinkedList<>();
    }

    // Method to add an operation to the list
    public void addOperation(String operation){
        operationsList.add(operation);
    }

    // Method to retrieve the list of operations
    public LinkedList<String> getOperationsList(){
        return operationsList;
    }



    public LinearRoute getBestRoute(String primaryMode, String secondaryMode, String priority, int maxDistanceOfSecondaryMode) {
        // Logic to determine the best route
        addOperation("calculateShortestPathFromOrigin - Primary Mode");
        DijkstraGraph primaryShortestPaths = new DijkstraGraph(geoModel, routeRequest, primaryMode);
//        transitShortestPaths = transitShortestPaths.calculateShortestPathFromSource(v01.getId());
        primaryShortestPaths = primaryShortestPaths.calculateShortestPathFromSource(routeRequest.originWaypoint.getId());


        addOperation("calculateShortestPathFromOrigin - Secondary Mode");
        DijkstraGraph secondaryShortestPaths = new DijkstraGraph(geoModel, routeRequest, secondaryMode);
//        transitShortestPaths = transitShortestPaths.calculateShortestPathFromSource(v01.getId());
        secondaryShortestPaths = secondaryShortestPaths.calculateShortestPathFromSource(routeRequest.originWaypoint.getId());
        return dijkstraGraph.findBestRoute(routeRequest, primaryMode, secondaryMode, priority, maxDistanceOfSecondaryMode);




    }

    public LinearRoute getAlternativeVertex(String primaryMode, String secondaryMode, String priority, String changeAtVertex) {
        // Logic to find an alternative route
        return dijkstraGraph.findAlternativeRoute(routeRequest, primaryMode, secondaryMode, priority, changeAtVertex);
    }

    // Additional methods as needed
}
