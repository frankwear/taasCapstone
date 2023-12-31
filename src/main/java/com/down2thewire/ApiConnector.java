package com.down2thewire;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

public class ApiConnector {
    private String apiKey = ApiKeys.getGoogleKey();
    private String url;




    public ApiConnector(String origin, String destination, String mode){
        this.url = buildDirectionsUrl(origin, destination, mode, apiKey);
    }
    public ApiConnector(String origin, String destination, String mode, Boolean alternatives){
        this.url = buildDirectionsUrl(origin, destination, mode, apiKey, alternatives);
    }

    public ApiConnector(BranchVertex originVertex, BranchVertex destinationVertex, String mode){
        this.url = buildDirectionsUrl(originVertex.getLocation().AsString(), destinationVertex.getLocation().AsString(), mode, apiKey);
    }
    public ApiConnector(LinearWayPoint originWayPoint, LinearWayPoint destinationWayPoint, String mode){
        this.url = buildDirectionsUrl(originWayPoint.location.AsString(), destinationWayPoint.location.AsString(), mode, apiKey);
    }

    private static String buildDirectionsUrl(String origin, String destination, String mode, String apiKey) {
        return "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin.replace(" ", "+") +
                "&destination=" + destination.replace(" ", "+") +
                "&mode=" + mode +
                "&key=" + apiKey;
    }
    private static String buildDirectionsUrl(String origin, String destination, String mode, String apiKey, Boolean alternatives) {
        return "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin.replace(" ", "+") +
                "&destination=" + destination.replace(" ", "+") +
                "&alternatives=true" +
                "&mode=" + mode +
                "&key=" + apiKey;
    }


