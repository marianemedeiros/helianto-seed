package org.helianto.user.controller;

import java.util.List;

import javax.inject.Inject;

import org.helianto.security.internal.UserAuthentication;
import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserGroupNameAdapter;
import org.helianto.user.service.UserAssociationQueryService;
import org.helianto.user.service.UserGroupCommandService;
import org.helianto.user.service.UserGroupQueryService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User group controller.
 * 
 * @author mauriciofernandesdecastro
 */
@RequestMapping("/api/user/group")
@RestController
@PreAuthorize("isAuthenticated()")
public class UserGroupController {
	
	@Inject
	private UserGroupQueryService userGroupQueryService;

	@Inject
	private UserGroupCommandService userGroupCommandService;

	@Inject
	private UserAssociationQueryService userAssociationQueryService;
	
	/**
	 * List user groups.
	 *
	 * GET       /api/user/group?qualifierValue
	 */
	@RequestMapping(method=RequestMethod.GET, params={"qualifierValue"})
	public Page<UserGroup> groups(UserAuthentication userAuthentication, @RequestParam Character qualifierValue
			, @RequestParam(defaultValue="0") Integer pageNumber, @RequestParam(defaultValue="20") Integer pageSize) {
		return userGroupQueryService.listUserGroup(userAuthentication.getEntityId(), qualifierValue, pageNumber, pageSize);
	}

	/**
	 * New group.
	 *
	 * POST		/api/user/group?qualifierValue
	 */
	@RequestMapping(method=RequestMethod.POST , params={"qualifierValue"})
	public UserGroup groupNew(UserAuthentication userAuthentication, @RequestParam Character qualifierValue) {
		return userGroupCommandService.newGroup(qualifierValue);
	}

	/**
	 * Some group.
	 *
	 * GET 	/api/user/group?groupId
	 */
	@RequestMapping(method=RequestMethod.GET, params={"groupId"})
	public UserGroup groupOpen(@RequestParam Integer groupId) {
		return userGroupQueryService.groupOpen(groupId);
	}

	/**
	 * Update group.
	 *
	 * PUT 	/api/user/group
	 */
	@RequestMapping(method=RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public UserGroup group(UserAuthentication userAuthentication, @RequestBody UserGroup command) {
		return userGroupCommandService.saveOrUpdate(userAuthentication.getEntityId(), command);
	}
	
	/**
	 * List groups by parent.
	 *
	 * GET 	/app/user/parent?userId
	 */
	@RequestMapping(value={"/parent"}, method=RequestMethod.GET, params={"userId"})
	public List<UserGroupNameAdapter> groupParent(UserAuthentication userAuthentication, @RequestParam Integer userId) {
		return userAssociationQueryService.parentNameList(userAuthentication.getEntityId(), userId);
	}

}
