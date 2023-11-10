package com.down2thewire;

import java.util.*;

public class DijkstraGraph {
    private HashSet<DijkstraNode> nodes = new HashSet<>();
    private UserRouteRequest routeRequested;


    public DijkstraGraph(BranchGeoModel gm, UserRouteRequest request, String mode){
        this.routeRequested = request;
        setNodesFromGeoModel(gm, mode);
        System.out.println("nodes created.");
    }

    public DijkstraGraph(BranchGeoModel geoModel) {
    }

    public void setNodesFromGeoModel(BranchGeoModel gm, String mode) {
        //***** add nodes without neighbors, use HashMap to associate Nodes with Vertices *****//
        Map<DijkstraNode, BranchVertex> nodeToVertexAssn = new HashMap<>();
        Map<BranchVertex, DijkstraNode> vertexToNodeAssn = new HashMap<>();
        for (int i = 0; i < gm.getVertexListSize(); i++) {
            BranchVertex tempVertex = gm.getVertex(i);
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
                    int neighborDistance = tempEdge.getDistance();
                    loopNode.addNeighbor(neighborNode, neighborDistance);
                }
            }
        }
    }



    private DijkstraNode getNodeFromID(Long nodeId) {
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

    public LinearRoute findBestRoute(UserRouteRequest routeRequest, String primaryMode, String secondaryMode, String priority, int maxDistanceOfSecondaryMode) {
        return null;
    }

    public LinearRoute findAlternativeRoute(UserRouteRequest routeRequest, String primaryMode, String secondaryMode, String priority, String changeAtVertex) {
        return null;
    }
}
