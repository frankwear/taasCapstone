package com.down2thewire;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DijkstraNode {
    private Long nodeId;
    private List<DijkstraNode> shortestPath = new LinkedList<>();
    private Integer distance = Integer.MAX_VALUE;
    Map<DijkstraNode, Integer> neighbors = new HashMap<>();




    public DijkstraNode(Long nodeId){
        this.nodeId = nodeId;
    }

    public void addNeighbor(DijkstraNode destination, int distance) {
        neighbors.put(destination, distance);
    }

    public List<DijkstraNode> getShortestPath() {
        return this.shortestPath;
    }
    public void setShortestPath(LinkedList<DijkstraNode> shortestPath) {
        this.shortestPath = shortestPath;
    }
    public Map<DijkstraNode, Integer> getNeighbors() {
        return this.neighbors;
    }
    public void setDistance (Integer d){
        this.distance = d;
    }
    public Integer getDistance() {
        return this.distance;
    }
    public Long getNodeId() {
        return this.nodeId;
    }
}

