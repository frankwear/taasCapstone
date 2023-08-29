package com.down2thewire;

import java.util.ArrayList;
import java.util.List;

public class GeoModel{
    WeightedGraph geographicMap = new WeightedGraph();
    List<WeightedGraph> routeList = new ArrayList<>();
    Vertex2 originVert;
    Vertex2 destinationVert;
    RouteRequest modelRouteRequest;


    public GeoModel() {
    }
    public GeoModel(RouteRequest rr) {
        modelRouteRequest = rr;
    }


    public WeightedGraph generateGeoModel() {
        String origin = modelRouteRequest.getOrigin();
        String destination = modelRouteRequest.getDestination();
        List<String> modes = modelRouteRequest.getModePrefAsList();
        RouteRequest tempRequest = new RouteRequest();
        WeightedGraph coreRoute = tempRequest.getAPIWeightedGraph(origin, destination, "transit");
        modelRouteRequest.originVertex = coreRoute.vertexList.getFirst();
        modelRouteRequest.destinationVertex = coreRoute.vertexList.getLast();
        coreRoute = removeAdjacentSameModeEdges(coreRoute);
        this.originVert = coreRoute.vertexList.getFirst();
        this.destinationVert = coreRoute.vertexList.getLast();
        this.routeList.add(coreRoute);
        for (int i = 1; i < coreRoute.vertexList.size(); i++) {
            Vertex2 legStart = coreRoute.vertexList.get(i-1);
            Vertex2 legEnd = coreRoute.vertexList.get(i);
            for (String loopMode : modes) {
                WeightedGraph legRoute = tempRequest.getAPIWeightedGraph(legStart, legEnd, loopMode);
                legRoute = GeoModel.removeAdjacentSameModeEdges(legRoute);
                this.routeList.add(legRoute);
                this.geographicMap.addGraph(legRoute);
            }
            geographicMap = GeoModel.removeDuplicateVertices(geographicMap);
        }
        //FixMe - some vertices have only one edge
        return geographicMap;
    }
    public static WeightedGraph removeAdjacentSameModeEdges(WeightedGraph route) {  // considering routes non-branching
        String lastMode = "";
        int listSize = route.edgeList.size();
        for (int i = 0; i<listSize; i++) {
            if (lastMode.equals(route.edgeList.get(i).mode)) {  // do two adjacent edges have the same mode, combine them
                Edge2 edge1 = route.edgeList.get(i-1);
                Edge2 edge2 = route.edgeList.get(i);
                edge1.distance += edge2.distance;
                edge1.duration += edge2.duration;
                edge1.cost += edge2.cost;
                edge1.end = edge2.end;
                route.edgeList.set(i-1, edge1);
                route.edgeList.remove(i); listSize--;
                route.vertex.List.remove(route.getVertex(edge2.start));
                i--;
            }
            lastMode = route.edgeList.get(i).mode;
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

    public static WeightedGraph removeDuplicateVertices(WeightedGraph graph) {
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
