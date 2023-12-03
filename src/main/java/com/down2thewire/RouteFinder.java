package com.down2thewire;

import java.util.LinkedList;

public class RouteFinder {

    private BranchGeoModel localNodes;
    private UserRouteRequest userRouteRequest;
    private BranchVertex userRequestOriginVertex;
    private BranchVertex userRequestDestinationVertex;

    // 9-16-23 added origin and destination to constructor as Vertices to work with findRoutesFromHere()
    public RouteFinder(BranchGeoModel localNodes, UserRouteRequest userRouteRequest) {
        this.localNodes = localNodes;
        this.userRouteRequest = userRouteRequest;
        BranchVertex tempOrigin = new BranchVertex(userRouteRequest.originWaypoint);  //create a vertex from a waypoint
        tempOrigin = addCloseNeighbors(tempOrigin, 5, "WALKING");  //todo - loop for all modes?
        this.userRequestOriginVertex = tempOrigin;
        tempOrigin = new BranchVertex(userRouteRequest.destinationWaypoint);
        tempOrigin = addCloseNeighbors(tempOrigin, 5, "WALKING");
        this.userRequestDestinationVertex = tempOrigin;
    }

    // 9-16-23 added method to integrate origin and destination into known GeoModel
    private BranchVertex addCloseNeighbors(BranchVertex v, int howMany, String mode) {
        LinkedList<BranchVertex> nearList = new LinkedList<>();  // List sorted by distance to v
        Long threshold = 0l;
        // create a list of vertices sorted by distance
        for (BranchVertex nodeVertex : localNodes.cloneVertexList()) {
            Long distanceToNode = Math.abs(v.getId() - nodeVertex.getId());
            // add in sorted order
            int i = 0;
            for (BranchVertex nearNode : nearList) {
                if (distanceToNode > Math.abs(nearNode.getId() - v.getId())) {
                    break;
                }
                i++;
            }
            nearList.add(i, nodeVertex);
        }

        // add the closest edges
        for (int i = 0; i < howMany; i++) {
            Edge<BranchVertex> tempEdge = new Edge(this.userRequestOriginVertex, nearList.get(i), mode, 0, 0.0, 0);
            Integer tempDistance = tempEdge.estimateDistance();
            tempEdge.estimateDuration(tempDistance);
            tempEdge.estimateCost();
            v.addEdge(tempEdge);
        }
        return v;
    }

    // 9-20-23 attempting Dijkstra algorithm
//    public LinearRoute findDijkstraRoute (String mode, String metric) {
//        // localNodes, origin, and destination from "this".
// //       ArrayList<DNode> dijkNodes = new ArrayList<>[];
//    }

    class DNode extends Node{
        // DNode is a specializedVertex for the Dijkstra algorithm
        public DNode (BranchVertex vertex) {

        }
    }





//    public LinkedList<LinearRoute> generateAllViableRoutes (){
//        return findRoutesFromHere(userRequestOriginVertex, userRequestDestinationVertex, new LinkedList<LinearRoute>(), new LinearRoute());
//    }


