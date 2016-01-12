(function() {
	app = angular.module('security', ['ui.bootstrap', 'app.services'])
	/**
	 * Resources
	 */
	.factory('resources', ['$resource', function($resource) {
		var service = {};
		service.loginResource = $resource("/login/"
			, { username:"@username", password: "@password", rememberme:"@rememberme"}
			, { login : { method:'POST',  headers : {'Content-Type': 'application/x-www-form-urlencoded'}}});
		service.locationResource = $resource("/api/location/:path"
		    , { stateId : "@stateId"}
		    , { save: { method: 'PUT' }, create: { method: 'POST' }});
		return service;
	}])
	.controller('SecurityController', ['$scope', '$window', '$http', '$resource', 'resources' , 'genericServices'
	                                  , function($scope, $window, $http, $resource, resources, genericServices) {
	
		$scope.baseName = "home";

		$scope.email = email!='undefined'?email:'' ;
		
		$scope.cannotChangePassword = true;
		$scope.$watch('[password,cpassword]', function () { 
			$scope.cannotChangePassword = !genericServices.verifyPassword($scope.password, $scope.cpassword);
		}, true);
		
		$scope.signUpResource = $resource("/signup/" , {principal:"@principal", tempEmail:"@tempEmail"});
		$scope.passwordResource = $resource("/signup/createPass");
		$scope.passwordRecoveryResource = $resource("/recovery/", { email:"@email"}, {
			update: { method: 'PUT' },
			create: { method: 'POST' }
		});
		
		
	    /**
	     * Login submission
	     */
		$scope.login = function(usernameVal,passwordVal){
			resources.loginResource.login($.param({username :usernameVal, password :passwordVal })).$promise.then(
			function(data, getReponseHeaders) {
				$scope.logged = data;
				$window.location.href = "/";
			});
		}
		
		$scope.saveEmail = function(emailVal){
			$scope.signUpResource.save({tempEmail:emailVal});
		}
		
		//form initializer
		$scope.create = function(){
			$scope.form =  $scope.signUpResource.get({create:true});
			$scope.form.$promise.then(function(data) {
				data.email = $scope.email;
				$scope.form = data;
			});
		} 
		$scope.create();
		$scope.signUp = function(){
			$scope.form.password = 'save';
			$scope.returnCode = $scope.signUpResource.save($scope.form);
			$scope.returnCode.$promise.then(function(data) {
				console.log(data);
			});
		};
	
		$scope.updateUser = function(){
			$scope.form.email = $scope.email;
			$scope.passwordResource.save($scope.form);
		};
		
		$scope.passwordEmail = function(val){
			$scope.passwordRecoveryResource.save({email:val});
		}
		
		$scope.updatePassword = function(){
			$scope.form.email = $scope.email;
			$scope.passwordRecoveryResource.update($scope.form);
		}
		
		$scope.passwordMatches = function(){
			return $scope.cpassword === $scope.form.password;
		}

		/**
		 * E-mail validator
		 * 
		 */
		$scope.emailOk = false;	
		$scope.userNotExists = true;
		$scope.canRecover = false;
		$scope.showAlerts = false;
		$scope.emailTester=function(){
			console.log($scope.principal);
			$scope.signUpResource.get({principal:$scope.principal}).$promise.then(function(data) {
				$scope.emailOk = !data.exists;
				$scope.userNotExists = !data.exists;
				if(data.exists){
					$scope.principal = '';
				}
			});
			$scope.showAlerts = true;
			
		}
	    
		/**
		 * States.
		 */
		$scope.getStates = function(){
			resources.locationResource.query({path: 'state'}).$promise.then(
			function(data){
				$scope.states = data;
				if(data.length>0){
					$scope.stateId = data[0].id;
					$scope.getCities($scope.stateId);
				}
			})
		};
		
		/**
		 * Cities.
		 */
		$scope.getCities = function(val){
			resources.locationResource.query({path: 'city', stateId:val}).$promise.then(
			function(data){
				$scope.cities = data;
			})
		};
		
		$scope.getStates();
		
	}]); // SecurityController
	
} )();
