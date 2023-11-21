package com.down2thewire;

import java.util.*;

public class DijkstraGraph {
    private HashSet<DijkstraNode> nodes = new HashSet<>();
    private UserRouteRequest routeRequested;


    public DijkstraGraph(BranchGeoModel gm, UserRouteRequest request, String mode, String metric){
        this.routeRequested = request;
        setNodesFromGeoModel(gm, mode, metric);
        System.out.println("nodes created.");
    }

//    public  LinkedList <DijkstraNode> getNearBy(Long){
//        Iterator<DijkstraNode> iterator = nodes.iterator();
//        while (iterator.hasNext()) {
//            DijkstraNode node = iterator.next();
//        }
//    }

    private void setNodesFromGeoModel(BranchGeoModel gm, String mode, String metric) {
        //***** add nodes without neighbors, use HashMap to associate Nodes with Vertices *****//
        Map<DijkstraNode, BranchVertex> nodeToVertexAssn = new HashMap<>();
        Map<BranchVertex, DijkstraNode> vertexToNodeAssn = new HashMap<>();
        for (int i = 0; i < gm.getVertexListSize(); i++) {
            BranchVertex tempVertex = gm.getVertex(i);
///// creating new nodes for each instance avoids coupling between Dijkstra Graphs using the same geomodel /////
            DijkstraNode tempNode = new DijkstraNode(tempVertex.getId());
            nodeToVertexAssn.put(tempNode, tempVertex);
            vertexToNodeAssn.put(tempVertex, tempNode);
            this.nodes.add(tempNode);
        }

        //***** add neighbors to nodes *****//
        // Loop through each vertex (and related node) creating and adding the edges
        for (DijkstraNode loopNode : nodes) {
            BranchVertex associatedVertex = nodeToVertexAssn.get(loopNode);
            Integer edgeListSize = associatedVertex.getEdgeListSize();

            // loop through vertex outgoingEdges and add each to the node
            for (int i = 0; i < edgeListSize; i++) {
                Edge<BranchVertex> tempEdge = associatedVertex.getEdge(i);
                if(tempEdge.getMode().equals(mode)) {
                    BranchVertex tempNeighborVert = tempEdge.getEnd();
                    DijkstraNode neighborNode = vertexToNodeAssn.get(tempNeighborVert);
                    int neighborMetric;
                    if (metric.equals("distance")){
                        neighborMetric = tempEdge.getDistance();
                    } else if (metric.equals("duration")){
                        neighborMetric = tempEdge.getDuration();
                    } else if (metric.equals("cost")) {
                        double dCost = tempEdge.getCost()*100.0;
                        int iCost = (int) dCost;
                        neighborMetric = iCost;
                    } else {
                        neighborMetric = tempEdge.getDistance();
                        System.out.println("Error - method DijkstraGraph.setNodesFromGeomodel calls for an invalid\n +" +
                                "metric.  Metric set to distance.");
                    }
                    loopNode.addNeighbor(neighborNode, neighborMetric);
                }
            }
        }
    }

    public HashMap<Long, Integer> getNearBy(Long destinationId, int maxDistanceOfSecondaryMode) {
        HashMap<Long, Integer> nearbyNodes = new HashMap<>();

        for (DijkstraNode node : this.nodes) {
            int distanceToDestination = node.getDistance();
            if (distanceToDestination <= maxDistanceOfSecondaryMode) {
                nearbyNodes.put(node.getNodeId(), node.getDistance());
            }
        }
        return nearbyNodes;
    }

    public DijkstraNode getNodeFromID(Long nodeId) {
        for(DijkstraNode loopNode: nodes){
            if (loopNode.getNodeId().equals(nodeId)){
                return loopNode;
            }
        }
        System.out.println("Didn't find node in list of nodes for graph");
        return new DijkstraNode(0l);
    }

    // getters and setters

    public DijkstraGraph calculateShortestPathFromSource(Long nodeId) {
        DijkstraNode source = getNodeFromID(nodeId);
        source.setDistance(0);

        Set<DijkstraNode> settleDijkstraNodes = new HashSet<>();
        Set<DijkstraNode> unsettleDijkstraNodes = new HashSet<>();

        unsettleDijkstraNodes.add(source);

        while (unsettleDijkstraNodes.size() != 0) {
            DijkstraNode currentNode = getLowestDistanceNode(unsettleDijkstraNodes);
            unsettleDijkstraNodes.remove(currentNode);
            for (Map.Entry< DijkstraNode, Integer> adjacencyPair:
                    currentNode.getNeighbors().entrySet()) {
                DijkstraNode adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settleDijkstraNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettleDijkstraNodes.add(adjacentNode);
                }
            }
            settleDijkstraNodes.add(currentNode);
        }
        return this;
    }
    private static DijkstraNode getLowestDistanceNode(Set< DijkstraNode > unsettleDijkstraNodes) {
        DijkstraNode lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (DijkstraNode node: unsettleDijkstraNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }
    private static void calculateMinimumDistance(DijkstraNode evaluationNode,
                                                 Integer edgeWeight, DijkstraNode sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeight < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeight);
            LinkedList<DijkstraNode> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    // Method to get the LinearRoute from the calculated shortest path
    public LinearRoute getLinearRoute(Long destinationId) {
        LinearRoute route = new LinearRoute();
        DijkstraNode destinationNode = getNodeFromID(destinationId);

        // Traverse the shortest path backwards from destination to source
        LinkedList<DijkstraNode> path = (LinkedList<DijkstraNode>) destinationNode.getShortestPath();
        for (DijkstraNode node : path) {
            // Convert DijkstraNode to LinearWayPoint and add to the route
            LinearWayPoint wayPoint = convertNodeToWayPoint(node);
            route.addWaypoint(wayPoint);
        }

        // Add the destination as the last waypoint
        route.addWaypoint(convertNodeToWayPoint(destinationNode));
        return route;
    }

    // Helper method to convert a DijkstraNode to a LinearWayPoint
//    public LinearWayPoint convertNodeToWayPoint(DijkstraNode node) {
//        // Implement the logic to convert a DijkstraNode to a LinearWayPoint
//        // This might involve fetching more information based on the node's ID or other properties
//        return new LinearWayPoint(node.getNodeId(), location);
//    }
    public LinearWayPoint convertNodeToWayPoint(DijkstraNode node) {
        // todo DijkstraGraph.convertNodeToWaypoint() may not be functional
        // Fetch the Location object corresponding to the DijkstraNode
        Location location = getLocationForNode(node); // Implement this method based on your data model

        // Optional: Determine a waypoint name based on the DijkstraNode
        String waypointName = getWaypointNameForNode(node); // Implement this method or use a default value

        // Create a LinearWayPoint
        LinearWayPoint wayPoint = new LinearWayPoint(location, waypointName);

        // Optional: If you need to set an edge to the waypoint
        Edge edge = getEdgeForNode(node); // Implement this if necessary
        if (edge != null) {
            wayPoint.setEdge(edge);
        }

        return wayPoint;
    }

    // Placeholder methods for fetching Location, name, and Edge
    private Location getLocationForNode(DijkstraNode node) {
        // Implement the logic to get a Location based on the node's ID or properties
        return new Location(0.0, 0.0);
    }

    private String getWaypointNameForNode(DijkstraNode node) {
        // Implement logic to determine waypoint name or return a default value
        return "Waypoint " + node.getNodeId();
    }

    private Edge getEdgeForNode(DijkstraNode node) {
        // todo complete method DijkstraGraph.getEdgeForNode() or delete
        // Implement logic to get an Edge related to the node, if necessary
        return null; // or a valid Edge
    }

}