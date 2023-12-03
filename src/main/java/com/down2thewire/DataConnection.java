package com.down2thewire;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DataConnection {

    static String[] credentials = ApiKeys.dbConnection();
    private static final String db_URL = credentials[0];
    private static final String db_USER = credentials[1];
    private static final String db_PASSWORD = credentials[2];//"database";

    //Geomodel - vertices & Edges ;//loop
    //
    // maven dependency added
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(db_URL, db_USER, db_PASSWORD)) {
            System.out.println("Connected to the database.");
            //retrieveVertexData(connection);
            retrieveEdgeData(connection);
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    //process geomodel - extract vertices and edges from geomodel
    public void insertGeomodelData(BranchGeoModel geoModel) throws SQLException {
        // Loop through the linked list of vertices
        for (int i = 0; i < geoModel.getVertexListSize(); i++) {
            BranchVertex vertex = geoModel.getVertex(i);
            // send each vertex to insert method to add to DB
            insertVertexData(vertex);
        }
    }

    //add from geomodel
    public void insertVertexData(BranchVertex vertex) throws SQLException {
        try (Connection connection = DriverManager.getConnection(db_URL, db_USER, db_PASSWORD)) {
            // Check if vertex_id already exists
            if (!isVertexIdExists(connection, vertex)) {
                String sql = "INSERT INTO vertex (vertex_id, latitude, longitude, description, thirdparty_id, last_updated, " +
                        "source, private, range) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                Location l = new Location(vertex.getLatitude(), vertex.getLongitude());
                boolean uniqueIdFound = false;
                long uniqueId = 0;

                while (!uniqueIdFound) {//checking uniqueID duplication
                    uniqueId = l.generateUniqueID();

                    try (PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM vertex WHERE vertex_id = ?")) {
                        checkStatement.setLong(1, uniqueId);
                        ResultSet resultSet = checkStatement.executeQuery();
                        resultSet.next();
                        int count = resultSet.getInt(1);

                        if (count == 0) {
                            uniqueIdFound = true;
                            System.out.println("Vertex_ID is unique, adding to the database.");
                        } else {
                            System.out.println("Vertex_ID already exists. Trying another ID.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                //the insertion code
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setLong(1, uniqueId);
                    preparedStatement.setDouble(2, vertex.getLocation().getLatitude());
                    preparedStatement.setDouble(3, vertex.getLocation().getLongitude());
                    preparedStatement.setString(4, vertex.getDescription());
                    preparedStatement.setString(5, vertex.getId().toString());
                    preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                    preparedStatement.setString(7, "source_placeholder");
                    preparedStatement.setBoolean(8, false);
                    preparedStatement.setDouble(9, 0.0d);

                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Inserting vertex failed, no rows affected.");
                    }
                    //calling insertedgedata method to add edges to DB
                    insertEdgeData(connection, vertex);
                }
            } else {

                System.out.println("Vertex with the same ID already exists. Skipping insertion. Check for Edges");
            }
            //calling insertedgedata method to add edges to DB - if vertexisNotunique
            insertEdgeData(connection, vertex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retrieveVertexData(Connection connection) {
        try {
            String sql = "SELECT * FROM vertex";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Long vertexId = resultSet.getLong("vertex_id");
                    double latitude = resultSet.getDouble("latitude");
                    double longitude = resultSet.getDouble("longitude");
                    String description = resultSet.getString("description");
                    String thirdPartyId = resultSet.getString("thirdparty_id");
                    Timestamp lastUpdated = resultSet.getTimestamp("last_updated");
                    String source = resultSet.getString("source");
                    boolean isPrivate = resultSet.getBoolean("private");
                    double range = resultSet.getDouble("range");

                    System.out.println("Data from vertex table:");
                    System.out.println("Vertex ID: " + vertexId);
                    System.out.println("Latitude: " + latitude);
                    System.out.println("Longitude: " + longitude);
                    System.out.println("Description: " + description);
                    System.out.println("Third Party ID: " + thirdPartyId);
                    System.out.println("Last Updated: " + lastUpdated);
                    System.out.println("Source: " + source);
                    System.out.println("Private: " + isPrivate);
                    System.out.println("Range: " + range);
                    System.out.println();
                }

                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //insert into Edge Table
    public static void insertEdgeData(Connection conncetion, BranchVertex vertex) {
        //List<Edge> edge = vertex.getEdge();
        List<Edge> edgesList = new ArrayList<Edge>();

        // Loop through all edges of the BranchVertex and add them to the list of edges
        for (int i = 0; i < vertex.getEdgeListSize(); i++) {
            edgesList.add(vertex.getEdge(i));
        }
        //for every edge in that list i need the loop to run and add below values to database for every edge from above edgeList
        try (Connection con = DriverManager.getConnection(db_URL, db_USER, db_PASSWORD)) {
            System.out.println("Connected to the database in insertEdgeData() method.");

            String sql = "INSERT INTO edge " +
                    "(start_vertex_id, end_vertex_id, cost, distance, duration, mode) " +
                    "VALUES (?,?,?,?,?,?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                // Iterating through the edgesList and inserting data for each edge
                for (Edge edge : edgesList) {
                    if (!isEdgeExists(con, edge)) {
                        preparedStatement.setLong(1, edge.getStart().getId());
                        preparedStatement.setLong(2, edge.getEnd().getId());
                        preparedStatement.setDouble(3, edge.getCost());
                        preparedStatement.setDouble(4, edge.getDistance());
                        preparedStatement.setInt(5, edge.getDuration());
                        preparedStatement.setString(6, edge.getMode());

                        preparedStatement.executeUpdate();
                    }
                    else {
                        System.out.println("Edge Already exists not inserting");
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //retrieve data from the edge table
    public static void retrieveEdgeData(Connection connection) {
        try {
            String sql = "SELECT * FROM edge";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Long startVertexId = resultSet.getLong("start_vertex_id");
                    Long endVertexId = resultSet.getLong("end_vertex_id");
                    double cost = resultSet.getDouble("cost");
                    double distance = resultSet.getDouble("distance");
                    int duration = resultSet.getInt("duration");
                    String mode = resultSet.getString("mode");

                    System.out.println("Data from edge table:");
                    System.out.println("Start Vertex ID: " + startVertexId);
                    System.out.println("End Vertex ID: " + endVertexId);
                    System.out.println("Cost: " + cost);
                    System.out.println("Distance: " + distance);
                    System.out.println("Duration: " + duration);
                    System.out.println("Mode: " + mode);
                    System.out.println();
                }

                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to check if vertex_id exists
    private boolean isVertexIdExists(Connection connection, BranchVertex vertex) throws SQLException {
        try (PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM vertex WHERE vertex_id = ?")) {
            checkStatement.setLong(1, vertex.getId());
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        }
    }
    //helper method chekcing duplication of edge - baed on "endvertex_ID" & "mode"
    private static boolean isEdgeExists(Connection connection, Edge edge) throws SQLException{
        String query = "SELECT COUNT(*) FROM edge WHERE start_vertex_id = ? AND end_vertex_id = ? AND mode = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(query)) {
            checkStatement.setLong(1, edge.getStart().getId());
            checkStatement.setLong(2, edge.getEnd().getId());
            checkStatement.setString(3, edge.getMode());

            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        }
    }
}