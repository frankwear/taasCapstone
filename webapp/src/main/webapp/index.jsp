<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Random" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transportation Selection Form</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<div class="navbar">
    <ul>
        <li><a href="#">User Profile</a></li>
        <li><a href="#">Saved Locations</a></li>
        <li><a href="#">Help</a></li>
        <li class="logout"><a href="#">Logout</a></li>
    </ul>
</div>
<h1 align="center">Transportation Selection Form</h1>
<div class="container">
    <form action="CaptureDataServlet" method="post" enctype="multipart/form-data">
        <!-- Your existing form fields... -->
        <div class="form-row-horizontal">
            <div class="form-row">
                <label for="origin">Origin:</label>
                <input type="text" id="origin" name="origin" placeholder="Enter Origin" required>
            </div>
            <div class="form-row">
                <label for="addStop">Add Stop:</label>
                <input type="text" id="addStop" name="addStop" placeholder="Enter stop">
            </div>
            <div class="form-row">
                <label for="destination">Destination:</label>
                <input type="text" id="destination" name="destination" placeholder="Enter destination" required>
            </div>
            <div class="form-row">
                <label for="walking">Walking (1 - Must Use):</label>
                <input type="number" id="walking" name="walking" min="1" max="4" value="1" required>
            </div>
            <div class="form-row">
                <label for="bicycling">Bicycling (1 - Must Use):</label>
                <input type="number" id="bicycling" name="bicycling" min="1" max="4" value="3" required>
            </div>
            <div class="form-row">
                <label for="drive">Drive (1 - Must Use):</label>
                <input type="number" id="drive" name="drive" min="1" max="4" value="1" required>
            </div>
            <div class="form-row">
                <label for="transit">Transit (1 - Must Use):</label>
                <input type="number" id="transit" name="transit" min="1" max="4" value="2" required>
            </div>
        </div>

        <div class="form">
            <label>Select an option:</label>
            <div class="radio-options-horizontal">
                <label><input type="radio" name="option" value="cost" onclick="setSelectedOption(this)">Cost</label>
                <label><input type="radio" name="option" value="speed" onclick="setSelectedOption(this)" checked>Speed </label>
                <label><input type="radio" name="option" value="best-route" onclick="setSelectedOption(this)">Best route</label>
                <label><input type="radio" name="option" value="low-carbon" onclick="setSelectedOption(this)">Low Carbon</label>
                <label><input type="radio" name="option" value="fewer-transfers" onclick="setSelectedOption(this)">Fewer transfers</label>
            </div>
        </div>

        <div>
            <!-- Button to toggle the dropdown -->
            <button type="button" id="submit" onclick="handleGoButtonClick()" class="submit-btn">Go</button>
            <!-- Dropdown menu -->
            <%
                // Initialize variables with random values
                Random random = new Random();
                int[] time = {random.nextInt(60) + 1, random.nextInt(60) + 1, random.nextInt(60) + 1};
                double[] distance = {random.nextDouble() * 10, random.nextDouble() * 10, random.nextDouble() * 10};
                int[] transits = {random.nextInt(10), random.nextInt(10), random.nextInt(10)};
            %>

            <!-- Dropdown menu -->
            <div id="dropdownMenu" class="dropdown-menu stylish-dropdown" style="display: none;">
                <select id="optionsDropdown">
                    <% for (var i = 0; i < time.length; i++) { %>
                    <option value="<%= i %>">
                        Time: <%= time[i] %> mins, Distance: <%= distance[i] %> miles, Transits: <%= transits[i] %>
                    </option>
                    <% } %>
                </select>
                <button onclick="selectOption()">Select</button>
            </div>
        </div>
    </form>

    <!-- Map placeholder -->
    <div class="map-placeholder">
        <img src="map_placeholder_image.jpeg" alt="Map placeholder" width="100%" height="100%">
    </div>
</div>
</body>
<script src="script.js"></script>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</html>
