//package com.down2thewire;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.sql.SQLSyntaxErrorException;
//import java.util.HashMap;
//import java.util.LinkedList;
//
//class ApiBypassJsonTest {
//    String location1Name = new String();
//    String location1Address = new String();
//    String location1LatLng = new String();
//    String location2Name = new String();
//    String location2Address = new String();
//    String location2LatLng = new String();
//
//    @BeforeEach
//    void setUp() {
//        // This test is to provide methods that either create or use a static JSON file or set of files
//        // Json files will be saved at src/test/resources
//        location1Name = "CVS Pharmacy, Sandy Springs, Northside Dr at Powers Ferry";
//        location1Address = "6370 Powers Ferry Rd NW, Atlanta, GA 30339";
//        location1LatLng = "33.9039546,-84.4364017";
//        location2Name = "Zoo Atlanta, Grant Park";
//        location2Address = "800 Cherokee Ave SE, Atlanta, GA 30315";
//        location2LatLng = "33.7337594,-84.3742353";
//    }
//
//    @Test
//    void createDirectionsJsonFileWalking() throws IOException {
//        String mode = "walking";
//        ApiConnector directionsApi = new ApiConnector(location1LatLng, location2LatLng, mode);
//        directionsApi.saveJsonToFile("walkingCvsToZoo");
//    }
//
//    @Test
//    void readDirectionsJsonFileWalking() throws IOException {
//        String fileName = "walkingCvsToZoo";
//        String mode = "walking";
//        ApiConnector directionsApi = new ApiConnector(location1LatLng, location2LatLng, mode);
//        String json = directionsApi.readJsonFromFileApi(fileName);
//        System.out.println("JSON file retrieved/n" + json);
//
//        LinkedList<LinearRoute> routes = directionsApi.constructRouteList(json);
//    }
//
//    @Test
//    void createDirectionsJsonFileTransit() throws IOException {
//        String mode = "transit";
//        ApiConnector directionsApi = new ApiConnector(location1LatLng, location2LatLng, mode);
//        directionsApi.saveJsonToFile("transitZooToCvs");
//    }
//
//    @Test
//    void readDirectionsJsonFileTransit() throws IOException {
//        String fileName = "transitZooToCvs";
//        String mode = "transit";
//        ApiConnector directionsApi = new ApiConnector(location1LatLng, location2LatLng, mode);
//        String json = directionsApi.readJsonFromFileApi(fileName);
//        System.out.println("JSON file retrieved:/n" + json);
//
//        LinkedList<LinearRoute> routes = directionsApi.constructRouteList(json);
//    }
//
//
//
//    @Test
//    void createPlacesJsonFileCVS() throws IOException{
//        HashMap<String, String> myParameters = new HashMap<>();
//        myParameters.put("location=", location1LatLng);
//        myParameters.put("&type=", "transit_station");
//        myParameters.put("&radius=","7500");
//        PlacesApi placesApi = new PlacesApi(myParameters);
//        placesApi.saveJsonToFile("CVSPlaces");
//    }
//
//    @Test
//    void readPlacesJsonFileCVS() throws IOException {
//        String fileName = "CVSPlaces";
//        PlacesApi placesApi = new PlacesApi(new HashMap<>());
//        String json = placesApi.readJsonFromFileApi(fileName);
//        System.out.println("JSON file retrieved:/n" + json);
//    }
//
//    @Test
//    void createPlacesJsonFileZoo() throws IOException{
//        HashMap<String, String> myParameters = new HashMap<>();
//        myParameters.put("location=", location2LatLng);
//        myParameters.put("&type=", "transit_station");
//        myParameters.put("&radius=","7500");
//        PlacesApi placesApi = new PlacesApi(myParameters);
//        placesApi.saveJsonToFile("ZooPlaces");
//    }
//
//    @Test
//    void readPlacesJsonFileZoo() throws IOException {
//        String fileName = "ZooPlaces";
//        PlacesApi placesApi = new PlacesApi(new HashMap<>());
//        String json = placesApi.readJsonFromFileApi(fileName);
//        System.out.println("JSON file retrieved:/n" + json);
//    }
//
//
//
//    @Test
//    void createDistanceMatrixJsonFileCVS() throws IOException {
//        BranchGeoModel graph = new BranchGeoModel();
//        HashMap<String, String> myParameters = new HashMap<>();
//        myParameters.put("location=", location1LatLng);
//        myParameters.put("&type=", "transit_station");
//        myParameters.put("&radius=","7500");
//        PlacesApi placesApi = new PlacesApi(myParameters);
//        graph = placesApi.getApiResponseAsGeoModel();
//        DistanceMatrixApi distanceMatrixApi = new DistanceMatrixApi(graph);
//        distanceMatrixApi.divideAndQueryAsFile("driving","CVSDistanceMatrix");
//    }
//
//    @Test
//    void readDistanceMatrixJsonFileCVS() throws IOException {
//    String fileName = "CVSDistanceMatrix4";
//    DistanceMatrixApi distanceMatrixApi = new DistanceMatrixApi(new BranchGeoModel());
//    String json = distanceMatrixApi.readJsonFromFileApi(fileName);
//        System.out.println("JSON file retrieved:/n" + json);
//    }
//
//
//
//}