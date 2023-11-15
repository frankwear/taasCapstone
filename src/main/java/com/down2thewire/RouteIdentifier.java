package com.down2thewire;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class RouteIdentifier {
    private BranchGeoModel  geoModel;
    private UserRouteRequest routeRequest;

    // Store and manage the set of Dijkstra graphs

    private DijkstraGraph dijkstraGraph;

    private LinkedList<DijkstraGraph> graphList;


    public RouteIdentifier(BranchGeoModel  geoModel, UserRouteRequest routeRequest) {
        this.geoModel = geoModel;
        this.routeRequest = routeRequest;
        // Initialize Dijkstra graphs here
        this.dijkstraGraph = new DijkstraGraph(geoModel, routeRequest, "defaultMode", "defaultMetric");
        this.graphList = new LinkedList<>();

    }

    // Method to add an operation to the list
    public void addGraph(DijkstraGraph graph){
        graphList.add(graph);
    }

    // Method to retrieve the list of operations
    public LinkedList<DijkstraGraph> getGraphList(){
        return graphList;
    }



    public LinearRoute getBestRoute(String primaryMode, String secondaryMode, String metric, int maxDistanceOfSecondaryMode) {
        // Logic to determine the best route
       // DijkstraGraph primaryGraph = new DijkstraGraph(geoModel, routeRequest, primaryMode, metric);
       // primaryGraph = primaryGraph.calculateShortestPathFromSource(routeRequest.originWaypoint.getId());

        DijkstraGraph primaryShortestPaths = new DijkstraGraph(geoModel, routeRequest, primaryMode, metric);
//        transitShortestPaths = transitShortestPaths.calculateShortestPathFromSource(v01.getId());
        primaryShortestPaths = primaryShortestPaths.calculateShortestPathFromSource(routeRequest.originWaypoint.getId());
        addGraph(primaryShortestPaths);


        DijkstraGraph secondaryShortestPaths = new DijkstraGraph(geoModel, routeRequest, secondaryMode, metric);
//        transitShortestPaths = transitShortestPaths.calculateShortestPathFromSource(v01.getId());
        secondaryShortestPaths = secondaryShortestPaths.calculateShortestPathFromSource(routeRequest.originWaypoint.getId());
        addGraph(secondaryShortestPaths);
        LinkedList<DijkstraNode> nearbyNodes = secondaryShortestPaths.getNearBy(routeRequest.destinationWaypoint.getId(), maxDistanceOfSecondaryMode);
        if (nearbyNodes.isEmpty()) {
            Long destinationId = routeRequest.destinationWaypoint.getId();
            //NodeId of the origin
           List<DijkstraNode> shortestPath = primaryShortestPaths.getNodeFromID(destinationId).getShortestPath();
           //LinearRoute shortestRoute = ne
            LinkedList<DijkstraNode> ProposedRoute = new LinkedList<>(shortestPath);
        }

        //return dijkstraGraph.findBestRoute(routeRequest, primaryMode, secondaryMode, priority, maxDistanceOfSecondaryMode);
        LinearRoute combinedRoute = combineRoutes(primaryShortestPaths, secondaryShortestPaths, maxDistanceOfSecondaryMode, nearbyNodes);

        return combinedRoute;

    }

    private LinearRoute combineRoutes(DijkstraGraph primaryGraph, DijkstraGraph secondaryGraph, int maxDistanceOfSecondaryMode, LinkedList<DijkstraNode> nearbyNodes) {
        // The logic here is a simple example and will need to be adjusted based on your specific requirements
        LinearRoute combinedRoute = new LinearRoute();

        // Assume that we have a method in DijkstraGraph to get the computed LinearRoute
        Long destinationId = routeRequest.destinationWaypoint.getId();
        LinearRoute primaryRoute = primaryGraph.getLinearRoute(destinationId);
        LinearRoute secondaryRoute = secondaryGraph.getLinearRoute(destinationId);

        // Add waypoints from the primary route until a condition is met
        for (LinearWayPoint waypoint : primaryRoute.wayPointLinkedList) {
            combinedRoute.addWaypoint(waypoint);
            // Check condition to switch to secondary route
            // This is an example condition, you'll need to define the actual logic
            if (calculateTotalDistance(combinedRoute) > maxDistanceOfSecondaryMode) {
                break;
            }
        }

        for (DijkstraNode node : nearbyNodes) {
            LinearWayPoint wayPoint = secondaryGraph.convertNodeToWayPoint(node);
            combinedRoute.addWaypoint(wayPoint);
        }

        // Add waypoints from the secondary route starting from where the primary left off
        LinearWayPoint lastPrimaryWayPoint = combinedRoute.wayPointLinkedList.getLast();
        int startIndexSecondary = secondaryRoute.getWaypointIndex(lastPrimaryWayPoint.getId());
        if (startIndexSecondary != -1) {
            // Start adding from the next waypoint in the secondary route to avoid duplication
            ListIterator<LinearWayPoint> iterator = secondaryRoute.wayPointLinkedList.listIterator(startIndexSecondary + 1);
            while (iterator.hasNext()) {
                combinedRoute.addWaypoint(iterator.next());
            }
        }

        return combinedRoute;
    }

    private int calculateTotalDistance(LinearRoute route) {
        // Implement the logic to calculate the total distance of a route
        // This is a placeholder for the actual calculation
        return 0; // Replace with actual calculation
    }


//    public LinearRoute getAlternativeVertex(String primaryMode, String secondaryMode, String priority, String changeAtVertex) {
//        // Logic to find an alternative route
//        return dijkstraGraph.findAlternativeRoute(routeRequest, primaryMode, secondaryMode, priority, changeAtVertex);
//    }

    // Additional methods as needed
}
