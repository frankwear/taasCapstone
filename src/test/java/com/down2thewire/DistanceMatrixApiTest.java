package com.down2thewire;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
public class DistanceMatrixApiTest {

    @Test
    void buildEdgeTest(GeographicModel gm) {
        HashMap<String, String> myParameters = new HashMap<>();
        myParameters.put("origins=", "33.8876001,-84.3142002|33.9383904,-84.5210259|33.7532211,-84.3931609");
        myParameters.put("&destinations=", "33.8876001,-84.3142002|33.9383904,-84.5210259|33.7532211,-84.3931609");
        myParameters.put("&mode=","walking");
        myParameters.put("&units=", "imperial");
        DistanceMatrixApi distanceMatrixApi = new DistanceMatrixApi(myParameters);
        distanceMatrixApi.geoModel.printGraph();
        // System.out.println(distanceMatrixApi.buildDistanceUrl(myParameters));
        //PlacesApi placesApi = new PlacesApi(myParameters);
    }
    @Test
    void test1(){
        HashMap<String, String> myParameters = new HashMap<>();
        myParameters.put("location=", "33.8876001,-84.3142002");
        myParameters.put("&type=", "transit_station");
        myParameters.put("&radius=","2000");
        PlacesApi placesApi = new PlacesApi(myParameters);
        System.out.println(placesApi.urlAsString);
        GeographicModel testGM = placesApi.constructGeoModel();
        DistanceMatrixApi myDM = new DistanceMatrixApi(testGM);
        myDM.saveJsonToString();
        myDM.addEdgesToGeoModel(myDM.jsonResultString);
        myDM.geoModel.printGraph();
    }
}