    /* variable scope and type:
        GeoModel localNodes scope: Class and final: final can not be enforced because GeoModel variables are not final
        Stack<WayPoints> path scope: Class
            WayPoints pathStep scope: created to add to path
        LinkedList<Route> knownPaths scope: Class

    */
/*

    private void findRouteSimplePseudo(Vertex currentVertex){
        For(edges in CurrentVertex){
            if(edge.getEnd() == userRequestDestinationVertex){
                add path to list as route;  //*** route must not be same instance as path (clone or change type)***
                continue to next edge for current vertex;
            } else {
                if(you can rule out the route){
                    continue to next edge for current vertex;
                }
            }
            findRouteSimplePseudo(edge.getEnd());

        }
    }

    private LinkedList<LinearRoute> findRoutesPseudoCode(BranchVertex currentVertex){
        if(isDestination(currentNode)){
            addToListOfRoute(deepCloneCurrentPath());
            removeLastFromPath();
        }
        if(isInViolationOfRules:,
            isVisitedInPath,
            isSecondInstanceOfDriving,
            pathDistancePlusRemainingLongerThanTwiceBirdPath,
            newLegDoublesRemainingDistance){
                then MarkVisited and GoBackOneAndCheckNextEdge
        }
        //??
        if(hitsAVertexThatIsComplete){
            addAllPathsFromThatVertexToPath
        }


    }
}

    private LinkedList<LinearRoute> findRoutesFromHere(BranchVertex currentVertex, BranchVertex destinationVertex, LinkedList<LinearRoute> currentList, LinearRoute currentPath){

        // prepare localNodes sort and remove duplicates
        localNodes.removeDuplicateVertices();
        LinearWayPoint tempWaypoint = new LinearWayPoint(currentVertex.getLocation());
        tempWaypoint.setDescription(currentVertex.getDescription());
        currentPath.

        // add vertex and metrics to current route from current edge
        if (currentID.equals(destinationID)){
            return currentList.add(currentPath.deepClone());

            //add route with metrics to LinkedList, step back one
        }
        foreach (edge in current vertex edges){
            LinkedList<LinearRoute> loopRouteList = (LinkedList<LinearRoute>) currentList.clone();
            findRoutesFromHere(loopEdge.endID)
        }
        if (//CurrentID is on visitedID list
        ){

        return new LinkedList<LinearRoute>();
        }
    }

*/

//    static GeographicModel routeModel = new GeographicModel();
//    RouteRequest userRequest = new RouteRequest();
//
//
//
//    public RouteAnalyzer(GeographicModel routeModel, RouteRequest userRequest) {
//        this.routeModel = routeModel;
//        this.userRequest = userRequest;
//    }
//
//    public LinkedList<GeographicModel> getBest4UserRoutes() {
//        LinkedList<GeographicModel> allPaths = getAllPaths(
//                userRequest.modePref,
//                userRequest.origin,
//                userRequest.destination
//        );
//
//        // Todo - Implement the logic to get the best general route based on user preferences and priority
//
//        return allPaths;
//    }
///*
//    private List<WeightedGraph> getBest1ModeRoute(int mode, WeightedGraph geoModel, String priority) {
//        String mandatedMode = WeightedGraph.Edge.getMode(mode);
//    //TODO remove priority
//        // Create a copy of the geoModel
//        WeightedGraph weightedGraphCopy = new WeightedGraph();
//        weightedGraphCopy.addGraph(geoModel);
//
//        // Remove edges that are incompatible with the mandated mode
//        List<WeightedGraph.Edge> incompatibleEdges = new ArrayList<>();
//        for (WeightedGraph.Edge edge : weightedGraphCopy.edgeList) {
//            if (!edge.getMode().equals(mandatedMode)) {
//                incompatibleEdges.add(edge);
//            }
//        }
//        weightedGraphCopy.edgeList.removeAll(incompatibleEdges);
//
//        // Get the origin and destination from the user request
//        WeightedGraph.Vertex originVert;
//        originVert = geoModel.vertexList.get(0);
//
//        WeightedGraph.Vertex destinationVert;
//        destinationVert = geoModel.vertexList.get(geoModel.vertexList.size()-1);
//
//        String destination = userRequest.destination;
//
//    // Find the best path in the modified weighted graph
//        List<WeightedGraph> bestPaths = getMultiModalBestPath(originVert, destinationVert, Collections.singletonList(mandatedMode));
//
//        // Return the best paths with the mandated mode
//        return bestPaths;
//    }
//
// */
///*
//    private List<WeightedGraph.Path> getBestGeneralRoute(WeightedGraph geoModel, int[] modePref, String priority) {
//        List<WeightedGraph.Path> bestRoute = new ArrayList<>();
//        double bestCost = Double.POSITIVE_INFINITY;
//
//        // Iterate over all vertices in the geoModel
//        for (WeightedGraph.Vertex startVertex : geoModel.vertexList) {
//            // Find the best paths from the current start vertex to all other vertices
//            List<WeightedGraph.Path> paths = geoModel.getBestPaths(startVertex.vertexName, null, null);
//
//            // Sort the paths based on the user's priority
//            sortPathsByPriority(paths, priority);
//
//            // Check if the current best path is better than the previously found best route
//            if (!paths.isEmpty() && paths.get(0).getTotalCost() < bestCost) {
//                // Update the best route and cost
//                bestRoute = new ArrayList<>(paths);
//                bestCost = paths.get(0).getTotalCost();
//            }
//        }
//        return bestRoute;
//    }*/
//
//    /*
//        private List<Path> createPathsFromPath(Path path) {
//            List<Path> paths = new ArrayList<>();
//            paths.add(path);
//            return paths;
//        }
//
//     */
//    public LinkedList<GeographicModel> getAllPaths(){
//        return getAllPaths(this.userRequest.modePref, this.userRequest.getOrigin(), this.userRequest.getDestination());
//    }
//    public LinkedList<GeographicModel> getAllPaths(int[] modePref, String origin, String destination) {
//        List<String> allowableModes = new ArrayList<>();
//        //todo change to Vertex2 ;
//        LinkedList<Vertex2> visited = new LinkedList<>();
//        LinkedList<GeographicModel> allPaths = new LinkedList<>();
//        Vertex2 startVertex = Vertex2.waypointToVertex(userRequest.originWaypoint);
//        Vertex2 endVertex = Vertex2.waypointToVertex(userRequest.destinationWaypoint);
//
//
//        // Map the mode preferences to the corresponding modes
//        for (int i = 0; i < modePref.length; i++) {
//            if (modePref[i] > 0) {
//                String mode = Edge.getMode(i);
//                allowableModes.add(mode);
//            }
//        }
//
//        // Initialize Recursive search for all available routes
////        allPaths = getAllPathsFromHere(startVertex, endVertex, allowableModes, new GeographicModel(), visited);
//
//        return allPaths;
//    }
//
///*    public LinkedList<WeightedGraph> getAllPathsFromHere(WeightedGraph.Vertex currentVertex,
//                                                         WeightedGraph.Vertex endVertex,
//                                                         List<String> allowableModes,
//                                                         WeightedGraph currentPath,
//                                                         LinkedList<WeightedGraph.Vertex> visited,
//                                                         LinkedList<WeightedGraph> allPathsFromHere) {
//
//        visited.add(currentVertex);
//        currentPath.addJustVertex(currentVertex);
//
//        // clone currentPath for new instance - keep same vertices or edges
//        WeightedGraph currentPathInstance = new WeightedGraph();
//        currentPathInstance.cloneWgToListLevel(currentPath);
//
//        List<WeightedGraph.Edge> adjacentEdgeList = currentVertex.getOutgoingEdges(this.routeModel.edgeList); //todo - remove edges with end in visited
//        adjacentEdgeList.removeAll(visited);
//        if (adjacentEdgeList.size() == 0) { // dead-end without reaching endVertex
//            return allPathsFromHere;
//        }
//        for (WeightedGraph.Edge edge : adjacentEdgeList) {
//            if (edge.getEnd().isMatch(endVertex)) {
//                currentPathInstance.addJustEdge(edge);
//                currentPathInstance.addJustVertex(edge.getEnd());
//                allPathsFromHere.add(currentPathInstance);  // fixme - have to add a clone of the current path
//                currentPathInstance.removeLastVertex();
//                currentPathInstance.removeLastEdge();
//            } else {
//                WeightedGraph.Vertex neighbor = edge.getEnd();
//                currentPathInstance.addJustEdge(edge);
//                allPathsFromHere = (getAllPathsFromHere(neighbor, endVertex, allowableModes, currentPathInstance, visited, allPathsFromHere));
//                // Todo - make allPathsFromHere into the collection of paths from the here to the destination and loop
//                // through them adding the last vertex and edge from the current path to each.
//                currentPathInstance.removeLastEdge();
//            }
//        }
//        visited.remove(currentVertex);  // check instances and currentPath before continuing
////        currentPath.removeLastEdge();
//        currentPath.removeLastVertex();
//        return allPathsFromHere;
//    }
//*/
//
////    public LinkedList<GeographicModel> getAllPathsFromHere(Vertex2 currentVertex,
////                                                           Vertex2 endVertex,
////                                                           List<String> allowableModes,
////                                                           GeographicModel currentPath,
////                                                           LinkedList<Vertex2> visited) {
////
////        // start this iteration with current path including the last edge, but not the last vertex.
////        // on the first iteration, the current path is empty, visited is empty, allPathsFromHere is empty
////        LinkedList<GeographicModel> allPathsFromHere = new LinkedList<>();
////        visited.add(currentVertex); // lists the vertices that are in the current path.
////        currentPath.addJustVertex(currentVertex);
////        //todo - change to Vertex2
////        List<Edge> adjacentEdgeList = currentVertex.getOutgoingEdges(this.routeModel.edgeList);
////        adjacentEdgeList.removeAll(visited);
////        if (adjacentEdgeList.size() == 0) { // dead-end without reaching endVertex
////            currentPath.removeLastVertex();
////            currentPath.removeLastEdge();
////            return allPathsFromHere;  // without adding any new paths
////        }
////        for (Edge edge : adjacentEdgeList) {
////            if (edge.getEnd().isMatch(endVertex)) { // if you reach the destination
////                currentPath.addJustEdge(edge);
////                currentPath.addJustVertex(edge.getEnd());
////                allPathsFromHere.add(currentPath.cloneOfWgAndLists());
////                currentPath.removeLastVertex();
////                currentPath.removeLastEdge();
////            } else {
////                Vertex2 neighbor = edge.getEnd();
////                currentPath.addJustEdge(edge);
////                allPathsFromHere.addAll(getAllPathsFromHere(neighbor, endVertex, allowableModes, currentPath, visited));
////                currentPath.removeLastEdge();
////            }
////        }
////        visited.remove(currentVertex);
////        currentPath.removeLastVertex();
////        return allPathsFromHere;
////    }
//
//
///*
//    private List<String> getAvailableModes(UserAccount userAccount) {
//        List<String> availableModes = new ArrayList<>();
//
//        if (userAccount.getWalkPref() > 0) {
//            availableModes.add("walking");
//        }
//        if (userAccount.getDrivePref() > 0) {
//            availableModes.add("driving");
//        }
//        if (userAccount.getRidesharePref() > 0) {
//            availableModes.add("rideshare");
//        }
//        if (userAccount.getCarRentalPref() > 0) {
//            availableModes.add("carrental");
//        }
//        if (userAccount.getBikePref() > 0) {
//            availableModes.add("bicycling");
//        }
//        if (userAccount.getScooterPref() > 0) {
//            availableModes.add("scooter");
//        }
//        if (userAccount.getTransitPref() > 0) {
//            availableModes.add("transit");
//        }
//        if (userAccount.getBusPref() > 0) {
//            availableModes.add("bus");
//        }
//        if (userAccount.getFlightPref() > 0) {
//            availableModes.add("airplane");
//        }
//
//        return availableModes;
//    }
//
//    private static void sortPathsByPriority(List<Path> paths, String priority) {
//        paths.sort((path1, path2) -> {
//            // Implement the sorting logic based on the user's priority
//            // Compare path1 and path2 based on priority and return -1, 0, or 1 accordingly
//
//            // Example sorting by total cost (ascending order)
//            double cost1 = path1.getTotalCost();
//            double cost2 = path2.getTotalCost();
//            if (cost1 < cost2) {
//                return -1;
//            } else if (cost1 > cost2) {
//                return 1;
//            } else {
//                return 0;
//            }
//        });
//    }
//    */
//
///*    public List<WeightedGraph> getMultiModalBestPath(WeightedGraph.Vertex startVertex, WeightedGraph.Vertex endVertex, List<String> availableModes) {
//        //Vertex startVertex = getVertex(startVertexName);
//        //Vertex endVertex = getVertex(endVertexName);
//
//        Set<WeightedGraph.Vertex> visited = new HashSet<>();
//        List<WeightedGraph> bestPaths = null;
//
//        findMultiModalPaths(startVertex, endVertex, availableModes, new WeightedGraph(), visited, bestPaths);
//
//        return bestPaths;
//    }
//*/
//
///*    public List<WeightedGraph.Edge> getOutgoingEdges(WeightedGraph weightedGraph) {
//        List<WeightedGraph.Edge> outgoingEdges = new ArrayList<>();
//        for (WeightedGraph.Edge edge : weightedGraph.edgeList) {
//            if (edge.start.equals(this)) {
//                outgoingEdges.add(edge);
//            }
//        }
//        return outgoingEdges;
//    }
//*/
//
//
///*
//    static class Path {
//        List<WeightedGraph.Edge> edges;
//        Double totalCost;
//        Integer totalDuration;
//
//        public Path(List<WeightedGraph.Edge> edges, Double totalCost, Integer totalDuration) {
//            this.edges = edges;
//            this.totalCost = totalCost;
//            this.totalDuration = totalDuration;
//        }
//
//        public List<WeightedGraph.Edge> getEdges() {
//            return edges;
//        }
//
//        public Double getTotalCost() {
//            return totalCost;
//        }
//
//        public Integer getTotalDuration() {
//            return totalDuration;
//        }
//    }
//*/
//

}

