// Declare routes as a global variable
var routes;
// Global variable to store the selected option value
var selectedOption;
// Function to handle radio button selection
function setSelectedOption(radio) {
    selectedOption = radio.value;
    console.log("Selected Option: " + selectedOption);
}

//Function to handle Go button submission
function handleGoButtonClick(){
    // selectOption();
    toggleDropdown();//toggle is displayed
    submitForm();

    //populateDropdown(routes); // Populate the dropdown with data from the JSON response
}

// Listening here for a change in the dropdown selection
document.getElementById('optionsDropdown').addEventListener('change', function () {
    const selectedIndex = this.value.split('-');
    const routeIndex = parseInt(selectedIndex[0]);
    const legIndex = parseInt(selectedIndex[1]);

    const selectedRoute = routes[routeIndex].legs[legIndex];
    console.log('Selected Route:', selectedRoute);
    // calling method to display selected route information in selectedDetails div
    displayRouteDetails(selectedRoute);
});
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

    // After performing initial actions, call populateDropdown with the parameter 'hi'
    // populateDropdown('hi');

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
            populateDropdown(routes);

        })
        .catch(error => {
            console.error('Error:', error);
        });
    function populateDropdown(routes) {
        var dropdown = document.getElementById('optionsDropdown');

        // Clear any existing options
        dropdown.innerHTML = '';

        // Add specific options based on the JSON array
        for (let i = 0; i < 3; i++) {
            const option = document.createElement('option');
            option.value = i+ '.0'; // Store the index as the value
            option.text = 'Option ' + i;

            dropdown.appendChild(option);
        }
    }
    // Listening here for a change in the dropdown selection
    document.getElementById('optionsDropdown').addEventListener('change', function () {
        const selectedIndex = this.value;

        const selectedRoute = routes[selectedIndex]; // Assuming 'routes' is a global variable
        console.log('Selected Route:', selectedRoute);
        // Further actions based on the selected route
        displayRouteDetails(selectedRoute);
    });

    // Function to display the details of the selected route
    // function displayRouteDetails(selectedRoute) {
    //     // Assuming there's an element with id 'routeDetails' to display the details
    //     var selectedDetailsElement = document.getElementById('selectedDetails');
    //     //routeDetailsElement.innerHTML = JSON.stringify(routes, null, 2); // Beautify the JSON string
    //     selectedDetailsElement.innerHTML='';
    //     // Check if the selected route has 'legs' property
    //     if (selectedRoute && selectedRoute.legs && selectedRoute.legs.length > 0) {
    //         selectedRoute.legs.forEach(function (leg, legIndex) {
    //             // Create a new element for each leg's details
    //             var legDetails = document.createElement('div');
    //             legDetails.textContent = 'Leg ' + (legIndex + 1) +
    //                 ': Distance: ' + leg.distance + ', Duration: ' + leg.duration +
    //                 ', Cost: ' + leg.cost + ', Mode: ' + leg.mode;
    //             legDetails.style.fontSize = '16px';
    //             legDetails.style.marginTop = '10px';
    //
    //             // Add the new leg details to the selected details element
    //             selectedDetailsElement.appendChild(legDetails);
    //         });
    //     } else {
    //         console.error("Selected route does not have the expected structure.");
    //     }
    // }
});
function displayRouteDetails(selectedRoute) {
    // Assuming there's an element with id 'routeDetails' to display the details
    var selectedDetailsElement = document.getElementById('selectedDetails');
    //routeDetailsElement.innerHTML = JSON.stringify(routes, null, 2); // Beautify the JSON string
    selectedDetailsElement.innerHTML='';
    // Check if the selected route has 'legs' property
    if (selectedRoute && selectedRoute.legs && selectedRoute.legs.length > 0) {
        selectedRoute.legs.forEach(function (leg, legIndex) {
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
// Function to show/hide the dropdown menu
function toggleDropdown() {
    // Check if the mandatory fields (origin and destination) are filled
    var origin = document.getElementById("origin").value;
    var destination = document.getElementById("destination").value;

    if (origin.trim() === "" || destination.trim() === "") {
        // If not filled, alert the user or provide any feedback
        alert("Please fill in the mandatory fields: Origin and Destination.");
    } else {
        // If filled, proceed to toggle the dropdown visibility
        var dropdown = document.getElementById("dropdownDiv");

        if (dropdown.style.display === "block") {
            dropdown.style.display = "none"; // Hide the dropdown when it's already open
        } else {
            dropdown.style.display = "block"; // Show the dropdown when the "Go" button is clicked
        }
    }
}
//replaces populating Dropdown; image with the selected option information from the drowpdown
function selectOption() {
    var dropdown = document.getElementById("optionsDropdown");
    var selectedOptionIndex = dropdown.options[dropdown.selectedIndex].value.split('.')[0];
    // Assuming there's an element with id 'selectedDetails' to display the details
    var selectedDetailsElement = document.getElementById('selectedDetails');
    // Clear previous details
    selectedDetailsElement.innerHTML = '';
    // Check if the selected route has 'legs' property
    if (routes[selectedOptionIndex] && routes[selectedOptionIndex].legs && routes[selectedOptionIndex].legs.length > 0) {
        routes[selectedOptionIndex].legs.forEach(function (leg, legIndex) {
            // Create a new element for each leg's details
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

// angular.module('routeApp', [])
//     .controller('RouteController', function ($scope, $http) {
//         // Simulate receiving routes from the backend
//         $scope.routes = [];
//
//         // Initialize selectedRoute
//         $scope.selectedRoute = null;
//
//         // Function to get route details from the backend
//         $scope.getRouteDetails = function () {
//             if ($scope.selectedRoute) {
//                 // Make an HTTP request to your back-end servlet
//                 $http.get('/YourProjectName/RouteOptionServlet?routeId=' + $scope.selectedRoute.routeId)
//                     .then(function (response) {
//                         // Update route details based on the response from the servlet
//                         // Example: $scope.selectedRoute.details = response.data.details;
//                     })
//                     .catch(function (error) {
//                         console.error('Error fetching route details:', error);
//                     });
//             }
//         };
//
//         // Simulate loading routes from the backend initially
//         $http.get('RouteOptionServlet')
//             .then(function (response) {
//                 $scope.routes = response.data;
//                 // Initialize selectedRoute
//                 $scope.selectedRoute = $scope.routes[0];
//             })
//             .catch(function (error) {
//                 console.error('Error fetching routes:', error);
//             });
//     });

// angular.module('routeApp', [])
//     .controller('RouteController', function ($scope) {
//         // Simulate receiving routes from the backend
//         $scope.routes = [
//             { routeDescription: 'Route 1' },
//             { routeDescription: 'Route 2' },
//             { routeDescription: 'Route 3' }
//             // Add more routes as needed
//         ];
//
//         // Initialize selectedRoute
//         $scope.selectedRoute = $scope.routes[0];
//     });