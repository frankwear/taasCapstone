package com.down2thewire;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class DistanceMatrixApi {
    GeographicModel geoModel;
    List<String> origins;
    List<String> destinations;
    String urlAsString;
    private String apiKey = ApiKeys.getGoogleKey();
    public String apiResponseAsString;

    public DistanceMatrixApi(List<String> origins, List<String> destinations, GeographicModel geographicModel) {
        this.origins = origins;
        this.destinations = destinations;
        this.geoModel = geographicModel;
    }
    public DistanceMatrixApi(GeographicModel geographicModel) {
        this.geoModel = geographicModel;
        this.urlAsString = buildDistanceUrl();
    }
//    public String getOriginFromGeomodel(){
//        this.geoModel.getVertices();
//        for(int i = 0 ; i<this.geoModel.getVertexListSize();i++){
//
//        }
//
//    }
    public GeographicModel jsonToGeomodel() {
        int maxRows = 100 / origins.size();

        for (int i = 0; i < destinations.size(); i += maxRows) {
            List<String> subDestinations = new LinkedList<>();
            for (int j = i; j < (i + maxRows); j++) {
                subDestinations.add(destinations.get(j));
            }
            String jsonURLString = buildDistanceUrl(origins, subDestinations);
            addEdgesToGeoModelRectangular(saveJsonToString(jsonURLString));
        }
        return this.geoModel;
    }
    public void addEdgesToGeoModelSquare(String jsonResult) { //duplicate method for  rectangular from square
        JSONObject rowsJSONObject = new JSONObject(jsonResult);
        JSONArray rowsJsonArray = rowsJSONObject.getJSONArray("rows");

        if (rowsJsonArray.length() > 0) {
            for (int i = 0; i < rowsJsonArray.length(); i++) {
                JSONObject row = rowsJsonArray.getJSONObject(i);
                JSONArray elements = row.getJSONArray("elements");
                for (int j = 0; j < elements.length(); j++) {
                    JSONObject element = elements.getJSONObject(j);
                    if (element.getJSONObject("distance").getInt("value") > 30) {
                        Edge2<Vertex2> tempEdge = new Edge2<>();
                        Vertex2 startVertex = geoModel.getVertex(i);
                        Vertex2 endVertex = geoModel.getVertex(j);

                        if (startVertex != null && endVertex != null) {
                            tempEdge.setStart(startVertex);
                            tempEdge.setEnd(endVertex);
                            try {

                                    tempEdge.setCost(element.getJSONObject("fare").getDouble("value"));
                                }
                            catch (Exception e){
                                tempEdge.setCost(0.00);
                            }
                            tempEdge.setDuration(element.getJSONObject("duration").getInt("value"));
                            tempEdge.setDistance(element.getJSONObject("distance").getInt("value"));
                            startVertex.addEdge(tempEdge);

                            Edge2<Vertex2> tempEdge1 = new Edge2<>();
                            tempEdge1.setStart(endVertex);
                            tempEdge1.setEnd(startVertex);
                            tempEdge1.setDuration(element.getJSONObject("duration").getInt("value"));
                            tempEdge1.setDistance(element.getJSONObject("distance").getInt("value"));
                            //tempEdge1.setCost(element.getJSONObject("fare").getDouble("value"));
                                try {
                                    tempEdge1.setCost(element.getJSONObject("fare").getDouble("value"));

                                }
                                catch (Exception ex) {
                                    tempEdge1.setCost(0.00);;
                                }
                            endVertex.addEdge(tempEdge1);
                        }
                    }
                }
            }
        }
    }
    public void addEdgesToGeoModelRectangular(String jsonResult) {
        JSONObject response = new JSONObject(jsonResult);

        JSONArray originAddresses = response.getJSONArray("origin_addresses");
        JSONArray destinationAddresses = response.getJSONArray("destination_addresses");
        JSONArray rows = response.getJSONArray("rows");

        for (int i = 0; i < originAddresses.length(); i++) {
            String originAddress = originAddresses.getString(i);

            for (int j = 0; j < destinationAddresses.length(); j++) {
                String destinationAddress = destinationAddresses.getString(j);
                JSONObject element = rows.getJSONObject(i).getJSONArray("elements").getJSONObject(j);

                // Checking if the distance value is greater than 30
                int distanceValue = element.getJSONObject("distance").getInt("value");
                if (distanceValue > 30) {
                    Vertex2 startVertex = geoModel.findVertexByAddress(originAddress);
                    Vertex2 endVertex = geoModel.findVertexByAddress(destinationAddress);

                    if (startVertex != null && endVertex != null) {
                        Edge2<Vertex2> tempEdge = new Edge2<>();
                        tempEdge.setStart(startVertex);
                        tempEdge.setEnd(endVertex);

                        try {
                            tempEdge.setCost(element.getJSONObject("fare").getDouble("value"));
                        } catch (Exception e) {
                            tempEdge.setCost(0.00);
                        }

                        tempEdge.setDuration(element.getJSONObject("duration").getInt("value"));
                        tempEdge.setDistance(distanceValue);

                        // Adding the edge to the vertices
                        startVertex.addEdge(tempEdge);

                        Edge2<Vertex2> tempEdge1 = new Edge2<>();
                        tempEdge1.setStart(endVertex);
                        tempEdge1.setEnd(startVertex);
                        tempEdge1.setDuration(element.getJSONObject("duration").getInt("value"));
                        tempEdge1.setDistance(distanceValue);

                        try {
                            tempEdge1.setCost(element.getJSONObject("fare").getDouble("value"));
                        } catch (Exception e) {
                            tempEdge1.setCost(0.00);
                        }

                        endVertex.addEdge(tempEdge1);
                    }
                }
            }
        }
    }
    public String saveJsonToString(String jsonUrlString) {
        URL apiEndpoint;
        String jsonText;
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
        this.apiResponseAsString = jsonText;
        return jsonText;
    }
    public String buildDistanceUrl() {
        String myUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?";
        String origin = "";

        for (int i = 0; i < geoModel.getVertexListSize(); i++) {
            String tempOrigin = "";
            origin = origin + geoModel.getVertex(i).getLatitude() + "," +
                    geoModel.getVertex(i).getLongitude() + "|";
            this.origins.add(geoModel.getVertex(i).getLatitude() + "," +
                    geoModel.getVertex(i).getLongitude());
        }
        origin = origin.substring(0, origin.length() - 1);
        myUrl = myUrl + "origins=" + origin.toString() + "&destinations=" + origin.toString();
        myUrl = myUrl + "&units=imperial" + "&mode=walking" + "&key=" + apiKey;
        return myUrl;
    }

    public String buildDistanceUrl(List<String> subOrigins, List<String> subDestinations) {
        String myUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?";
        String originsStr = "";
        String destinationsStr = "";

        if (subOrigins != null && !subOrigins.isEmpty()) {
            originsStr = String.join("|", subOrigins);
        }
        if (subDestinations != null && !subDestinations.isEmpty()) {
            destinationsStr = String.join("|", subDestinations);
        }
        myUrl = myUrl + "origins=" + originsStr + "&destinations=" + destinationsStr;
        myUrl = myUrl + "&units=imperial" + "&mode=walking" + "&key=" + apiKey;
        //mode needs to me modified ;
        return myUrl;
    }
}
