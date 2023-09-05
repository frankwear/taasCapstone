package com.down2thewire;

import java.util.ArrayList;
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
        List<String> modes = modelRouteRequest.getModePrefAsList();
        RouteRequest tempRequest = new RouteRequest();
//        Route coreRoute = tempRequest.getRouteFromApi(origin, destination, "transit");

        LinkedList<Route> coreRoute = tempRequest.getRoutesFromApi(origin, destination, "transit", Boolean.TRUE);

        // the following 7 code lines are to process just the first returned route from the coreRoute list
        // this we become a loop after tested successfully, to process all routes in list

        modelRouteRequest.originWaypoint = coreRoute.getFirst().wayPointLinkedList.getFirst();
        modelRouteRequest.destinationWaypoint = coreRoute.getFirst().wayPointLinkedList.getLast();
        coreRoute.set(0, removeAdjacentSameModeEdges(coreRoute.getFirst()));
        this.originWayPoint = coreRoute.getFirst().wayPointLinkedList.getFirst();
        this.destinationWayPoint = coreRoute.getFirst().wayPointLinkedList.getLast();
        this.routeList.add(coreRoute.getFirst());
        for (int i = 1; i < coreRoute.getFirst().wayPointLinkedList.size(); i++) {
            WayPoint legStart = coreRoute.getFirst().wayPointLinkedList.get(i-1);
            WayPoint legEnd = coreRoute.getFirst().wayPointLinkedList.get(i);

//            modelRouteRequest.originWaypoint = coreRoute.wayPointLinkedList.getFirst();
//            modelRouteRequest.destinationWaypoint = coreRoute.wayPointLinkedList.getLast();
//            coreRoute = removeAdjacentSameModeEdges(coreRoute));
//            this.originWayPoint = coreRoute.wayPointLinkedList.getFirst();
//            this.destinationWayPoint = coreRoute.wayPointLinkedList.getLast();
//            this.routeList.add(coreRoute);
//            for (int i = 1; i < coreRoute.wayPointLinkedList.size(); i++) {
//                WayPoint legStart = coreRoute.wayPointLinkedList.get(i-1);
//                WayPoint legEnd = coreRoute.wayPointLinkedList.get(i);

            for (String loopMode : modes) {
                Route legRoute = tempRequest.getRouteFromApi(legStart, legEnd, loopMode);
                legRoute = GeoModelAnalyzer.removeAdjacentSameModeEdges(legRoute);
                this.routeList.add(legRoute);
 //               this.geographicMap.addGraph(legRoute);
            }
            geographicMap = GeoModelAnalyzer.removeDuplicateVertices(geographicMap);
        }
        for (int i = 0; i < routeList.size(); i++){
            int size = routeList.get(i).wayPointLinkedList.size();
            Vertex2 lastLegDestination = new Vertex2(new Location(0.0d, 0.0d));
            for (int j = 0; j < size-1; j++){
                WayPoint w1 = routeList.get(i).wayPointLinkedList.get(j);
                Vertex2 v1 = new Vertex2(new Location(0.0,0.0));
                if (j==0) {
                    v1 = Vertex2.waypointToVertex(w1);
                } else {
                     v1 = lastLegDestination;
                }
                Edge2<WayPoint> e1 = w1.getEdge();
                WayPoint w2 = routeList.get(i).wayPointLinkedList.get(j+1);
                Vertex2 v2 = Vertex2.waypointToVertex(w2);
                Edge2<Vertex2> forward = new Edge2<>(v1, v2, e1.getMode(), e1.getDuration(), e1.getCost(), e1.distance);
                v1.addEdge(forward);
                Edge2<Vertex2> backward = new Edge2<>(v2, v1, e1.getMode(), e1.getDuration(), e1.getCost(), e1.distance);
                v2.addEdge(backward);
                geographicMap.addVertex(v1);
                if (j == size-1){
                    geographicMap.addVertex(v2);
                }
                lastLegDestination = v2;
            }
        }
        //FixMe - some vertices have only one edge
        return geographicMap;
    }
    public static Route removeAdjacentSameModeEdges(Route route) {  // considering routes non-branching
//todo - Correct logic on this to have edge as part of vertex.  Also change to apply only to Routes and not geomodels.

        String lastMode = "";
        int listSize = route.wayPointLinkedList.size();
        for (int i = 0; i<listSize-1; i++) {
            if (lastMode.equals(route.wayPointLinkedList.get(i).getEdge().getMode())) {  // do two adjacent edges have the same mode, combine them
                Edge2 edge1 = route.wayPointLinkedList.get(i-1).getEdge();
                Edge2 edge2 = route.wayPointLinkedList.get(i).getEdge();
                edge1.distance += edge2.distance;
                edge1.duration += edge2.duration;
                edge1.cost += edge2.cost;
                edge1.end = edge2.end;
                route.wayPointLinkedList.get(i-1).setEdge(edge1);
                route.wayPointLinkedList.remove(i); listSize--;
                i--;
            }
            lastMode = route.wayPointLinkedList.get(i).getEdge().getMode();
        }
        return route;
    }
