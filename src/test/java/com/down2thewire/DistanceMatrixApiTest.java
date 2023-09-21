package com.down2thewire;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

public class DistanceMatrixApiTest {

    @Test
    void buildEdgeTest() {
        List<String> origins = Arrays.asList("33.8876001,-84.3142002", "33.9383904,-84.5210259", "33.7532211,-84.3931609");
        List<String> destinations = Arrays.asList("33.8876001,-84.3142002", "33.9383904,-84.5210259", "33.7532211,-84.3931609");
        HashMap<String, String> myParameters = new HashMap<>();
        myParameters.put("location=", "33.8876001,-84.3142002");
        myParameters.put("&type=", "transit_station");
        myParameters.put("&radius=","2000");
        GeographicModel testGM = PlacesApi.buildPlacesFromApiCall(myParameters);
        DistanceMatrixApi distanceMatrixApi = new DistanceMatrixApi(origins, destinations,testGM );
        distanceMatrixApi.geoModel.printGraph();
        System.out.println(distanceMatrixApi.buildDistanceUrl());
        //PlacesApi placesApi = new PlacesApi(myParameters);
    }

    @Test
    void constructGeoModelWithEdges(){
        HashMap<String, String> myParameters = new HashMap<>();
        myParameters.put("location=", "33.8876001,-84.3142002");
        myParameters.put("type=", "transit_station");
        myParameters.put("radius=","2000");
        GeographicModel testGM = PlacesApi.buildPlacesFromApiCall(myParameters);
        DistanceMatrixApi myDM = new DistanceMatrixApi(testGM);
        myDM.jsonToGeomodel();
        myDM.addEdgesToGeoModelSquare(myDM.apiResponseAsString);
        myDM.geoModel.printGraph();
    }
}
