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
        String graphName = createMapKey(isOrigin, mode, metric);
        graphList.put(graphName,graph);
    }
    public void addGraph(String graphName, DijkstraGraph graph){
        graphList.put(graphName,graph);
    }

    public String createMapKey(Boolean fromOrigin, String mode, String metric) {
        Set<String> validModes = new HashSet<>(Arrays.asList("DRIVING", "WALKING", "BICYCLING", "TRANSIT"));
        Set<String> validMetrics = new HashSet<>(Arrays.asList("duration", "cost", "distance"));
        String tempKey = new String("");
        if (fromOrigin){
            tempKey = "origin";
        } else {
            tempKey = "destination";
        }
        if (validModes.contains(mode)) {
            tempKey = tempKey.concat(mode);
        } else {
            tempKey = tempKey.concat("NoMode");
        }
        if (validMetrics.contains(metric)){
            tempKey = tempKey.concat(metric);
        } else {
            tempKey = tempKey.concat("NoMetric");
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
        primaryGraph = discoverGraph(Boolean.TRUE, primaryMode, metric);
        secondaryGraph = discoverGraph(Boolean.FALSE, secondaryMode, metric);


        HashMap<Long, Integer> nearbyNodes = secondaryGraph.getNearBy(routeRequest.destinationWaypoint.getId(), maxDistanceOfSecondaryMode);
        if (nearbyNodes.isEmpty()) {
            LinearRoute route = getRouteToNode(primaryGraph, routeRequest.destinationWaypoint.getId(), primaryMode);
            if (route.wayPointLinkedList.isEmpty()){
                System.out.println("No nearby node found Found.  Need to query API.");
            }
            return route;
        }

        Long closestNodeId = findClosestSharedNode(nearbyNodes, primaryGraph);
        if (closestNodeId.equals(0L)){  // because there is no matching ID
            LinearRoute route = getRouteToNode(primaryGraph, routeRequest.destinationWaypoint.getId(), primaryMode);
            if (route.wayPointLinkedList.isEmpty()){
                System.out.println("No Route Found.  Need to query API");
            }
            return route;
        } else {
            LinearRoute route = getRouteToNode(primaryGraph, closestNodeId, primaryMode);
            LinearRoute fromDestination = getRouteToNode(secondaryGraph, closestNodeId, secondaryMode);
            LinearRoute toDesination = LinearRoute.reverseRoute(fromDestination);
            // todo check edge handling to combine routes
            route.combineRoutes(toDesination);
            return route;
        }

    }

    private Long findClosestSharedNode(HashMap<Long, Integer> nodes, DijkstraGraph graph) {
        // Ticket #193 - Identify Connecting Vertex with Shortest Distance from Origin
        Long closestSharedNodeId = 0L;
        Integer distance = Integer.MAX_VALUE;
        for (Long nodeID: nodes.keySet()) {

            // getNodeFromId returns a node with ID 0 if the node doesn't exist in graph.
            if (!graph.getNodeFromID(nodeID).getNodeId().equals(0L)) {
                if (graph.getNodeFromID(nodeID).getDistance() < distance) {
                    distance = graph.getNodeFromID(nodeID).getDistance();
                    closestSharedNodeId = nodeID;
                }
            }
        }
        return closestSharedNodeId;
    }

    private DijkstraGraph discoverGraph(Boolean fromOrigin, String mode, String metric) {
        DijkstraGraph graph;
        String tempKey = createMapKey(fromOrigin, mode, metric);
        if (graphList.containsKey(tempKey)){
            graph = graphList.get(tempKey);
        } else {
            if (fromOrigin) {
                graph = new DijkstraGraph(geoModel, routeRequest, mode, metric, routeRequest.originWaypoint.wayPointID);
            } else {
                graph = new DijkstraGraph(geoModel, routeRequest, mode, metric, routeRequest.destinationWaypoint.wayPointID);
            }
            addGraph(tempKey, graph);
        }
        return graph;
    }

    private LinearRoute getRouteToNode(DijkstraGraph graph, Long id, String mode) {
        DijkstraNode destinationNode = graph.getNodeFromID(id);
        List<DijkstraNode> shortestPath = destinationNode.getShortestPath();
        if (shortestPath.isEmpty()){
            // todo handling for no path found - get direct route from API
            return new LinearRoute();
        }
        LinearRoute shortestRoute = getRouteFromPath(shortestPath, mode, destinationNode.getNodeId());
        return shortestRoute;
    }

    private LinearRoute getRouteFromPath(List<DijkstraNode> shortestPath, String mode, Long destinationId){
        LinearRoute route = new LinearRoute();
        BranchVertex currentVertex = geoModel.getVertex(shortestPath.get(0).getNodeId());
        LinearWayPoint currentWaypoint = new LinearWayPoint(currentVertex.getLocation(), currentVertex.getDescription());
        route.addWaypoint(currentWaypoint);
        for (int i = 0; i < shortestPath.size()-1; i++){
            BranchVertex endVertex = geoModel.getVertex(shortestPath.get(i+1).getNodeId());
            LinearWayPoint endWaypoint = new LinearWayPoint(endVertex.getLocation(), endVertex.getDescription());
            Edge<BranchVertex> currentVertexEdge = currentVertex.matchEdge(endVertex, mode);
            Edge<LinearWayPoint> currentWaypointEdge = new Edge<>(
                    currentWaypoint,
                    endWaypoint,
                    mode,
                    currentVertexEdge.getDuration(),
                    currentVertexEdge.getCost(),
                    currentVertexEdge.getDistance());
            currentWaypoint.setEdge(currentWaypointEdge);
            currentWaypoint = endWaypoint;
            route.addWaypoint(currentWaypoint);
        }

        // Dijkstra ShortestPath does not include the last vertex, so we have to add it.
        // current waypoint is the last waypoint before the destination
        BranchVertex endVertex = geoModel.getVertex(destinationId);
        LinearWayPoint endWaypoint = new LinearWayPoint(endVertex.getLocation(), endVertex.getDescription());
        Edge<BranchVertex> currentVertexEdge = currentVertex.matchEdge(endVertex, mode);
        Edge<LinearWayPoint> currentWaypointEdge = new Edge<>(
                currentWaypoint,
                endWaypoint,
                mode,
                currentVertexEdge.getDuration(),
                currentVertexEdge.getCost(),
                currentVertexEdge.getDistance());
        currentWaypoint.setEdge(currentWaypointEdge);
        route.addWaypoint(endWaypoint);
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