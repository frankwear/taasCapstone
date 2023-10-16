//package com.down2thewire;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.LinkedList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class ApiConnectorTest {
//    private ApiConnector apiConnector;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize the ApiConnector with appropriate parameters
//        String origin = "New York, NY";
//        String destination = "Los Angeles, CA";
//        String mode = "driving";
//        apiConnector = new ApiConnector(origin, destination, mode);
//    }
//
//    @Test
//    void testSaveJsonToString() {
//        // Test whether saveJsonToString() returns a non-null string
//        String json = apiConnector.getJsonStringFromApi();
//        assertNotNull(json);
//    }
//
//    @Test
//    void testConstructRouteList() {
//        // Test whether constructRouteList() returns a non-empty list of routes
//        String json = apiConnector.getJsonStringFromApi();
//        LinkedList<LinearRoute> routes = apiConnector.constructRouteList(json);
//        assertFalse(routes.isEmpty());
//    }
//
//    @Test
//    void testConstructRoute() {
//        // Test whether constructRoute() returns a non-null route
//        String json = apiConnector.getJsonStringFromApi();
//        LinearRoute route = apiConnector.constructRoute(json);
//        assertNotNull(route);
//    }
//}