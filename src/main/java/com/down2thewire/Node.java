package com.down2thewire;

abstract class Node<T extends Node> {
    Location location;
    String description;
    Long id;
    Edge2 edge;




    public Node (Location location, String description){
        this.location = location;
        this.description = description;
        this.id = location.generateUniqueID();
    }

//    public Node (Location location) {
//        this.location = location;
//        this.description = "";
//        this.id = location.generateUniqueID();
//    }

    public Node() {

    }

    public Double getLongitude (){
        return this.location.getLongitude();
    }

    public Double getLatitude (){
        return this.location.getLatitude();
    }

    public void setDescription (String description){
        this.description = description;
    }

//    public Location getLocation (){
//        return this.location;
//    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public Boolean isNear(Node<T> node, int feet){
        return node.location.isNear(this.location, feet);
    }

    public Boolean isMatchById(Node<T> node) {
        if (node.getId().equals(this.id)){return Boolean.TRUE;}
        else return Boolean.FALSE;
    }

    public Boolean isMatchById(Long id) {
        if (this.getId().equals(id)) {return Boolean.TRUE;}
        else return Boolean.FALSE;
    }
}

