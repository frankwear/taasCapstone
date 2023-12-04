package com.down2thewire;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        TaasApplicationTest userInstance = new TaasApplicationTest();
        System.out.println("Welcome to TaaS" +
                "\nTransportation as a Service helps you find new ways to get where you want to go.\n\n");
        int userResponse = userInstance.menu1Input();

        projectDemonstration(userResponse, userInstance);
//        routesDemo(userInstance);
    }
    public int menu1Input(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int userResponse = 0;
        System.out.println("Welcome to Taas Demonstration Panel.  Select the demonstration mode:\n\n");
        System.out.println("Options:\n" +
                "1 - Old (Pre-capstone, very different algorithm) Demo from Directions API\n" +
                "2 - Dijkstra Graph - Basic\n" +
                "3 - Places API\n" +
                "4 - Capture User Input in Backend\n" +
                "5 - Full Places to DistanceMatrix to GeoModel\n" +
                "ENTER YOUR CHOICE (1-5):  ");
        try {
            userResponse = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid Number.  Enter 1-5");
        }
        return userResponse;
    }

    private static UserRouteRequest getUserRequest() {
        UserInterface instanceUI = new UserInterface();
        instanceRR = instanceUI.getRequest();
        return instanceRR;
    }

    private static void projectDemonstration(int userInput, TaasApplicationTest session){
        switch(userInput) {

            // Geomodel From Routes API - original demo
            case 1:
                routesDemo(session);
                break;

                // Basic Dijkstra Graph
            case 2:
                dijkstraDemo();
                break;

                // Pulling data from Google APIs for geoModel
            case 3:
                placesDistanceMatrixDemo();
                break;


                // Saving and retrieving data from database
//            case 4:
//                databaseDemo();
//                break;

                // User Interface interaction
            case 4:
                captureUserInputInBackend();
                break;
            case 5:
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

    private static void routesDemo(TaasApplicationTest session){
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
        myParameters.put("&radius=", "13200");  //2.5 miles

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
        BranchVertex destinationVertex = testGM.getVertex(1); // at random
        UserRouteRequest testRR = new UserRouteRequest();
        LinearWayPoint originWaypoint = new LinearWayPoint(originVertex.getLocation(), originVertex.getDescription());
        LinearWayPoint destinationWaypoint = new LinearWayPoint(destinationVertex.getLocation(), destinationVertex.getDescription());
        testRR.setOriginWaypoint(originWaypoint);
        testRR.setDestinationWaypoint(destinationWaypoint);
        testRR.modePref = new Integer[]{2, 1, 2, 1};

        // use the RouteIdentifier to find a good route
        RouteIdentifier testRId = new RouteIdentifier(testGM, testRR);
        LinearRoute testResultingRoute = testRId.getBestRoute("DRIVING", "WALKING", "distance", 5280);
        System.out.println("\n\n\n\nRESULTING ROUTE\nFROM ORIGIN TO DESTINATION\nUSING TWO MODES OF TRANSPORTATION\n");
        testResultingRoute.printGraph();
    }
}