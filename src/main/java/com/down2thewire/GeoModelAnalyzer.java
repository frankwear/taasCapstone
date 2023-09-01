package com.down2thewire;

import java.util.ArrayList;
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
        Route coreRoute = tempRequest.getAPIWeightedGraph(origin, destination, "transit");
        modelRouteRequest.originWaypoint = coreRoute.wayPointLinkedList.getFirst();
        modelRouteRequest.destinationWaypoint = coreRoute.wayPointLinkedList.getLast();
        coreRoute = removeAdjacentSameModeEdges(coreRoute);
        this.originWayPoint = coreRoute.wayPointLinkedList.getFirst();
        this.destinationWayPoint = coreRoute.wayPointLinkedList.getLast();
        this.routeList.add(coreRoute);
        for (int i = 1; i < coreRoute.wayPointLinkedList.size(); i++) {
            WayPoint legStart = coreRoute.wayPointLinkedList.get(i-1);
            WayPoint legEnd = coreRoute.wayPointLinkedList.get(i);
            for (String loopMode : modes) {
                Route legRoute = tempRequest.getAPIWeightedGraph(legStart, legEnd, loopMode);
                legRoute = GeoModelAnalyzer.removeAdjacentSameModeEdges(legRoute);
                this.routeList.add(legRoute);
 //               this.geographicMap.addGraph(legRoute);
            }
            geographicMap = GeoModelAnalyzer.removeDuplicateVertices(geographicMap);
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
