<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transportation Selection Form</title>
    <link rel="stylesheet" href="styles.css">

</head>
<body>
    <h1>Transportation Selection Form</h1>
    <p>Please fill out the form below:</p>
    <div class="container">
        <form action="CaptureDataServlet" method="post" enctype="multipart/form-data">
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
                <div class="radio-options-horizontal" >
                    <label><input type="radio" name="option" value="cost" onclick="setSelectedOption(this)">Cost</label>
                    <label><input type="radio" name="option" value="speed" onclick="setSelectedOption(this)" checked>Speed </label>
                    <label><input type="radio" name="option" value="best-route" onclick="setSelectedOption(this)">Best route</label>
                    <label><input type="radio" name="option" value="low-carbon" onclick="setSelectedOption(this)">Low Carbon</label>
                    <label><input type="radio" name="option" value="fewer-transfers" onclick="setSelectedOption(this)">Fewer transfers</label>
                </div>
            </div>
            <button type="button" id="submit" onclick="submitForm(), populateDropdown()">Go</button>
        </form>
<h1>Select a Route from below options </h1>
        <button id="loadRoutesButton">Load Routes</button>
        <select id="routesDropdown"></select>
        <div class="map-placeholder">
            <img src="map_placeholder_image.jpeg" alt="Map placeholder" width="100%" height="100%">
        </div>
    </div>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="script.js"></script>

</html>
<%--        // fetch('YourServletEndpoint', {--%>
<%--        //     method: 'GET',  // You can use GET or any other HTTP method--%>
<%--        //     headers: {--%>
<%--        //         'Content-Type': 'application/json'--%>
<%--        //     },--%>
<%--        // })--%>
<%--        //     .then(response => response.json())--%>
<%--        //     .then(data => {--%>
<%--        //         // Handle the response data from the servlet--%>
<%--        //         console.log('Data from servlet:', data);--%>
<%--        //--%>
<%--        //         // Perform actions with the received data--%>
<%--        //     })--%>
<%--        //     .catch(error => {--%>
<%--        //         console.error('Error:', error);--%>
<%--        //     });--%>


<%--        // Function to fetch and populate route options--%>
<%--        // function populateRouteOptions() {--%>
<%--        //     // Fetch route options from the servlet--%>
<%--        //     $.ajax({--%>
<%--        //         url: 'RouteOptionServlet', // servlet URL--%>
<%--        //         type: 'GET',--%>
<%--        //         dataType: 'json',--%>
<%--        //         success: function (routeOptions) {--%>
<%--        //             // Populate the dropdown with route options--%>
<%--        //             var dropdown = $('#routeDropdown');--%>
<%--        //             dropdown.empty(); // Clear existing options--%>
<%--        //             $.each(routeOptions, function (index, route) {--%>
<%--        //                 dropdown.append($('<option>').text('Route ' + (index + 1)).val(index));--%>
<%--        //             });--%>
<%--        //--%>
<%--        //             // Handle dropdown change event--%>
<%--        //             dropdown.on('change', function () {--%>
<%--        //                 var selectedIndex = $(this).val();--%>
<%--        //                 displayRouteDetails(routeOptions[selectedIndex]);--%>
<%--        //             });--%>
<%--        //         },--%>
<%--        //         error: function () {--%>
<%--        //             console.error('Error fetching route options');--%>
<%--        //         }--%>
<%--        //     });--%>
<%--        // }--%>
<%--        //--%>
<%--        // // Call the function to populate route options on page load--%>
<%--        // $(document).ready(function () {--%>
<%--        //     populateRouteOptions();--%>
<%--        // });--%>

