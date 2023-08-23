package com.down2thewire;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    @Test
    void generateUniqueID() {
        Location testloc = new Location(34.0380828, -84.584152);
        Long id = testloc.generateUniqueID();
        Long l1 = 1925440135880L;
        assertEquals(l1,id, "The result is correct");
    }
}