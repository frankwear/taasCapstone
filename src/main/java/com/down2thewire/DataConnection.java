package com.down2thewire;
import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.PreparedStatement;
import java.sql.SQLException;
public class DataConnection {
    private static final String db_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String db_USER = "postgres";
    private static final String db_PASSWORD = "postgres";
    // maven dependency added
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(db_URL, db_USER, db_PASSWORD)) {
            System.out.println("Connected to the database.");
            //connection.close();
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
}

//    public void insertDistanceMatrixData(
//            double originLatitude, double originLongitude,
//            double destinationLatitude, double destinationLongitude,
//            String mode, int distance, int duration, String apiRequestUrl) {
//
//        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
//            String sql = "INSERT INTO distance_matrix_data " +
//                    "(origin_latitude, origin_longitude, destination_latitude, destination_longitude, " +
//                    "mode, distance, duration, api_request_url) " +
//                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//
//            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                preparedStatement.setDouble(1, originLatitude);
//                preparedStatement.setDouble(2, originLongitude);
//                preparedStatement.setDouble(3, destinationLatitude);
//                preparedStatement.setDouble(4, destinationLongitude);
//                preparedStatement.setString(5, mode);
//                preparedStatement.setInt(6, distance);
//                preparedStatement.setInt(7, duration);
//                preparedStatement.setString(8, apiRequestUrl);
//
//                preparedStatement.executeUpdate();
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    }
//}
