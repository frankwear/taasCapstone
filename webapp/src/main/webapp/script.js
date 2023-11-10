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
// Fetch data from the server
document.addEventListener('DOMContentLoaded', function() {
    fetch('RouteOptionServlet')
        .then(response => response.json())
        .then(data => {
            // Handle the response
            console.log(data);//testing display
            populateDropdown(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });

// Function to populate the dropdown with routes
    function populateDropdown(routes) {
        var dropdown = document.getElementById('routesDropdown');

        // Clear existing options
        dropdown.innerHTML = '';

        // Add each route as an option
        routes.forEach(function (route) {
            console.log(route);// test display
            var option = document.createElement('option');
            option.value = JSON.stringify(route); // Store the entire route JSON as the value
            option.text = route.description; // have to adjust this based on  route object structure

            dropdown.appendChild(option);
        });
    }


// Listening here for a change in the dropdown selection
    document.getElementById('routesDropdown').addEventListener('change', function () {
        var selectedRoute = JSON.parse(this.value); // Parse the JSON string back to an object
        console.log('Selected Route:', selectedRoute);
        // further actions based on the selected route
    });

});
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