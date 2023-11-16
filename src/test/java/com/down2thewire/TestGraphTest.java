package com.down2thewire;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestGraphTest {

    @Test
    void addNode() {
    }

    @Test
    void calculateShortestPathFromSource() {
    }

    @Test
    void testGraphClass() {
        TestNode nodeA = new TestNode("A");
        TestNode nodeB = new TestNode("B");
        TestNode nodeC = new TestNode("C");
        TestNode nodeD = new TestNode("D");
        TestNode nodeE = new TestNode("E");
        TestNode nodeF = new TestNode("F");

        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);

        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);

        nodeC.addDestination(nodeE, 10);

        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);

        nodeF.addDestination(nodeE, 5);

        TestGraph graph = new TestGraph();
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);

        graph = TestGraph.calculateShortestPathFromSource(graph, nodeA);
    }
}