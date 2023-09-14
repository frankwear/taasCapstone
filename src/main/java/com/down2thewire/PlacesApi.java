package com.down2thewire;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class PlacesApi {
    private String apiKey = ApiKeys.getGoogleKey();
    String urlAsString;

    public PlacesApi(HashMap<String, String> parameters) {
        this.urlAsString = buildPlacesUrl(parameters);
    }

    public GeographicModel constructGeoModel() {
        String jsonAsString = saveJsonToString();
        GeographicModel apiGeoModelResult = new GeographicModel();
        apiGeoModelResult = constructGeoModel(jsonAsString);
        return apiGeoModelResult;
    }

    private String buildPlacesUrl(HashMap<String, String> parameters) {
        String myUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
//        Iterator<Map.Entry<String, String>> parametersIterator = parameters.entrySet().iterator();
//        while (parametersIterator.hasNext()) {
//            HashMap.Entry<String, String> tempEntry = (Map.Entry) parametersIterator.next();
//            if (parametersIterator.next() == )
        for (String key : parameters.keySet()) {
            myUrl = myUrl + key + parameters.get(key) + "&";
        }
        myUrl = myUrl + "key=" + apiKey;
        return myUrl;

    }

    public String saveJsonToString() {
        URL apiEndpoint;
        String jsonText;
        HttpURLConnection connection;
        try {
            apiEndpoint = new URL(this.urlAsString);
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
        return jsonText;
    }

    public GeographicModel constructGeoModel(String json) {
        GeographicModel apiGm = new GeographicModel();
        JSONObject placesJsonObject = new JSONObject(json);
        JSONArray resultsArray = placesJsonObject.getJSONArray("results");
        if (resultsArray.length() > 0) {
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject apiResult = resultsArray.getJSONObject(i);
                double lat = apiResult.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                double lng = apiResult.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                String name = apiResult.getString("name");
                String placeID = apiResult.getString("place_id");
                Vertex2 tempVert = apiGm.addVertex(lat, lng);
                tempVert.setId();
                tempVert.setDescription(name);
                tempVert.setThirdPartyId(placeID);
                apiGm.addVertex(tempVert);
            }
        }
        return apiGm;
    }
}

//
//            for (int i = 0; i < legsArray.length(); i++) {
//                JSONObject leg = legsArray.getJSONObject(i);
//
//                JSONArray stepsArray = leg.getJSONArray("steps");
//                String startVertexHumanName = "";
//                WayPoint lastLegDestination = new WayPoint(new Location(0.00,0.00));
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
//                    Location start = new Location(sLatitude, sLongitude);
//                    double eLongitude = step.getJSONObject("end_location").getDouble("lng");
//                    double eLatitude = step.getJSONObject("end_location").getDouble("lat");
//                    Location end = new Location(eLatitude, eLongitude);
//                    String mode = step.getString("travel_mode").toLowerCase();
//
//                    // Construct the name generator and retrieve the human-readable names
//////                    WeightedGraphNameGenerator nameGenerator = new WeightedGraphNameGenerator();
////                    if (startVertexHumanName.equals("")) {
////                        startVertexHumanName = nameGenerator.getHumanReadableName(sLatitude, sLongitude);
////                    }
////                    String endVertexHumanName = nameGenerator.getHumanReadableName(eLatitude, eLongitude);
//
//                    // Create vertices with human-readable names
//                    WayPoint source = new WayPoint(start);
//                    WayPoint destination = new WayPoint(end);
//
//                    if(j == 0){  //first iteration, normally
//                        route.addWaypoint(source);
//                    } else {
//                        source = lastLegDestination;
//                    }
//                    source.setEdge(new Edge2(source, destination, mode, duration, 0.00, distance));
//                    route.addWaypoint(destination);
//
//                    if (route.wayPointLinkedList.size() > 30) {break;}
//                    lastLegDestination = destination;
//
///*
//                    // Get the existing start and end vertices
//                    WeightedGraph.Vertex2 source = new WeightedGraph.Vertex2(new Location(sLatitude, sLongitude), startVertexName);
//                    WeightedGraph.Vertex2 destination = new WeightedGraph.Vertex2(new Location(eLatitude, eLongitude), endVertexName);
//
//                    weightedGraph.addEdge(source, destination, mode, duration, 0.0, distance);
//                    */
//                }
//                if (route.wayPointLinkedList.size() > 30) {break;}
//            }
//        }
//        return route;
//    }
//}