package com.down2thewire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GeoModelAnalyzer {
    GeographicModel geographicMap = new GeographicModel();
    List<Route> routeList = new ArrayList<>();
    WayPoint originWayPoint;

    WayPoint destinationWayPoint;
    RouteRequest modelRouteRequest;



    public GeoModelAnalyzer() {
    }
    public GeoModelAnalyzer(RouteRequest rr) {
        modelRouteRequest = rr;
    }



    public GeographicModel generateGeoModel(){
        String origin = modelRouteRequest.getOrigin();
        String destination = modelRouteRequest.getDestination();
 //       List<String> modes = modelRouteRequest.getModePrefAsList();
        RouteRequest tempRequest = new RouteRequest();
//        Route coreRoute = tempRequest.getRouteFromApi(origin, destination, "transit");

        LinkedList<Route> coreRoute = tempRequest.getRoutesFromApi(origin, destination, "transit", Boolean.TRUE);

        this.originWayPoint = coreRoute.get(0).wayPointLinkedList.getFirst();
        this.destinationWayPoint = coreRoute.get(0).wayPointLinkedList.getLast();
        // the following 7 code lines are to process just the first returned route from the coreRoute list
        // this we become a loop after tested successfully, to process all routes in list

        for (int j = 0; j < coreRoute.size(); j++) {
            coreRoute.get(j).removeAdjacentSameModeEdges();
            this.routeList.add(coreRoute.get(j));
            this.geographicMap.addGraph(convertRouteToGeoModel(coreRoute.get(j)));
        }
        LinkedList<Vertex2> tempVertices= new LinkedList<>();
        tempVertices= (LinkedList<Vertex2>) geographicMap.cloneVertexList();
        for (int i = 1; i < tempVertices.size(); i++) {
            Vertex2 legStart = tempVertices.get(i-1);
            Vertex2 legEnd = tempVertices.get(i);
            // todo - Set up repetitive lookup to a nexted loop


            List<String> modes= Arrays.asList("driving", "bicycling", "transit", "walking");

            for (String loopMode : modes) {
                Route legRoute = tempRequest.getRouteFromApi(legStart, legEnd, loopMode);
                legRoute.removeAdjacentSameModeEdges();

                // todo add to geographic model
                //create a bug and add to sprint
                this.routeList.add(legRoute);
                GeographicModel tempGeoModel = convertRouteToGeoModel(legRoute);
                geographicMap.addGraph(tempGeoModel);
            }
        }
        geographicMap.removeDuplicateVertices();
        //FixMe - some vertices have only one edge
        return geographicMap;
    }

    public GeographicModel convertRouteToGeoModel (Route route) {
            GeographicModel tempGeoModel = new GeographicModel();
            int size = route.wayPointLinkedList.size();
            Vertex2 lastLegDestination = new Vertex2(new Location(0.0d, 0.0d));
            for (int j = 0; j < size-1; j++){
                WayPoint w1 = route.wayPointLinkedList.get(j);
                Vertex2 v1 = new Vertex2(new Location(0.0,0.0));
                if (j==0) {
                    v1 = Vertex2.waypointToVertex(w1);
                } else {
                    v1 = lastLegDestination;
                }
                Edge2<WayPoint> e1 = w1.getEdge();
                WayPoint w2 = route.wayPointLinkedList.get(j+1);
                Vertex2 v2 = Vertex2.waypointToVertex(w2);
                Edge2<Vertex2> forward = new Edge2<>(v1, v2, e1.getMode(), e1.getDuration(), e1.getCost(), e1.getDistance());
                v1.addEdge(forward);
                Edge2<Vertex2> backward = new Edge2<>(v2, v1, e1.getMode(), e1.getDuration(), e1.getCost(), e1.getDistance());
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
