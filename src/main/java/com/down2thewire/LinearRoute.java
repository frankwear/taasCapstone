package com.down2thewire;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;

public class LinearRoute {
    LinkedList<LinearWayPoint> wayPointLinkedList;


    public LinearRoute() {
        this.wayPointLinkedList = new LinkedList<LinearWayPoint>();
    }

    public LinearWayPoint addWaypoint(LinearWayPoint wayPoint){
        if (!wayPointLinkedList.isEmpty()){
            if (Objects.equals(wayPointLinkedList.getLast().getWayPointID(), wayPoint.getWayPointID())) {
                wayPointLinkedList.removeLast();  //todo need to correct prior edge end
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
        for (int i = 0; i < getWaypointListSize(); i++){
            if (wayPointLinkedList.get(i).getWpDescription().contains(s)) {
                return i;
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



    public void combineRoutes(LinearRoute g) {

        // iterate over edges of argument g - adding an edge adds if vertices if they are unique
        // todo correct code to combine head and tail waypoint to make a single route

        // merging tail of first with head of last
        if (this.wayPointLinkedList.isEmpty()) {
            this.wayPointLinkedList = g.wayPointLinkedList;
        }
        if (g.wayPointLinkedList.isEmpty()) {
            return;
        }
        LinearWayPoint tail = this.wayPointLinkedList.get(this.getWaypointListSize()-1);
        LinearWayPoint head = g.getWaypoint(0);

        if (tail.getId().equals(head.getId())){
            if (this.getWaypointListSize() < 2){
                this.wayPointLinkedList = g.wayPointLinkedList;
            } else{
                LinearWayPoint oneBack = this.wayPointLinkedList.get(this.getWaypointListSize()-2);
                Edge<LinearWayPoint> lastEdge = oneBack.getEdge();
                lastEdge.setEnd(head);
                this.wayPointLinkedList.removeLast();
                for (int i = 0; i < g.getWaypointListSize(); i++){
                    this.addWaypoint(g.getWaypoint(i));
                }
            }
        } else {
            System.out.println("Cannot combine routes without metrics at LinearRoute.combineRoutes()");
        }
//        ListIterator<LinearWayPoint> wIterator = (ListIterator<LinearWayPoint>) g.wayPointLinkedList.iterator();
//        while (wIterator.hasNext()) {
//            this.addWaypoint(wIterator.next());
//        }
    }
    public void combineRoutes(LinearRoute g,
                              String mode,
                              Integer duration,
                              Double cost,
                              Integer distance) {

        // merging tail of first with head of last
        LinearWayPoint tail = this.wayPointLinkedList.get(this.getWaypointListSize()-1);
        LinearWayPoint head = g.getWaypoint(0);
        if (tail.getId().equals(head.getId())){
            combineRoutes(g);
        } else {
            Edge<LinearWayPoint> tailToHead = new Edge<>(tail, head, mode, duration, cost, distance);
            tail.setEdge(tailToHead);
            for (int i = 0; i < g.getWaypointListSize(); i++) {
                this.addWaypoint(g.getWaypoint(i));
            }
        }
//        ListIterator<LinearWayPoint> wIterator = (ListIterator<LinearWayPoint>) g.wayPointLinkedList.iterator();
//        while (wIterator.hasNext()) {
//            this.addWaypoint(wIterator.next());
//        }
    }




//    public void addGraph(Graph graph){}

    public static LinearRoute cloneRoute(LinearRoute r){
        LinearRoute clonedRoute = new LinearRoute();
        for (int i = 0; i < r.getWaypointListSize(); i++){
            LinearWayPoint currentWP = r.wayPointLinkedList.get(i);
            LinearWayPoint tempWP = new LinearWayPoint(currentWP.getLocation(), currentWP.getWpDescription());
            clonedRoute.addWaypoint(tempWP);
        }
        for (int i = 0; i < r.getWaypointListSize() - 1; i++){
            Edge<LinearWayPoint> currentEdge = r.wayPointLinkedList.get(i).getEdge();
            Edge<LinearWayPoint> tempEdge = new Edge<>(
                    clonedRoute.getWaypoint(i),
                    clonedRoute.getWaypoint(i+1),
                    currentEdge.getMode(),
                    currentEdge.getDuration(),
                    currentEdge.getCost(),
                    currentEdge.getDistance());
            clonedRoute.getWaypoint(i).setEdge(tempEdge);
        }
        return clonedRoute;
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
            Edge<LinearWayPoint> tempEdge = tempWaypoint.getEdge();
            System.out.println("\n" + tempWaypoint.getWpDescription() +
                    "\n     longitude: " + tempWaypoint.getLongitude() + "  latitude: " + tempWaypoint.getLatitude());
            if (tempEdge != null) {
                System.out.println("     " + tempEdge.getMode() +
                    "\n     Dist/Dur/Cost:  " + tempEdge.getDistance() + "   " + tempEdge.getDuration() + "   " + tempEdge.getCost());
            }
        }
        System.out.println("\n\n");
//        Iterator<Edge> edgeIterator = edgeList.iterator();
//        while (edgeIterator.hasNext()) {
//            Edge tempEdge = edgeIterator.next();
//            System.out.println("\n\nFrom: " + tempEdge.getStart().getDescription() + "\nTo " + tempEdge.getEnd().getDescription() +
//                    "\nMode: " + tempEdge.getMode() + "\nDistance: " + tempEdge.getDistance() +
//                    "\nDuration: " + tempEdge.getDuration() +
//                    "\nCost: " + tempEdge.getCost());
//        }
    }

    public static LinearRoute reverseRoute(LinearRoute r1) {
        if (r1.getWaypointListSize() < 2) return r1;
        LinearRoute tempRoute = LinearRoute.cloneRoute(r1);  // so you don't change the original
        LinearRoute r2 = new LinearRoute();
        LinearWayPoint wp2 = tempRoute.getWaypoint(tempRoute.getWaypointListSize()-1);
        for (int i = tempRoute.getWaypointListSize()-1; i > -1; i--) {
            r2.addWaypoint(tempRoute.getWaypoint(i));  // with edges pointing the wrong direction
        }
        for (int i = 0; i < r2.getWaypointListSize()-1; i++) {
            LinearWayPoint currentWp = r2.getWaypoint(i);
            LinearWayPoint nextWp = r2.getWaypoint(i + 1);
            Edge<LinearWayPoint> currentEdge = nextWp.getEdge(); //reversing requires reassigning edge
            currentEdge.setStart(currentWp);
            currentEdge.setEnd(nextWp);
            currentWp.setEdge(currentEdge);
        }
        r2.getWaypoint(r2.getWaypointListSize()-1).edge = null;
        return r2;
    }
}
