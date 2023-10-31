package com.down2thewire.webapp;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.Gson;

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
        } catch (Exception e) {
            // handle any exception, log an error, or send an error response
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
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