    //10-10-2023, we refactored the name from saveJsonToString to get getJsonStringFromApi to match the PlacesApi.
    public String getJsonStringFromApi() {
        String jsonText;
        if (apiKey.isEmpty()) {
            System.out.println("API Key is empty");
            jsonText = "";
        } else {
            URL apiEndpoint;
            HttpURLConnection connection;
            try {
                apiEndpoint = new URL(this.url);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection = (HttpURLConnection) apiEndpoint.openConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (InputStream inputStream = connection.getInputStream()) {
                jsonText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            connection.disconnect();

        }
        return jsonText;
    }

    // 10-3-23 added saveJsonToFile() method for ApiBypassJsonTest
    public void saveJsonToFile(String fileName) throws IOException {
        URL apiEndpoint;
        String jsonText;
        HttpURLConnection connection;
        try {
            apiEndpoint = new URL(this.url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = (HttpURLConnection) apiEndpoint.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (InputStream inputStream = connection.getInputStream()) {
            fileName = "src/test/resources/" + fileName + ".json";
            Path filePath = Path.of(fileName);
            saveResponseToFile(inputStream,filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection.disconnect();
    }

    // 10-3-23 added saveResponseToFile() method for ApiBypassJsonTest
    private void saveResponseToFile(InputStream inputStream, Path filePath) throws IOException {

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE)) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                writer.write(new String(buffer, 0, bytesRead));
            }
        }
    }

    public String readJsonFromFileApi(String fileName) throws IOException {
        fileName = "src/test/resources/" + fileName + ".json";
        Path filePath = Path.of(fileName);

        return new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
    }


    public LinkedList<LinearRoute> constructRouteList(String json) {
        LinkedList<LinearRoute> routes = new LinkedList<>();

        JSONObject apiDirectionsJson = new JSONObject(json);
        JSONArray routesArray = apiDirectionsJson.getJSONArray("routes");
        for (int k = 0; k < routesArray.length(); k++) {
            JSONObject apiRoute = routesArray.getJSONObject(k);
            JSONArray legsArray = apiRoute.getJSONArray("legs");
            for (int i = 0; i < legsArray.length(); i++) {
                JSONObject leg = legsArray.getJSONObject(i);
                JSONArray apiEdgesArray = leg.getJSONArray("steps");
                String startVertexHumanName = "";
                LinearWayPoint lastLegDestination = new LinearWayPoint(new Location(0.00,0.00));
                LinearRoute route = new LinearRoute();
                for (int j = 0; j < apiEdgesArray.length(); j++) {
                    JSONObject step = apiEdgesArray.getJSONObject(j);

                    int duration = step.getJSONObject("duration").getInt("value");
                    int distance = step.getJSONObject("distance").getInt("value");

//                    String startVertexName = step.getJSONObject("start_location").toString();
//                    String endVertexName = step.getJSONObject("end_location").toString();
                    double sLongitude = step.getJSONObject("start_location").getDouble("lng");
                    double sLatitude = step.getJSONObject("start_location").getDouble("lat");
                    Location start = new Location(sLatitude, sLongitude);
                    double eLongitude = step.getJSONObject("end_location").getDouble("lng");
                    double eLatitude = step.getJSONObject("end_location").getDouble("lat");
                    Location end = new Location(eLatitude, eLongitude);
                    String mode = step.getString("travel_mode").toLowerCase();

                    // Construct the name generator and retrieve the human-readable names
////                    WeightedGraphNameGenerator nameGenerator = new WeightedGraphNameGenerator();
//                    if (startVertexHumanName.equals("")) {
//                        startVertexHumanName = nameGenerator.getHumanReadableName(sLatitude, sLongitude);
//                    }
//                    String endVertexHumanName = nameGenerator.getHumanReadableName(eLatitude, eLongitude);

                    // Create vertices with human-readable names
                    LinearWayPoint source = new LinearWayPoint(start);
                    LinearWayPoint destination = new LinearWayPoint(end);

                    if(j == 0){  //first iteration, normally
                        route.addWaypoint(source);
                    } else {
                        source = lastLegDestination;
                    }
                    source.setEdge(new Edge(source, destination, mode, duration, 0.00, distance));
                    route.addWaypoint(destination);

                    if (route.wayPointLinkedList.size() > 30) {break;}
                    lastLegDestination = destination;

/*
                    // Get the existing start and end vertices
                    WeightedGraph.Vertex2 source = new WeightedGraph.Vertex2(new Location(sLatitude, sLongitude), startVertexName);
                    WeightedGraph.Vertex2 destination = new WeightedGraph.Vertex2(new Location(eLatitude, eLongitude), endVertexName);

                    weightedGraph.addEdge(source, destination, mode, duration, 0.0, distance);
                    */
                }
                routes.add(route);
                if (route.wayPointLinkedList.size() > 30) {break;}
            }
        }
        return routes;
    }

    public LinearRoute constructRoute(String json) {
        LinearRoute route = new LinearRoute();


        JSONObject directionsJson = new JSONObject(json);
        JSONArray routesArray = directionsJson.getJSONArray("routes");
        if (routesArray.length() > 0) {
            JSONObject apiRoute = routesArray.getJSONObject(0);

            JSONArray legsArray = apiRoute.getJSONArray("legs");
            for (int i = 0; i < legsArray.length(); i++) {
                JSONObject leg = legsArray.getJSONObject(i);

                JSONArray stepsArray = leg.getJSONArray("steps");
                String startVertexHumanName = "";
                LinearWayPoint lastLegDestination = new LinearWayPoint(new Location(0.00,0.00));
                for (int j = 0; j < stepsArray.length(); j++) {
                    JSONObject step = stepsArray.getJSONObject(j);

                    int duration = step.getJSONObject("duration").getInt("value");
                    int distance = step.getJSONObject("distance").getInt("value");

//                    String startVertexName = step.getJSONObject("start_location").toString();
//                    String endVertexName = step.getJSONObject("end_location").toString();
//todo check json lan and long
                    double sLongitude = step.getJSONObject("start_location").getDouble("lng");
                    double sLatitude = step.getJSONObject("start_location").getDouble("lat");
                    Location start = new Location(sLatitude, sLongitude);
                    double eLongitude = step.getJSONObject("end_location").getDouble("lng");
                    double eLatitude = step.getJSONObject("end_location").getDouble("lat");
                    Location end = new Location(eLatitude, eLongitude);
                    String mode = step.getString("travel_mode").toLowerCase();

                    // Construct the name generator and retrieve the human-readable names
////                    WeightedGraphNameGenerator nameGenerator = new WeightedGraphNameGenerator();
//                    if (startVertexHumanName.equals("")) {
//                        startVertexHumanName = nameGenerator.getHumanReadableName(sLatitude, sLongitude);
//                    }
//                    String endVertexHumanName = nameGenerator.getHumanReadableName(eLatitude, eLongitude);

                    // Create vertices with human-readable names
                    LinearWayPoint source = new LinearWayPoint(start);
                    LinearWayPoint destination = new LinearWayPoint(end);

                    if(j == 0){  //first iteration, normally
                        route.addWaypoint(source);
                    } else {
                        source = lastLegDestination;
                    }
                    source.setEdge(new Edge(source, destination, mode, duration, 0.00, distance));
                    route.addWaypoint(destination);

                    if (route.wayPointLinkedList.size() > 30) {break;}
                    lastLegDestination = destination;

/*
                    // Get the existing start and end vertices
                    WeightedGraph.Vertex2 source = new WeightedGraph.Vertex2(new Location(sLatitude, sLongitude), startVertexName);
                    WeightedGraph.Vertex2 destination = new WeightedGraph.Vertex2(new Location(eLatitude, eLongitude), endVertexName);

                    weightedGraph.addEdge(source, destination, mode, duration, 0.0, distance);
                    */
                }
                if (route.wayPointLinkedList.size() > 30) {break;}
            }
        }
        return route;
    }
}







//package com.down2thewire;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//
//public class ApiConnector {
//    String apiKey = ApiKeys.getGoogleKey();
//    String url;
//
//
//
//
//    public ApiConnector(String origin, String destination, String mode){
//        this.url = buildDirectionsUrl(origin, destination, mode, apiKey);
//    }
//
//    public ApiConnector(Vertex originVertex, Vertex destinationVertex, String mode){
//        this.url = buildDirectionsUrl(originVertex.location.AsString(), destinationVertex.location.AsString(), mode, apiKey);
//    }
//
//    private static String buildDirectionsUrl(String origin, String destination, String mode, String apiKey) {
//        return "https://maps.googleapis.com/maps/api/directions/json?" +
//                "origin=" + origin.replace(" ", "+") +
//                "&destination=" + destination.replace(" ", "+") +
//                "&mode=" + mode +
//                "&key=" + apiKey;
//    }
//
//    public String saveJsonToString() {
//        URL apiEndpoint;
//        String jsonText;
//        HttpURLConnection connection;
//        try {
//            apiEndpoint = new URL(this.url);
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            connection = (HttpURLConnection) apiEndpoint.openConnection();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        try (InputStream inputStream = connection.getInputStream()) {
//            jsonText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        connection.disconnect();
//        return jsonText;
//    }
//
//    public WeightedGraph constructWeightedGraph(String json) {
//        WeightedGraph weightedGraph = new WeightedGraph();
//
//        JSONObject directionsJson = new JSONObject(json);
//        JSONArray routesArray = directionsJson.getJSONArray("routes");
//        if (routesArray.length() > 0) {
//            JSONObject route = routesArray.getJSONObject(0);
//
//            JSONArray legsArray = route.getJSONArray("legs");
//            for (int i = 0; i < legsArray.length(); i++) {
//                JSONObject leg = legsArray.getJSONObject(i);
//
//                JSONArray stepsArray = leg.getJSONArray("steps");
//                String startVertexHumanName = "";
//                for (int j = 0; j < stepsArray.length(); j++) {
//                    JSONObject step = stepsArray.getJSONObject(j);
//
//                    int duration = step.getJSONObject("duration").getInt("value");
//                    int distance = step.getJSONObject("distance").getInt("value");
//
////                    String startVertexName = step.getJSONObject("start_location").toString();
////                    String endVertexName = step.getJSONObject("end_location").toString();
////todo check json lan and long
//                    double sLongitude = step.getJSONObject("start_location").getDouble("lng");
//                    double sLatitude = step.getJSONObject("start_location").getDouble("lat");
//                    double eLongitude = step.getJSONObject("end_location").getDouble("lng");
//                    double eLatitude = step.getJSONObject("end_location").getDouble("lat");
//                    String mode = step.getString("travel_mode").toLowerCase();
//
//                    // Construct the name generator and retrieve the human-readable names
//                    WeightedGraphNameGenerator nameGenerator = new WeightedGraphNameGenerator();
//                    if (startVertexHumanName.equals("")) {
//                        startVertexHumanName = nameGenerator.getHumanReadableName(sLatitude, sLongitude);
//                    }
//                    String endVertexHumanName = nameGenerator.getHumanReadableName(eLatitude, eLongitude);
//
//                    // Create vertices with human-readable names
//                    Vertex source = new Vertex(new Location(sLatitude, sLongitude), startVertexHumanName);
//                    Vertex destination = new Vertex(new Location(eLatitude, eLongitude), endVertexHumanName);
//
//                    weightedGraph.addEdge(source, destination, mode, duration, 0.0, distance);
//                    if (weightedGraph.vertexList.size() > 30) {break;}
//                    startVertexHumanName = endVertexHumanName; // prep for next iteration
//
///*
//                    // Get the existing start and end vertices
//                    WeightedGraph.Vertex source = new WeightedGraph.Vertex(new Location(sLatitude, sLongitude), startVertexName);
//                    WeightedGraph.Vertex destination = new WeightedGraph.Vertex(new Location(eLatitude, eLongitude), endVertexName);
//
//                    weightedGraph.addEdge(source, destination, mode, duration, 0.0, distance);
//                    */
//                }
//                if (weightedGraph.vertexList.size() > 30) {break;}
//            }
//        }
//        return weightedGraph;
//    }
//}
