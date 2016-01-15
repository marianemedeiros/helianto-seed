package org.helianto.user.controller;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.internal.QualifierAdapter;
import org.helianto.identity.service.IdentityQueryService;
import org.helianto.identity.service.IdentityQueryService.IdentityUserCreateResponse;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.seed.PageDecorator;
import org.helianto.user.domain.User;
import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserReadAdapter;
import org.helianto.user.service.UserCommandService;
import org.helianto.user.service.UserQueryService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * User controller.
 * 
 * @author mauriciofernandesdecastro
 */
@RequestMapping("/api/user")
@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

	@Inject
	private UserQueryService userQueryService;
	
	@Inject
	private UserCommandService userCommandService;
	
	@Inject
	private IdentityQueryService identityQueryService;
	

	/**
	 * List qualifiers.
	 * 
	 * GET		/api/user/qualifier
	 */
	@RequestMapping(value={"/qualifier"}, method=RequestMethod.GET)
	public List<QualifierAdapter> qualifier(UserAuthentication userAuthentication) {
		return userQueryService.qualifierList(userAuthentication.getEntityId());
	}
	
	/**
	 * List users.
	 * 
	 * GET		/api/user/?userType&userStates&pageNumber&itemsPerPage
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params={"userType", "userStates"})
	public Page<User> userList(UserAuthentication userAuthentication, @RequestParam Character userType
			, @RequestParam String userStates, @RequestParam(defaultValue="1") Integer pageNumber
			, @RequestParam(defaultValue="20") Integer itemsPerPage) {
		Page<User> userList = new PageDecorator<User>(
				
				userQueryService.userList(userAuthentication.getEntityId(), userType, userStates, pageNumber - 1, itemsPerPage));
		return userList;
	}
		
//	/**
//	 * List users.
//	 * 
//	 * GET		/api/user/?userType&userStates&pageNumber&itemsPerPage
//	 */
//	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params={"userId"})
//	public User user(UserAuthentication userAuthentication, @RequestParam Integer userId) {
//		return userQueryService.user(userAuthentication.getEntityId(), userId);
//	}
	

	/**
	 * New user.
	 *
	 * POST		/app/user/?userGroupId
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.POST, params={"userGroupId"})
	public UserReadAdapter userNew(UserAuthentication userAuthentication, @RequestParam Integer userGroupId) {
		return new UserReadAdapter(0, userGroupId, "", "", 'A', 'N');
	}

	/**
	 * find One.
	 *
	 * GET 	/app/user/?userId
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params={"userId"})
	public User userOne(@RequestParam Integer userId) {
		return userQueryService.userOne(userId);
	}

	/**
	 * Save and update user.
	 *
	 * PUT 	/app/user/
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public User user(UserAuthentication userAuthentication, @RequestBody UserReadAdapter command) {
		return userCommandService.user(userAuthentication, command);
	}

	/**
	 * User activation.
	 *
	 * PUT 	/app/user/
	 */
	@RequestMapping(value={"/activate"}, method=RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public User userActivate(UserAuthentication userAuthentication, @RequestBody User command) {
		return userCommandService.userActivate(userAuthentication, command);
	}

	/**
	 * Will create new user?.
	 *
	 * POST		/app/user/group?qualifierValue
	 */
	@RequestMapping(method=RequestMethod.POST, params={"search","novo"})
	public IdentityUserCreateResponse userSearchNew(UserAuthentication userAuthentication, 
			@RequestParam String search) {
		return identityQueryService.createNewUser(search, userAuthentication.getEntityId());
	}
	


	
}
