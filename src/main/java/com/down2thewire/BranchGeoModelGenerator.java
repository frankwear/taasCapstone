package com.down2thewire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BranchGeoModelGenerator {

    BranchGeoModel geographicMap = new BranchGeoModel();
    List<LinearRoute> routeList = new ArrayList<>();
    LinearWayPoint originWayPoint;
    LinearWayPoint destinationWayPoint;
    UserRouteRequest modelRouteRequest;



    public BranchGeoModelGenerator() {
    }
    public BranchGeoModelGenerator(UserRouteRequest rr) {
        modelRouteRequest = rr;
    }
    public BranchGeoModelGenerator(Location center, Integer radiusInFeet, String mode) {

    }


    public BranchGeoModel generateGeoModel(){
        String origin = modelRouteRequest.getOrigin();
        String destination = modelRouteRequest.getDestination();
 //       List<String> modes = modelRouteRequest.getModePrefAsList();
        UserRouteRequest tempRequest = new UserRouteRequest();
//        Route coreRoute = tempRequest.getRouteFromApi(origin, destination, "transit");

        LinkedList<LinearRoute> coreRoute = tempRequest.getRoutesFromApi(origin, destination, "transit", Boolean.TRUE);

        this.originWayPoint = coreRoute.get(0).wayPointLinkedList.getFirst();
        this.destinationWayPoint = coreRoute.get(0).wayPointLinkedList.getLast();
        // the following 7 code lines are to process just the first returned route from the coreRoute list
        // this we become a loop after tested successfully, to process all routes in list

        for (int j = 0; j < coreRoute.size(); j++) {
            coreRoute.get(j).removeAdjacentSameModeEdges();
            this.routeList.add(coreRoute.get(j));
            this.geographicMap.addGraph(convertRouteToGeoModel(coreRoute.get(j)));
        }
        LinkedList<BranchVertex> tempVertices= new LinkedList<>();
        tempVertices= (LinkedList<BranchVertex>) geographicMap.cloneVertexList();
        for (int i = 1; i < tempVertices.size(); i++) {
            BranchVertex legStart = tempVertices.get(i-1);
            BranchVertex legEnd = tempVertices.get(i);
            // todo - Set up repetitive lookup to a nexted loop


            List<String> modes= Arrays.asList("driving", "bicycling", "transit", "walking");

            for (String loopMode : modes) {
                LinearRoute legRoute = tempRequest.getRouteFromApi(legStart, legEnd, loopMode);
                legRoute.removeAdjacentSameModeEdges();

                // todo add to geographic model
                //create a bug and add to sprint
                this.routeList.add(legRoute);
                BranchGeoModel tempGeoModel = convertRouteToGeoModel(legRoute);
                geographicMap.addGraph(tempGeoModel);
            }
        }
        geographicMap.removeDuplicateVertices();
        //FixMe - some vertices have only one edge
        return geographicMap;
    }

    public BranchGeoModel convertRouteToGeoModel (LinearRoute route) {
            BranchGeoModel tempGeoModel = new BranchGeoModel();
            int size = route.wayPointLinkedList.size();
            BranchVertex lastLegDestination = new BranchVertex(new Location(0.0d, 0.0d));
            for (int j = 0; j < size-1; j++){
                LinearWayPoint w1 = route.wayPointLinkedList.get(j);
                BranchVertex v1 = new BranchVertex(new Location(0.0,0.0));
                if (j==0) {
                    v1 = BranchVertex.waypointToVertex(w1);
                } else {
                    v1 = lastLegDestination;
                }
                Edge<LinearWayPoint> e1 = w1.getEdge();
                LinearWayPoint w2 = route.wayPointLinkedList.get(j+1);
                BranchVertex v2 = BranchVertex.waypointToVertex(w2);
                Edge<BranchVertex> forward = new Edge<>(v1, v2, e1.getMode(), e1.getDuration(), e1.getCost(), e1.getDistance());
                v1.addEdge(forward);
                Edge<BranchVertex> backward = new Edge<>(v2, v1, e1.getMode(), e1.getDuration(), e1.getCost(), e1.getDistance());
                v2.addEdge(backward);
                tempGeoModel.addVertex(v1);
                if (j == size-2){
                    tempGeoModel.addVertex(v2);
                }
                lastLegDestination = v2;
            }
        return tempGeoModel;
    }
}
