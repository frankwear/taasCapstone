package com.down2thewire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class RouteIdentifierTest {

    @Test
    void getBestRoute() {
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
        v06.setDescription("Never-Never Land");  // transit by pirate ship, lol

        //addEdge(start, end, mode, duration, cost, distance)
        v01.addEdge(v01, v02, "WALKING", 900, 0.00, 347);
        v01.addEdge(v01, v04, "WALKING", 2100, 0.00, 700);
        v01.addEdge(v01, v02, "TRANSIT", 1200, 4.00, 543);
        v01.addEdge(v01, v03, "DRIVING", 180, 0.72, 2765);
        v01.addEdge(v01, v05, "DRIVING", 2945 ,14.65 , 119624);

        v02.addEdge(v02, v01, "WALKING", 964, 0.00, 391);
        v02.addEdge(v02, v03, "TRANSIT", 1400, 4.00, 14522);
        v02.addEdge(v02, v03, "WALKING", 10532, 0.00, 3512);
        v02.addEdge(v02, v06, "TRANSIT", 4380, 12.50, 73083);

        v03.addEdge(v03, v01, "TRANSIT", 1744, 4.00, 17010);
        v03.addEdge(v03, v02, "WALKING", 11520, 0.00, 3620);
        v03.addEdge(v03, v04, "WALKING", 22657, 0.00, 7455);
        v03.addEdge(v03, v05, "WALKING", 618674, 0.00, 122798);
        v03.addEdge(v03, v04, "DRIVING", 520, 2.32, 7640);
        v03.addEdge(v03, v05, "DRIVING", 2845, 15.33, 104353);
        v03.addEdge(v03, v01, "DRIVING", 351, 14.42, 7122);

        v04.addEdge(v04, v01, "WALKING", 68542, 0.00, 13204);
        v04.addEdge(v04, v05, "WALKING", 542685, 0.00, 105657);
        v04.addEdge(v04, v03, "WALKING", 22547, 0.00, 7457);
        v04.addEdge(v04, v05, "TRANSIT", 8294, 10.00, 237665);
        v04.addEdge(v04, v02, "TRANSIT", 1422, 4.00, 7345);
        v04.addEdge(v04, v01, "DRIVING", 480, 13.01, 14342);
        v04.addEdge(v04, v05, "DRIVING", 1945, 15.77, 99323);
        v04.addEdge(v04, v03, "DRIVING", 3540 ,5.34 ,1672);

        v05.addEdge(v05, v01, "WALKING", 614524, 0.00, 120985);
        v05.addEdge(v05, v02, "TRANSIT", 5482, 6.00, 109642);
        v05.addEdge(v05, v04, "TRANSIT", 8457, 10.00, 126398);
        v05.addEdge(v05, v06, "WALKING", 191785, 0.00, 63641);
        v05.addEdge(v05, v04, "DRIVING", 1894 ,13.45, 97654);
        v05.addEdge(v05, v01, "DRIVING", 1933, 21.22, 101234);

        v06.addEdge(v06, v04, "WALKING", 206981, 0.00, 68521);
        v06.addEdge(v06, v05, "WALKING", 196584, 0.00, 64354);
        v06.addEdge(v06, v02, "TRANSIT", 8634, 14.00, 72481);

        UserRouteRequest userRouteRequest = new UserRouteRequest();  //from KSU to ZooAtlanta
        userRouteRequest.setOrigin(v05);  //check this method.  It need to be coded first.
        userRouteRequest.setDestination(v03);

        RouteIdentifier routeIdentifier = new RouteIdentifier(graph, userRouteRequest);

// duration is measured in seconds, 4500 (75 minutes) is the longest time the person is willing to spend on a transit system

        LinearRoute driveTransitRoute = routeIdentifier.getBestRoute("DRIVING", "TRANSIT", "duration", 4500);
        assertTrue (driveTransitRoute.getWaypoint(1).getDescription.includes("Kroger"));  // or as appropriate

// check another route using walking with max distance about 1.5 miles

        LinearRoute transitWalkingRoute = routeIdentifier.getBestRoute("TRANSIT", "WALKING", "distance", 8000);
//        assertTrue (something);

// check alternative route for Transit and Walking through v04 (Kroger)

//        LinearRoute transitWalkingKrogerRoute = new LinearRoute(routeIdentifier.getAlternativeVertex("TRANSIT", "WALKING, "distance", v04);
//                assertTrue (something);
    }

    private RouteIdentifier routeIdentifier;
    private BranchGeoModel geoModel;
    private UserRouteRequest routeRequest;


    @BeforeEach
    void setUp() {
        // Initialize the geoModel and routeRequest with mock data or mocks
        geoModel = new BranchGeoModel();
        routeRequest = new UserRouteRequest();


        // Initialize RouteIdentifier with the mock data
        routeIdentifier = new RouteIdentifier(geoModel, routeRequest);
    }

    @Test
    void testAddOperationAndListRetrieval() {
        DijkstraGraph graph = new DijkstraGraph(geoModel, routeRequest, "mode", "metric");
        routeIdentifier.addGraph(graph);
        LinkedList<DijkstraGraph> graphList = routeIdentifier.getGraphList();

        assertTrue(graphList.contains(graph));
    }

    @Test
    void testGetBestRoutePrimaryModeOnly() {
        // Assuming mock data where primary mode is sufficient

        // Call the method under test
        LinearRoute result = routeIdentifier.getBestRoute("car", "walking", "fastest", 10000);

        // Perform assertions to verify the result
        assertNotNull(result);
        // Add more detailed checks based on the expected behavior of the method
    }

    @Test
    void testGetBestRouteWithSecondaryMode() {
        // Assuming mock data where secondary mode is necessary

        // Call the method under test
        LinearRoute result = routeIdentifier.getBestRoute("car", "bus", "cheapest", 5000);

        // Perform assertions to verify the result
        assertNotNull(result);
        // Add more detailed checks based on the expected behavior of the method
    }

    @Test
    void testGetBestRouteNoPossiblePath() {
        // Assuming mock data where no path is possible

        // Call the method under test
        LinearRoute result = routeIdentifier.getBestRoute("car", "bus", "cheapest", 500);

        // Perform assertions to verify the result
        assertNull(result); // Assuming method returns null or throws an exception when no path is possible
        // If an exception is expected, use assertThrows
    }

//    @Test
//    void testGetAlternativeVertex() {
//        // Assuming mock data
//
//        // Call the method under test
//        LinearRoute result = routeIdentifier.getAlternativeVertex("car", "bus", "cheapest", "VertexX");
//
//        // Perform assertions to verify the result
//        assertNotNull(result);
//        // Add more detailed checks based on the expected behavior of the method
//    }

    // Additional tests for combineRoutes, calculateTotalDistance, etc. can be added here

    // You could also add more tests for edge cases, invalid inputs, etc.
}