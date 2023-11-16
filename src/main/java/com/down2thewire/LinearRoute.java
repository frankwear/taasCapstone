package com.down2thewire;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class LinearRoute {
    LinkedList<LinearWayPoint> wayPointLinkedList = new LinkedList<>();
    LinkedList<Edge> edgeList;

    public LinearRoute() {
        this.wayPointLinkedList = new LinkedList<LinearWayPoint>();
    }


    public LinearWayPoint addWaypoint(LinearWayPoint wayPoint){
        if (!wayPointLinkedList.isEmpty()){
            if (wayPointLinkedList.getLast().getId() == wayPoint.getId()) {
                wayPointLinkedList.removeLast();
            }
        }
        wayPointLinkedList.addLast(wayPoint);
        return wayPoint;
    }

    public void removeWaypoint(LinearWayPoint wayPoint){
        int wIndex = findMatch(wayPoint);
        wayPointLinkedList.remove(wIndex);
    };
    public void removeWaypoint(Integer wIndex){
        wayPointLinkedList.remove(wIndex);
    };


    public Boolean isUnique(LinearWayPoint v){
        Boolean hasMatch = false;
        for (LinearWayPoint mainVertex : this.wayPointLinkedList) {
            if (mainVertex.isMatchById(v)) {
                hasMatch = true;
                break;
            }
        }
        return !hasMatch;
    }
    public int findMatch(LinearWayPoint tempVer) {
        int index = 0;
        for (LinearWayPoint mainVertex : this.wayPointLinkedList) {
            if (mainVertex.isMatchById(tempVer)) {
                return index;
            }
            index++;
        }
        return -1;
    }
    private LinearWayPoint getWaypoint(int i) {
        return wayPointLinkedList.get(i);  // may return out of bounds if vertex doesn't exist
    }

    public int getWaypointIndex(LinearWayPoint v) {
        ListIterator<LinearWayPoint> vertexIterator = (ListIterator<LinearWayPoint>) wayPointLinkedList.iterator();
        while (vertexIterator.hasNext()) {
            if (vertexIterator.next() == v) {
                return vertexIterator.previousIndex();
            } //TODO Figure out what this means, maybe at an iterator ++
        }   //vertexIterator++;
        return -1; // return -1 if not found
    }

    public int getWaypointIndex(String s) {
        ListIterator<LinearWayPoint> wayPointListIterator = (ListIterator<LinearWayPoint>) wayPointLinkedList.iterator();
        while (wayPointListIterator.hasNext()) {
            if (wayPointListIterator.next().description.contains(s)) {
                return wayPointListIterator.previousIndex();
            } //TODO Figure out what this means, maybe at an iterator ++
        }   //vertexIterator++;
        return -1; // return -1 if not found

    }

    public LinearWayPoint getWaypoint(String s) {
        int wIndex = getWaypointIndex(s);
        if (wIndex >= 0) {
            return this.wayPointLinkedList.get(wIndex);
        } else {return null;}
    }



        // make Vertex mode true at source and destination of the edge
//        int sIndex = getVertexIndex(e.start.vertexName);
//        this.vertexList.get(sIndex).modes[Edge.getMode(e.mode)] = true;
//        int dIndex = getVertexIndex(e.end.vertexName);
//        this.vertexList.get(dIndex).modes[Edge.getMode(e.mode)] = true;
//        return this.edgeList.getLast();



    public void addRoute(LinearRoute g) {

        // iterate over edges of argument g - adding an edge adds if vertices if they are unique
        ListIterator<LinearWayPoint> wIterator = (ListIterator<LinearWayPoint>) g.wayPointLinkedList.iterator();
        while (wIterator.hasNext()) {
            this.addWaypoint(wIterator.next());
        }
    }
//    public void addGraph(Graph graph){}

    public LinearRoute cloneRoute(LinearRoute route){

    //public GeographicModel cloneOfWgAndLists()
        // Vertices and Edges are NOT cloned //Only route to be cloned here not WeightedGraph
        // todo - clone of route to be created here
        /*
        GeographicModel cloneWG = new GeographicModel();
        cloneWG.vertexList = new LinkedList<WayPoint>();
        for (WayPoint vertex : this.wayPointLinkedList) {
            cloneWG.addJustVertex(vertex);
        }
        for (Edge2 edge : this.edgeList) {
            cloneWG.addJustEdge(edge);
        }
    return cloneWG;
         */
        return route;
    }

    public void removeAdjacentSameModeEdges() {  // considering routes non-branching
//todo - Correct logic on this to have edge as part of vertex.
// todo depricated

        int listSize = wayPointLinkedList.size();
        for (int i = listSize - 2; i > 0; i--) {  // last index (size - 1) doesn't have an edge
            // If do two adjacent edges have the same mode, combine them
            if (wayPointLinkedList.get(i).getEdge().getMode().equals(wayPointLinkedList.get(i-1).getEdge().getMode())) {
                Edge priorEdge = wayPointLinkedList.get(i-1).getEdge();
                Edge currentEdge = wayPointLinkedList.get(i).getEdge();
                priorEdge.setDistance(priorEdge.getDistance() + currentEdge.getDistance());
                priorEdge.setDuration(priorEdge.getDuration() + currentEdge.getDuration());
                priorEdge.setCost(priorEdge.getCost() + currentEdge.getCost());
                priorEdge.setEnd(currentEdge.getEnd());
                wayPointLinkedList.get(i-1).setEdge(priorEdge);
                wayPointLinkedList.remove(i);
            }
        }
    }

    public int getWaypointListSize() {
        return wayPointLinkedList.size();
    }



    public void printGraph(){
        Iterator<LinearWayPoint> waypointIterator = wayPointLinkedList.iterator();
        while (waypointIterator.hasNext()) {
            LinearWayPoint tempWaypoint = waypointIterator.next();
            System.out.println(tempWaypoint.getLongitude() + "  " + tempWaypoint.getLatitude() + "  " +
                    tempWaypoint.getDescription());
        }
        Iterator<Edge> edgeIterator = edgeList.iterator();
        while (edgeIterator.hasNext()) {
            Edge tempEdge = edgeIterator.next();
            System.out.println("\n\nFrom: " + tempEdge.getStart().getDescription() + "\nTo " + tempEdge.getEnd().getDescription() +
                    "\nMode: " + tempEdge.getMode() + "\nDistance: " + tempEdge.getDistance() +
                    "\nDuration: " + tempEdge.getDuration() +
                    "\nCost: " + tempEdge.getCost());
        }
    }

/*
    public void loadTestGraph1(WeightedGraph graph){

        // add vertices
        graph.addVertex(new Vertex(new Location(34.0333005,-84.5788771), "KSU Kennesaw"));
        graph.addVertex(new Vertex(new Location(33.9211998,-84.3442140), "Dunwoody Marta Station"));
        graph.addVertex(new Vertex(new Location(33.9518345,-84.5442312), "National Cemetery of Marietta"));
        graph.addVertex(new Vertex(new Location(33.6323356,-84.4378869), "Hartsfield Jackson Airport"));
        graph.addVertex(new Vertex(new Location(33.8021506,-84.1539056), "Stone Mountain Park"));

        // add edges
        // for testing clarity, making each vertex a separate variable
        Vertex v1 = graph.getVertex("KSU Kennesaw");
        Vertex v2 = graph.getVertex("Dunwoody Marta Station");
        graph.addEdge(v1, v2, "WALKING", 23991, 0.00, 31766 );
        // Fare - CobbLinc route 45 is local, 1-way, $2.50 for adult, free transfer to route 10 and Marta for 3 hours.
        graph.addEdge(v1, v2, "TRANSIT", 10537, 2.50, 64413);  //this is mixed walk/transit
        graph.addEdge(v1, v2, "DRIVING", 1466, 14.48, 34286);  //cost is .68*miles
        // duration is driving plus 10 min pickup
        // cost is $10 + $1.60/mile
        graph.addEdge(v1, v2, "RIDESHARE", 2065, 45.20, 34286);
        graph.addEdge(v1, v2, "BICYCLING", 7124, 0.00, 32629);

        Vertex v3 = graph.getVertex("National Cemetery of Marietta");
        graph.addEdge(v3, v1, "TRANSIT", 1992, 2.50, 17967);
        graph.addEdge(v3, v1, "RIDESHARE", 737, 21.36,11467);
        graph.addEdge(v3, v1, "BICYCLING", 2606, 0.00, 12898);

        graph.printGraph();
    }
    public void loadTestGraphDunMacysToPiedmont(WeightedGraph graph){

        // add vertices
        Vertex v1 = graph.addVertex(new Vertex(new Location(33.9228732,-84.3418493), "Macys - Perimeter Mall"));
        Vertex v2 = graph.addVertex(new Vertex(new Location(33.921227,-84.344398), "Rail stop - Dunwoody Marta Station"));
        Vertex v3 = graph.addVertex(new Vertex(new Location(33.789112,-84.387383), "Rail stop - Arts Center Marta Station"));
        Vertex v4 = graph.addVertex(new Vertex(new Location(33.7892632,-84.3873414), "Bus stop - Arts Center Marta Station"));
        Vertex v5 = graph.addVertex(new Vertex(new Location(33.8082253,-84.3934548), "Bus stop - Peachtree Rd at Collier Rd"));
        Vertex v6 = graph.addVertex(new Vertex(new Location(33.8085817,-84.3943387), "Piedmont Hospital - Peachtree Rd"));


        // add edges
        // for testing clarity, making each vertex a separate variable

        Edge e1 = graph.addEdge(v1, v2, "WALKING", 271, 0.00, 347);
        Edge e2 = graph.addEdge(v2, v3, "TRANSIT", 900, 0.00, 17083);
        Edge e3 = graph.addEdge(v3, v4, "WALKING", 18, 0.00, 17);
        Edge e4 = graph.addEdge(v4, v5, "TRANSIT", 699, 0.00, 3083);
        Edge e5 = graph.addEdge(v5, v6, "WALKING", 103, 0.00, 121);
    }
    public void loadTestGraphDunMacysToPiedmont2(){

        // add vertices
        Vertex v1 = this.addVertex(new Vertex(new Location(33.9228732,-84.3418493), "Macys - Perimeter Mall"));
        Vertex v2 = this.addVertex(new Vertex(new Location(33.921227,-84.344398), "Rail stop - Dunwoody Marta Station"));
        Vertex v3 = this.addVertex(new Vertex(new Location(33.789112,-84.387383), "Rail stop - Arts Center Marta Station"));
        Vertex v4 = this.addVertex(new Vertex(new Location(33.7892632,-84.3873414), "Bus stop - Arts Center Marta Station"));
        Vertex v5 = this.addVertex(new Vertex(new Location(33.8082253,-84.3934548), "Bus stop - Peachtree Rd at Collier Rd"));
        Vertex v6 = this.addVertex(new Vertex(new Location(33.8085817,-84.3943387), "Piedmont Hospital - Peachtree Rd"));


        // add edges
        // for testing clarity, making each vertex a separate variable

        Edge e1 = this.addEdge(v1, v2, "WALKING", 271, 0.00, 347);
        Edge e2 = this.addEdge(v2, v3, "TRANSIT", 900, 0.00, 17083);
        Edge e3 = this.addEdge(v3, v4, "WALKING", 18, 0.00, 17);
        Edge e4 = this.addEdge(v4, v5, "WALKING", 699, 0.00, 3083);
        Edge e5 = this.addEdge(v5, v6, "TRANSIT", 103, 0.00, 121);
    }

*/

    public static void main(String[] args) {


        BranchGeoModel graph = new BranchGeoModel();
        // graph.loadTestGraph1(graph);
       // graph.loadTestGraphDunMacysToPiedmont(graph);
        graph.printGraph();
    }


}
