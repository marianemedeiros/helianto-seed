(function() {
	app = angular.module('root', ['ui.bootstrap', 'app.services', 'ngResource', 'ngSanitize', 'angular-loading-bar']);
	
	app.controller('RootController', ['$scope', '$window', '$http', '$resource', 'qualifierService', 'lang'
	                                  , function($scope, $window, $http, $resource, qualifierService, lang) {

		$scope.baseName = "root";

		var baseUrl = '/api/root/';

		/**
		 * Resources
		 */
		$scope.rootResource = $resource(baseUrl + "entity", {entityType: "@entityType", rootUserId: "@rootUserId"}, {
			save: { method: 'PUT' }
			, create: { method: 'POST' }
			, search : { method: 'POST', url: baseUrl+'search'}
			, authorize : { method: 'GET'}
		});
		
		
		/**
		 * Entity Resource.
		 */
		$scope.entityResource = $resource("/api/entity/:path", {}, {
			save: { method: 'PUT' }
			, create: { method: 'POST' }
		});
		
		/**
		 * Location Resource.
		 */
		$scope.locationResource = $resource("/api/location/:path", { stateId : "@stateId"}, {
			save: { method: 'PUT' }
			, create: { method: 'POST' }
		});
		
		
		
		$scope.root;

		/**
		 * Qualifier
		 */
		$scope.setQualifier = function(value, data) {
			if (Array.isArray(data)) {
				$scope.qualifiers = data;
			}
			$scope.qualifierValue = value;
			$scope.rootList = [];
			$scope.listRoots(value);
		}
		qualifierService.run($resource(baseUrl + "qualifier"), $scope.setQualifier, 0);

		/**
		 * Root entities
		 */
		$scope.rootValue = 0;
		// list
		$scope.listRoots = function(qualifierValue) {
			$scope.rootList = $scope.rootResource.get({entityType: qualifierValue});
			$scope.rootList.$promise.then(function(data) {
				if ($scope.rootValue === 0 && data.length>0) {
					$scope.rootValue = $scope.rootList[0].id;
				}
			})
		};
		
		/**
		 * States.
		 */
		$scope.getStates = function(){
			$scope.locationResource.query({path: 'state'}).$promise.then(function(data){
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
			$scope.locationResource.query({path: 'city', stateId:val}).$promise.then(function(data){
				$scope.cities = data;
			})
		};

		/**
		 * Search 
		 */
		$scope.search = function(page, searchUrl) {
			$scope.searchString = $("#searchString").val();
			search(page, $scope.searchString, searchUrl) ;
		}

		function search(page, searchString, searchUrl) {
			var dataObj = {	
					"searchString" : searchString ,
					"qualifierValue" : $scope.categoryId,
			};
			var res =  $http.post(searchUrl+'/'+$scope.sectionTab+'/'+page
					, dataObj
					, {});
			res.success(function(data, status, headers, config) {
				$scope.resultFromSearch = data;
				$scope.searchBool = true;
				$scope.page = page;
				$scope.nextAndPrevious = genericServices.getNextAndPreviousLinkByList(data);
				if(data.totalElements == 1){
					$scope.root = data.content[0];
				}
				else if(data.totalElements>1) {
					$scope.rootList = data;	
				}
				else {					
					$scope.rootList = [];
				}
			})    
		}
		
		/**
		 * Some root
		 */
		$scope.getRoot = function(id) {
			console.log("USER ID = "+id);
			if (id==0) {
				$scope.root = $scope.rootResource.create({categoryId:$scope.qualifierValue});
			}
			else {
				$scope.root = $scope.rootResource.get({userId: id});
			}
			$scope.root.$promise.then(
					function(data, getReponseHeaders) {
						if (data.length>0) {
							$scope.rootValue = data.userId;
						}
					}
			);
		}
		
		// authorize
		$scope.authorize = function() {
			$scope.newUser = $scope.rootResource.authorize({rootUserId:$scope.root.userId});
			$scope.newUser.$promise.then(
					function(data, getReponseHeaders) {
						if (data.success==1) {
							$window.location = "/";
						}
					}
			);
		}
		
		/**
		 * Entity
		 */
		$scope.createEntity = function(){
			$scope.entityResource.create().$promise.then(
					function(data){
						$scope.root = data;
						$scope.getStates();
						$scope.openForm('root-entity')
					});
		}

		$scope.saveEntity = function(){
			$scope.entityResource.save($scope.root).$promise.then(
					function(data){
						$scope.root = data;
						$scope.listRoots($scope.qualifierValue, 1);
						$("#modalBody").modal('hide');
					});
		}


		$scope.getFormUrl = function(){
			return $scope.formUrl;
		} 

		$scope.open = function($event,value) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.datePicker = [];
			$scope.datePicker[value]=true;
		};
		

		$scope.formats = ['ativo', 'inativo'];
		$scope.format = $scope.formats[0];

		$scope.openForm = function(formName){
			openForm(formName);
		}

		function openForm(formName){
			$scope.message = [];
			$scope.formUrl = '/assets/'+$scope.baseName+'/form/'+formName+'.html';
			$("#modalBody").modal('show');

		}
		
	}])

	.directive('rootWrapper', function() {
		return {
			restrict: 'EA',
			scope: { root: '='},
			transclude: true,
			templateUrl: '/assets/root/ng-root-template-entity.html'
		}

	})

})();
