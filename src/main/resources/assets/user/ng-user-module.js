(function() {
	app = angular.module('user', ['ui.bootstrap', 'app.layout', 'angular-loading-bar', 'app.services', 'ngResource', 'ngSanitize'])
	/**
	 * User resources
	 */
	.factory('resources', ['$resource', function($resource) {
		var service = {};
		var actions = {save: {method: 'PUT'}, create: {method: 'POST' }, remove:{method:'DELETE'}};
		service.qualifierResource = $resource("/api/user/qualifier");
		service.resource =          $resource("/api/user/:method", null, actions);
		service.identityResource =  $resource('/api/identity/', null, actions);
		return service;
	}])
	/**
	 * User controller.
	 * 
	 * @param $scope
	 * @param $http
	 * @param resources
	 * @param qualifierService
	 */
	.controller('UserController', ['$scope', '$http', 'resources', 'qualifierService'
	                     , function($scope,   $http,   resources,   qualifierService) {
		
		$scope.baseName = "user";
		$scope.externalId = (externalId==null || externalId=='undefined')?0:externalId;
		
		$scope.userStates = "A";
		$scope.userItemsPerPage = 20;
		
		$scope.todayDate =  new Date();
		$scope.dateOptionsBirthDate = {};

		/**
		 * Qualifier call back, required by the qualifierService below.
		 * 
		 * @param value
		 * @param data
		 */
		$scope.setQualifier = function(value, data) {
			if (Array.isArray(data)) {
				$scope.qualifiers = data;
			}
			$scope.qualifierValue = value;
			$scope.userGroupValue = 0;
			$scope.listUserGroups(value);
		}
		
		/**
		 * Qualifier service runner.
		 */
		qualifierService.run(resources.qualifierResource, $scope.setQualifier, 0);
		
		/**
		 * True to show that groups typed with A or G are exclusive to system use.
		 */
		$scope.isSystemQualifier = function() {
			return $scope.qualifierValue=='A' || $scope.qualifierValue=='G';
		}
		
		/**
		 * List groups.
		 * 
		 * GET /api/user/group/?qualifierValue
		 * 
		 * @param userGroupCategory
		 */
		$scope.listUserGroups = function(userGroupCategory) {
			resources.resource.get({method:'group', qualifierValue:userGroupCategory}).$promise.then(
			function(data) {
				$scope.userGroupList = data;
				$scope.userValue = 0;
				if ($scope.userGroupValue == 0 && data.content.length>0) {
					setUserGroup(data.content[0]);
				}
			});
		};
		
		/**
		 * Get one group.
		 * 
		 * GET /api/user/group/?userGroupId
		 * 
		 * @param id
		 */
		$scope.getUserGroup = function(id) {
			resources.resource.get({method:'group', userGroupId: id}).$promise.then(
			function(data) { 
				setUserGroup(data); 
				$scope.openForm('user-group');
			});
		};
		
		/**
		 * Set one group.
		 * 
		 * @param value
		 */
		var setUserGroup = function(value) {
			$scope.userGroup = value;
			$scope.userGroupValue = value.id;
			$scope.listUsers($scope.userGroupValue);
		};
		
		/**
		 * Create new group.
		 * 
		 * POST /api/user/group/?qualifierValue
		 */
		$scope.newUserGroup = function() {
			resources.resource.create({method:'group', qualifierValue:$scope.qualifierValue}, null).$promise.then(
			function(data, getReponseHeaders) {
				setUserGroup(data); 
				$scope.openForm('user-group');
			});
		};
		
		/**
		 * Update one group.
		 * 
		 * PUT /api/user/group
		 */
		$scope.updateUserGroup = function() {
			resources.resource.save({method:'group'}, $scope.userGroup).$promise.then(
			function(data, getReponseHeaders) {
				setUserGroup(data); 
				$("#modalBody").modal('hide');
			});
		};
		
		/**
		 * List user parents.
		 * 
		 * @param userId
		 */
		$scope.listUserParents = function(userId) {
			resources.resource.query({method:'parent', userId:userId}).$promise.then(
			function(data) {
				$scope.userParentList = data;
			});
		};
		
		/**
		 * Associate.
		 * 
		 * @param userParentIndex
		 * @param checked
		 */
		$scope.associate = function(userParentIndex, checked) {
			$scope.userParentList[userParentIndex].checked = checked;
		};
		
		/**
		 * Update associations.
		 */
		$scope.updateAssociation = function() {
			resources.resource.save({method:'parent', userId:userId}, $scope.userParentList).$promise.then(
			function(data) {
				$scope.userParentList = data;
			});
		};
		
		/**
		 * List users.
		 * 
		 * @param userGroupValue
		 * @param pageNumberVal
		 * @param userState
		 */
		$scope.userState = 'A';
		$scope.listUsers = function(userGroupValue, pageNumberVal, userState) {
			$scope.userGroupValue = userGroupValue;
			if (angular.isDefined(userState)) { $scope.userState = userState; }
			resources.resource.get({userGroupId:userGroupValue
				, pageNumber: pageNumberVal, itemsPerPage: $scope.userItemsPerPage, userState:$scope.userState}).$promise.then(
			function(data) {
				$scope.userList = data;
				if ($scope.userValue == 0 && data.content.length>0) {
					$scope.setUser(data.content[0]);
				}
			});
		};
		
		/**
		 * User pagination, on user page change.
		 */
		$scope.userPageChanged = function() {
		    $scope.listUsers($scope.userGroupValue, $scope.userList.number);
		}
		
		/**
		 * User pagination, page size change.
		 */
		$scope.setUserItemsPerPage = function(value) {
			$scope.userItemsPerPage = value;
		    $scope.listUsers($scope.userGroupValue, 1);
		}
		
		/**
		 * Get one user.
		 * 
		 * @param id
		 */
		$scope.getUser = function(id){
			resources.resource.get({userId: id}).$promise.then(
			function(data) {
				setUser(data);
			});
		};
		
		/**
		 * Set one user.
		 * 
		 * @param value
		 */
		var setUser = function(value){
			$scope.user = value;
			$scope.userValue = value.id;
			$scope.listUserGroupByUser($scope.userValue);
			$scope.listRequirements($scope.userValue);
			$scope.listAssessments($scope.userValue);
			$scope.listUserParents($scope.userValue);
			$scope.getKnowledges($scope.userValue);
		};
		
		/**
		 * New user.
		 */
		$scope.newUser = function(){
			$scope.openForm('user-new');	
		};
		
		/**
		 * Update user.
		 */
		$scope.updateUser = function() {
			console.log($scope.identity);
		};
		
		/**
		 * Usera search, before creating a new one.
		 */
		$scope.userSearch= function(){
			resources.resource.create($scope.search).$promise.then(
			function(data) {
				$scope.searchDetails = data;
				if(!data.cannotCreate){
					//não existe identity
					if(data.createIdentity){
						var userName = data.userName;
						resources.identityResource.create().$promise.then(
						function(data) {
							$scope.identity = data;
							$scope.identity.principal = userName;
							$scope.identity.notification = 'A';
							$scope.openForm('identity');	
						});
					//existe identity mas não usuário amarrá-los	
					}else if(data.createUser){
						$scope.identity = resources.identityResource.get({identityId: data.identityId});
						$scope.openForm('identity');	
					}
				}
				console.log(data);
			});
		};

		/**
		 * List user associated groups.
		 * 
		 * @param userValue
		 */
		$scope.listUserGroupByUser = function(userValue) {
			resources.resource.query({method:'groups', userId:userValue}).$promise.then(
			function(data) {
				$scope.userGroupByUserList = data;
				$scope.getMinimalEducationRequirement(data);
			});
		};
		
		/**
		 * True if user is active.
		 */
		$scope.isUserActive = function() {
			return $scope.user.userState == 'A';
		}
		
		/**
		 * Activate user.
		 * 
		 * @param value
		 */
		$scope.activateUser = function(value) {
			$scope.user.userState = value;
			resources.resource.save({method:'activate'}, $scope.user).$promise.then(
			function(data, getReponseHeaders) {
				$scope.setUser(data); 
			});
		}
		
		/**
		 * Associate
		 */
		$scope.associate = function() {
			$scope.openForm('associate');
		}

		/**
		 * Identity data
		 */
		$scope.userGenderIcon = [];
		$scope.userGenderIcon.F ="fa fa-female";
		$scope.userGenderIcon.M ="fa fa-male";
		$scope.userGenderIcon.N ="fa fa-transgender";
		$scope.userGenderIcon.FEMALE ="fa fa-female";
		$scope.userGenderIcon.MALE ="fa fa-male";
		$scope.userGenderIcon.NOT_SUPPLIED ="fa fa-transgender";
		
		$scope.identityTypes = ["NOT_ADDRESSABLE", "ORGANIZATIONAL_EMAIL", "PERSONAL_EMAIL" ];
		$scope.genders = ["NOT_SUPPLIED", "MALE", "FEMALE" ];
		
		$scope.myIdentity= false;	
		
		/**
		 * Update identity.
		 */
		$scope.updateIdentity = function(){
			if($scope.myIdentity){
				$scope.identity = resources.identityResource.save({mine:true}, $scope.identity);
			}
			else{
				$scope.identity = resources.identityResource.save($scope.identity);
			}	
			$scope.identity.$promise.then(function(data) {
				if(data.id>0){
					$("#modalBody").modal('hide');	
					$scope.myIdentity= false;
				}
			});
		};

		/**
		 * Profile
		 * 
		 * GET /app/identity/?identityId
		 */
		$scope.getProfile = function(){
			resources.identityResource.get({identityId:$scope.user.identityId}).$promise.then(
			function(data) {
				$scope.identity = data;
				$scope.openForm('profile');
			});
		};
		
		/**
		 * Profile
		 * 
		 * PUT /app/identity/
		 */
		$scope.updateProfile = function(){
			$scope.identity = resources.identityResource.save({}, $scope.identity).$promise.then(
			function(data) {
				$scope.identity = data;
			});
			$("#modalBody").modal('hide');	
		};
		
		$scope.search = {"search": "", "novo":true};
		//flag para dizer que ainda não pesquisou
		$scope.searchDetails = {"identityId":-1};

		/**
		 * Get form.
		 */
	    $scope.getFormUrl = function() {
			return $scope.formUrl;
		} 

	    /**
	     * Open form.
	     * 
	     * @param formName
	     */
		$scope.openForm = function(formName){
			$scope.message =[];
			$scope.formUrl = '/assets/user/form/'+formName+'.html';
			$("#modalBody").modal('show');
		}
		
		/**
		 * Open.
		 */
		$scope.open = function($event,value) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.datePicker = [];
			$scope.datePicker[value]=true;
		};
		
	}]);
} )();

