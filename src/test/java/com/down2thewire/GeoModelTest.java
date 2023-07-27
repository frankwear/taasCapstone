package com.down2thewire;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoModelTest {

    @Test
    void generateGeoModel() {
    }

    @Test
    void removeAdjacentSameModeEdges() {
        WeightedGraph testRoute = createTestGraphWithUnchangedMode();
        testRoute = GeoModel.removeAdjacentSameModeEdges(testRoute);
        assertEquals(5, testRoute.vertexList.size());
        assertEquals(4, testRoute.edgeList.size());
    }

    private WeightedGraph createTestGraphWithUnchangedMode() {
        WeightedGraph graph = new WeightedGraph();
        WeightedGraph.Vertex v1 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.9228732,-84.3418493), "Macys - Perimeter Mall"));
        WeightedGraph.Vertex v2 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.921227,-84.344398), "Rail stop - Dunwoody Marta Station"));
        WeightedGraph.Vertex v3 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.789112,-84.387383), "Rail stop - Arts Center Marta Station"));
        WeightedGraph.Vertex v4 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.7892632,-84.3873414), "Bus stop - Arts Center Marta Station"));
        WeightedGraph.Vertex v5 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.8082253,-84.3934548), "Bus stop - Peachtree Rd at Collier Rd"));
        WeightedGraph.Vertex v6 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.8085817,-84.3943387), "Piedmont Hospital - Peachtree Rd"));


        // add edges
        // for testing clarity, making each vertex a separate variable

        graph.addEdge(v1, v2, "WALKING", 271, 0.00, 347);
        graph.addEdge(v2, v3, "TRANSIT", 900, 0.00, 17083);
        graph.addEdge(v3, v4, "TRANSIT", 18, 0.00, 17);
        graph.addEdge(v4, v5, "WALKING", 699, 0.00, 3083);
        graph.addEdge(v5, v6, "TRANSIT", 103, 0.00, 121);

        return graph;
    }

    @Test
    void removeDuplicateVertices() {
        WeightedGraph testGeoModel = createTestGraphWithDuplicateVertices();
        testGeoModel = GeoModel.removeDuplicateVertices(testGeoModel);
        assertEquals(7, testGeoModel.vertexList.size());
        assertEquals(7, testGeoModel.edgeList.size());
        testGeoModel.printGraph();
    }

    private WeightedGraph createTestGraphWithDuplicateVertices() {
        // add vertices from two routes
        // route 1  - Walking and Transit
        WeightedGraph graph = new WeightedGraph();
        WeightedGraph.Vertex v01 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.9228732,-84.3418493), "Macys - Perimeter Mall"));
        WeightedGraph.Vertex v02 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.921227,-84.344398), "Rail stop - Dunwoody Marta Station"));
        WeightedGraph.Vertex v03 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.789112,-84.387383), "Rail stop - Arts Center Marta Station"));
        WeightedGraph.Vertex v04 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.7892632,-84.3873414), "Bus stop - Arts Center Marta Station"));
        WeightedGraph.Vertex v05 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.8082253,-84.3934548), "Bus stop - Peachtree Rd at Collier Rd"));
        WeightedGraph.Vertex v06 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.8085817,-84.3943387), "Piedmont Hospital - Peachtree Rd"));

        // route 2   - Driving and Transit
        WeightedGraph.Vertex v07 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.9228732,-84.3418493), "Macys - Perimeter Mall"));  // duplicate
        WeightedGraph.Vertex v08 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.9251111,-84.3401111), "Parking Lot - Dunwoody Marta Station"));  // unique - parking lot vs. station
        WeightedGraph.Vertex v09 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.921227,-84.344398), "Rail stop - Dunwoody Marta Station"));  // duplicate
        WeightedGraph.Vertex v10 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.789114,-84.387384), "Rail stop - Arts Center Marta Station"));  // duplicate by proximity
        WeightedGraph.Vertex v11 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.7892632,-84.3873414), "Bus stop - Arts Center Marta Station"));  // duplicate
        WeightedGraph.Vertex v12 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.8082253,-84.3934548), "Bus stop - Peachtree Rd at Collier Rd"));  // duplicate
        WeightedGraph.Vertex v13 = graph.addVertex(new WeightedGraph.Vertex(new Location(33.8085817,-84.3943387), "Piedmont Hospital - Peachtree Rd"));  // duplicate


        // add edges from two routes
        // route 1
        graph.addEdge(v01, v02, "WALKING", 271, 0.00, 347);
        graph.addEdge(v02, v03, "TRANSIT", 900, 0.00, 17083);
        graph.addEdge(v03, v04, "WALKING", 18, 0.00, 17);
        graph.addEdge(v04, v05, "TRANSIT", 699, 0.00, 3083);
        graph.addEdge(v05, v06, "WALKING", 103, 0.00, 121);

        // route 2
        graph.addEdge(v07, v08, "DRIVING", 60, 0.00, 347);  // Unique
        graph.addEdge(v08, v09, "WALKING", 64, 0.00, 59);  // Unique
        graph.addEdge(v09, v10, "TRANSIT", 900, 0.00, 17083);  // duplicate
        graph.addEdge(v10, v11, "WALKING", 18, 0.00, 17);  // duplicate
        graph.addEdge(v11, v12, "TRANSIT", 699, 0.00, 3083);  // duplicate
        graph.addEdge(v12, v13, "WALKING", 103, 0.00, 121);  // duplicate

        return graph;
    }
}