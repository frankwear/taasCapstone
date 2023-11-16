package com.down2thewire;

import org.junit.jupiter.api.Test;

class BranchGeoModelGeneratorTest {

    @Test
    void generateGeoModel() {
    }

//    @Test
//    void removeAdjacentSameModeEdges() {
//        Route testRoute = createTestGraphWithUnchangedMode();
//        testRoute = GeoModelAnalyzer.removeAdjacentSameModeEdges(testRoute);
//        assertEquals(5, testRoute.wayPointLinkedList.size());
////        assertEquals(4, testRoute.edgeList.size());
//    }

    private LinearRoute createTestGraphWithUnchangedMode() {
        LinearRoute route = new LinearRoute();
        Location tempLocation = new Location(33.9228732,-84.3418493);
        LinearWayPoint v1 = route.addWaypoint(new LinearWayPoint(tempLocation));
        tempLocation = new Location(33.921227,-84.344398);
        LinearWayPoint v2 = route.addWaypoint(new LinearWayPoint(tempLocation));
        tempLocation = new Location(33.789112,-84.387383);
        LinearWayPoint v3 = route.addWaypoint(new LinearWayPoint(tempLocation));
        tempLocation = new Location(33.7892632,-84.3873414);
        LinearWayPoint v4 = route.addWaypoint(new LinearWayPoint(tempLocation));
        tempLocation = new Location(33.8082253,-84.3934548);
        LinearWayPoint v5 = route.addWaypoint(new LinearWayPoint(tempLocation ));
        tempLocation = new Location(33.8085817,-84.3943387);
        LinearWayPoint v6 = route.addWaypoint(new LinearWayPoint(tempLocation));

        // add edges
        // for testing clarity, making each vertex a separate variable

        v1.setEdge(new Edge(v1, v2, "WALKING", 271, 0.00, 347));
        v2.setEdge(new Edge(v2, v3, "TRANSIT", 900, 0.00, 17083));
        v3.setEdge(new Edge(v3, v4, "TRANSIT", 18, 0.00, 17));
        v4.setEdge(new Edge(v4, v5, "WALKING", 699, 0.00, 3083));
        v5.setEdge(new Edge(v5, v6, "TRANSIT", 103, 0.00, 121));

        return route;
    }

//    @Test
//    void removeDuplicateVertices() {
//        GeographicModel testGeoModel = createTestGraphWithDuplicateVertices();
////        testGeoModel = GeoModelAnalyzer.removeDuplicateVertices(testGeoModel);
//        assertEquals(7, testGeoModel.vertexList.size());
////        assertEquals(7, testGeoModel.edgeList.size());
//        testGeoModel.printGraph();
//    }

    public static BranchGeoModel createTestGraphWithDuplicateVertices() {
        // add vertices from two routes
        // route 1  - Walking and Transit
        BranchGeoModel graph = new BranchGeoModel();
        Location tempLocation = new Location(33.9228732,-84.3418493);
        BranchVertex v01 = graph.addVertex(33.9228732,-84.3418493);
        tempLocation = new Location(33.921227,-84.344398);
        BranchVertex v02 = graph.addVertex(33.921227,-84.344398);;
        tempLocation = new Location(33.789112,-84.387383);
        BranchVertex v03 = graph.addVertex(33.789112,-84.387383);
        tempLocation = new Location(33.7892632,-84.3873414);
        BranchVertex v04 = graph.addVertex(33.7892632,-84.3873414);
        tempLocation = new Location(33.8082253,-84.3934548);
        BranchVertex v05 = graph.addVertex(33.8082253,-84.3934548);
        tempLocation = new Location(33.8085817,-84.3943387);
        BranchVertex v06 = graph.addVertex(33.8085817,-84.3943387);

        // route 2   - Driving and Transit
        tempLocation = new Location(33.9228732,-84.3418493);
        BranchVertex v07 = graph.addVertex(33.9228732,-84.3418493);  // duplicate
        tempLocation = new Location (33.9251111,-84.3401111);
        BranchVertex v08 = graph.addVertex(33.9251111,-84.3401111);  // unique - parking lot vs. station
        tempLocation = new Location(33.921227,-84.344398);
        BranchVertex v09 = graph.addVertex(33.921227,-84.344398);
        BranchVertex v10 = graph.addVertex(33.789114,-84.387384);  // duplicate by proximity
        BranchVertex v11 = graph.addVertex(33.7892632,-84.3873414);  // duplicate
        BranchVertex v12 = graph.addVertex(33.8082253,-84.3934548);  // duplicate
        BranchVertex v13 = graph.addVertex(33.8085817,-84.3943387);  // duplicate


        // add edges from two routes
        // route 1
        v01.addEdge(v01, v02, "WALKING", 271, 0.00, 347);
        v02.addEdge(v02, v03, "TRANSIT", 900, 0.00, 17083);
        v03.addEdge(v03, v04, "WALKING", 18, 0.00, 17);
        v04.addEdge(v04, v05, "TRANSIT", 699, 0.00, 3083);
        v05.addEdge(v05, v06, "WALKING", 103, 0.00, 121);

        // route 2
        v07.addEdge(v07, v08, "DRIVING", 60, 0.00, 347);  // Unique
        v08.addEdge(v08, v09, "WALKING", 64, 0.00, 59);  // Unique
        v09.addEdge(v09, v10, "TRANSIT", 900, 0.00, 17083);  // duplicate
        v10.addEdge(v10, v11, "WALKING", 18, 0.00, 17);  // duplicate
        v11.addEdge(v11, v12, "TRANSIT", 699, 0.00, 3083);  // duplicate
        v12.addEdge(v12, v13, "WALKING", 103, 0.00, 121);  // duplicate

        return graph;
    }
}