/*    public WeightedGraph createRouteFromApi(String origin, String destination) {
        //Todo - Test and Confirm
        RouteRequest transitRoutes = new RouteRequest();
        WeightedGraph transitMap = transitRoutes.getAPIWeightedGraph(origin, destination, "TRANSIT");
        transitMap = removeExtraVerticesFromRoute(transitMap);
        return transitMap;
    } */

    public static GeographicModel removeDuplicateVertices(GeographicModel graph) {
 /*     // This version is not dependent on order, but way less efficient and more complex.  Save in case
        // needed for combined weighted graphs that are not linear.
        */
        // Remove duplicate vertices and reassign edges to kept vertex
        int vLSize = graph.vertexList.size();
        for (int i = 0; i < vLSize; i++) {
            for (int j = i + 1; j < vLSize; j++) {
                if (graph.vertexList.get(i).isMatch(graph.vertexList.get(j))) {
                    // redirect edges from vert j to vert i so you can delete vertex j
                    // delete edges between the two matched vertices
                    int eLSize = graph.edgeList.size();
                    for (int k = 0; k < eLSize; k++) {
                        if (graph.edgeList.get(k).start.isMatch(graph.vertexList.get(j))) {
                            graph.edgeList.get(k).start = graph.vertexList.get(i);
                        }
                        if (graph.edgeList.get(k).end.isMatch(graph.vertexList.get(j))) {
                            graph.edgeList.get(k).end = graph.vertexList.get(i);
                        }
                        if (graph.edgeList.get(k).start.isMatch(graph.edgeList.get(k).end)) {
                            graph.edgeList.remove(k);
                            eLSize--;
                            k--;
                        }
                    }
                    graph.vertexList.remove(j);
                    vLSize--;
                    j--;
                }
            }
        }
        // Remove duplicate edges
        int eLSize = graph.edgeList.size();
        for (int i = 0; i < eLSize; i++) {
            for (int j = i + 1; j < eLSize; j++) {
                if (graph.edgeList.get(i).mode == (graph.edgeList.get(j).mode)) {
                    // reduce repeated lookups with local variables
                    Edge2 edge1 = graph.edgeList.get(i);
                    Edge2 edge2 = graph.edgeList.get(j);
                    if (edge1.start.isMatch(edge2.start) && (edge1.end.isMatch(edge2.end))) {
                        graph.edgeList.remove(j);  eLSize--; j--;
                    }
                    if (edge1.start.isMatch(edge2.end) && (edge1.end.isMatch(edge1.start))) {
                        graph.edgeList.remove(j);  eLSize--; j--;
                    }
                }
            }
        }
        return graph;
    }
}
