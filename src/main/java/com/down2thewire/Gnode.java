package com.down2thewire;

abstract class GNode<T> {
    Location location;
    String description;
    Long id;
    T vertex;

    /*
        public GNode (Location location, String description, T edge) {
            this.location = location;
            this.description = description;
            this.id = location.generateUniqueID();
            this.edge = edge;
        }
        public GNode (Location location, T edge){
            this.location = location;
            this.description = "";
            this.id = location.generateUniqueID();
            this.edge = edge;
        }
    */
    public GNode (Location location, String description){
        this.location = location;
        this.description = description;
        this.id = location.generateUniqueID();
    }

    public GNode (Location location) {
        this.location = location;
        this.description = "";
        this.id = location.generateUniqueID();
    }
    public GNode (){
        this.description = "";
    }

    public Double getLongitude (){
        return this.location.longitude;
    }

    public Double getLatitude (){
        return this.location.latitude;
    }

    public void setDescription (String description){
        this.description = description;
    }

    public Location getLocation (){
        return this.location;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return this.id;
    }

    public Boolean isMatch(GNode<T> node){
        return node.location.isMatch(this.location);
    }
}