package com.down2thewire;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouteRequestTest {

    @Test
    void setPriority() {
        RouteRequest testRR = new RouteRequest();
        testRR.setPriority("CHEAP");
        assertTrue(testRR.priority == "CHEAP");
        testRR.setPriority("WILD PARTY");
        assertTrue(testRR.priority == " ");
    }

    @Test
    void validatePriority() {
    }

    @Test
    void getPriority() {
    }

    @Test
    void setOriginVertex() {
    }

    @Test
    void getOriginVertex() {
    }

    @Test
    void setDestinationVertex() {
    }

    @Test
    void getDestinationVertex() {
    }

    @Test
    void generateTransitRequestTest2() {
    }

    @Test
    void setEndsDunwoodyToAmtrak() {
    }

    @Test
    void setEndsKennesawToHartsfield() {
    }

    @Test
    void setEndsPigNChikToBurlingtonCoatFac() {
    }

    @Test
    void setEndsOfficeBarToKingQueen() {
    }

    @Test
    void getAPIWeightedGraph() {
    }

    @Test
    void setModePrefFromAccount() {
    }

    @Test
    void userSetup() {
        UserAccount testUser = new UserAccount("SallyFields");
        testUser.initializeTransitUser();  // Transit Mode and CHEAP priority

        // Get settings from user account
        RouteRequest testRR = new RouteRequest();
        testRR.setModePrefFromAccount(testUser);
        testRR.setPriority(testUser.getPriority());
        testRR.setOrigin("Dunwoody Marta Station");
        testRR.setDestination("Piedmont Atlanta Hospital");
        GeoModel testModel = new GeoModel(testRR);
        WeightedGraph resultingGraph = testModel.generateGeoModel();
        resultingGraph.printGraph();
    }
}