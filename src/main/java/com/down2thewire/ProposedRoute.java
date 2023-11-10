package com.down2thewire;


import java.util.LinkedList;
public class ProposedRoute {

    private LinkedList<Leg> legs = new LinkedList<>();


    public ProposedRoute(LinearRoute pr) {
        Leg summaryLeg = new Leg(new Edge<>());
        summaryLeg.mode = "Summary";
        for (Edge edge : pr.edgeList) {
            legs.addLast(new Leg(edge));
            summaryLeg.distance += edge.getDistance();
            summaryLeg.duration += edge.getDuration();
            summaryLeg.cost += edge.getCost();
        }
        legs.addFirst(summaryLeg);
    }
    public LinkedList<Leg> getLegs() {
        return legs;
    }
    public class Leg {
        Integer distance;
        Integer duration;
        Double cost;
        String mode;
        public Leg(Edge edge) {
            distance = edge.getDistance();
            duration = edge.getDuration();
            cost = edge.getCost();
            mode = edge.getMode();
        }
    }
}