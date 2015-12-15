(function() {
	app = angular.module('user', ['ui.bootstrap', 'angular-loading-bar', 'app.services', 'ngResource', 'ngSanitize', 'ngFileUpload', 'ngImgCrop'])
	/**
	 * User resources.
	 */
	.factory('resources', ['$resource', function($resource) {
		var service = {};
		var actions = {save: { method: 'PUT'}, create: {method: 'POST' }, remove:{method:'DELETE'}};
		service.qualifierResource = $resource("/app/user2/qualifier");
		service.resource = $resource("/app/user2/:method", null, actions);
		service.identityResource = $resource('/app/identity/', null, actions);
		service.passwordUp = $resource('/recovery/submit', null, actions);
		return service;
	}])
	/**
	 * User controller.
	 */
	.controller('UserController', ['$scope', '$http', 'resources', 'qualifierService'
	                                  , function($scope, $http, resources, qualifierService) {
		
		var baseUrl = '/app/user2/';
		$scope.baseName = "user";
		$scope.externalId = (externalId==null || externalId=='undefined')?0:externalId;
		$scope.userStates = "A";
		$scope.userItemsPerPage = 20;
		$scope.knowledgeItemsPerPage = 20;
		
		/**
		 * Qualifier callback.
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
		 * Qualifier runner.
		 */
		qualifierService.run(resources.qualifierResource, $scope.setQualifier, 0);
		
		/**
		 * True if qualifier is system.
		 */
		$scope.isSystemQualifier = function() {
			return $scope.qualifierValue=='A' || $scope.qualifierValue=='G';
		}
		
		/**
		 * List groups.
		 * 
		 * GET /app/user2/group/?qualifierValue
		 */
		$scope.listUserGroups = function(userGroupCategory) {
			resources.resource.get({method:'group', qualifierValue:userGroupCategory}).$promise.then(
			function(data) {
				$scope.userGroupList = data;
				$scope.userValue = 0;
				if ($scope.userGroupValue == 0 && data.content.length>0) {
					$scope.setUserGroup(data.content[0]);
				}
			});
		}
		
		/**
		 * Some group.
		 * 
		 * GET /app/user2/group/?userGroupId
		 */
		$scope.getUserGroup = function(id) {
			resources.resource.get({method:'group', userGroupId: id}).$promise.then(
			function(data) { 
				$scope.setUserGroup(data); 
				$scope.openForm('user-group');
			});
		}
		
		/**
		 * User group setter.
		 */
		$scope.setUserGroup = function(value) {
			$scope.userGroup = value;
			$scope.userGroupValue = value.id;
			$scope.listUsers($scope.userGroupValue);
		}
		
		/**
		 * New user group.
		 * 
		 * POST /app/user2/group/?qualifierValue
		 */
		$scope.newUserGroup = function() {
			resources.resource.create({method:'group', qualifierValue:$scope.qualifierValue}, null).$promise.then(
			function(data, getReponseHeaders) {
				$scope.setUserGroup(data); 
				$scope.openForm('user-group');
			});
		}
		
		/**
		 * Update user group.
		 * 
		 * PUT /app/user2/group
		 */
		$scope.updateUserGroup = function() {
			resources.resource.save({method:'group'}, $scope.userGroup).$promise.then(
			function(data, getReponseHeaders) {
				$scope.setUserGroup(data); 
				$("#modalBody").modal('hide');
			});
		}
		
		/**
		 * True if user group is a function or a job.
		 */
		$scope.isUserGroupFunction = function() {
			if ($scope.userGroup.userType=='F' || $scope.userGroup.userType=='J') {
				return true;
			}
			return false;
		}
		
		/**
		 * User group pinned state.
		 */
		var userGroupPinned = false;
		
		/**
		 * Toggle user group pinning.
		 */
		$scope.toggleUserGroupPinned = function() {
			if (userGroupPinned) {
				userGroupPinned = false;
			}
			else {
				userGroupPinned = true;
			}
			return userGroupPinned;
		}
		
		/**
		 * True if user group is pinned.
		 */
		$scope.isUserGroupPinned = function() {
			return userGroupPinned;
		}
		
		/**
		 * List user parents.
		 * 
		 * GET /app/user2/parent/?userId
		 */
		$scope.listUserParents = function(userId) {
			resources.resource.query({method:'parent', userId:userId}).$promise.then(
			function(data) {
				$scope.userParentList = data;
			});
		}
		
		/**
		 * Update association.
		 * 
		 * PUT /app/user2/parent
		 */
		$scope.updateAssociation = function() {
			var checkedGroup = [0];
			var uncheckedGroup = [0];
			$scope.userParentList.forEach(function(userParent) {
				if (userParent.checked) {
					checkedGroup.push(userParent.id);
				}
				else {
					uncheckedGroup.push(userParent.id);
				}
			})
			resources.resource.save({method:'parent', userId:$scope.userValue, checked:checkedGroup, unchecked:uncheckedGroup}, null).$promise.then(
			function(data) {
				$scope.userParentList = data;
				$("#modalBody").modal('hide');
			});
		}
		
		/**
		 * User intital state.
		 */
		$scope.userState = 'A';

		/**
		 * List users.
		 * 
		 * GET /app/user2/?userGroupId&pageNumber&itemsPerPage&userState
		 * 
		 * @param userGroupValue
		 * @param pageNumberVal
		 * @param userState
		 */
		$scope.listUsers = function(userGroupValue, pageNumberVal, userState) {
			$scope.userGroupValue = userGroupValue;
			if (angular.isDefined(userState)) { $scope.userState = userState; }
			resources.resource.get({userGroupId:userGroupValue
				, pageNumber: pageNumberVal, itemsPerPage: $scope.userItemsPerPage, userState:$scope.userState}).$promise.then(
			function(data) {
				$scope.userList = data;
				userGroupPinned = true;
				if ($scope.userValue == 0 && data.content.length>0) {
					$scope.setUser(data.content[0]);
				}
			});
		}
		
		/**
		 * User page change event.
		 */
		$scope.userPageChanged = function() {
		    $scope.listUsers($scope.userGroupValue, $scope.userList.number);
		}
		
		/**
		 * User page size setter.
		 */
		$scope.setUserItemsPerPage = function(value) {
			$scope.userItemsPerPage = value;
		    $scope.listUsers($scope.userGroupValue, 1);
		}
		
		/**
		 * Some user.
		 * 
		 * GET /app/user2/?userId
		 */
		$scope.getUser = function(id){
			resources.resource.get({userId: id}).$promise.then(
			function(data) {
				$scope.setUser(data);
			});
		}
		
		/**
		 * User setter.
		 */
		$scope.setUser = function(value){
			$scope.user = value;
			$scope.userValue = value.id;
			$scope.listUserGroupByUser($scope.userValue);
			$scope.listUserParents($scope.userValue);
		}
		
		/**
		 * New user.
		 */
		$scope.newUser = function(){
			$scope.openForm('user-new');	
		}

		/**
		 * Update user.
		 */
		$scope.updateUser = function(){
			if($scope.myIdentity){
				$scope.identity = resources.identityResource.save({mine:true}, $scope.identity);
			}else{
				$scope.identity = resources.identityResource.save($scope.identity);
			}	
			$scope.identity.$promise.then(function(data) {
				if(data.id>0){
					$("#modalBody").modal('hide');	
					$scope.myIdentity= false;
				}
			});
		}

		/**
		 * Search before creating new user.
		 * 
		 * POST /app/user2/
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
					}else if(data.createUser){
						$scope.identity = resources.identityResource.get({identityId: data.identityId});
						$scope.openForm('identity');	
					}
				}
			});
		};
		
		/**
		 * List parent groups.
		 * 
		 * GET /app/user2/groups/?userId
		 */
		$scope.listUserGroupByUser = function(userValue) {
			resources.resource.query({method:'groups', userId:userValue}).$promise.then(
			function(data) {
				$scope.userGroupByUserList = data;
			});
		}
		
		/**
		 * User activity.
		 */
		$scope.isUserActive = function() {
			return $scope.user.userState == 'A';
		}
		
		/**
		 * User activate or deactivate.
		 * 
		 * PUT /app/user2/activate
		 */
		$scope.activateUser = function(value) {
			$scope.user.userState = value;
			resources.resource.save({method:'activate'}, $scope.user).$promise.then(
					function(data, getReponseHeaders) {
						$scope.setUser(data); 
					});
		}
		
		/**
		 * Identity state
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
		
		/**
		 * User self identity state.
		 */
		$scope.myIdentity= false;
		
		/**
		 * Update my identity.
		 */
		$scope.updateMyIdentity = function(){
			$scope.myIdentity = true;
			$scope.identity = resources.identityResource.get({ mine: true});
			$scope.openForm('identity');	
		}
		
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
		}
		
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
		}
		
		$scope.search = {"search": "", "novo":true};
		
		$scope.searchDetails = {"identityId":-1};

	    $scope.getFormUrl = function(){
			return $scope.formUrl;
		} 

		$scope.openForm = function(formName){
			$scope.message =[];
			$scope.formUrl = '/assets/user/form/'+formName+'.html';
			$("#modalBody").modal('show');
		}
		
		$scope.todayDate =  new Date();
		
		$scope.dateOptionsBirthDate = {
		}

		$scope.open = function($event,value) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.datePicker = [];
			$scope.datePicker[value]=true;
		}
		
	}])
	;
} )();

