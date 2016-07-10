var app = angular.module('volunteers', []);
app.controller("volunteerController", volunteerController);
//volunteerController.$inject = [ '$scope', '$http', '$window'];
function volunteerController($scope, $http, $window) {
	console.log('Angular Started');
	
	$http({
		method : 'POST',
		url : '/list-refugees',
		data : {
		}
	}).success(function(response) {
		if (response.status === "success") {
			$scope.refugees = response.refugees;
			console.log(response.refugees);
		} else {
			console.log("error");
		}			
	}).error(function(error) {
		console.log(error);
	});
}