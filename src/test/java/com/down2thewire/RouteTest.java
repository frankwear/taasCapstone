package com.down2thewire;

import org.junit.jupiter.api.Test;

import java.io.InvalidClassException;

import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {
    @Test
    void AddVertexAndDuplicate(){
    Route route = new Route();
    WayPoint w1 = new WayPoint(new Location(34.0380828, -84.584152));
    //Vertex v2 = new Vertex(new Location(34.0380828, -84.584152), "Marietta");

    assertTrue(route.isUnique(w1));
    //assertFalse(route.isUnique(v2));

    }
    @Test
    void VertexLocation(){
        Route route = new Route();
        Vertex v1 = new Vertex(new Location(0.0, 0.0), "Marietta");
        assertTrue(route.isUnique(v1));
    }
}
