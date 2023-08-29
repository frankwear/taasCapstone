package com.down2thewire;
import java.util.List;
//import java.util.TreeMap;
abstract class Graph {

    //new MyBSTree;

    abstract void addNode(Vertex node);
    abstract void removeNode(Vertex node);
    abstract void addGraph(Graph graph);
    abstract Boolean isUnique(Vertex node);
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