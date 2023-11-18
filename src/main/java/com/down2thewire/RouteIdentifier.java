package com.down2thewire;

import java.util.*;

public class RouteIdentifier {
    private BranchGeoModel  geoModel;
    private UserRouteRequest routeRequest;

    private HashMap<String, DijkstraGraph> graphList;


    public RouteIdentifier(BranchGeoModel  geoModel, UserRouteRequest routeRequest) {
        this.geoModel = geoModel;
        this.routeRequest = routeRequest;
        // Initialize Dijkstra graphs here
        this.graphList = new HashMap<>();
    }

    // Method to add an operation to the list
    public void addGraph(Boolean isOrigin, String mode, String metric, DijkstraGraph graph){
        String graphName = getKey(isOrigin, mode, metric);
        graphList.put(graphName,graph);
    }
    public void addGraph(String graphName, DijkstraGraph graph){
        graphList.put(graphName,graph);
    }

    private String getKey(Boolean fromOrigin, String mode, String metric) {
        Set<String> validModes = new HashSet<>(Arrays.asList("DRIVING", "WALKING", "BICYCLING", "TRANSIT"));
        Set<String> validMetrics = new HashSet<>(Arrays.asList("duration", "cost", "distance"));
        String tempKey = new String("");
        if (fromOrigin){
            tempKey.concat("origin");
        } else {
            tempKey.concat("destination");
        }
        if (validModes.contains(mode)) {
            tempKey.concat(mode);
        } else {
            tempKey.concat("NoMode");
        }
        if (validMetrics.contains(metric)){
            tempKey.concat(metric);
        } else {
            tempKey.concat("NoMetric");
        }
        return tempKey;
    }

    // Method to retrieve the list of known graphs
    public HashMap<String, DijkstraGraph> getGraphList(){
        return graphList;
    }



    public LinearRoute getBestRoute(String primaryMode, String secondaryMode, String metric, int maxDistanceOfSecondaryMode) {

        // Create and Save primary and secondary graphs based on origin and destination respectively.
        DijkstraGraph primaryGraph;
        DijkstraGraph secondaryGraph;
        String tempKey = getKey(Boolean.TRUE, primaryMode, metric);
        if (graphList.containsKey(tempKey)){
            primaryGraph = graphList.get(tempKey);
        } else {
            primaryGraph = new DijkstraGraph(geoModel, routeRequest, primaryMode, metric);
            primaryGraph = primaryGraph.calculateShortestPathFromSource(routeRequest.originWaypoint.getId());
            addGraph(tempKey, primaryGraph);
        }
        tempKey = getKey(Boolean.FALSE, secondaryMode, metric);
        if (graphList.containsKey(tempKey)){
            secondaryGraph = graphList.get(tempKey);
        } else {
            secondaryGraph = new DijkstraGraph(geoModel, routeRequest, secondaryMode, metric);
            secondaryGraph = secondaryGraph.calculateShortestPathFromSource(routeRequest.destinationWaypoint.getId());
            addGraph(tempKey, secondaryGraph);
        }


        HashMap<Long, Integer> nearbyNodes = secondaryGraph.getNearBy(routeRequest.destinationWaypoint.getId(), maxDistanceOfSecondaryMode);
        if (nearbyNodes.isEmpty()) {
            Long destinationId = routeRequest.destinationWaypoint.getId();
            //todo Create handling for no path to destination primaryGraph
            DijkstraNode destinationNode = primaryGraph.getNodeFromID(destinationId);
            List<DijkstraNode> shortestPath = destinationNode.getShortestPath();
            LinearRoute shortestRoute = getRouteFromPath(shortestPath, primaryMode);
            return shortestRoute;
        }



        //return dijkstraGraph.findBestRoute(routeRequest, primaryMode, secondaryMode, priority, maxDistanceOfSecondaryMode);
        LinearRoute combinedRoute = combineRoutes(primaryGraph, secondaryGraph, maxDistanceOfSecondaryMode, nearbyNodes);

        return combinedRoute;

    }

    private LinearRoute getRouteFromPath(List<DijkstraNode> shortestPath, String mode){
        LinearRoute route = new LinearRoute();
        BranchVertex currentVertex = geoModel.getVertex(shortestPath.get(0).getNodeId());
        LinearWayPoint currentWaypoint = new LinearWayPoint(currentVertex.getLocation(), currentVertex.getDescription());
        for (int i = 0; i < shortestPath.size()-1; i++){
            BranchVertex endVertex = geoModel.getVertex(shortestPath.get(i+1).getNodeId());
            LinearWayPoint endWaypoint = new LinearWayPoint(endVertex.getLocation(), endVertex.getDescription());
            Edge<BranchVertex> currentVertexEdge = currentVertex.matchEdge(endVertex, mode);
            Edge<LinearWayPoint> currenWaypointEdge = new Edge<>(currentWaypoint, endWaypoint, mode,
                    +currentVertexEdge.getDuration(), currentVertexEdge.getCost(), currentVertexEdge.getDistance());
            currentWaypoint.setEdge(currenWaypointEdge);
            route.addWaypoint(currentWaypoint);
        }
        return route;
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
        int startIndexSecondary = secondaryRoute.getWaypointIndex(lastPrimaryWayPoint);
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