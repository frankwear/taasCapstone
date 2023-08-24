package com.down2thewire;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
        Location location;
        String vertexName;
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

        private boolean[] modes = {false, false, false, false, false, false, false, false, false, false};
        private double tentativeDistance;

        public Vertex(Location location, String uniqueNameId) {
            this.location = location;
            this.vertexName = uniqueNameId;
            this.tentativeDistance = Double.POSITIVE_INFINITY;
        }

        public Vertex() {
        }

        public Double getLongitude() {
            return this.location.getLongitude();
        }

        public Double getLatitude() {
            return this.location.getLatitude();
        }

        public void autoVertexName() {
            // Get Location Name from GoogleLocation API
            // ToDo
        }

        public void setWalk(boolean tf) {
            modes[0] = tf;
        }

        public void setCarPark(boolean tf) {
            modes[1] = tf;
        }

        public void setRideShare(boolean tf) {
            modes[2] = tf;
        }

        public void setCarRental(boolean tf) {
            modes[3] = tf;
        }

        public void setBikeRack(boolean tf) {
            modes[4] = tf;
        }

        public void setScooterRental(boolean tf) {
            modes[5] = tf;
        }

        public void setTransit(boolean tf) {
            modes[6] = tf;
        }

        public void setBusStop(boolean tf) {
            modes[7] = tf;
        }

        public boolean getWalk() {
            return modes[0];
        }

        public boolean getCarPark() {
            return modes[1];
        }

        public boolean getRideShare() {
            return modes[2];
        }

        public boolean getCarRental() {
            return modes[3];
        }

        public boolean getBikeRack() {
            return modes[4];
        }

        public boolean getScooterRental() {
            return modes[5];
        }

        public boolean getTransit() {
            return modes[6];
        }

        public boolean getBusStop() {
            return modes[7];
        }

        public double getTentativeDistance() {
            return tentativeDistance;
        }

        public void setTentativeDistance(double tentativeDistance) {
            this.tentativeDistance = tentativeDistance;
        }

        public List<Edge> getOutgoingEdges(List<Edge> edgeList) {
            List<Edge> outgoingEdges = new ArrayList<>();
            for (Edge edge : edgeList) {
                if (edge.start.isMatch(this)) {
                    outgoingEdges.add(edge);
                }
            }
            return outgoingEdges;
        }


        public boolean isMatch(com.down2thewire.Vertex tempVer) {
            // match rounded to 4 decimal places - about 10 meters, so 20-meter square box around location
            if (this.location.isMatch(tempVer.location)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
    }
