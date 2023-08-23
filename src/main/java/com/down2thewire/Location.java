package com.down2thewire;

public class Location {
    double longitude;
    double latitude;


    public Location(double la, double lo) {
        this.longitude = lo;
        this.latitude = la;
    }
    public boolean isMatch(Location tempLoc) {
        // .0001 degrees is about 10 meters, so 20 meter square box around location
        if (Math.abs(tempLoc.getLatitude() - this.getLatitude()) < .0001){
            if (Math.abs(tempLoc.getLongitude() - this.getLongitude()) < .0001){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String AsString() {
        double roundedLatitude = Math.round(getLatitude()*100000);
        roundedLatitude = roundedLatitude/100000;
        double roundedLongitude = Math.round(getLongitude()*100000);
        roundedLongitude = roundedLongitude/100000;
        return String.valueOf(roundedLatitude) + "," + String.valueOf(roundedLongitude);
    }

    public Long generateUniqueID() {
        //todo

        double tempLat = (this.latitude + 90) * 10000;
        double tempLong = (this.longitude + 180) * 10000;
        long tempLat2 = (long) tempLat;
        long tempLong2 = (long) tempLong;

        long id = 0;
        for (int i = 7; i > 0; i--) {
            long r = (long) (tempLong2 / Math.pow(10, (i - 1)));
            r = (long) (r * (Math.pow(10, (2*i -1))));
            tempLong2 = (long) (tempLong2 % Math.pow(10, i - 1));

            long r1 = (long) (tempLat2 / Math.pow(10, (i - 1)));
            r1 = (long) (r1 * (Math.pow(10, (2*i -2))));
            tempLat2 = (long) (tempLat2 % Math.pow(10, i - 1));
            id = id + r + r1;
        }
        return id;
    }

/* public static void main(String args[]) {
     Location testloc = new Location(34.0380828, -84.584152);
     Long id = testloc.generateUniqueID();
     Long l1 = 935440135880L;

 }*/
}
