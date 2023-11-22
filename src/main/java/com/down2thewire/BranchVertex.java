









package com.down2thewire;

import java.util.*;

public class BranchVertex extends Node {
    private Location location;
    private String description;
    private Long id;
    private String thirdPartyId;
    private LinkedList<Edge> outgoingEdges = new LinkedList<>();
    private double tentativeDistance;

    // 0-walk, 1-drive, 2-rideshare, 3-carRental, 4-bicycle, 5-scooter, 6-transit, 7-bus, 8-airplane, 9-unused
    /*
     * Walking is allowed in most places with stops. It is not allowed on the Interstate
     * Changing modes to/from driving requires a parking lot, and the cost of parking should be added to the final driven leg
     * Rideshare is allowed almost anywhere. In some places like the airport, there are specific pickup areas.
     * Car Rental locations - where you can rent a car, for instance, the airport car rental lots
     * Bicycle is allowed in most places with stops, but changing to/from bike means bike rental or locking rack.
     * Biking is not allowed on the Interstate
     * Scooter Rental Locations - where you can rent a scooter.
     */



    public BranchVertex(Location location, Long uniqueNameId) {
        super();
        this.location = location;
        this.id = uniqueNameId;
        //       this.tentativeDistance = Double.POSITIVE_INFINITY;
    }

    public BranchVertex(Location location) {
        this.location = location;
        this.id = location.generateUniqueID();
//        super.location = location;
    }
    public BranchVertex(LinearWayPoint wayPoint) {
        BranchVertex vertex = new BranchVertex(wayPoint.getLocation());
        vertex.id = wayPoint.getId();
        vertex.outgoingEdges.add(wayPoint.getEdge());
        vertex.description = wayPoint.getDescription();
    }

    public Location getLocation() {
        return this.location;
    }

    // 9-24-23 Added GetLocationAsString to facilitate API lookups using lat/lng
    public String getLocationAsString() {
        String locationString = getLatitude().toString() + "," + getLongitude().toString();
        return locationString;
    }
    public Double getLongitude() {
        return this.location.getLongitude();
    }
    public Double getLatitude() {
        return this.location.getLatitude();
    }

    public void setDescription (String d) {
        this.description = d;
    }
    public String getDescription() {
        return this.description;
    }

    public void setId() {
        this.id = location.generateUniqueID();
    }
    @Override
    public Long getId() { return this.id;}

    public String getThirdPartyId() {
        return thirdPartyId;
    }
    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    static BranchVertex waypointToVertex(LinearWayPoint wayPoint) {
        BranchVertex vertex = new BranchVertex(wayPoint.getLocation());
        vertex.id = wayPoint.getId();
        //       vertex.outgoingEdges.add(wayPoint.getEdge());
        vertex.description = wayPoint.getDescription();
        return vertex;
    }
//    public Vertex2() {
//        super();
//    }

    public void addEdge(Edge<BranchVertex> e) {
        addEdge(e.getStart(), e.getEnd(), e.getMode(), e.getDuration(), e.getCost(), e.getDistance());
    }

    public void addEdge(BranchVertex start, BranchVertex end, String mode, Integer duration, double cost, Integer distance) {
        Edge<BranchVertex> tempEdge = new Edge<>();
        tempEdge.setStart(this);
        tempEdge.setEnd(end);
        tempEdge.setMode(mode);
        tempEdge.setDuration(duration);
        tempEdge.setCost(cost);
        tempEdge.setDistance(distance);
        outgoingEdges.add(tempEdge);

        // note see next few lines for code from Route
    }
    public Edge getEdge(Integer index) {
        return outgoingEdges.get(index);
    }
    public LinkedList<Edge> getOutgoingEdges(){
        return outgoingEdges;
    }

    public void printEdges(){
        Iterator<Edge> edge2Iterator = outgoingEdges.iterator();
        for (Edge edge : outgoingEdges ){
            System.out.println("Destination: " + edge.getEnd().getId().toString() + "\nMode: " + edge.getMode() +
                    "\nDistance: " + edge.getDistance());
        }
//        while (edge2Iterator.hasNext()){
//            Edge2<Vertex2> tempEdge = edge2Iterator.next();
//            System.out.println("Destination: " + tempEdge.getEnd().getId().toString() + "\nMode: " + tempEdge.getMode() +
//                    "\nDistance: " + tempEdge.distance);
//        }
    }

    public int getEdgeListSize() {
        return outgoingEdges.size();
    }

    // This method is normally in preparation for deleting this vertex, so oldNeighbor would be this Vertex2
    public void updateNeighborsEdges(BranchVertex oldNeighbor, BranchVertex newNeighbor) {
        for (Edge<BranchVertex> edge  : outgoingEdges){
            if (edge.getEnd().getId().equals(oldNeighbor.getId())){
                edge.setEnd(newNeighbor);
            }
        }
    }

