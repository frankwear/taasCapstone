package com.down2thewire;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class TestGraph {
    private Set<TestNode> nodes = new HashSet<>();

    public void addNode(TestNode nodeA) {
        nodes.add(nodeA);
    }

    // getters and setters

    public static TestGraph calculateShortestPathFromSource(TestGraph graph, TestNode source) {
        source.setDistance(0);

        Set<TestNode> settledNodes = new HashSet<>();
        Set<TestNode> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            TestNode currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry< TestNode, Integer> adjacencyPair:
                    currentNode.getAdjacentNodes().entrySet()) {
                TestNode adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }
    private static TestNode getLowestDistanceNode(Set< TestNode > unsettledNodes) {
        TestNode lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (TestNode node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }
    private static void calculateMinimumDistance(TestNode evaluationNode,
                                                 Integer edgeWeigh, TestNode sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<TestNode> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

}

