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
public class DistanceMatrixApi {
    GeographicModel geoModel;
    HashMap<String, String> parameters;
    String jsonURL;
    private String apiKey = ApiKeys.getGoogleKey();
    public String jsonResultString;

    public DistanceMatrixApi(HashMap<String, String> parameters) {
        this.parameters = parameters;
        PlacesApi placesApi = new PlacesApi(parameters);
        this.geoModel = placesApi.buildPlacesFromApiCall();
        String jsonURLString = buildDistanceUrl();
        this.jsonURL = jsonURLString;
        addEdgesToGeoModel(saveJsonToString());
    }
    public DistanceMatrixApi(GeographicModel geographicModel){
        this.geoModel = geographicModel;
        String jsonURLString = buildDistanceUrl();
        this.jsonURL = jsonURLString;
    }


    public void addEdgesToGeoModel(String jsonResult) {
        //GeographicModel apiGm = new GeographicModel();
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
                        tempEdge.setStart(geoModel.getVertex(i));
                        tempEdge.setEnd(geoModel.getVertex(j));
                        tempEdge.setMode(parameters.get("&mode="));
                        tempEdge.setDuration(element.getJSONObject("duration").getInt("value"));
                        tempEdge.setDistance(element.getJSONObject("distance").getInt("value"));
                        geoModel.getVertex(i).addEdge(tempEdge);

                        Edge2<Vertex2> tempEdge1 = new Edge2<>();
                        tempEdge1.setEnd(geoModel.getVertex(i));
                        tempEdge1.setStart(geoModel.getVertex(j));
                        tempEdge1.setMode(parameters.get("&mode="));
                        tempEdge1.setDuration(element.getJSONObject("duration").getInt("value"));
                        tempEdge1.setDistance(element.getJSONObject("distance").getInt("value"));
                        geoModel.getVertex(j).addEdge(tempEdge1);
                    }
                }
            }
        }
    }

    public void addEdges() {

    }

    public String saveJsonToString() {
        URL apiEndpoint;
        String jsonText;
        HttpURLConnection connection;
        try {
            apiEndpoint = new URL(this.jsonURL);
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

    public String buildDistanceUrl() {
        String myUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?";
        String origin = "";

        for (int i = 0; i < geoModel.getVertexListSize(); i++) {
            origin = origin + geoModel.getVertex(i).getLatitude() + "," +
                    geoModel.getVertex(i).getLongitude() + "|";
        }
        origin = origin.substring(0, origin.length() - 1);
        myUrl = myUrl + "origins=" + origin.toString() + "&destinations=" + origin.toString();
        myUrl = myUrl + "&units=imperial" + "&mode=walking" + "&key=" + apiKey;
        return myUrl;
    }
}