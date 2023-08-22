package com.down2thewire;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WeightedGraphNameGenerator {
    private String apiKey = ApiKeys.getGoogleKey();;  // Todo - Outdated API key.  Teams chat with Jeff.

    public String getHumanReadableName(double latitude, double longitude) {
        String url = buildGeocodingUrl(latitude, longitude, apiKey);
        String jsonText = getJsonFromUrl(url);

        String humanReadableName = parseHumanReadableName(jsonText);
        return humanReadableName;
    }

    private static String buildGeocodingUrl(double latitude, double longitude, String apiKey) {
        return "https://maps.googleapis.com/maps/api/geocode/json?" +
                "latlng=" + latitude + "," + longitude +
                "&key=" + apiKey;
    }

    private String getJsonFromUrl(String url) {
        URL apiEndpoint = null;
        String jsonText;
        HttpURLConnection connection = null;
        try {
            apiEndpoint = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = (HttpURLConnection) apiEndpoint.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            InputStream inputStream = connection.getInputStream();
            jsonText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            jsonText = "location name unknown";
            // todo - add better error handling
//            throw new RuntimeException(e);
        }
        connection.disconnect();
        return jsonText;
    }

    private String parseHumanReadableName(String json) {
        JSONObject geocodeJson = new JSONObject(json);
        JSONArray resultsArray = geocodeJson.getJSONArray("results");
        if (resultsArray.length() > 0) {
            JSONObject result = resultsArray.getJSONObject(0);
            return result.getString("formatted_address");
        }
        return null;
    }
}
