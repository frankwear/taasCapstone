package com.down2thewire;

import org.junit.jupiter.api.Test;

class DijkstraGraphTest {




    @Test
    void myDijkstraTest() {
        BranchGeoModel graph = new BranchGeoModel();

        BranchVertex v01 = graph.addVertex(33.9228732,-84.3418493);
        v01.setDescription("Biltmore Hotel");
        BranchVertex v02 = graph.addVertex(33.921227,-84.344398);;
        v02.setDescription("10th St Marta Station");
        BranchVertex v03 = graph.addVertex(33.789112,-84.387383);
        v03.setDescription("Zoo Atlanta");
        BranchVertex v04 = graph.addVertex(33.7892632,-84.3873414);
        v04.setDescription("Kroger - 100 Kim Rogers Ave");
        BranchVertex v05 = graph.addVertex(33.8082253,-84.3934548);
        v05.setDescription("KSU Marietta Campus");
        BranchVertex v06 = graph.addVertex(33.8085817,-84.3943387);
        v06.setDescription("Never-Never Land");

        v01.addEdge(v01, v02, "WALKING", 271, 0.00, 347);
        v01.addEdge(v01, v04, "WALKING", 271, 0.00, 500);

        v02.addEdge(v02, v01, "WALKING", 271, 0.00, 347);
        v02.addEdge(v02, v03, "TRANSIT", 900, 0.00, 17083);
        v02.addEdge(v02, v03, "WALKING", 271, 0.00, 3423);
        v02.addEdge(v02, v06, "TRANSIT", 900, 0.00, 17083);

        v03.addEdge(v03, v01, "TRANSIT", 900, 0.00, 17010);
        v03.addEdge(v03, v02, "WALKING", 900, 0.00, 17083);
        v03.addEdge(v03, v04, "WALKING", 18, 0.00, 17);
        v03.addEdge(v03, v05, "WALKING", 18, 0.00, 122);

        v04.addEdge(v04, v01, "WALKING", 18, 0.00, 100);
        v04.addEdge(v04, v05, "WALKING", 699, 0.00, 3083);
        v04.addEdge(v04, v03, "WALKING", 18, 0.00, 17);
        v04.addEdge(v04, v05, "TRANSIT", 699, 0.00, 3028);

        v05.addEdge(v05, v01, "WALKING", 103, 0.00, 121);
        v05.addEdge(v05, v02, "TRANSIT", 699, 0.00, 3060);
        v05.addEdge(v05, v04, "TRANSIT", 699, 0.00, 3083);
        v05.addEdge(v05, v06, "WALKING", 103, 0.00, 121);

        v06.addEdge(v06, v04, "WALKING", 103, 0.00, 103);
        v06.addEdge(v06, v05, "WALKING", 103, 0.00, 121);

        UserRouteRequest userRouteRequest = new UserRouteRequest();
        userRouteRequest.setOrigin("155 Peachtree Ave, Atlanta, GA");
        userRouteRequest.setDestination("Hartsfield Airport");

        DijkstraGraph transitShortestPaths = new DijkstraGraph(graph, userRouteRequest, "TRANSIT", "distance");
        transitShortestPaths = transitShortestPaths.calculateShortestPathFromSource(v02.getId());
        DijkstraGraph walkingShortestPaths = new DijkstraGraph(graph, userRouteRequest, "WALKING", "distance");
//        transitShortestPaths = transitShortestPaths.calculateShortestPathFromSource(v01.getId());
        walkingShortestPaths = walkingShortestPaths.calculateShortestPathFromSource(v04.getId());

        graph.printGraph();

        System.out.println("done");
    }
}