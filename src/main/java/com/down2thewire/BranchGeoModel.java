package com.down2thewire;

import java.util.*;

public class BranchGeoModel {
    private LinkedList<BranchVertex> vertexList;

    public BranchGeoModel() {
        this.vertexList = new LinkedList<>();
    }
    public BranchGeoModel(LinearRoute route){
        convertRouteToGeoModel(route);
    }

    public BranchVertex addVertex(BranchVertex v) {
        if(isUnique(v)) {
            vertexList.addLast(v);
            return v;
        } else {
            System.out.println("Duplicate Vertex " + v.getId() + " not added.");  // Remove comment for debugging
            return vertexList.get(findMatchById(v.getId()));
        }
    }

    public BranchVertex addVertex(Double latitude, Double longitude) {
        Location tempLocation = new Location (latitude, longitude);
        BranchVertex tempVertex = new BranchVertex(tempLocation, tempLocation.generateUniqueID());
        return addVertex(tempVertex);  // only adds a vertex is it is unique
    }

    //////// Warning - this method removes the vertex from the linkedList regardless of other edges that may
    // refer to is as start or end.  Only use after validating that it is not referenced.  ////////
    private void removeVertexAndEdgesHard(int i){
        this.vertexList.remove(i);
    }

    public void removeDuplicateVertices() {

        // No duplicates should be added, but this is a cleanup just in case

        sortVertexList();
        for (int i = getVertexListSize() - 1; i > 0; i--) {
            BranchVertex currentVertex = getVertex(i);
            BranchVertex priorVertex = getVertex(i - 1);

            if (currentVertex.getId().equals(priorVertex.getId())) {
                currentVertex.updateNeighborsEdges(currentVertex, priorVertex);

                // update currentVertex edges
                for(int j = 0; j < currentVertex.getEdgeListSize(); j++){
                    currentVertex.getOutgoingEdges().get(j).setStart(priorVertex);
                    priorVertex.addEdge(currentVertex.getOutgoingEdges().get(j));
                }
                removeVertexAndEdgesHard(i);
            }
        }
    }

