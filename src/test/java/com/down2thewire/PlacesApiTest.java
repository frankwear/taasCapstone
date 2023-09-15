package com.down2thewire;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

class PlacesApiTest {
    @Test
    void buildLocationsFromApiStaticTEST() {
        HashMap<String, String> myParameters = new HashMap<>();
        myParameters.put("location=", "33.8876001,-84.3142002");
        myParameters.put("&type=", "transit_station");
        myParameters.put("&radius=","7500");
        BranchGeoModel testGM = PlacesApi.buildPlacesFromApiCall(myParameters);
        testGM.printGraph();
    }
}