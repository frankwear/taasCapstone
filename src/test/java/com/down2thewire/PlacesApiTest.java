package com.down2thewire;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PlacesApiTest {
    @Test
    void buildDirectionsUrlTEST() {
        HashMap<String, String> myParameters = new HashMap<>();
        myParameters.put("location=", "33.8876001,-84.3142002");
        myParameters.put("&type=", "transit_station");
        myParameters.put("&radius=","7500");
        PlacesApi placesApi = new PlacesApi(myParameters);
        System.out.println(placesApi.urlAsString);
        GeographicModel testGM = placesApi.constructGeoModel();
        testGM.printGraph();
    }
}