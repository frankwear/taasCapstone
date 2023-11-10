package com.down2thewire;


import java.util.LinkedList;
public class ProposedRoute {
    private LinkedList<Leg> legs = new LinkedList<>();

    public ProposedRoute(LinearRoute pr) {
        Leg summaryLeg = new Leg(new Edge<>());
        summaryLeg.mode = "Summary";
        if (pr.wayPointLinkedList != null) {
            //LinearWayPoint wayPoint;
            for (int i=0; i<pr.wayPointLinkedList.size()-1; i++) {// LinearWayPoint wayPoint : pr.wayPointLinkedList) {
                legs.addLast(new Leg(pr.wayPointLinkedList.get(i).getEdge()));
                summaryLeg.distance += pr.wayPointLinkedList.get(i).getEdge().getDistance();
                summaryLeg.duration += pr.wayPointLinkedList.get(i).getEdge().getDuration();
                summaryLeg.cost += pr.wayPointLinkedList.get(i).getEdge().getCost();
            }
        }
        legs.addFirst(summaryLeg);
    }

    public LinkedList<Leg> getLegs() {
        return legs;
    }

    public static class Leg {
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