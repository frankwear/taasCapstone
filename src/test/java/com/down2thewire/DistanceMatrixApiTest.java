package com.down2thewire;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DistanceMatrixApiTest {

//    void testAddEdgesToGeoModelFromApi() {
//        // Create a GeographicModel with some vertices for testing
//        GeographicModel testGeoModel = createTestGeoModel();
//        // Create an instance of DistanceMatrixApi
//        DistanceMatrixApi dm = new DistanceMatrixApi(testGeoModel);
//        // test values for parameters (startLocation, endLocations, mode)
//        String startLocation = "33.8876001,-84.3142002";
//        String endLocations = "33.8876001,-84.3142002|33.8876002,-84.3142003";
//        String mode = "walking";
//        // Call the method to add edges to the GeoModel from the API
//        GeographicModel updatedGeoModel = dm.addEdgesToGeomodelFromApi(testGeoModel, startLocation, endLocations, mode);
//        // Assert that the updatedGeoModel is not null
//        assertNotNull(updatedGeoModel);
//
//        // Test if the edges have been added to the GeoModel as expected
//        testEdgesInGeoModel(updatedGeoModel);
//    }
//    private void testEdgesInGeoModel(GeographicModel geoModel) {
//        // Assuming you have a method to get vertices from the GeoModel
//        List<Vertex2> vertices = geoModel.getVertices();
//
//        // Assert that the number of vertices is as expected
//        assertEquals(3, vertices.size());
//
//        // Test edges for each vertex
//        for (Vertex2 vertex : vertices) {
//            if (vertex.getDescription().equals("33.8876001,-84.3142002")) {
//                // Check if this vertex has the expected number of edges, e.g., assuming it should have 2 edges
//                for(int i=0; i<vertex.getEdgeListSize();i++){
//                assertEquals(2, vertex.getEdge(i));
//                }
//
//                // You can further test properties of the edges if needed
//                // For example, you can check the mode or duration of each edge.
//            } else {
//                // Handle other vertices similarly if needed
//            }
//        }
//    }
//    @Test
//    //test1
//    void constructGeoModelWithEdges() {
//        GeographicModel testGeoModel = createTestGeoModel();
//        DistanceMatrixApi distanceMatrixApi = new DistanceMatrixApi(testGeoModel);
//        // variables for test parameters (startLocation, endLocations, mode)
//        String startLocation = "33.8876001,-84.3142002";
//        String endLocations = "33.8876001,-84.3142002|33.8876002,-84.3142003"; // Pipe-separated string
//        String mode = "walking";
//        GeographicModel updatedGeoModel = distanceMatrixApi.addEdgesToGeomodelFromApi(testGeoModel, startLocation, endLocations, mode);
//        assertNotNull(updatedGeoModel); //test tp see if geomodel is not null
//        testGeoModel.printGraph();//check to print
//    }
//
//
//
//    private GeographicModel createTestGeoModel() {
//        // Create a test GeographicModel with some vertices
//        GeographicModel geoModel = new GeographicModel();
//
//        // Add vertices to the GeoModel
//        Vertex2 vertex1 = new Vertex2(new Location(33.8876001, -84.3142002));
//        Vertex2 vertex2 = new Vertex2(new Location(33.8876002, -84.3142003));
//        Vertex2 vertex3 = new Vertex2(new Location(33.8876003, -84.3142004));
//
//        geoModel.addVertex(vertex1);
//        geoModel.addVertex(vertex2);
//        geoModel.addVertex(vertex3);
//
//        return geoModel;
//    }
    @Test
    void buildEdgeTest() {
        String origins = "33.8876001,-84.3142002|33.9383904,-84.5210259|33.7532211,-84.3931609";
        String destinations = "33.8876001,-84.3142002|33.9383904,-84.5210259|33.7532211,-84.3931609";
        HashMap<String, String> myParameters = new HashMap<>();
        myParameters.put("location=", "33.8876001,-84.3142002");
        myParameters.put("&type=", "transit_station");
        myParameters.put("&radius=","2000");
        GeographicModel testGM = PlacesApi.buildPlacesFromApiCall(myParameters);
        //testGM.printGraph();
        DistanceMatrixApi distanceMatrixApi = new DistanceMatrixApi(testGM);
        GeographicModel updatedGeoModel = distanceMatrixApi.addEdgesToGeomodelFromApi(testGM, origins, destinations, "walking");
        distanceMatrixApi.getLocalGeography(); //.printGraph();
        //System.out.println(distanceMatrixApi.buildDistanceUrl());
        //PlacesApi placesApi = new PlacesApi(myParameters);
    }
}
//
//public class DistanceMatrixApiTest {
//

//
//    @Test
//    void constructGeoModelWithEdges(){
//        HashMap<String, String> myParameters = new HashMap<>();
//        myParameters.put("location=", "33.8876001,-84.3142002");
//        myParameters.put("type=", "transit_station");
//        myParameters.put("radius=","2000");
//        GeographicModel testGM = PlacesApi.buildPlacesFromApiCall(myParameters);
//        DistanceMatrixApi myDM = new DistanceMatrixApi(testGM);
//        myDM.jsonToGeomodel();
//        myDM.addEdgesToGeoModelSquare(myDM.jsonResultString);
//        myDM.geoModel.printGraph();
//    }
//}
