package com.down2thewire;

import java.util.*;

public class GeographicModel {
    LinkedList<Vertex2> vertexList;
    LinkedList<Edge2> edgeList;


    public Edge2 getEdge(Vertex2 start, Vertex2 end) {
        for (Edge2 edge : edgeList) {
            if (edge.getStart().equals(start) && edge.getEnd().equals(end)) {
                return edge;
            }
        }
        return null; // Edge not found
    }



    public GeographicModel() {
        this.vertexList = new LinkedList<>();
        this.edgeList = new LinkedList<>();
    }

    public Vertex2 addVertex(Vertex2 v) {

        if(isUnique(v)) {
            vertexList.addLast(v);
        } else {
            v = getVertex(findMatch(v));
        }

        return getVertex(getVertexIndex(v));  // return new vertex or the one that matched
    }
    public Vertex2 addVertex(Double latitude, Double longitude) {
        Location tempLocation = new Location (latitude, longitude);
        Vertex2 tempVertex = new Vertex2(tempLocation, tempLocation.generateUniqueID());
        return tempVertex;
    }
    public Boolean isUnique(Vertex2 v){
        return !vertexList.contains(v);
    }


    public void sortVertexList() {
        Comparator<Vertex2> compareVertexList = (v1, v2) -> {
        Long v1Id=v1.getId();
        Long v2Id= v2.getId();
        return v1Id.compareTo(v2Id);
        };
        Collections.sort(vertexList, compareVertexList);

    }

    public int findMatch(Vertex2 tempVer) {
        int index = 0;
        for (Vertex2 mainVertex : this.vertexList) {
            if (mainVertex.isMatch(tempVer)) {
                return index;
            }
            index++;
        }
        return -1;
    }
    private Vertex2 getVertex(int i) {
        return vertexList.get(i);  // may return out of bounds if vertex doesn't exist
    }

    public int getVertexIndex(Vertex2 v) {
        ListIterator<Vertex2> vertexIterator = (ListIterator<Vertex2>) vertexList.iterator();
        while (vertexIterator.hasNext()) {
            if (vertexIterator.next() == v) {
                return vertexIterator.previousIndex();
            } //TODO Figure out what this means, maybe at an iterator ++
        }   //vertexIterator++;
        return -1; // return -1 if not found
    }

    public int getVertexIndex(String s) {
        ListIterator<Vertex2> vertexIterator = (ListIterator<Vertex2>) vertexList.iterator();
        while (vertexIterator.hasNext()) {
            if (vertexIterator.next().description.contains(s)) {
                return vertexIterator.previousIndex();
            } //TODO Figure out what this means, maybe at an iterator ++
        }   //vertexIterator++;
        return -1; // return -1 if not found
    }

    public Vertex2 getVertex(String s) {
        int vIndex = getVertexIndex(s);
        if (vIndex >= 0) {
            return this.vertexList.get(vIndex);
        } else {return null;}
    }


    public void removeLastEdge() {
        edgeList.removeLast();
    }

    public void removeLastVertex() {
        vertexList.removeLast();
    }


    public Vertex2 convertToVertex (WayPoint wayPoint){
        Vertex2 tempVertex = new Vertex2(wayPoint.location, wayPoint.getId());
        return tempVertex;
    }

    public void addGraph(Route route) {

        // iterate over edges of argument g - adding an edge adds if vertices if they are unique
        ListIterator<WayPoint> wayPointListIteratorIterator = route.wayPointLinkedList.listIterator();
        while (wayPointListIteratorIterator.hasNext()) {
            vertexList.add(convertToVertex(wayPointListIteratorIterator.next()));
        }
    }
    public GeographicModel addGraph(GeographicModel tempGeoModel) {
        LinkedList<Vertex2> tempVertexList = new LinkedList<>(tempGeoModel.vertexList);
        ListIterator<Vertex2> vertex2ListIterator = tempVertexList.listIterator();
        while (vertex2ListIterator.hasNext()) {
            this.vertexList.add(vertex2ListIterator.next());
        }
        return this;
    }




    public void printGraph(){
        Iterator<Vertex2> vertexIterator = vertexList.iterator();
        while (vertexIterator.hasNext()) {
            Vertex2 tempVertex = vertexIterator.next();
            System.out.println("\nVertex: " + tempVertex.location.longitude + "  " + tempVertex.location.latitude + "  " +
                    tempVertex.getId());
            Iterator<Edge2> edge2Iterator = tempVertex.outgoingEdges.iterator();
            while (edge2Iterator.hasNext()){
                Edge2<Vertex2> tempEdge = edge2Iterator.next();
                System.out.println("Destination: " + tempEdge.getEnd().getId().toString() + "\nMode: " + tempEdge.getMode() +
                        "\nDistance: " + tempEdge.distance);
            }
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
