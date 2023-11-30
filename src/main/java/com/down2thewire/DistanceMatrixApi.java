package com.down2thewire;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;

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
        this.originStrings = initialGeography.getLocationsAsListOfString();  //todo shouldn't this be by ID?
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

        //** Set maximum number of destinations that can be attempted at once **//
        Integer maxDestinationColsPerCall = destinationStrings.size();  // request limit
        maxDestinationColsPerCall = Math.min(maxDestinationColsPerCall, 100/originStrings.size());  // Google Limits


        for (int colBlock = 0; colBlock < destinationStrings.size(); colBlock = colBlock + maxDestinationColsPerCall) {

            //** get a list of destinations for this loop **//
            LinkedList<String> loopDestinations = new LinkedList<>();
            for (int colNum = colBlock; colNum < (colBlock+maxDestinationColsPerCall); colNum++) {
                if (colNum < destinationStrings.size()) {  // to prevent going out of bounds
                    loopDestinations.addLast(destinationStrings.get(colNum));
                }
            }

            //** set up URL and call API **//
            String tempOrigins = createStringOfLocations(originStrings);
            String tempDestinationsString = createStringOfLocations(loopDestinations);
            String urlAsString = createUrlAsString(tempOrigins, tempDestinationsString, mode);
            URL loopUrl;
            try {
                loopUrl = new URL(urlAsString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            String loopJsonResultAsString;
            //**  get data as a JSON string  **//
            if(apiKey.isEmpty()){
                System.out.println("API Key is empty");
                String tempFileName = "CVSDistanceMatrix" + Integer.toString(colBlock + 1) + ".json";
                try {
                    loopJsonResultAsString = readJsonFromFileApi(tempFileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                loopJsonResultAsString = getJsonFileAsString(loopUrl);
            }

            //** hashmap of each metricName and value in 2D table **//
            HashMap<String, Integer>[][] metricsTable = getJsonResultAsTable(loopJsonResultAsString);

            //** loop through table adding edges to each vertex **//
            int numRows = metricsTable.length;
            int numCols = metricsTable[0].length;

            for (int row = 0; row < numRows; row++){
                BranchVertex origVertex = origin_VertexMap.get(originStrings.get(row));
                for (int col = 0; col < numCols; col++) {
                    Integer edgeDistance;
                    Integer edgeDuration;
                    try {
                        edgeDuration = metricsTable[row][col].get("duration");
                        edgeDistance = metricsTable[row][col].get("distance");
                    } catch (Exception e) {
                        edgeDuration = Integer.MAX_VALUE;
                        edgeDistance = Integer.MAX_VALUE;
                    }

                    if (edgeDistance != 0) {
                        Edge<BranchVertex> loopEdge = new Edge<>(
                                origVertex,
                                destination_VertexMap.get(destinationStrings.get(col)),
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






    public void divideAndQueryAsFile(String mode, String baseFilename) {
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
            //filename = filename.concat(String.valueOf(k));
            String filename = "src/test/resources/" + baseFilename + k + ".json";
            try {
                saveJsonToFile(loopJsonAsString, filename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            k++;
        }


    }

    public String readJsonFromFileApi(String fileName) throws IOException {
        fileName = "src/test/resources/" + fileName;
        Path filePath = Path.of(fileName);

        return new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
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

    private HashMap<String, Integer>[][] getJsonResultAsTable(String jsonResult) {

        //** Set the size of the table **//
        JSONObject rowsJSONObject = new JSONObject(jsonResult);
        JSONArray rowsJsonArray = rowsJSONObject.getJSONArray("rows");
        JSONObject tempRow = rowsJsonArray.getJSONObject(0);
        JSONArray tempRowElements = tempRow.getJSONArray("elements");
        HashMap<String, Integer>[][] metricsTable = new HashMap[rowsJsonArray.length()][tempRowElements.length()];


        if (rowsJsonArray.length() > 0) {
            //** for each row, get the elements **//
            for (int i = 0; i < rowsJsonArray.length(); i++) {
                JSONObject row = rowsJsonArray.getJSONObject(i);
                JSONArray elementArray = row.getJSONArray("elements");

                //** For each element get the metrics **//
                for (int j = 0; j < elementArray.length(); j++) {
                    metricsTable[i][j] = new HashMap<>();  //** initialize the element **//
                    JSONObject element = elementArray.getJSONObject(j);
                    metricsTable[i][j].put("distance",element.getJSONObject("distance").getInt("value"));
                    metricsTable[i][j].put("duration",element.getJSONObject("duration").getInt("value"));
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
        if (locations.size() == 0){
            return "";
        }
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
            int vIndex = currentGeography.getVertexIndexById(tempLocation.generateUniqueID());
            if (vIndex == -1){
                continue;
//                vIndex = currentGeography.getVertexListSize();
//                currentGeography.addVertex(latitude, longitude);
            }
            BranchVertex loopVertex = currentGeography.getVertex(vIndex);
            location_VertexMap.put(loopLocation, loopVertex);
        }
        return location_VertexMap;
    }
}