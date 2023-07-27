package com.down2thewire;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiConnector {
    String apiKey = ApiKeys.getGoogleKey();
    String url;
    String resultText;



    public ApiConnector(String origin, String destination, String mode){
        this.url = buildDirectionsUrl(origin, destination, mode, apiKey);
    }

    private static String buildDirectionsUrl(String origin, String destination, String mode, String apiKey) {
        return "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin.replace(" ", "+") +
                "&destination=" + destination.replace(" ", "+") +
                "&mode=" + mode +
                "&key=" + apiKey;
    }

    public String saveJsonToString() {
        URL apiEndpoint = null;
        String jsonText;
        HttpURLConnection connection = null;
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
        return jsonText;
    }

    public WeightedGraph constructWeightedGraph(String json) {
        WeightedGraph weightedGraph = new WeightedGraph();

        JSONObject directionsJson = new JSONObject(json);
        JSONArray routesArray = directionsJson.getJSONArray("routes");
        if (routesArray.length() > 0) {
            JSONObject route = routesArray.getJSONObject(0);

            JSONArray legsArray = route.getJSONArray("legs");
            for (int i = 0; i < legsArray.length(); i++) {
                JSONObject leg = legsArray.getJSONObject(i);

                JSONArray stepsArray = leg.getJSONArray("steps");
                for (int j = 0; j < stepsArray.length(); j++) {
                    JSONObject step = stepsArray.getJSONObject(j);

                    int duration = step.getJSONObject("duration").getInt("value");
                    int distance = step.getJSONObject("distance").getInt("value");

                    String startVertexName = step.getJSONObject("start_location").toString();
                    String endVertexName = step.getJSONObject("end_location").toString();
//todo check json lan and long
                    double sLongitude = step.getJSONObject("start_location").getDouble("lng");
                    double sLatitude = step.getJSONObject("start_location").getDouble("lat");
                    double eLongitude = step.getJSONObject("end_location").getDouble("lng");
                    double eLatitude = step.getJSONObject("end_location").getDouble("lat");
                    String mode = step.getString("travel_mode").toLowerCase();

                    // Construct the name generator and retrieve the human-readable names
                    WeightedGraphNameGenerator nameGenerator = new WeightedGraphNameGenerator();
                    String startVertexHumanName = nameGenerator.getHumanReadableName(sLatitude, sLongitude);
                    String endVertexHumanName = nameGenerator.getHumanReadableName(eLatitude, eLongitude);

                    // Create vertices with human-readable names
                    WeightedGraph.Vertex source = new WeightedGraph.Vertex(new Location(sLatitude, sLongitude), startVertexHumanName);
                    WeightedGraph.Vertex destination = new WeightedGraph.Vertex(new Location(eLatitude, eLongitude), endVertexHumanName);

                    weightedGraph.addEdge(source, destination, mode, duration, 0.0, distance);
                    if (weightedGraph.vertexList.size() > 30) {break;}

/*
                    // Get the existing start and end vertices
                    WeightedGraph.Vertex source = new WeightedGraph.Vertex(new Location(sLatitude, sLongitude), startVertexName);
                    WeightedGraph.Vertex destination = new WeightedGraph.Vertex(new Location(eLatitude, eLongitude), endVertexName);

                    weightedGraph.addEdge(source, destination, mode, duration, 0.0, distance);
                    */
                }
                if (weightedGraph.vertexList.size() > 30) {break;}
            }
        }
        return weightedGraph;
    }
}
