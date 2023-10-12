package com.down2thewire;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class DataConnection {

    static String[] credentials = ApiKeys.dbConnection();
    private static final String db_URL = credentials[0];
    private static final String db_USER = credentials[1];
    private static final String db_PASSWORD = credentials[2];//"database";



    // maven dependency added
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(db_URL, db_USER, db_PASSWORD)) {
            System.out.println("Connected to the database.");
//            insertJsonTableData(33.9228732, -84.3418493,
//                    33.9228732, -84.3418493,
//                    "walking", 20, 12);
            //connection.close();
            // Retrieve and print data from the jsontable
            retrieveJsonTableData(connection);
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    public static void insertJsonTableData(
            double originLatitude, double originLongitude,
            double destinationLatitude, double destinationLongitude,
            String mode, int distance, int duration) {
        try (Connection connection = DriverManager.getConnection(db_URL, db_USER, db_PASSWORD)) {
            System.out.println("Connected to the database in insertJsonTableData() method.");
            String sql = "INSERT INTO jsontable " +
                    "(origin_latitude, origin_longitude, destination_latitude, destination_longitude, " +
                    "mode, distance, duration) " +
                    "VALUES (?,?,?,?,?,?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDouble(1, originLatitude);
                preparedStatement.setDouble(2, originLongitude);
                preparedStatement.setDouble(3, destinationLatitude);
                preparedStatement.setDouble(4, destinationLongitude);
                preparedStatement.setString(5, mode);
                preparedStatement.setInt(6, distance);
                preparedStatement.setInt(7, duration);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void retrieveJsonTableData(Connection connection) {
        try {
            String sql = "SELECT * FROM jsontable";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    double originLatitude = resultSet.getDouble("origin_latitude");
                    double originLongitude = resultSet.getDouble("origin_longitude");
                    double destinationLatitude = resultSet.getDouble("destination_latitude");
                    double destinationLongitude = resultSet.getDouble("destination_longitude");
                    String mode = resultSet.getString("mode");
                    int distance = resultSet.getInt("distance");
                    int duration = resultSet.getInt("duration");

                    System.out.println("Data from jsontable:");
                    System.out.println("Origin Latitude: " + originLatitude);
                    System.out.println("Origin Longitude: " + originLongitude);
                    System.out.println("Destination Latitude: " + destinationLatitude);
                    System.out.println("Destination Longitude: " + destinationLongitude);
                    System.out.println("Mode: " + mode);
                    System.out.println("Distance: " + distance);
                    System.out.println("Duration: " + duration);
                    System.out.println();
                }

                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}