<%--        // // Fetch route options from the servlet--%>
<%--        // $.ajax({--%>
<%--        //     url: 'RouteOptionServlet', //  servlet URL--%>
<%--        //     type: 'GET',--%>
<%--        //     dataType: 'json',--%>
<%--        //     success: function (routeOptions) {--%>
<%--        //         // Populate the dropdown with route options--%>
<%--        //         var dropdown = $('#routeDropdown');--%>
<%--        //         $.each(routeOptions, function (index, route) {--%>
<%--        //             dropdown.append($('<option>').text('Route ' + (index + 1)).val(index));--%>
<%--        //         });--%>
<%--        //--%>
<%--        //         // Handle dropdown change event--%>
<%--        //         dropdown.on('change', function () {--%>
<%--        //             var selectedIndex = $(this).val();--%>
<%--        //             displayRouteDetails(routeOptions[selectedIndex]);--%>
<%--        //         });--%>
<%--        //     },--%>
<%--        //     error: function () {--%>
<%--        //         console.error('Error fetching route options');--%>
<%--        //     }--%>
<%--        // });--%>
<%--        //--%>
<%--        // // Function to display route details--%>
<%--        // function displayRouteDetails(route) {--%>
<%--        //     var routeDetailsContainer = $('#routeDetails');--%>
<%--        //     routeDetailsContainer.empty(); // Clear previous details--%>
<%--        //--%>
<%--        //     // Add route details to the container--%>
<%--        //     $.each(route.waypoints, function (index, waypoint) {--%>
<%--        //         routeDetailsContainer.append('<p>' + waypoint.name + '</p>');--%>
<%--        //     });--%>
<%--       // }}--%>
<%--</script>--%>
<%--</html>--%>
    <%--    fetch('RouteOptionServlet')--%>
    <%--        .then(response => {--%>
    <%--            if (!response.ok) {--%>
    <%--                throw new Error(`Error: ${response.statusText}`);--%>
    <%--            }--%>
    <%--            return response.json();--%>
    <%--        })--%>
    <%--        .then(data => {--%>
    <%--            if (data && data.length > 0) {--%>
    <%--                // Populate the dropdown with route options--%>
    <%--                const routeOptionsDropdown = document.getElementById('routeOptionsDropdown');--%>
    <%--                const routeOptionsSelect = document.getElementById('routeOptions');--%>

    <%--                routeOptionsSelect.innerHTML = ''; // Clear previous options--%>

    <%--                data.forEach(routeOption => {--%>
    <%--                    const option = document.createElement('option');--%>
    <%--                    option.value = routeOption.id; // Assuming each route has an ID--%>
    <%--                    option.text = routeOption.name; // shud Adjust this based on route data--%>
    <%--                    routeOptionsSelect.add(option);--%>
    <%--                });--%>

    <%--                routeOptionsDropdown.style.display = 'block'; // Display the dropdown--%>
    <%--                console.log()--%>
    <%--            } else {--%>
    <%--                console.error('No route options found.');--%>
    <%--            }--%>
    <%--        })--%>
    <%--        .catch(error => {--%>
    <%--            console.error('Error fetching route options:', error);--%>
    <%--        });--%>
    <%--}--%>
<%--        // Function to handle the selected option in the dropdown--%>
        <%--function displaySelectedOption(selectedOption) {--%>
        <%--    const selectedRouteId = selectedOption.value;--%>
        <%--    // can use the selectedRouteId to fetch additional details from the backend if needed--%>

        <%--    // todo: Update the map placeholder with the selected option text--%>
        <%--    const mapPlaceholder = document.querySelector('.map-placeholder');--%>
        <%--    mapPlaceholder.innerHTML = `<p>Selected Route: ${selectedOption.options[selectedOption.selectedIndex].text}</p>`;--%>
        <%--}--%>


<%--<script>--%>
<%--    // Function to handle form submission--%>
<%--    function submitForm() {--%>
<%--        // Retrieve values from the form fields--%>
<%--        var origin = document.getElementById("origin").value;--%>
<%--        var addStop = document.getElementById("addStop").value;--%>
<%--        var destination = document.getElementById("destination").value;--%>
<%--        //integer values :--%>
<%--        var walking = document.getElementById("walking").value;--%>
<%--        var bicycling = document.getElementById("bicycling").value;--%>
<%--        var drive = document.getElementById("drive").value;--%>
<%--        var transit = document.getElementById("transit").value;--%>

<%--        // Retrieve radio button value--%>
<%--        var selectedOption = document.querySelector('input[name="option"]:checked').value;--%>

<%--        // Prepare the form data--%>
<%--        var formData = new FormData();--%>
<%--        formData.append("origin", origin);--%>
<%--        formData.append("addStop", addStop);--%>
<%--        formData.append("destination", destination);--%>
<%--        formData.append("walking", walking);--%>
<%--        formData.append("bicycling", bicycling);--%>
<%--        formData.append("drive", drive);--%>
<%--        formData.append("transit", transit);--%>
<%--        formData.append("option", selectedOption);--%>
<%--        //testing if the values are assigned properly--%>
<%--        console.log('Origin:', origin);--%>
<%--        console.log('Add Stop:', addStop);--%>
<%--        console.log('Destination:', destination);--%>


<%--        // Send form data to the servlet using fetch API--%>
<%--        fetch('CaptureDataServlet', {--%>
<%--            method: 'POST',--%>
<%--            body: formData,--%>
<%--        })--%>
<%--            .then(response => {--%>
<%--                if (!response.ok) {--%>
<%--                    throw new Error('Network response was not ok');--%>
<%--                }--%>
<%--                return response.text();--%>
<%--            })--%>
<%--            .then(data => {--%>
<%--                // Handle the response from the servlet if needed--%>
<%--                console.log('Response from servlet:', data);--%>
<%--            })--%>
<%--            .catch(error => {--%>
<%--                console.error('Error:', error);--%>
<%--            });--%>
<%--    }--%>
<%--</script>--%>

