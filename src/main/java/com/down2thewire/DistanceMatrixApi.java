package com.down2thewire;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DistanceMatrixApi {
    private final GeographicModel localGeography;
    private static final String apiKey = ApiKeys.getGoogleKey();
    private String jsonResultString;
    public DistanceMatrixApi(GeographicModel localGeography) {
        this.localGeography = localGeography;
    }
    public GeographicModel getLocalGeography() {
        //logic
        return localGeography;
    }
//    public GeographicModel addEdgesToGeomodelFromApi(GeographicModel gm,String startLocation, String endLocations, String mode) {
//        String locationString = createStringOfLocation(startLocation, endLocations.toString());
//        String jsonUrlString = createUrl(locationString, mode);
//        String jsonResponseString = getJsonResponseString(jsonUrlString);
//        //addEdgesToGeoModelRectangular(jsonResponseString);
//        return localGeography;
//    }
public GeographicModel addEdgesToGeomodelFromApi(GeographicModel gm, String startLocation, String endLocations, String mode) {
    String locationString = createStringOfLocation(startLocation, endLocations.toString());
    //manageQuerymethod goes here
    String jsonUrlString = createUrl(locationString, mode);
    String jsonResponseString = getJsonResponseString(jsonUrlString);
    // Parse the JSON response & add edges to the geomodel
    addEdgesFromJsonResponse(gm, jsonResponseString);
    return gm;
}
    private void addEdgesFromJsonResponse(GeographicModel gm, String jsonResponse) {
        // Parse the JSON response and add edges to gm
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray rowsArray = jsonObject.getJSONArray("rows");

        for (int i = 0; i < rowsArray.length(); i++) {
            JSONArray elementsArray = rowsArray.getJSONObject(i).getJSONArray("elements");
            for (int j = 0; j < elementsArray.length(); j++) {
                JSONObject element = elementsArray.getJSONObject(j);

                // Extract --> information (e.g., distance, duration, mode) from JSON element
                double distance = element.getJSONObject("distance").getDouble("value");
                int duration = element.getJSONObject("duration").getInt("value");
                String travelMode = "walking"; // todo replace with actual mode later

                // Check --> distance is > 0.0 before adding the edge
                if (distance > 0.0) {
                    Vertex2 startVertex = gm.getVertex(i);
                    Vertex2 endVertex = gm.getVertex(j);

                    // Check if the edge already exists before adding it
                    if (!startVertex.hasEdgeTo(endVertex)) {
                        // Add the edge to the geographic model
                        startVertex.addEdge(startVertex, endVertex, travelMode, duration, 0.00, (int) distance);

                        // Print debug information
                        System.out.println("Added Edge: " + startVertex.getId() + " -> " + endVertex.getId() + " (Distance: " + distance + ")");
                        System.out.println("Start Location: " + startVertex.getLocation());
                        System.out.println("End Location: " + endVertex.getLocation());
                    }
                }
            }
        }
    }

    private String createStringOfLocation(String startLocation, String endLocations) {
        return startLocation + "|" + endLocations;
    }
    private String createStringOfLocation(GeographicModel geoModel) {
        StringBuilder locationString = new StringBuilder();
        for (int i = 0; i < geoModel.getVertexListSize(); i++) {
            Vertex2 vertex = geoModel.getVertex(i);
            Location location = vertex.getLocation();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Append the location to the string in the required format
            locationString.append(latitude).append(",").append(longitude);

            if (i < geoModel.getVertexListSize() - 1) {
                locationString.append("|");
            }
        }
        return locationString.toString();

    }
    private String createStringOfLocation(List<Location> locations) {

        StringBuilder locationString = new StringBuilder();

        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // add location to string
            locationString.append(latitude).append(",").append(longitude);

            if (i < locations.size() - 1) {
                locationString.append("|");
            }
        }

        return locationString.toString();
    }
    private List<String> manageQuerySize(List<String> origins, List<String> destinations) {
        //List<String> listOfUsableURLs=new ArrayList<>();
        // manage query size
        // split and should return a list of usable mini URLs
        List<String> listOfUsableURLs = new ArrayList<>();

        // vertices elements calculation -Calculating the number of elements - need to modify:
        int totalElements = origins.size() * destinations.size();

        // Checking  total number of elements exceeds the maximum limit (100)
        if (totalElements <= 100) {
            // if The request is within limits,add
            String url = createUrl(origins.toString(), destinations.toString());
            listOfUsableURLs.add(url);
        } else {
            // Split the request -small size
            List<List<String>> originBatches = splitIntoBatches(origins, 25);
            List<List<String>> destinationBatches = splitIntoBatches(destinations, 25);

            // Generate requests for each batch
            for (List<String> originBatch : originBatches) {
                for (List<String> destinationBatch : destinationBatches) {
                    String url = createUrl(originBatch.toString(), destinationBatch.toString());
                    listOfUsableURLs.add(url);
                }
            }
        }

        return listOfUsableURLs;
    }
    private List<List<String>> splitIntoBatches(List<String> list, int batchSize) {
        List<List<String>> batches = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, list.size());
            batches.add(list.subList(i, endIndex));
        }
        return batches;
    }
    private String createUrl(String locations, String mode) {
            String myUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?";
            myUrl += "origins=" + locations + "&destinations=" + locations;
            myUrl += "&units=imperial" + "&mode=" + mode + "&key=" + apiKey;
            return myUrl;
    }
    private String getJsonResponseString(String jsonUrlString) {
        URL apiEndpoint;
        String jsonText="";
        HttpURLConnection connection;
        try {
            apiEndpoint = new URL(jsonUrlString);
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
        this.jsonResultString = jsonText;
        return jsonText;
    }
}
