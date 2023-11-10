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

            <button type="button" id="submit" onclick="submitForm()">Go</button>

        </form>

        <div class="map-placeholder">
            <img src="map_placeholder_image.jpeg" alt="Map placeholder" width="100%" height="100%">
        </div>
    </div>
</body>
<script>
        //radiobutton value:
        var selectedOption; // Global variable to store the selected option value
        function setSelectedOption(radio) {
        selectedOption =  radio.value;
        console.log("radio value selected is "+ selectedOption); //this is getting printed in console of chrome;
    }
    // Function to handle form submission
    function submitForm() {
        // Retrieve values from the form fields
        var origin = document.getElementById("origin").value;
        var addStop = document.getElementById("addStop").value;
        var destination = document.getElementById("destination").value;
        var walking = document.getElementById("walking").value;
        var bicycling = document.getElementById("bicycling").value;
        var drive = document.getElementById("drive").value;
        var transit = document.getElementById("transit").value;

        // Prepare the data as an object
        var formData = {
            origin: origin,
            addStop: addStop,
            destination: destination,
            walking: walking,
            bicycling: bicycling,
            drive: drive,
            transit: transit,
            selectedOption: selectedOption
        };

        // Send data to the servlet using fetch API
        fetch('CaptureDataServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
            .then(response => response.text())
            .then(data => {
                // Handle the response from the servlet if needed
                console.log('Response from servlet:', data);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
</script>
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
</html>
