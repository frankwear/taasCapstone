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

    public void removeDuplicateVertices() {
        geographicMap.sortVertexList();

        for (int i = geographicMap.vertexList.size() - 1; i > 0; i--) {
            Vertex2 currentVertex = geographicMap.vertexList.get(i);

            Vertex2 nextVertex = geographicMap.vertexList.get(i - 1);

            if (currentVertex.getId().equals(nextVertex.getId())) {
                LinkedList<Edge2> tempEdges= geographicMap.vertexList.get(i).outgoingEdges;
                for(int j = 0; j< tempEdges.size();j++){
                    tempEdges.get(j).start=nextVertex;
                    nextVertex.outgoingEdges.add(tempEdges.get(j));
                }
                geographicMap.vertexList.remove(i);//modified all edges; delete vertex i
            }
        }
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
            coreRoute.set(j, removeAdjacentSameModeEdges(coreRoute.get(j)));
            this.routeList.add(coreRoute.get(j));
            this.geographicMap.addGraph(convertRouteToGeoModel(coreRoute.get(j)));
        }
        LinkedList<Vertex2> tempVertices= new LinkedList<>();
        tempVertices= (LinkedList<Vertex2>) geographicMap.vertexList.clone();
        for (int i = 1; i < tempVertices.size(); i++) {
            Vertex2 legStart = tempVertices.get(i-1);
            Vertex2 legEnd = tempVertices.get(i);
            // todo - Set up repetitive lookup to a nexted loop


            List<String> modes= Arrays.asList("driving", "bicycling", "transit", "walking");

            for (String loopMode : modes) {
                Route legRoute = tempRequest.getRouteFromApi(legStart, legEnd, loopMode);
                legRoute = GeoModelAnalyzer.removeAdjacentSameModeEdges(legRoute);

                // todo add to geographic model
                //create a bug and add to sprint
                this.routeList.add(legRoute);
                GeographicModel tempGeoModel = convertRouteToGeoModel(legRoute);
                geographicMap.addGraph(tempGeoModel);
            }
        }
        removeDuplicateVertices();
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
                Edge2<Vertex2> forward = new Edge2<>(v1, v2, e1.getMode(), e1.getDuration(), e1.getCost(), e1.distance);
                v1.addEdge(forward);
                Edge2<Vertex2> backward = new Edge2<>(v2, v1, e1.getMode(), e1.getDuration(), e1.getCost(), e1.distance);
                v2.addEdge(backward);
                tempGeoModel.addVertex(v1);
                if (j == size-2){
                    tempGeoModel.addVertex(v2);
                }
                lastLegDestination = v2;
            }
        return tempGeoModel;
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
        //redo this method
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
