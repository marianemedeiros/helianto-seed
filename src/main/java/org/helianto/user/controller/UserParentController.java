package org.helianto.user.controller;

import java.util.List;

import javax.inject.Inject;

import org.helianto.security.internal.UserAuthentication;
import org.helianto.user.repository.UserGroupNameAdapter;
import org.helianto.user.service.UserAssociationCommandService;
import org.helianto.user.service.UserAssociationQueryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User parent controller.
 * 
 * @author mauriciofernandesdecastro
 */
@RequestMapping("/api/user/parent")
@RestController
@PreAuthorize("isAuthenticated()")
public class UserParentController {
	
	@Inject
	private UserAssociationQueryService userAssociationQueryService;
	
	@Inject
	private UserAssociationCommandService userAssociationCommandService;
	
	/**
	 * List groups by parent.
	 *
	 * GET 	/app/user/parent?userId
	 */
	@RequestMapping(method=RequestMethod.GET, params={"userId"})
	public List<UserGroupNameAdapter> groupParent(UserAuthentication userAuthentication, @RequestParam Integer userId) {
		return userAssociationQueryService.parentNameList(userAuthentication.getEntityId(), userId);
	}

	/**
	 * Updates groups and their associations.
	 *
	 * PUT 	/app/user/parent?userId
	 */
	@RequestMapping(method=RequestMethod.PUT, params={"userId"})
	public List<UserGroupNameAdapter> groupParent(UserAuthentication userAuthentication, @RequestParam Integer userId
			, @RequestParam Integer[] checked, @RequestParam Integer[] unchecked) {
		return userAssociationCommandService.associate(userAuthentication.getEntityId(), userId, checked, unchecked);
	}
	
}
