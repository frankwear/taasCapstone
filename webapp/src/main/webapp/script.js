// Declare routes as a global variable
var routes;


function handleGoButtonClick() {
    console.log("just before toggleDropdown method");
    // Call toggleDropdown
    toggleDropdown();
    console.log("just before submitForm method");
    // Call submitForm
    submitForm();
}
document.getElementById('submit').addEventListener('click', handleGoButtonClick);

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

// Fetch data from the server
document.addEventListener('DOMContentLoaded', function() {
    fetch('RouteOptionServlet')
        .then(response => response.json())
        .then(data => {
            // Set routes as the received data
            routes = data;
            // Handle the response
            console.log(data);//testing display
            populateDropdown(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });

    // Function to populate the dropdown with routes
    function populateDropdown(data) {
        var dropdown = document.getElementById('dropdownMenu');

        // Clear any existing options
        dropdown.innerHTML = '';

        // Add each route as an option
        data.forEach(function (route, index) {
            var option = document.createElement('option');
            option.value = index; // Store the entire route JSON as the value
            option.text = 'Option ' + (index+1); // have to adjust this based on  route object structure

            dropdown.appendChild(option);
        });
    }

    // Listening here for a change in the dropdown selection
    document.getElementById('dropdownMenu').addEventListener('change', function () {
        const selectedIndex = this.value;

        const selectedRoute = routes[selectedIndex]; // Assuming 'routes' is a global variable
        console.log('Selected Route:', selectedRoute);
        // Further actions based on the selected route
        displayRouteDetails(selectedRoute);
    });

    // Function to display the details of the selected route
    function displayRouteDetails(route) {
        // Assuming there's an element with id 'routeDetails' to display the details
        var routeDetailsElement = document.getElementById('routeDetails');
        routeDetailsElement.innerHTML = JSON.stringify(route, null, 2); // Beautify the JSON string
    }
});




//Below is my code, need to eliminate duplicate methods



// Global variable to store the selected option value
var selectedOption;

// Function to handle radio button selection
function setSelectedOption(radio) {
    selectedOption = radio.value;
    console.log("Selected Option: " + selectedOption);
}

// Function to toggle the visibility of the dropdown menu
function toggleDropdown() {
    // Check if the mandatory fields (origin and destination) are filled
    var origin = document.getElementById("origin").value;
    var destination = document.getElementById("destination").value;

    if (origin.trim() === "" || destination.trim() === "") {
        // If not filled, alert the user or provide any feedback
        alert("Please fill in the mandatory fields: Origin and Destination.");
    } else {
        // If filled, proceed to toggle the dropdown visibility
        var dropdownMenu = document.getElementById("dropdownMenu");

        if (dropdownMenu.style.display === "block") {
            dropdownMenu.style.display = "none"; // Hide the dropdown when it's already open
        } else {
            console.log("toggleDropdown function is running");
            dropdownMenu.style.display = "block";// Show the dropdown when the "Go" button is clicked
            console.log("toggleDropdown function is running 2");
        }
    }
}

//Below is to capture dropdown value and display it in place of map image.
var time = [10, 20, 25];
var distance = [8, 12, 14];
var transits = [1, 2, 3];

function selectOption() {
    var dropdown = document.getElementById("dropdownMenu");
    var selectedOptionIndex = dropdown.options[dropdown.selectedIndex].value;

    console.log("Selected Option: " + selectedOptionIndex);

    // Remove the previous option text and image if they exist
    var previousOptionText = document.querySelector('.map-placeholder .option-text');
    if (previousOptionText) {
        previousOptionText.remove();
    }

    var mapImage = document.querySelector('.map-placeholder img');
    if (mapImage) {
        mapImage.style.display = "none";
    }

    // Create a new element for the selected option text
    var optionText = document.createElement('div');
    optionText.textContent = "Time: " + time[selectedOptionIndex] + " mins, Distance: " + distance[selectedOptionIndex] + " miles, Transits: " + transits[selectedOptionIndex];
    optionText.className = "option-text";
    optionText.style.fontSize = "16px";
    optionText.style.textAlign = "center";
    optionText.style.marginTop = "20px";

    // Add the new option text to the map placeholder
    var mapPlaceholder = document.querySelector('.map-placeholder');
    mapPlaceholder.appendChild(optionText);

    // Hide the dropdown after selection
    var dropdownMenu = document.getElementById("dropdownMenu");
    dropdownMenu.style.display = "none";
}