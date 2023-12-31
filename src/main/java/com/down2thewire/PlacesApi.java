package com.down2thewire;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class PlacesApi {
    private static final String apiKey = ApiKeys.getGoogleKey();
    private String urlAsString = "";
    private String apiResponseAsString = "";

    private String url;

    public PlacesApi(HashMap<String, String> parameters) {
        this.urlAsString = buildUrl(parameters);
    }

    private String buildUrl(HashMap<String, String> parameters) {
        String myUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        for (String key : parameters.keySet()) {
            myUrl = myUrl + key + parameters.get(key) + "&";
        }
        myUrl = myUrl + "key=" + apiKey;
        return myUrl;
    }


    //****** Static Implementation *****//
    static BranchGeoModel buildPlacesFromApiCall(HashMap<String, String> parameters) {
        PlacesApi tempPlaces = new PlacesApi(parameters);
        String jsonResponsesString = tempPlaces.getApiResponseAsString();
        return tempPlaces.getApiResponseAsGeoModel();
    }
    //**************//


    public String getApiResponseAsString() {
        if (apiResponseAsString.isBlank()) {
            this.apiResponseAsString = getJsonStringFromApi();
            if (apiResponseAsString.isBlank()) {
                System.out.println("PlacesApi.getApiResponse() no response.  Check Url.");
            }
        }
        return apiResponseAsString;
    }

    public BranchGeoModel getApiResponseAsGeoModel() {
        if (apiResponseAsString.isBlank()) {
            this.apiResponseAsString = getJsonStringFromApi();
        }
        BranchGeoModel locationsOnly = constructGeoModel(apiResponseAsString);
        return locationsOnly;
    }


    private String getJsonStringFromApi() {
        String jsonText;
        if (apiKey.isEmpty()) {
            System.out.println("API Key is empty");
            jsonText = "";
        } else {
            URL apiEndpoint;
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
        }
        return jsonText;
    }


    private BranchGeoModel constructGeoModel(String json) {
        if (json == null || json.isBlank()){
            System.out.println("PlacesApi.constructGeoModel() could not create a GeographicMap.  String is blank.");
            return new BranchGeoModel();
        }
        BranchGeoModel apiGm = new BranchGeoModel();
        JSONObject placesJsonObject = new JSONObject(json);
        JSONArray resultsArray = placesJsonObject.getJSONArray("results");
        if (resultsArray.length() > 0) {
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject apiResult = resultsArray.getJSONObject(i);
                double lat = apiResult.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                double lng = apiResult.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                BranchVertex tempVert = new BranchVertex(new Location(lat, lng));
                tempVert.setDescription(apiResult.getString("name"));
                tempVert.setThirdPartyId(apiResult.getString("place_id"));
                tempVert.setId();
                apiGm.addVertex(tempVert);
            }
        }
        return apiGm;
    }

    public void saveJsonToFile(String fileName) throws IOException {
        URL apiEndpoint;
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
            fileName = "src/test/resources/" + fileName + ".json";
            Path filePath = Path.of(fileName);
            saveResponseToFile(inputStream,filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection.disconnect();
    }

    public String readJsonFromFileApi(String fileName) throws IOException {
        fileName = "src/test/resources/" + fileName + ".json";
        Path filePath = Path.of(fileName);

        return new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
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
