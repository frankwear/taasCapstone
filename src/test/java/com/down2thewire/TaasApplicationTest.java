package com.down2thewire;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

class TaasApplicationTest {
    static BranchVertex.BranchGeoModelGenerator instanceGmAnalyzer;
    //    static RouteAnalyzer instanceRA;
    static UserRouteRequest instanceRR;
    static LinkedList<BranchGeoModel> instanceRouteOffering;
    static BranchGeoModel chosenRoute;
    static UserInterface instanceUI;


    // Todo - interface with UI
//    UserInterface instanceUI = new UserInterface();


    public static void main(String[] args) {
        TaasApplication userInstance = new TaasApplication();
        projectDemonstration(userInstance);
//        routesDemo(userInstance);
    }
    /*    private static RouteRequest userSetup() {  // Todo - remove any usages, then delete
            UserAccount testUser = new UserAccount("SallyFields");
            testUser.initializeTransitUser();  // Transit Mode and CHEAP priority

            // Get settings from user account
            RouteRequest testRR = new RouteRequest();
            testRR.setModePrefFromAccount(testUser);
            testRR.setPriority(testUser.getPriority());
            testRR.setOrigin("Dunwoody Marta Station");
            testRR.setDestination("Piedmont Atlanta Hospital");
            return testRR;
        }  */
    private static UserRouteRequest getUserRequest() {
        UserInterface instanceUI = new UserInterface();
        instanceRR = instanceUI.getRequest();
        return instanceRR;
    }

    private static void projectDemonstration(TaasApplication session){
        System.out.println("Welcome to Taas Demonstration Panel" +
                "\nPlease select the demonstration mode:");
        int selectedDemo = menuDemoOptions();
        switch(selectedDemo) {

            // Geomodel From Routes API - original demo
            case 0:
                routesDemo(session);
                break;

                // Basic Dijkstra Graph
            case 1:
                dijkstraDemo();
                break;

                // Pulling data from Google APIs for geoModel
            case 2:
                placesDistanceMatrixDemo();
                break;

                // Identifying a route from a geoModel using Dijkstra
            case 3:
//                apiToGeoModelToDijkstraToRouteDemo();
//                break;

                // Saving and retrieving data from database
            case 4:
//                databaseDemo();
                break;

                // User Interface interaction
            case 5:
                captureUserInputInBackend();
                break;
            case 6:
                fullApiToGeoModelToDijkstraToRouteTest();
                break;
            default:
                System.out.println("Invalid Selection.");
        }


    }

    private static int menuDemoOptions() {
        return 0;
    }

    private static void captureUserInputInBackend() {
    }

    private static void databaseDemo() throws SQLException {
        DataConnectionTest dcTest = new DataConnectionTest();
        dcTest.insertVertexData();
    }

    private static void apiToGeoModelToDijkstraToRouteDemo() {

    }

    private static void placesDistanceMatrixDemo() {
        PlacesApiTest pdmTest = new PlacesApiTest();
        pdmTest.buildLocationsFromApiStaticTEST();
    }

    private static void dijkstraDemo() {
        DijkstraGraphTest dTest = new DijkstraGraphTest();
        dTest.myDijkstraTest();
    }

    private static void routesDemo(TaasApplication session){
        session.instanceUI = new UserInterface();
        session.instanceRR = session.instanceUI.getRequest();

        session.instanceGmAnalyzer = new BranchVertex.BranchGeoModelGenerator(session.instanceRR);
        session.instanceGmAnalyzer.generateGeoModel();
        session.instanceGmAnalyzer.geographicMap.printGraph();
//        session.instanceRA = new RouteAnalyzer(session.instanceGM.geographicMap, session.instanceRR); // comment
//        session.instanceRA.getAllPaths();

//        session.instanceRouteOffering = session.instanceRA.getBest4UserRoutes(); // comment
        System.out.println("Done");
//          chosenRoute = instanceUI.getUsersChoiceOfRoute(session.instanceRouteOffering);
//          instanceUI.displayRoute(chosenRoute);
    }

    private static void fullApiToGeoModelToDijkstraToRouteTest() {
        HashMap<String, String> myParameters = new HashMap<>();
        myParameters.put("location=", "33.8876001,-84.3142002");
        myParameters.put("&type=", "transit_station");
        myParameters.put("&radius=", "15840");  //3 miles

        // get vertices for nearby transit locations
        BranchGeoModel testGM = PlacesApi.buildPlacesFromApiCall(myParameters);
        System.out.println("\n\n\n\nGEOMODEL FROM PLACES API:\n");
        testGM.printGraph();

        // get metrics and edges connecting the locations
        DistanceMatrixApi distanceMatrixApi = new DistanceMatrixApi(testGM);
        distanceMatrixApi.updateGeoModel("DRIVING");
        distanceMatrixApi.updateGeoModel("WALKING");
        System.out.println("\n\n\n\nFULL GEOMODEL AFTER DISTANCE MATRIX CREATES EDGES:\n");
        testGM = distanceMatrixApi.getCurrentGeoModel();
        testGM.printGraph();

        // create a specific request
        BranchVertex originVertex = testGM.getVertex(5);  // for demonstration - no specific reason - at random
        BranchVertex destinationVertex = testGM.getVertex(13); // at random
        UserRouteRequest testRR = new UserRouteRequest();
        LinearWayPoint originWaypoint = new LinearWayPoint(originVertex.getLocation(), originVertex.getDescription());
        LinearWayPoint destinationWaypoint = new LinearWayPoint(destinationVertex.getLocation(), destinationVertex.getDescription());
        testRR.setOriginWaypoint(originWaypoint);
        testRR.setDestinationWaypoint(destinationWaypoint);
        testRR.modePref = new Integer[]{2, 1, 2, 1};

        // use the RouteIdentifier to find a good route
        RouteIdentifier testRId = new RouteIdentifier(testGM, testRR);
        LinearRoute testResultingRoute = testRId.getBestRoute("WALKING", "TRANSIT", "distance", 5280);
        System.out.println("\n\n\n\nRESULTING ROUTE\nFROM ORIGIN TO DESTINATION\nUSING TWO MODES OF TRANSPORTATION\n");
        testResultingRoute.printGraph();
    }
}