    public Boolean isUnique(BranchVertex v){
        for (BranchVertex mainVertex : this.vertexList) {
            if (mainVertex.getId().equals(v.getId())) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public int findMatchById(Long id) {
        int index = 0;
        for (BranchVertex mainVertex : vertexList) {
            if (mainVertex.isMatchById(id)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public int getVertexListSize(){
        return vertexList.size();
    }
    public BranchVertex getVertex(int i) {
        return vertexList.get(i);  // may return out of bounds if vertex doesn't exist
    }

    public LinkedList<BranchVertex> getMatchingVertexNames(String s) {
        LinkedList<BranchVertex> matchingList = new LinkedList<>();
        ListIterator<BranchVertex> vertexIterator = (ListIterator<BranchVertex>) vertexList.iterator();
        while (vertexIterator.hasNext()) {
            if (vertexIterator.next().getDescription().contains(s)) {
                matchingList.addLast(vertexIterator.next());
            } //TODO Figure out what this means, maybe at an iterator ++
        }
        return matchingList;
    }


    //todo - merge methods getVertexIndexById and findMatchByID - same functionality
    public int getVertexIndexById(Long id) {
        ListIterator<BranchVertex> vertexIterator = (ListIterator<BranchVertex>) vertexList.iterator();
        while (vertexIterator.hasNext()) {
            if (vertexIterator.next().getId().equals(id)) {
                return vertexIterator.previousIndex();
            } //TODO Figure out what this means, maybe at an iterator ++
        }   //vertexIterator++;
        return -1; // return -1 if not found
    }

    // 9-24-23 added method to get vertex locations as a list of lat/lng strings
    public LinkedList<String> getLocationsAsListOfString(){
        LinkedList<String> tempVertices = new LinkedList<>();
        for(BranchVertex vertex : vertexList) {
            tempVertices.add(vertex.getLocationAsString());
        }
        return tempVertices;
    }

    private void convertRouteToGeoModel (LinearRoute route) {
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
            addVertex(v1);
            if (j == size-2){
                addVertex(v2);
            }
            lastLegDestination = v2;
        }
    }



    public void removeLastVertex() {
        vertexList.removeLast();
    }

    // todo - Move this to the Waypoint class as getAsVertex() and refactor
    public BranchVertex convertToVertex (LinearWayPoint wayPoint){
        BranchVertex tempVertex = new BranchVertex(wayPoint.location, wayPoint.getId());
        return tempVertex;
    }

    public void addGraph(LinearRoute route) {

        // iterate over edges of argument g - adding an edge adds if vertices if they are unique
        ListIterator<LinearWayPoint> wayPointListIteratorIterator = route.wayPointLinkedList.listIterator();
        while (wayPointListIteratorIterator.hasNext()) {
            vertexList.add(convertToVertex(wayPointListIteratorIterator.next()));
        }
    }
    public BranchGeoModel addGraph(BranchGeoModel tempGeoModel) {
        LinkedList<BranchVertex> tempVertexList = new LinkedList<>(tempGeoModel.vertexList);
        ListIterator<BranchVertex> vertex2ListIterator = tempVertexList.listIterator();
        while (vertex2ListIterator.hasNext()) {
            this.vertexList.add(vertex2ListIterator.next());
        }
        return this;
    }

    public void sortVertexList(){
        vertexList.sort(Comparator.comparing(BranchVertex::getId));
    }

    public LinkedList<BranchVertex> cloneVertexList(){
        return (LinkedList<BranchVertex>) vertexList.clone();
    }


    public void printGraph(){
        Iterator<BranchVertex> vertexIterator = vertexList.iterator();
        while (vertexIterator.hasNext()) {
            BranchVertex tempVertex = vertexIterator.next();
            System.out.println("\nName: " + tempVertex.getDescription());
            System.out.println("Vertex: " + tempVertex.getLongitude() + "  " + tempVertex.getLatitude() + "  " +
                    tempVertex.getId());
            tempVertex.printEdges();
        }
    }


    Node addNode(Node node) {
        return null;
    }


    void removeNode(Node node) {

    }



    List<Node> find(String s) {
        return null;
    }


    List<Node> find(Location l) {
        return null;
    }


    Node findNext(String s) {
        return null;
    }


    Node findNext(Location l) {
        return null;
    }


    Node getNextNode(Location l) {
        return null;
    }




//    public void loadTestGraph1(WeightedGraph graph){
//
//        // add vertices
//        graph.addVertex(new Vertex2(new Location(34.0333005,-84.5788771), "KSU Kennesaw"));
//        graph.addVertex(new Vertex2(new Location(33.9211998,-84.3442140), "Dunwoody Marta Station"));
//        graph.addVertex(new Vertex2(new Location(33.9518345,-84.5442312), "National Cemetery of Marietta"));
//        graph.addVertex(new Vertex2(new Location(33.6323356,-84.4378869), "Hartsfield Jackson Airport"));
//        graph.addVertex(new Vertex2(new Location(33.8021506,-84.1539056), "Stone Mountain Park"));
//
//        // add edges
//        // for testing clarity, making each vertex a separate variable
//        Vertex2 v1 = graph.getVertex("KSU Kennesaw");
//        Vertex2 v2 = graph.getVertex("Dunwoody Marta Station");
//        graph.addEdge(v1, v2, "WALKING", 23991, 0.00, 31766 );
//        // Fare - CobbLinc route 45 is local, 1-way, $2.50 for adult, free transfer to route 10 and Marta for 3 hours.
//        graph.addEdge(v1, v2, "TRANSIT", 10537, 2.50, 64413);  //this is mixed walk/transit
//        graph.addEdge(v1, v2, "DRIVING", 1466, 14.48, 34286);  //cost is .68*miles
//        // duration is driving plus 10 min pickup
//        // cost is $10 + $1.60/mile
//        graph.addEdge(v1, v2, "RIDESHARE", 2065, 45.20, 34286);
//        graph.addEdge(v1, v2, "BICYCLING", 7124, 0.00, 32629);
//
//        Vertex2 v3 = graph.getVertex("National Cemetery of Marietta");
//        graph.addEdge(v3, v1, "TRANSIT", 1992, 2.50, 17967);
//        graph.addEdge(v3, v1, "RIDESHARE", 737, 21.36,11467);
//        graph.addEdge(v3, v1, "BICYCLING", 2606, 0.00, 12898);
//
//        graph.printGraph();
//    }
//    public void loadTestGraphDunMacysToPiedmont(WeightedGraph graph){
//
//        // add vertices
//        Vertex2 v1 = graph.addVertex(new Vertex2(new Location(33.9228732,-84.3418493), "Macys - Perimeter Mall"));
//        Vertex2 v2 = graph.addVertex(new Vertex2(new Location(33.921227,-84.344398), "Rail stop - Dunwoody Marta Station"));
//        Vertex2 v3 = graph.addVertex(new Vertex2(new Location(33.789112,-84.387383), "Rail stop - Arts Center Marta Station"));
//        Vertex2 v4 = graph.addVertex(new Vertex2(new Location(33.7892632,-84.3873414), "Bus stop - Arts Center Marta Station"));
//        Vertex2 v5 = graph.addVertex(new Vertex2(new Location(33.8082253,-84.3934548), "Bus stop - Peachtree Rd at Collier Rd"));
//        Vertex2 v6 = graph.addVertex(new Vertex2(new Location(33.8085817,-84.3943387), "Piedmont Hospital - Peachtree Rd"));
//
//
//        // add edges
//        // for testing clarity, making each vertex a separate variable
//
//        Edge e1 = graph.addEdge(v1, v2, "WALKING", 271, 0.00, 347);
//        Edge e2 = graph.addEdge(v2, v3, "TRANSIT", 900, 0.00, 17083);
//        Edge e3 = graph.addEdge(v3, v4, "WALKING", 18, 0.00, 17);
//        Edge e4 = graph.addEdge(v4, v5, "TRANSIT", 699, 0.00, 3083);
//        Edge e5 = graph.addEdge(v5, v6, "WALKING", 103, 0.00, 121);
//    }
//    public void loadTestGraphDunMacysToPiedmont2(){
//
//        // add vertices
//        Vertex2 v1 = this.addVertex(new Vertex2(new Location(33.9228732,-84.3418493), "Macys - Perimeter Mall"));
//        Vertex2 v2 = this.addVertex(new Vertex2(new Location(33.921227,-84.344398), "Rail stop - Dunwoody Marta Station"));
//        Vertex2 v3 = this.addVertex(new Vertex2(new Location(33.789112,-84.387383), "Rail stop - Arts Center Marta Station"));
//        Vertex2 v4 = this.addVertex(new Vertex2(new Location(33.7892632,-84.3873414), "Bus stop - Arts Center Marta Station"));
//        Vertex2 v5 = this.addVertex(new Vertex2(new Location(33.8082253,-84.3934548), "Bus stop - Peachtree Rd at Collier Rd"));
//        Vertex2 v6 = this.addVertex(new Vertex2(new Location(33.8085817,-84.3943387), "Piedmont Hospital - Peachtree Rd"));
//
//
//        // add edges
//        // for testing clarity, making each vertex a separate variable
//
//        Edge e1 = this.addEdge(v1, v2, "WALKING", 271, 0.00, 347);
//        Edge e2 = this.addEdge(v2, v3, "TRANSIT", 900, 0.00, 17083);
//        Edge e3 = this.addEdge(v3, v4, "WALKING", 18, 0.00, 17);
//        Edge e4 = this.addEdge(v4, v5, "WALKING", 699, 0.00, 3083);
//        Edge e5 = this.addEdge(v5, v6, "TRANSIT", 103, 0.00, 121);
//    }



//    public static void main(String[] args) {
//
//
//        WeightedGraph graph = new WeightedGraph();
//        // graph.loadTestGraph1(graph);
//        graph.loadTestGraphDunMacysToPiedmont(graph);
//        graph.printGraph();
//    }

}






//package com.down2thewire;
//
//import java.util.*;
//
//public class WeightedGraph {
//    LinkedList<Vertex> vertexList;
//    LinkedList<Edge> edgeList;
//
//
//    public Edge getEdge(Vertex start, Vertex end) {
//        for (Edge edge : edgeList) {
//            if (edge.getStart().equals(start) && edge.getEnd().equals(end)) {
//                return edge;
//            }
//        }
//        return null; // Edge not found
//    }
//
//    public WeightedGraph() {
//        this.vertexList = new LinkedList<>();
//        this.edgeList = new LinkedList<>();
//    }
//
//    public Vertex addVertex(Vertex v) {
//        if(isUnique(v)) {
//            vertexList.addLast(v);
//        } else {
//            v = getVertex(findMatch(v));
//        }
//
//        return getVertex(getVertexIndex(v));  // return new vertex or the one that matched
//    }
//    public Boolean isUnique(Vertex v){
//        Boolean hasMatch = false;
//        for (Vertex mainVertex : this.vertexList) {
//            if (mainVertex.isMatch(v)) {
//                hasMatch = true;
//                break;
//            }
//        }
//        return !hasMatch;
//    }
//    public int findMatch(Vertex tempVer) {
//        int index = 0;
//        for (Vertex mainVertex : this.vertexList) {
//            if (mainVertex.isMatch(tempVer)) {
//                return index;
//            }
//            index++;
//        }
//        return -1;
//    }
//    private Vertex getVertex(int i) {
//        return vertexList.get(i);  // may return out of bounds if vertex doesn't exist
//    }
//
//    public int getVertexIndex(Vertex v) {
//        ListIterator<Vertex> vertexIterator = (ListIterator<Vertex>) vertexList.iterator();
//        while (vertexIterator.hasNext()) {
//            if (vertexIterator.next() == v) {
//                return vertexIterator.previousIndex();
//            } //TODO Figure out what this means, maybe at an iterator ++
//        }   //vertexIterator++;
//        return -1; // return -1 if not found
//    }
//
//    public int getVertexIndex(String s) {
//        ListIterator<Vertex> vertexIterator = (ListIterator<Vertex>) vertexList.iterator();
//        while (vertexIterator.hasNext()) {
//            if (vertexIterator.next().vertexName.contains(s)) {
//                return vertexIterator.previousIndex();
//            } //TODO Figure out what this means, maybe at an iterator ++
//        }   //vertexIterator++;
//        return -1; // return -1 if not found
//    }
//
//    public Vertex getVertex(String s) {
//        int vIndex = getVertexIndex(s);
//        if (vIndex >= 0) {
//            return this.vertexList.get(vIndex);
//        } else {return null;}
//    }
//
//    public void addEdge(Edge e) {
//        addEdge(e.getStart(), e.getEnd(), e.getMode(), e.getDuration(), e.getCost(), e.distance);
//
//        // make Vertex mode true at source and destination of the edge
////        int sIndex = getVertexIndex(e.start.vertexName);
////        this.vertexList.get(sIndex).modes[Edge.getMode(e.mode)] = true;
////        int dIndex = getVertexIndex(e.end.vertexName);
////        this.vertexList.get(dIndex).modes[Edge.getMode(e.mode)] = true;
////        return this.edgeList.getLast();
//
//    }
//
//    public Edge addEdge(Vertex start, Vertex end, String mode, Integer duration, Double cost, Integer distance)
//    {
//
//        if (isUnique(start)){
//            start = addVertex(start);
//        } else {
//            start = this.vertexList.get(findMatch(start));
//        }
//        if (isUnique(end)){
//            end = addVertex(end);
//        } else {
//            end = this.vertexList.get(findMatch(end));
//        }
//        this.edgeList.addLast(new Edge(start, end, mode, duration, cost, distance));
//
//        // make Vertex mode true at source and destination of the edge
//        // todo - alter vertex mode list when edge is added.  Resolve
///*        int sIndex = getVertexIndex(start.vertexName);
//        this.vertexList.get(sIndex).modes[Edge.getMode(mode)] = true;
//        int dIndex = getVertexIndex(end.vertexName);
//        this.vertexList.get(dIndex).modes[Edge.getMode(mode)] = true; */
//        return this.edgeList.getLast();
//
//        //
//    }
//    public void removeLastEdge() {
//        edgeList.removeLast();
//    }
//
//    public void removeLastVertex() {
//        vertexList.removeLast();
//    }
//
//    public Vertex addJustVertex(Vertex v) {
//        this.vertexList.addLast(v);
//        return v;
//    }
//    public Edge addJustEdge(Edge e) {
//        this.edgeList.addLast(e);
//        return e;
//    }
//    public void addGraph(WeightedGraph g) {
//
//        // iterate over edges of argument g - adding an edge adds if vertices if they are unique
//        ListIterator<Edge> eIterator = (ListIterator<Edge>) g.edgeList.iterator();
//        while (eIterator.hasNext()) {
//            this.addEdge(eIterator.next());
//        }
//    }
//
//    public WeightedGraph cloneOfWgAndLists() {
//        // Vertices and Edges are NOT cloned
//        WeightedGraph cloneWG = new WeightedGraph();
//        cloneWG.vertexList = new LinkedList<Vertex>();
//        for (Vertex vertex : this.vertexList) {
//            cloneWG.addJustVertex(vertex);
//        }
//        for (Edge edge : this.edgeList) {
//            cloneWG.addJustEdge(edge);
//        }
//        return cloneWG;
//    }
//
//
//
//    public void printGraph(){
//        Iterator<Vertex> vertexIterator = vertexList.iterator();
//        while (vertexIterator.hasNext()) {
//            Vertex tempVertex = vertexIterator.next();
//            System.out.println(tempVertex.location.longitude + "  " + tempVertex.location.latitude + "  " +
//                    tempVertex.vertexName);
//        }
//        Iterator<Edge> edgeIterator = edgeList.iterator();
//        while (edgeIterator.hasNext()) {
//            Edge tempEdge = edgeIterator.next();
//            System.out.println("\n\nFrom: " + tempEdge.start.vertexName + "\nTo " + tempEdge.end.vertexName +
//                    "\nMode: " + tempEdge.mode + "\nDistance: " + tempEdge.distance +
//                    "\nDuration: " + tempEdge.duration +
//                    "\nCost: " + tempEdge.cost);
//        }
//    }
//
//
//    public void loadTestGraph1(WeightedGraph graph){
//
//        // add vertices
//        graph.addVertex(new Vertex(new Location(34.0333005,-84.5788771), "KSU Kennesaw"));
//        graph.addVertex(new Vertex(new Location(33.9211998,-84.3442140), "Dunwoody Marta Station"));
//        graph.addVertex(new Vertex(new Location(33.9518345,-84.5442312), "National Cemetery of Marietta"));
//        graph.addVertex(new Vertex(new Location(33.6323356,-84.4378869), "Hartsfield Jackson Airport"));
//        graph.addVertex(new Vertex(new Location(33.8021506,-84.1539056), "Stone Mountain Park"));
//
//        // add edges
//        // for testing clarity, making each vertex a separate variable
//        Vertex v1 = graph.getVertex("KSU Kennesaw");
//        Vertex v2 = graph.getVertex("Dunwoody Marta Station");
//        graph.addEdge(v1, v2, "WALKING", 23991, 0.00, 31766 );
//        // Fare - CobbLinc route 45 is local, 1-way, $2.50 for adult, free transfer to route 10 and Marta for 3 hours.
//        graph.addEdge(v1, v2, "TRANSIT", 10537, 2.50, 64413);  //this is mixed walk/transit
//        graph.addEdge(v1, v2, "DRIVING", 1466, 14.48, 34286);  //cost is .68*miles
//        // duration is driving plus 10 min pickup
//        // cost is $10 + $1.60/mile
//        graph.addEdge(v1, v2, "RIDESHARE", 2065, 45.20, 34286);
//        graph.addEdge(v1, v2, "BICYCLING", 7124, 0.00, 32629);
//
//        Vertex v3 = graph.getVertex("National Cemetery of Marietta");
//        graph.addEdge(v3, v1, "TRANSIT", 1992, 2.50, 17967);
//        graph.addEdge(v3, v1, "RIDESHARE", 737, 21.36,11467);
//        graph.addEdge(v3, v1, "BICYCLING", 2606, 0.00, 12898);
//
//        graph.printGraph();
//    }
//    public void loadTestGraphDunMacysToPiedmont(WeightedGraph graph){
//
//        // add vertices
//        Vertex v1 = graph.addVertex(new Vertex(new Location(33.9228732,-84.3418493), "Macys - Perimeter Mall"));
//        Vertex v2 = graph.addVertex(new Vertex(new Location(33.921227,-84.344398), "Rail stop - Dunwoody Marta Station"));
//        Vertex v3 = graph.addVertex(new Vertex(new Location(33.789112,-84.387383), "Rail stop - Arts Center Marta Station"));
//        Vertex v4 = graph.addVertex(new Vertex(new Location(33.7892632,-84.3873414), "Bus stop - Arts Center Marta Station"));
//        Vertex v5 = graph.addVertex(new Vertex(new Location(33.8082253,-84.3934548), "Bus stop - Peachtree Rd at Collier Rd"));
//        Vertex v6 = graph.addVertex(new Vertex(new Location(33.8085817,-84.3943387), "Piedmont Hospital - Peachtree Rd"));
//
//
//        // add edges
//        // for testing clarity, making each vertex a separate variable
//
//        Edge e1 = graph.addEdge(v1, v2, "WALKING", 271, 0.00, 347);
//        Edge e2 = graph.addEdge(v2, v3, "TRANSIT", 900, 0.00, 17083);
//        Edge e3 = graph.addEdge(v3, v4, "WALKING", 18, 0.00, 17);
//        Edge e4 = graph.addEdge(v4, v5, "TRANSIT", 699, 0.00, 3083);
//        Edge e5 = graph.addEdge(v5, v6, "WALKING", 103, 0.00, 121);
//    }
//    public void loadTestGraphDunMacysToPiedmont2(){
//
//        // add vertices
//        Vertex v1 = this.addVertex(new Vertex(new Location(33.9228732,-84.3418493), "Macys - Perimeter Mall"));
//        Vertex v2 = this.addVertex(new Vertex(new Location(33.921227,-84.344398), "Rail stop - Dunwoody Marta Station"));
//        Vertex v3 = this.addVertex(new Vertex(new Location(33.789112,-84.387383), "Rail stop - Arts Center Marta Station"));
//        Vertex v4 = this.addVertex(new Vertex(new Location(33.7892632,-84.3873414), "Bus stop - Arts Center Marta Station"));
//        Vertex v5 = this.addVertex(new Vertex(new Location(33.8082253,-84.3934548), "Bus stop - Peachtree Rd at Collier Rd"));
//        Vertex v6 = this.addVertex(new Vertex(new Location(33.8085817,-84.3943387), "Piedmont Hospital - Peachtree Rd"));
//
//
//        // add edges
//        // for testing clarity, making each vertex a separate variable
//
//        Edge e1 = this.addEdge(v1, v2, "WALKING", 271, 0.00, 347);
//        Edge e2 = this.addEdge(v2, v3, "TRANSIT", 900, 0.00, 17083);
//        Edge e3 = this.addEdge(v3, v4, "WALKING", 18, 0.00, 17);
//        Edge e4 = this.addEdge(v4, v5, "WALKING", 699, 0.00, 3083);
//        Edge e5 = this.addEdge(v5, v6, "TRANSIT", 103, 0.00, 121);
//    }
//
//
//
//    public static void main(String[] args) {
//
//
//        WeightedGraph graph = new WeightedGraph();
//        // graph.loadTestGraph1(graph);
//        graph.loadTestGraphDunMacysToPiedmont(graph);
//        graph.printGraph();
//    }
//
//}
