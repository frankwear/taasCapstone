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
    public BranchVertex getVertex(int vertIndex) {
        return vertexList.get(vertIndex);  // may return out of bounds if vertex doesn't exist
    }
    public BranchVertex getVertex(Long vertId) {
        int vertIndex = getVertexIndexById(vertId);
        return getVertex(vertIndex);
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




}



