package com.down2thewire.webapp;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.down2thewire.UserRouteRequest;

@WebServlet(name = "CaptureDataServlet", value = "/CaptureDataServlet")
public class CaptureDataServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Retrieve data from the request-request's input stream
            BufferedReader reader = request.getReader();
            // Parse JSON data using Gson library
            Gson gson = new Gson();
            FormData formData = gson.fromJson(reader, FormData.class);
            //String requestData = reader.lines().collect(Collectors.joining());


            // Accessing data from FormData object and storing in variables
            String origin = formData.getOrigin();
            String addStop = formData.getAddStop();
            String destination = formData.getDestination();
            int walking = formData.getWalking();
            int bicycling = formData.getBicycling();
            int drive = formData.getDrive();
            int transit = formData.getTransit();
            String selectedOption = formData.getSelectedOption();
            //route 1
            //route 2
            //route 3
            // Created an instance of LinearRoute
            //List<LinearRoute> routeOptions = generateRoute(/*origin, addStop, destination*/);
// Generate routes using RouteGenerator class
        //    List<Map<String, Object>> routeOptions = RouteOption.generateRoutes(origin, addStop, destination,
         //           walking, 0.5, drive, 2.0, transit, 5.0);
            // Convert the route to JSON (used Gson library for this)
         // String routeJson = gson.toJson(routeOptions);
            // printing the data to console
            System.out.println("Selected Origin is: " + origin);
            System.out.println("Selected Add Stop is: " + addStop);
            System.out.println("Selected Destination is: " + destination);
            System.out.println("Priority for Walking: " + walking);
            System.out.println("Priority for Bicycling: " + bicycling);
            System.out.println("Priority for Drive in : " + drive);
            System.out.println("Priority for Transit: " + transit);
            System.out.println("Preference is: " + selectedOption);
            // send a response back to the client as per need
            response.getWriter().write("Data received in backend");

            // Send the JSON response back to the client
            response.setContentType("application/json");
           // response.getWriter().write(json);
            Integer[] modePref = {walking, bicycling, drive, transit};
            UserRouteRequest userRouteRequest = new UserRouteRequest(origin, destination, modePref, selectedOption);
            //todo call userRouteRequest to return the LinkedList<ProposedRoute>

        } catch (Exception e) {
            // handle any exception, log an error, or send an error response
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }

    }
    //private List<LinearRoute> generateRoute() {
        /*
        // Create an instance of LinearRoute
        LinearRoute linearRoute = new LinearRoute();
        // Created Location objects for waypoints based on the sample data here sample considered is :origin : Piedmont Park;  destination : Downtown Atlanta  ; addStop:Peachtree St NE & 5th St NE
        Location originLocation = new Location(33.785254, 84.373777);
        Location addStopLocation = new Location(33.785098, 84.383364);
        Location destinationLocation = new Location(33.749299, 84.387988);


        // Add waypoints based on the input data
        linearRoute.addWaypoint(new LinearWayPoint(originLocation));
        linearRoute.addWaypoint(new LinearWayPoint(addStopLocation));
        linearRoute.addWaypoint(new LinearWayPoint(destinationLocation));

        return linearRoute;
        */
   // }
    // FormData class  represents the structure of the formdata sent by FE
    private static class FormData {
        private String origin;
        private String addStop;
        private String destination;
        private int walking;
        private int bicycling;
        private int drive;
        private int transit;
        private String selectedOption;


        //Getter methods

        public String getOrigin() {
            return origin;
        }

        public String getAddStop() {
            return addStop;
        }

        public String getDestination() {
            return destination;
        }

        public int getWalking() {
            return walking;
        }

        public int getBicycling() {
            return bicycling;
        }

        public int getDrive() {
            return drive;
        }

        public int getTransit() {
            return transit;
        }

        public String getSelectedOption() {
            return selectedOption;
        }

    }
}
