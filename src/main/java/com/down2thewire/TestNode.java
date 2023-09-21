package com.down2thewire;

import java.util.*;

public class TestNode {
    private String name;

    private List<TestNode> shortestPath = new LinkedList<>();

    private Integer distance = Integer.MAX_VALUE;

    Map<TestNode, Integer> adjacentNodes = new HashMap<>();

    public void addDestination(TestNode destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public TestNode(String name) {
        this.name = name;
    }
    public void setDistance (Integer d){
        this.distance = d;
    }

    public Integer getDistance() {
        return this.distance;
    }

    public List<TestNode> getShortestPath() {
        return this.shortestPath;
    }

    public void setShortestPath(LinkedList<TestNode> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Map<TestNode, Integer> getAdjacentNodes() {
        return this.adjacentNodes;
    }

    //    public Map<TestNode, Integer>


    // getters and setters
}