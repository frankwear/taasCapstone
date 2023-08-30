package com.down2thewire;
import java.util.List;
//import java.util.TreeMap;
abstract class Graph<T>{

    //new MyBSTree;

    abstract Node addNode(Node node);
    abstract void removeNode(Node node);


    abstract void addGraph(Graph graph);
    Node> node);
    public abstract Boolean isUnique(T node);

    public abstract void addGraph(Route g);

    //in the addGraph - adding two graphs so that there are no duplicate nodes
    abstract Graph cloneGraph(Graph graph);
    //
    abstract void printGraph(Graph graph);
    abstract List<Node> find(String s);
    //effectively same as getNode in vertex;
    //if it doesn't find match returns list of nodes ;string[nodes]
    abstract List<Node> find(Location l);
    abstract Node findNext(String s);
    //overloaded with location
    abstract Node findNext(Location l);
    abstract Node getNextNode(Location l);

}