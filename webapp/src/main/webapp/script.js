// Declare routes as a global variable
var routes;
// Global variable to store the selected option value
var selectedOption;
var map;
// Global variable to store markers
var originMarker, destinationMarker;

// Extracted function to handle geocoding and map initialization
function initializeMapWithOrigin(originAddress, destinationAddress) {
    const geocoder = new google.maps.Geocoder();

    // Defining the icon with the correct path
    const icon = {
        url: 'iconMap.png', // path to the  location of the marker icon
        scaledSize: new google.maps.Size(50, 50),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(0, 0)
    };

    geocoder.geocode({ 'address': originAddress }, function (originResults, originStatus) {
        if (originStatus === 'OK') {
            const originLocation = originResults[0].geometry.location;

            // Geocode destination address
            geocoder.geocode({ 'address': destinationAddress }, function (destinationResults, destinationStatus) {
                if (destinationStatus === 'OK') {
                    const destinationLocation = destinationResults[0].geometry.location;

                    map = new google.maps.Map(document.getElementById("map"), {
                        center: originLocation, // You might want to center the map between origin and destination
                        zoom: 8
                    });

                    // Marker for Origin
                    originMarker = new google.maps.Marker({
                        position: originLocation,
                        map: map,
                        label: "A",
                        title: "Origin",
                        draggable: false,
                        animation: google.maps.Animation.DROP,
                        icon: icon
                    });

                    // Marker for Destination
                    destinationMarker = new google.maps.Marker({
                        position: destinationLocation,
                        map: map,
                        label: "B",
                        title: "Destination",
                        draggable: false,
                        animation: google.maps.Animation.DROP,
                        icon: icon
                    });
                } else {
                    alert('Geocode was not successful for the following reason: ' + destinationStatus);
                }
            });
        } else {
            alert('Geocode was not successful for the following reason: ' + originStatus);
        }
    });
}

// Function to handle radio button selection
function setSelectedOption(radio) {
    selectedOption = radio.value;
    // console.log("Selected Option: " + selectedOption);
}

// Function to handle Go button submission
function handleGoButtonClick() {
    const originInput = document.getElementById("origin");
    const destinationInput = document.getElementById("destination");
    const originAddress = originInput.value;
    const destinationAddress = destinationInput.value;

    // Call the function to initialize map with origin
    initializeMapWithOrigin(originAddress, destinationAddress);

    // selectOption();
    toggleDropdown();
    submitForm();

    // Geocoding for origin and destination
    geocodeAddress(originAddress, function (error, originLocation) {
        if (error) {
            // Handle geocoding error for origin
            alert("Error geocoding origin: " + error.message);
        }

        geocodeAddress(destinationAddress, function (error, destinationLocation) {
            if (error) {
                // Handle geocoding error for destination
                alert("Error geocoding destination: " + error.message);
            }
            console.log("Origin Location:", originLocation);
            console.log("Destination Location:", destinationLocation);

            // Update destination marker position
            destinationMarker.setPosition(destinationLocation);
        });
    });
}

// InitMap function remains the same...
async function initMap() {
    // Define the icon with the correct path
    const icon = {
        url: 'path/to/map.jpg', // Update this path to the actual location of your marker icon
        scaledSize: new google.maps.Size(50, 50),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(0, 0)
    };

    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 34.7488, lng: -84.3877 },
        zoom: 8
    });

    new google.maps.Marker({
        position: { lat: 33.74, lng: -84.3877 },
        map: map,
        label: "A",
        title: "",
        draggable: true,
        animation: google.maps.Animation.DROP,
        icon: icon
    });
}

