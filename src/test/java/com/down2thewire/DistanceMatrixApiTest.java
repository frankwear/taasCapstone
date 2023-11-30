package com.down2thewire;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
public class DistanceMatrixApiTest {


    @Test
    void buildEdgeFromPlacesTest() {
        HashMap<String, String> myParameters = new HashMap<>();
        myParameters.put("location=", "33.8876001,-84.3142002");
        myParameters.put("&type=", "transit_station");
        myParameters.put("&radius=", "2000");
        BranchGeoModel testGM = PlacesApi.buildPlacesFromApiCall(myParameters);
        testGM.printGraph();

        DistanceMatrixApi distanceMatrixApi = new DistanceMatrixApi(testGM);

        distanceMatrixApi.updateGeoModel("WALKING");
        distanceMatrixApi.getCurrentGeoModel().printGraph();
    }


    @Test
    void constructGeoModelWithEdges() throws SQLException {
        BranchGeoModel graph = new BranchGeoModel();
        BranchVertex v01 = graph.addVertex(33.9228732,-84.3418493);
        BranchVertex v02 = graph.addVertex(33.921227,-84.344398);;
        BranchVertex v03 = graph.addVertex(33.789112,-84.387383);
        BranchVertex v04 = graph.addVertex(33.7892632,-84.3873414);
        BranchVertex v05 = graph.addVertex(33.8082253,-84.3934548);
        BranchVertex v06 = graph.addVertex(33.8085817,-84.3943387);

        DistanceMatrixApi myDM = new DistanceMatrixApi(graph);
        myDM.updateGeoModel("DRIVING");
        myDM.getCurrentGeoModel().printGraph();
        DataConnection dmdata= new DataConnection();
        dmdata.insertGeomodelData(myDM.getCurrentGeoModel());
    }

//    @Test
//    void name() {
//    }
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
}