angular.module('app.services')

/**
 * Language filter.
 */
.filter('i18n', ['lang', function (lang) {
	return function (key, p) {
		if (typeof lang[key] != 'undefined' && lang[key] != '') {
			return (typeof p === "undefined") ? lang[key] : lang[key].replace('@{}@', p);
		}
		return key;
	}
}])
.directive("slimScroll",[function(){
	return{
		restrict:"A"
		,link:function(scope,ele,attrs) {
			return ele.slimScroll({height:attrs.scrollHeight||"100%"})
		}
	}
}])

/**
 * View controller
 */
.controller('ViewController', ['$rootScope', '$http', '$resource', 'lang'
                               , function($rootScope, $http, $resource, lang) {
		
	$rootScope.logout = function() {
		return $http.post('/logout');
    }
	/**
	 * Tabs
	 */
	$rootScope.sectionTab = 1;
	$rootScope.setSectionTab = function(value) {
		$rootScope.sectionTab = value;
    };
    $rootScope.isSectionTabSet = function(value) {
        return $rootScope.sectionTab === value;
    };

    $rootScope.userAuthResource = $resource("/api/entity/auth", {userId: "@userId"}, {});
    $rootScope.roleList = [];
	/**
	 * Authorization
	 */
    $rootScope.getAuthorizedRoles = function(userIdVal) {
    	$rootScope.userAuthResource.get({userId:userIdVal}).$promise.then(function(data) {
			console.log(data)
			$rootScope.roleList = data;
			$rootScope.isAdmin=$rootScope.isAuthorized('ADMIN', 'MANAGER');
			console.log($rootScope.isAuthorized('ADMIN', 'MANAGER'));
		});
	}
    
    $rootScope.getAuthorizedRoles();
    
    console.log($rootScope.roleList);
    
    
    
	$rootScope.isAuthorized = function(role, ext){
		var result = false;
		$rootScope.roleList.forEach(function(entry) {
			if(entry.serviceName == (role) && entry.serviceExtension.indexOf(extension)>-1){
				result = true;
			}
		});
		return result;
	};
	
		 
}]);