// Function to draw route on the map
function drawRoute(originLocation, destinationLocation) {
    // Example: Draw a route line between origin and destination
    const directionsService = new google.maps.DirectionsService();
    const directionsRenderer = new google.maps.DirectionsRenderer({ map: map });

    const request = {
        origin: originLocation,
        destination: destinationLocation,
        travelMode: google.maps.TravelMode.DRIVING,
    };

    directionsService.route(request, function (response, status) {
        if (status === 'OK') {
            directionsRenderer.setDirections(response);
        } else {
            alert('Error displaying route: ' + status);
        }
    });
}
// Listening here for a change in the dropdown selection
document.getElementById('optionsDropdown').addEventListener('change', function () {
    const selectedIndex = this.value;
    //console.log('SelectedIndex is', selectedIndex);
    const selectedRoute = routes[selectedIndex]; // Assuming 'routes' is a global variable
    //console.log('Selected Route:', selectedRoute);
    // Further actions based on the selected route
    displayRouteDetails(selectedRoute);
});
function submitForm() {
    // Retrieving values from the form fields
    var origin = document.getElementById("origin").value;
    var addStop = document.getElementById("addStop").value;
    var destination = document.getElementById("destination").value;
    var walking = document.getElementById("walking").value;
    var bicycling = document.getElementById("bicycling").value;
    var drive = document.getElementById("drive").value;
    var transit = document.getElementById("transit").value;

    // Preparing the data as an object
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

    // Sending data to the servlet using fetch API
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
            // Setting routes as the received data
            routes = data;

            populateDiv(routes);// tile display for 3 routes
        })
        .catch(error => {
            console.error('Error:', error);
        });
    function populateDropdown(routes) {
        var dropdown = document.getElementById('optionsDropdown');
        // Clearing any existing options
        dropdown.innerHTML = '';

        // Adding specific options based on the JSON array
        for (let i = 0; i < 3; i++) {
            const option = document.createElement('option');

            option.value = i + '.0'; // Store the index as the value

            if (routes[i] && routes[i].legs && routes[i].legs.length > 0) {
                // Formatting the text for each option
                const leg = routes[i].legs[0];
                option.text = `Option ${i + 1} - Distance is ${leg.distance}, Duration is ${leg.duration}, Cost is ${leg.cost}, Mode is ${leg.mode}`;
            } else {
                option.text = `Option ${i + 1} - No data available`;
            }

            dropdown.appendChild(option);
        }
    }
    // Listening here for a change in the dropdown selection
    function populateDiv(routes) {
        var divContainer = document.getElementById('optionsContainer');
        // Clearing any existing content
        divContainer.innerHTML = '';
        // Adding specific options based on the JSON array
        for (let i = 0; i < routes.length; i++) {
            // Create a new tile or section for each option
            const optionTile = document.createElement('div');
            optionTile.classList.add('option-tile'); // Add a class for styling
            // Setting the content of the tile or section
            optionTile.innerHTML = `Option ${i + 1} - Distance: ${routes[i].legs[0].distance}, Duration: ${routes[i].legs[0].duration}, Cost: ${routes[i].legs[0].cost}, Mode: ${routes[i].legs[0].mode}`;
            // Adding a click event listener to show more details on click
            optionTile.addEventListener('click', function () {
                displayAdditionalDetails(routes[i]);
            });

            // Adding the tile or section to the container
            divContainer.appendChild(optionTile);
        }
    }

    function displayAdditionalDetails(route) {
        var detailsContainer = document.getElementById('detailsContainer');

        // Clearing previous details if any
        detailsContainer.innerHTML = '';

        // Checking if the selected route has 'legs' property
        if (route && route.legs && route.legs.length > 0) {
            route.legs.forEach(function (leg, legIndex) {
                // Creating a new element for each leg's details
                var legDetails = document.createElement('div');
                legDetails.textContent = `Leg ${legIndex + 1} - Distance: ${leg.distance}, Duration: ${leg.duration}, Cost: ${leg.cost}, Mode: ${leg.mode}`;
                legDetails.style.fontSize = '16px';
                legDetails.style.marginTop = '10px';

                // Adding the new leg details to the details container
                detailsContainer.appendChild(legDetails);
            });

            // displaying the details container
            detailsContainer.style.display = 'block';
        } else {
            console.error("Selected route does not have the expected structure.");
        }
    }


});
function displayRouteDetails(selectedRoute) {
    var selectedDetailsElement = document.getElementById('selectedDetails');
    // Clear previous details
    selectedDetailsElement.innerHTML = '';

    //checking if the selected route is defined and has the expected structure
    if (selectedRoute && Array.isArray(selectedRoute) && selectedRoute.length > 0) {
        selectedRoute.forEach(function (leg, legIndex) {
            // Creating a new element for each leg's details
            var legDetails = document.createElement('div');
            legDetails.textContent = `Option ${legIndex + 1} - [${JSON.stringify(leg)}]`;
            legDetails.style.fontSize = '16px';
            legDetails.style.marginTop = '10px';

            // Add the new leg details to the selected details element
            selectedDetailsElement.appendChild(legDetails);
        });
    } else {
        console.error("Selected route does not have the expected structure or is undefined.");
    }
}
// Function to show/hide the dropdown menu
function toggleDropdown() {
    // Check if the mandatory fields (origin and destination) are filled
    var origin = document.getElementById("origin").value;
    var destination = document.getElementById("destination").value;

    if (origin.trim() === "" || destination.trim() === "") {
        // If not filled, alert the user or provide any feedback
        alert("Please fill in the mandatory fields: Origin and Destination.");
    } else {
        // If filled, toggle the routeContainer visibility
        var routeContainer = document.getElementById("routeContainer");

        if (routeContainer.style.display === "block") {
            routeContainer.style.display = "none"; // Hide the routeContainer when it's already open
        } else {
            routeContainer.style.display = "block"; // Show the routeContainer when the "Go" button is clicked
        }
    }
}
//replaces populating Dropdown; image with the selected option information from the drowpdown
function selectOption() {
    var dropdown = document.getElementById("optionsDropdown");
    var selectedOptionIndex = dropdown.options[dropdown.selectedIndex].value.split('.')[0];

    // \ there's an element with id 'selectedDetails' to display the details
    var selectedDetailsElement = document.getElementById('selectedDetails');

    // Clear previous details
    selectedDetailsElement.innerHTML = '';

    // Check if the selected route has 'legs' property
    if (routes[selectedOptionIndex] && routes[selectedOptionIndex].legs && routes[selectedOptionIndex].legs.length > 0) {
        routes[selectedOptionIndex].legs.forEach(function (leg, legIndex) {
            // Creating a new element for each leg's details
            var legDetails = document.createElement('div');
            legDetails.textContent = 'Leg ' + (legIndex + 1) +
                ': Distance: ' + leg.distance + ', Duration: ' + leg.duration +
                ', Cost: ' + leg.cost + ', Mode: ' + leg.mode;
            legDetails.style.fontSize = '16px';
            legDetails.style.marginTop = '10px';

            // Add the new leg details to the selected details element
            selectedDetailsElement.appendChild(legDetails);
        });
    } else {
        console.error("Selected route does not have the expected structure.");
    }
}