    public Edge<BranchVertex> matchEdge(BranchVertex endVertex, String mode) {
        for (int i = 0; i < outgoingEdges.size(); i++) {
            if (outgoingEdges.get(i).getEnd().getId().equals(endVertex.getId())){
                if (outgoingEdges.get(i).getMode().equals(mode)){
                    return outgoingEdges.get(i);
                }
            }
        }
        System.out.println("Edge not found BranchVertex.matchEdge()");
        return null;
    }

    public static class BranchGeoModelGenerator {

        BranchGeoModel geographicMap = new BranchGeoModel();
        List<LinearRoute> routeList = new ArrayList<>();
        LinearWayPoint originWayPoint;
        LinearWayPoint destinationWayPoint;
        UserRouteRequest modelRouteRequest;



        public BranchGeoModelGenerator() {
        }
        public BranchGeoModelGenerator(UserRouteRequest rr) {
            modelRouteRequest = rr;
        }
        public BranchGeoModelGenerator(Location center, Integer radiusInFeet, String mode) {

        }


        public BranchGeoModel generateGeoModel(){
            String origin = modelRouteRequest.getOrigin();
            String destination = modelRouteRequest.getDestination();
     //       List<String> modes = modelRouteRequest.getModePrefAsList();
            UserRouteRequest tempRequest = new UserRouteRequest();
    //        Route coreRoute = tempRequest.getRouteFromApi(origin, destination, "transit");

            LinkedList<LinearRoute> coreRoute = tempRequest.getRoutesFromApi(origin, destination, "transit", Boolean.TRUE);

            this.originWayPoint = coreRoute.get(0).wayPointLinkedList.getFirst();
            this.destinationWayPoint = coreRoute.get(0).wayPointLinkedList.getLast();
            // the following 7 code lines are to process just the first returned route from the coreRoute list
            // this we become a loop after tested successfully, to process all routes in list

            for (int j = 0; j < coreRoute.size(); j++) {
                coreRoute.get(j).removeAdjacentSameModeEdges();
                this.routeList.add(coreRoute.get(j));
                this.geographicMap.addGraph(convertRouteToGeoModel(coreRoute.get(j)));
            }
            LinkedList<BranchVertex> tempVertices= new LinkedList<>();
            tempVertices= (LinkedList<BranchVertex>) geographicMap.cloneVertexList();
            for (int i = 1; i < tempVertices.size(); i++) {
                BranchVertex legStart = tempVertices.get(i-1);
                BranchVertex legEnd = tempVertices.get(i);
                // todo - Set up repetitive lookup to a nexted loop


                List<String> modes= Arrays.asList("driving", "bicycling", "transit", "walking");

                for (String loopMode : modes) {
                    LinearRoute legRoute = tempRequest.getRouteFromApi(legStart, legEnd, loopMode);
                    legRoute.removeAdjacentSameModeEdges();

                    // todo add to geographic model
                    //create a bug and add to sprint
                    this.routeList.add(legRoute);
                    BranchGeoModel tempGeoModel = convertRouteToGeoModel(legRoute);
                    geographicMap.addGraph(tempGeoModel);
                }
            }
            geographicMap.removeDuplicateVertices();
            //FixMe - some vertices have only one edge
            return geographicMap;
        }

        public BranchGeoModel convertRouteToGeoModel (LinearRoute route) {
                BranchGeoModel tempGeoModel = new BranchGeoModel();
                int size = route.wayPointLinkedList.size();
                BranchVertex lastLegDestination = new BranchVertex(new Location(0.0d, 0.0d));
                for (int j = 0; j < size-1; j++){
                    LinearWayPoint w1 = route.wayPointLinkedList.get(j);
                    BranchVertex v1 = new BranchVertex(new Location(0.0,0.0));
                    if (j==0) {
                        v1 = waypointToVertex(w1);
                    } else {
                        v1 = lastLegDestination;
                    }
                    Edge<LinearWayPoint> e1 = w1.getEdge();
                    LinearWayPoint w2 = route.wayPointLinkedList.get(j+1);
                    BranchVertex v2 = waypointToVertex(w2);
                    Edge<BranchVertex> forward = new Edge<>(v1, v2, e1.getMode(), e1.getDuration(), e1.getCost(), e1.getDistance());
                    v1.addEdge(forward);
                    Edge<BranchVertex> backward = new Edge<>(v2, v1, e1.getMode(), e1.getDuration(), e1.getCost(), e1.getDistance());
                    v2.addEdge(backward);
                    tempGeoModel.addVertex(v1);
                    if (j == size-2){
                        tempGeoModel.addVertex(v2);
                    }
                    lastLegDestination = v2;
                }
            return tempGeoModel;
        }
    }
}

