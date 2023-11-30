package com.down2thewire;

import com.beust.ah.A;
import com.sun.jdi.IntegerValue;
import org.testng.internal.collections.Pair;

import javax.lang.model.element.Element;
import java.util.*;

public class RouteManager {
    private UserRouteRequest routeRequest;
    BranchGeoModel areaModel = new BranchGeoModel();
    RouteIdentifier routeIdentifier;
    LinkedList<LinearRoute> routes = new LinkedList<>();


    public RouteManager(UserRouteRequest routeRequest) {
        if (routeRequest.destinationWaypoint == null || routeRequest.originWaypoint == null) {
            System.out.println("User Route Requests must include origin and destination waypoints");
        }
        this.routeRequest = routeRequest;
        this.areaModel = getAreaModel(routeRequest, 1.0f); // todo adjust size factor or make variable
        routeIdentifier = new RouteIdentifier(areaModel, routeRequest);
        LinkedList<Pair<String, String>> modePairs = getOrderedModes();
        Integer maxLoops = Math.max(5, modePairs.size());
        for (int i = 0; i < maxLoops; i++) {
            routes.add(
                    routeIdentifier.getBestRoute(modePairs.getFirst().first(), modePairs.getFirst().second(), routeRequest.getPriority(), 5000)
            );
        }
    }


    private BranchGeoModel getAreaModel(UserRouteRequest routeRequest, Float sizeFactor) {
        Location midpoint = getMidpoint(routeRequest);
        Double straightDistance = estimateDistance();
        BranchGeoModel localDbAreaModel = queryDbForBoundedGeography(sizeFactor);
        if (isNotSufficient(localDbAreaModel)){
            BranchGeoModel localApiAreaModel = new BranchGeoModel();
//            PlacesApi placesApi = new PlacesApi()
//            localApiAreaModel = P
        }
//        HashMap<String, String> myParameters = new HashMap<>();
//        myParameters.put("location=", "33.8876001,-84.3142002");
//        myParameters.put("&type=", "transit_station");
//        myParameters.put("&radius=","7500");
//        BranchGeoModel testGM = PlacesApi.buildPlacesFromApiCall(myParameters);


        //todo - make db query for geomodel inside corners

        //todo - evaluate value of response from database
        //todo - set up API call if necessary
        return new BranchGeoModel();
    }

    private boolean isNotSufficient(BranchGeoModel localDbAreaModel) {
        return Boolean.FALSE;
    }

    private Double estimateDistance() {
        Double startLatitude = routeRequest.getOriginWaypoint().getLocation().getLatitude();
        Double startLongitude = routeRequest.getOriginWaypoint().getLocation().getLongitude();
        Double endLatitude = routeRequest.getDestinationWaypoint().getLocation().getLatitude();
        Double endLongitude = routeRequest.getDestinationWaypoint().getLocation().getLongitude();
        Double estDistance = (Math.abs(startLatitude - endLatitude) + Math.abs(startLongitude - endLongitude)) * 300000.0d;
        return estDistance;
    }

    private Location getMidpoint(UserRouteRequest routeRequest) {
        Double startLatitude = routeRequest.getOriginWaypoint().getLocation().getLatitude();
        Double startLongitude = routeRequest.getOriginWaypoint().getLocation().getLongitude();
        Double endLatitude = routeRequest.getDestinationWaypoint().getLocation().getLatitude();
        Double endLongitude = routeRequest.getDestinationWaypoint().getLocation().getLongitude();
        Double midLatitude = (startLatitude + endLatitude)/2.0d;
        Double midLongitude = (startLongitude + endLongitude)/2.0d;
        return new Location(midLatitude, midLongitude);
    }

    private BranchGeoModel queryDbForBoundedGeography(Float sizeFactor) {
        Location[] corners = getCorners(routeRequest, sizeFactor);
        DataConnection dataConnection = new DataConnection();
//        return dataConnection.getBoundedGeography(corners);
        return new BranchGeoModel();
    }

    private Location[] getCorners(UserRouteRequest routeRequest, Float sizeFactor) {
        Location l1 = routeRequest.getOriginWaypoint().getLocation();
        Location l2 = routeRequest.getDestinationWaypoint().getLocation();
        Location minimumCorner = new Location(
                Math.min(l1.getLatitude(), l2.getLatitude()),
                Math.min(l1.getLongitude(), l2.getLongitude()));
        Location maximumCorner = new Location(
                Math.max(l1.getLatitude(), l2.getLatitude()),
                Math.max(l1.getLongitude(), l2.getLongitude()));
        Location[] corners = new Location[]{minimumCorner, maximumCorner};
        return corners;
    }





    public LinkedList<Pair<String, String>> getOrderedModes() {
        LinkedList<Pair<String, String>> knownModePairs = new LinkedList<Pair<String, String>>();
        Integer[] rrModePref = routeRequest.getModePrefAsArray();
        LinkedList<String> modesWithHigherPreference = new LinkedList<String>();
        for (int tempPref = 3; tempPref > 0; tempPref--) { // leaves out preference 0 - disallowed
            // generate a list of modes at each preference
            LinkedList<String> modesWithSamePreference = new LinkedList<String>();
            for (int i = 0; i < rrModePref.length; i++) {
                if (rrModePref[i] == tempPref) {
                    String modeName = getModeName(i);
                    modesWithSamePreference.add(modeName);
                }
            }
            if (modesWithSamePreference.isEmpty()) {
                continue;  // go on to next preference level
            }
            Pair<String, String> modePair;
            if (modesWithHigherPreference.isEmpty()) {
                if (modesWithSamePreference.size() == 1) {
                    modesWithHigherPreference.add(modesWithSamePreference.getFirst());
                    continue;
                }
                if (modesWithSamePreference.size() == 2) {
                    modePair = new Pair<>(modesWithSamePreference.get(0), modesWithSamePreference.get(1));
                    knownModePairs.add(modePair);
                    for (String modeName : modesWithSamePreference) {
                        modesWithHigherPreference.add(modeName);
                    }
                    continue;
                }
                if (modesWithSamePreference.size() == 3) {
                    modePair = new Pair<>(modesWithSamePreference.get(0), modesWithSamePreference.get(1));
                    knownModePairs.add(modePair);
                    modePair = new Pair<>(modesWithSamePreference.get(0), modesWithSamePreference.get(2));
                    knownModePairs.add(modePair);
                    modePair = new Pair<>(modesWithSamePreference.get(1), modesWithSamePreference.get(2));
                    knownModePairs.add(modePair);
                }
                if (modesWithSamePreference.size() > 3) {
                    for (int i = 1; i < modesWithSamePreference.size(); i++) {
                        modePair = new Pair<>(modesWithSamePreference.get(0), modesWithSamePreference.get(i));
                        knownModePairs.add(modePair);
                    }
                }
            } else {
                for (String mode1 : modesWithHigherPreference) {
                    for (String mode2 : modesWithSamePreference) {
                        modePair = new Pair<>(mode1, mode2);
                        knownModePairs.add(modePair);
                    }
                }
            }
            modesWithHigherPreference.addAll(modesWithSamePreference);
        }
        return knownModePairs;
    }

    public String getModeName(int i) {
        switch (i) {
            case 1:
                return "BICYCLE";
            case 2:
                return "DRIVING";
            case 3:
                return "TRANSIT";
            case 0:
            default:
                return "WALKING";
        }
    }
}


