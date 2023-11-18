package com.down2thewire.webapp;
import com.down2thewire.*;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RouteOptionServlet", value = "/RouteOptionServlet")
public class RouteOptionServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Create three routes for demonstration
        List<ProposedRoute> proposedRoutes = createProposedRoutes();
        // Now you can access proposedRoutes using the 'proposedRoutes' list
//        for (ProposedRoute proposedRoute : proposedRoutes) {
//            // each route can be used for example print details
//            System.out.println(proposedRoute.toString());
//        }
        // Convert the list to JSON
        String jsonData = new Gson().toJson(proposedRoutes);
        // Log the JSON data
        System.out.println("Data to be sent to frontend: " + jsonData);
        // Set content type and write the JSON data to the response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonData);
    }

    private List<ProposedRoute> createProposedRoutes() {
        List<LinearRoute> linearRoutes = createLinearRoute();
        List<ProposedRoute> proposedRoutes = new ArrayList<>();

        for (LinearRoute linearRoute : linearRoutes) {
            proposedRoutes.add(new ProposedRoute(linearRoute));
            //System.out.println(proposedRoutes);
        }
        return proposedRoutes;
    }
    private List<LinearRoute> createLinearRoute(){
        //ProposedRoute pr;
    List<LinearRoute> routes = new ArrayList<>();
        // Primary Route
        LinearRoute route1 = new LinearRoute();
        Location l1 = new Location(33.9228732,-84.3418493);
        LinearWayPoint wp1 = new LinearWayPoint(l1, "Biltmore Hotel");
        Location l2 = new Location(33.921227,-84.344398);
        LinearWayPoint wp2 = new LinearWayPoint(l2, "10th St Marta Station");
        Location l3 = new Location(33.789112,-84.387383);
        LinearWayPoint wp3 = new LinearWayPoint(l3, "Zoo Atlanta");

        Edge<LinearWayPoint> e1 = new Edge<>(wp1, wp2, "WALKING", 900, 0.00, 347);
        wp1.setEdge(e1);
        Edge<LinearWayPoint> e2 = new Edge<>(wp2, wp3, "TRANSIT", 1400, 4.00, 14522);
        wp2.setEdge(e2);


        route1.addWaypoint(wp1);
        route1.addWaypoint(wp2);
        route1.addWaypoint(wp3);

        //pr=new ProposedRoute(route1);

        // Alternate vertex where the user gets onto the bus
        LinearRoute route2 = new LinearRoute();
        Location l4 = new Location(33.9228732,-84.3418493);
        LinearWayPoint wp4 = new LinearWayPoint(l4, "Biltmore Hotel");
        Location l5 = new Location(33.7892632,-84.3873414);
        LinearWayPoint wp5 = new LinearWayPoint(l5, "Kroger - 100 Rogers Ave");
        Location l6 = new Location(33.789112,-84.387383);
        LinearWayPoint wp6 = new LinearWayPoint(l6, "Zoo Atlanta");

        Edge<LinearWayPoint> e4 = new Edge<>(wp4, wp5, "WALKING", 2100, 0.00, 700);
        wp4.setEdge(e4);
        Edge<LinearWayPoint> e5 = new Edge<>(wp5, wp6, "TRANSIT", 8084, 4.00, 16246);
        wp5.setEdge(e5);

        route2.addWaypoint(wp4);
        route2.addWaypoint(wp5);
        route2.addWaypoint(wp6);
        //pr=new ProposedRoute(route2);

        // Alternate Primary Mode with parking costs
        LinearRoute route3 = new LinearRoute();
        Location l7 = new Location(33.9228732,-84.3418493);
        LinearWayPoint wp7 = new LinearWayPoint(l7, "Biltmore Hotel");
        Location l8 = new Location(33.995648,-84.344398);
        LinearWayPoint wp8 = new LinearWayPoint(l8, "14th St Marta Station");
        Location l9 = new Location(33.789112,-84.387383);
        LinearWayPoint wp9 = new LinearWayPoint(l9, "Zoo Atlanta");

        Edge<LinearWayPoint> e7 = new Edge<>(wp7, wp8, "DRIVING", 60, 14.00, 347);
        wp7.setEdge(e7);
        Edge<LinearWayPoint> e8 = new Edge<>(wp8, wp9, "TRANSIT", 1400, 4.00, 14522);
        wp8.setEdge(e8);

        route3.addWaypoint(wp7);
        route3.addWaypoint(wp8);
        route3.addWaypoint(wp9);

       //pr=new ProposedRoute(route3);

        routes.add(route1);
        routes.add(route2);
        routes.add(route3);

return routes;
    }
}
//    private static LinearRoute createRoute1() {
//        LinearRoute route1 = new LinearRoute();
//        Location l1 = new Location(33.9228732, -84.3418493);
//        LinearWayPoint wp1 = new LinearWayPoint(l1, "Biltmore Hotel");
//        Location l2 = new Location(33.921227, -84.344398);
//        LinearWayPoint wp2 = new LinearWayPoint(l2, "10th St Marta Station");
//        Location l3 = new Location(33.789112, -84.387383);
//        LinearWayPoint wp3 = new LinearWayPoint(l3, "Zoo Atlanta");
//        Edge<LinearWayPoint> e1 = new Edge<>(wp1, wp2, "WALKING", 900, 0.00, 347);
//        wp1.setEdge(e1);
//        Edge<LinearWayPoint> e2 = new Edge<>(wp2, wp3, "TRANSIT", 1400, 4.00, 14522);
//        wp1.setEdge(e1);
//        route1.addWaypoint(wp1);
//        route1.addWaypoint(wp2);
//        route1.addWaypoint(wp3);
//
//        return route1;
//    }
//    List<String> generateRouteDescriptions() {
//        // Primary Route
//        LinearRoute route1 = new LinearRoute();
//        Location l1 = new Location(33.9228732, -84.3418493);
//        LinearWayPoint wp1 = new LinearWayPoint(l1, "Biltmore Hotel");
//        Location l2 = new Location(33.921227, -84.344398);
//        LinearWayPoint wp2 = new LinearWayPoint(l2, "10th St Marta Station");
//        Location l3 = new Location(33.789112, -84.387383);
//        LinearWayPoint wp3 = new LinearWayPoint(l3, "Zoo Atlanta");
//
//        // Populate route description
//        String routeDescription1 = generateRouteDescription(route1);
//
//        // Alternate vertex where the user gets onto the bus
//        LinearRoute route2 = new LinearRoute();
//        Location l4 = new Location(33.9228732, -84.3418493);
//        LinearWayPoint wp4 = new LinearWayPoint(l4, "Biltmore Hotel");
//        Location l5 = new Location(33.7892632, -84.3873414);
//        LinearWayPoint wp5 = new LinearWayPoint(l5, "Kroger - 100 Rogers Ave");
//        Location l6 = new Location(33.789112, -84.387383);
//        LinearWayPoint wp6 = new LinearWayPoint(l6, "Zoo Atlanta");
//
//        // Populate route description
//        String routeDescription2 = generateRouteDescription(route2);
//
//        // Alternate Primary Mode
//        LinearRoute route3 = new LinearRoute();
//        Location l7 = new Location(33.9228732, -84.3418493);
//        LinearWayPoint wp7 = new LinearWayPoint(l7, "Biltmore Hotel");
//        Location l8 = new Location(33.995648, -84.344398);
//        LinearWayPoint wp8 = new LinearWayPoint(l8, "14th St Marta Station");
//        Location l9 = new Location(33.789112, -84.387383);
//        LinearWayPoint wp9 = new LinearWayPoint(l9, "Zoo Atlanta");
//
//        // Populate route description
//        String routeDescription3 = generateRouteDescription(route3);
//
//        // Create a list of route descriptions
//        List<String> routeDescriptions = new LinkedList<>();
//        routeDescriptions.add(routeDescription1);
//        routeDescriptions.add(routeDescription2);
//        routeDescriptions.add(routeDescription3);
//
//        return routeDescriptions;
//    }
//
//    // Helper method to generate route description from LinearRoute
//    private String generateRouteDescription(LinearRoute route) {
//        StringBuilder description = new StringBuilder();
//        for (LinearWayPoint waypoint : route.getWaypoint()) {
//            description.append(waypoint.getDescription()).append(" -> ");
//        }
//        // Remove the last " -> " from the description
//        if (description.length() > 4) {
//            description.setLength(description.length() - 4);
//        }
//        return description.toString();
//    }
//}
