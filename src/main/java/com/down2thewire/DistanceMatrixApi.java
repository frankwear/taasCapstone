package com.down2thewire;


import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class DistanceMatrixApi {
    private BranchGeoModel currentGeography = new BranchGeoModel();
    private LinkedList<String> originStrings = new LinkedList<>();
    private HashMap<String, BranchVertex> origin_VertexMap = new HashMap<>();
    private LinkedList<String> destinationStrings = new LinkedList<>();
    private HashMap<String, BranchVertex> destination_VertexMap = new HashMap<>();
    private String apiKey = ApiKeys.getGoogleKey();



    public DistanceMatrixApi(BranchGeoModel initialGeography, LinkedList<String> originStrings, LinkedList<String> destinationStrings) {
        this.originStrings = originStrings;
        this.destinationStrings = destinationStrings;
        this.currentGeography = initialGeography;
        this.origin_VertexMap = getHashmap(originStrings);  //also adds unknown locations to currentGeography
        this.destination_VertexMap = getHashmap(destinationStrings);
    }
    public DistanceMatrixApi(BranchGeoModel initialGeography){

        // **** Assume the user wants a complete graph from the Geomodel of 25 or less locations *****//
        this.currentGeography = initialGeography;
        this.originStrings = initialGeography.getLocationsAsListOfString();
        this.destinationStrings = this.originStrings;
        this.origin_VertexMap = getHashmap(originStrings);
        this.destination_VertexMap = getHashmap(destinationStrings);
    }
    public DistanceMatrixApi(LinkedList<String> originStrings, LinkedList<String> destinationStrings){
        this.originStrings = originStrings;
        this.destinationStrings = destinationStrings;
        this.currentGeography = new BranchGeoModel();
        this.origin_VertexMap = getHashmap(originStrings);
        this.destination_VertexMap = getHashmap(destinationStrings);
    }


    public void updateGeoModel(String mode){
//        for (URL loopUrl: urlsList){
//            String loopJsonAsString = getJsonFileAsString(loopUrl);
//            URL url = new URL()
//            HashMap<String, Integer>[][] metrics = getJsonFileAsTable(loopJsonAsString, mode);
//        }
        divideAndQuery(mode);
    }

    private void divideAndQuery(String mode) {
        //List<String> listOfUsableURLs=new ArrayList<>();
        // manage query size
        // split and should return a list of usable mini URLs

        if(originStrings.size()>25 || destinationStrings.size()>25){
            System.out.println("DistanceMatrixApi.createUrlsList() error - Google Distance Matrix API will not all\n" +
                    "more than 25 locations in the origin or the destination.\n\n" +
                    "******URLs NOT CREATED******\n\n");
            return;  // end method without continuing.
        }

        // Split the request -small size
        Integer maxDestinationRows = 100/originStrings.size();
        if (maxDestinationRows > 25){ maxDestinationRows = 25;}
        if (maxDestinationRows > destinationStrings.size()) {maxDestinationRows = destinationStrings.size();}
        for (int i = 0; i < destinationStrings.size(); i = i + maxDestinationRows) {
            LinkedList<String> loopDestinations = new LinkedList<>();
            for (int j = 0; j < maxDestinationRows; j++) {
                if (i < destinationStrings.size()) {  // to prevent going out of bounds
                    loopDestinations.addLast(destinationStrings.get(j));
                }
            }
            String tempOrigins = createStringOfLocations(originStrings);
            String tempDestinationsString = createStringOfLocations(loopDestinations);
            String urlAsString = createUrlAsString(tempOrigins, tempDestinationsString, mode);
            URL tempUrl;
            try {
                tempUrl = new URL(urlAsString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            String loopJsonAsString = getJsonFileAsString(tempUrl);
            if(apiKey.isEmpty()){
                //todo;
            } else {
            HashMap<String, Integer>[][] metricsTable = getJsonFileAsTable(loopJsonAsString);
            for (int row = 0; row < originStrings.size(); row++){
                for (int col = 0; col < destinationStrings.size(); col++) {
                    Integer edgeDuration = metricsTable[row][col].get("duration");
                    Integer edgeDistance = metricsTable[row][col].get("distance");
                    if (edgeDistance != 0) {
                        BranchVertex origVertex = origin_VertexMap.get(originStrings.get(col));
                        Edge<BranchVertex> loopEdge = new Edge<>(
                                origVertex,
                                destination_VertexMap.get(destinationStrings.get(row)),
                                mode,
                                edgeDuration,
                                0.0d,
                                edgeDistance);
                        origVertex.addEdge(loopEdge);
                    }
                  }
                }
            }
        }
    }





    public void divideAndQueryAsFile(String mode, String filename) {
        //List<String> listOfUsableURLs=new ArrayList<>();
        // manage query size
        // split and should return a list of usable mini URLs


        // Split the request -small size
        Integer maxDestinationRows = 100/originStrings.size();
        if (maxDestinationRows > 25){ maxDestinationRows = 25;}
        if (maxDestinationRows > destinationStrings.size()) {maxDestinationRows = destinationStrings.size();}
        int k = 1;
        for (int i = 0; i < destinationStrings.size(); i = i + maxDestinationRows) {
            LinkedList<String> loopDestinations = new LinkedList<>();
            for (int j = 0; j < maxDestinationRows; j++) {
                if (i < destinationStrings.size()) {  // to prevent going out of bounds
                    loopDestinations.addLast(destinationStrings.get(j));
                }
            }
            String tempOrigins = createStringOfLocations(originStrings);
            String tempDestinationsString = createStringOfLocations(loopDestinations);
            String urlAsString = createUrlAsString(tempOrigins, tempDestinationsString, mode);
            URL tempUrl;
            try {
                tempUrl = new URL(urlAsString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            String loopJsonAsString = getJsonFileAsString(tempUrl);
            filename = filename.concat(String.valueOf(k));
            filename = "src/test/resources/" + filename + ".json";
            try {
                saveJsonToFile(loopJsonAsString, filename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            k++;
        }


    }

//****** This is the method working from 9-25 - renamed to "divideAndQuery()" in new code *****//
//
//    private LinkedList<URL> createUrlsList(String mode) {
//        //List<String> listOfUsableURLs=new ArrayList<>();
//        // manage query size
//        // split and should return a list of usable mini URLs
//
//        if(originStrings.size()>25 || destinationStrings.size()>25){
//            System.out.println("DistanceMatrixApi.createUrlsList() error - Google Distance Matrix API will not all\n" +
//                    "more than 25 locations in the origin or the destination.\n\n" +
//                    "******URLs NOT CREATED******\n\n");
//            return new LinkedList<URL>();
//        }
//        LinkedList<URL> listOfUsableURLs = new LinkedList<>();
//
//        // vertices elements calculation -Calculating the number of elements - need to modify:
//        int totalElements = originStrings.size() * destinationStrings.size();
//
//        // Checking  total number of elements exceeds the maximum limit (100)
//        if (totalElements <= 100) {
//            String tempOrigins = createStringOfLocations(originStrings);
//            String tempDestinations = createStringOfLocations(destinationStrings);
//            String urlAsString = createUrlAsString(tempOrigins, tempDestinations, mode);
//            try{
//                URL tempUrl  = new URL(urlAsString);
//                listOfUsableURLs.addLast(tempUrl);
//            } catch (MalformedURLException e) {
//                throw new RuntimeException(e);
//            }
//            return listOfUsableURLs;
//        } else {
//
//            // Split the request -small size
//            Integer maxDestinationRows = 100/originStrings.size();
//            for (int i = 0; i < destinationStrings.size(); i = i + maxDestinationRows) {
//                LinkedList<String> loopDestinations = new LinkedList<>();
//                for (int j = 0; j < maxDestinationRows; j++) {
//                    if (i < destinationStrings.size()) {  // to prevent going out of bounds
//                        loopDestinations.addLast(destinationStrings.get(i));
//                    }
//                }
//                String tempOrigins = createStringOfLocations(originStrings);
//                String tempDestinationsString = createStringOfLocations(loopDestinations);
//                String urlAsString = createUrlAsString(tempOrigins, tempDestinationsString, mode);
//                try {
//                    URL tempUrl = new URL(urlAsString);
//                    listOfUsableURLs.addLast(tempUrl);
//                } catch (MalformedURLException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        return listOfUsableURLs;
//    }


    private String getJsonFileAsString(URL url) {
        URL apiEndpoint = url;
        String jsonText;
        HttpURLConnection connection;

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

    private HashMap<String, Integer>[][] getJsonFileAsTable(String jsonResult) {
        //GeographicModel apiGm = new GeographicModel();
        JSONObject rowsJSONObject = new JSONObject(jsonResult);
        JSONArray rowsJsonArray = rowsJSONObject.getJSONArray("rows");
        HashMap<String, Integer>[][] metricsTable = new HashMap[25][25];
        if (rowsJsonArray.length() > 0) {
            for (int i = 0; i < rowsJsonArray.length(); i++) {
                JSONObject row = rowsJsonArray.getJSONObject(i);
                JSONArray elements = row.getJSONArray("elements");
                for (int j = 0; j < elements.length(); j++) {
                    metricsTable[i][j] = new HashMap<>();
                    JSONObject element = elements.getJSONObject(j);
                    if (element.getJSONObject("distance").getInt("value") > 30) {
                        metricsTable[i][j].put("distance",element.getJSONObject("distance").getInt("value"));
                        metricsTable[i][j].put("duration",element.getJSONObject("duration").getInt("value"));
                    }
                    else {
                        metricsTable[i][j].put("distance",0);
                        metricsTable[i][j].put("duration",0);
                    }
                }
            }
        }
        return metricsTable;
    }


    public void saveJsonToFile(String jsonResponseAsString, String filename) throws IOException {
        PrintWriter out = new PrintWriter(filename);
        out.println(jsonResponseAsString);
        out.close();
    }




//****** This is the method working from 9-25 *****//
//
//    private void addEdgesToGeoModelFromString(String jsonResult, String mode) {
//        //GeographicModel apiGm = new GeographicModel();
//        JSONObject rowsJSONObject = new JSONObject(jsonResult);
//        JSONArray rowsJsonArray = rowsJSONObject.getJSONArray("rows");
//
//        if (rowsJsonArray.length() > 0) {
//            for (int i = 0; i < rowsJsonArray.length(); i++) {
//                JSONObject row = rowsJsonArray.getJSONObject(i);
//                JSONArray elements = row.getJSONArray("elements");
//                for (int j = 0; j < elements.length(); j++) {
//                    JSONObject element = elements.getJSONObject(j);
//                    if (element.getJSONObject("distance").getInt("value") > 30) {
//                        Edge<BranchVertex> tempEdge = new Edge<>();
//                        tempEdge.setStart(currentGeography.getVertex(i));
//                        tempEdge.setEnd(currentGeography.getVertex(j));
//                        tempEdge.setMode(mode);
//                        tempEdge.setDuration(element.getJSONObject("duration").getInt("value"));
//                        tempEdge.setDistance(element.getJSONObject("distance").getInt("value"));
//                        currentGeography.getVertex(i).addEdge(tempEdge);  //todo - getVertexById or Location
//
//                        Edge<BranchVertex> tempEdge1 = new Edge<>();
//                        tempEdge1.setEnd(currentGeography.getVertex(i));
//                        tempEdge1.setStart(currentGeography.getVertex(j));
//                        tempEdge1.setMode(mode);
//                        tempEdge1.setDuration(element.getJSONObject("duration").getInt("value"));
//                        tempEdge1.setDistance(element.getJSONObject("distance").getInt("value"));
//                        currentGeography.getVertex(j).addEdge(tempEdge1);
//                    }
//                }
//            }
//        }
//    }

    public BranchGeoModel getCurrentGeoModel(){
        return this.currentGeography;
    }

    // 9-24-23 Utilized internal method to reduce complexity and coding
    private String createStringOfLocations(BranchGeoModel geoModel) {
        String tempLocations = createStringOfLocations(geoModel.getLocationsAsListOfString());
        return tempLocations;
    }


    // 9-24-23 Modified Method to accept a list of location strings rather than the locations themselves
    // reduce coupling
    private String createStringOfLocations(LinkedList<String> locations) {
        String locationString = "";
        for (String loopLocation : locations) {
            locationString = locationString + loopLocation + "|";
        }

        // remove the last bar
        locationString = locationString.substring(0,locationString.length()-1);
        return locationString;
    }

    private String createUrlAsString(String locations, String mode) {
        String myUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?";
        myUrl += "origins=" + locations + "&destinations=" + locations;
        myUrl += "&units=imperial" + "&mode=" + mode + "&key=" + apiKey;
        return myUrl;
    }

    private String createUrlAsString(String origins, String destinations, String mode) {
        String myUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?";
        myUrl += "origins=" + origins + "&destinations=" + destinations;
        myUrl += "&units=imperial" + "&mode=" + mode + "&key=" + apiKey;
        return myUrl;
    }

    private HashMap<String, BranchVertex> getHashmap(LinkedList<String> locationStrings){
        HashMap<String, BranchVertex> location_VertexMap = new HashMap<>();
        for (String loopLocation : locationStrings){
            String[] coordinates = loopLocation.split(",");
            Double latitude = Double.parseDouble(coordinates[0]);
            Double longitude = Double.parseDouble(coordinates[1]);
            Location tempLocation = new Location(latitude, longitude);
            int vIndex = currentGeography.getVertexIndexById(
                    tempLocation.generateUniqueID());
            if (vIndex == -1){
                vIndex = currentGeography.getVertexListSize(); //because "index-1"
                currentGeography.addVertex(latitude, longitude);
            }
            BranchVertex loopVertex =
                    currentGeography.getVertex(vIndex);
            location_VertexMap.put(loopLocation, loopVertex);
        }
        return location_VertexMap;
    }
}