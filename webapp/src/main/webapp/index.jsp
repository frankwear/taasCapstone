<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transportation Selection Form</title>
    <link rel="stylesheet" href="styles.css">
    <script>
        // Function to handle form submission
        function submitForm() {
            var form = document.querySelector('form');
            var formData = new FormData(form);

            // Send form data to the servlet using fetch API
            fetch('CaptureFormDataServlet', {
                method: 'POST',
                body: formData,
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
</head>
<body>
    <h1>Transportation Selection Form</h1>
    <p>Please fill out the form below:</p>
    <div class="container">
        <form action="submit.jsp" method="post">
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
                    <label><input type="radio" name="option" value="cost"> Cost</label>
                    <label><input type="radio" name="option" value="speed" checked> Speed </label>
                    <label><input type="radio" name="option" value="best-route"> Best route</label>
                    <label><input type="radio" name="option" value="low-carbon"> Low Carbon</label>
                    <label><input type="radio" name="option" value="fewer-transfers"> Fewer transfers</label>
                </div>
            </div>

            <button type="submit" id="submit" onclick="submitForm()">Go</button>
        </form>

        <div class="map-placeholder">
            <img src="map_placeholder_image.jpeg" alt="Map placeholder" width="100%" height="100%">
        </div>
    </div>
</body>
</html>
