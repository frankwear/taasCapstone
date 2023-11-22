package com.down2thewire;

public class DockerTest {

    public static void main(String[] args){
        BranchGeoModel graph = new BranchGeoModel();
//        Location tempLocation = new Location(33.9228732,-84.3418493);
        BranchVertex v01 = graph.addVertex(33.9228732,-84.3418493);
//        tempLocation = new Location(33.921227,-84.344398);
        BranchVertex v02 = graph.addVertex(33.921227,-84.344398);;
//        tempLocation = new Location(33.789112,-84.387383);
        BranchVertex v03 = graph.addVertex(33.789112,-84.387383);
//        tempLocation = new Location(33.7892632,-84.3873414);
        BranchVertex v04 = graph.addVertex(33.7892632,-84.3873414);
//        tempLocation = new Location(33.8082253,-84.3934548);
        BranchVertex v05 = graph.addVertex(33.8082253,-84.3934548);
//        tempLocation = new Location(33.8085817,-84.3943387);
        BranchVertex v06 = graph.addVertex(33.8085817,-84.3943387);

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

        DijkstraGraph transitShortestPaths = new DijkstraGraph(graph, userRouteRequest, "TRANSIT", "distance", v01.getId());
        DijkstraGraph walkingShortestPaths = new DijkstraGraph(graph, userRouteRequest, "WALKING", "distance", v06.getId());

        graph.printGraph();

        System.out.println("done");
    }

}
