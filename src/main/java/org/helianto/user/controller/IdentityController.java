package org.helianto.user.controller;

import javax.inject.Inject;

import org.helianto.core.def.Appellation;
import org.helianto.core.def.Gender;
import org.helianto.core.def.IdentityType;
import org.helianto.core.def.Notification;
import org.helianto.core.repository.IdentityReadAdapter;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.user.service.UserCommandService;
import org.helianto.user.service.UserQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Identity controller.
 * 
 * @author Eldevan Nery Jr
 */
@PreAuthorize("isAuthenticated()")
@RequestMapping(value={"/api/identity"})
@RestController
public class IdentityController {

	private static final Logger logger = LoggerFactory.getLogger(IdentityController.class);
	
	@Inject
	private UserCommandService userCommandService;
	
	@Inject
	private UserQueryService userQueryService;
	
	/**
	 * New identity.
	 *
	 * POST		/app/identity
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.POST)
	public IdentityReadAdapter identityNew() {
		return userCommandService.newIdentity();
	}
	
	/**
	 * Identity.
	 *
	 * GET 	/app/identity/?identityId
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params={"identityId"})
	public IdentityReadAdapter userOne(@RequestParam Integer identityId) {
		return userQueryService.identityOne(identityId);
	}

	/**
	 * My identity.
	 * 
	 * @param userAuthentication
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params={"mine"})
	public IdentityReadAdapter identityOne(UserAuthentication userAuthentication) {
		return userQueryService.identityOne(userAuthentication.getIdentityId());
	}

	/**
	 * Create user.
	 *
	 * PUT 	/app/user/
	 * @deprecated
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public IdentityReadAdapter user(UserAuthentication userAuthentication, @RequestBody IdentityReadAdapter command) {
		return userCommandService.identity(userAuthentication, command);
	}

	/**
	 * Update user.
	 *
	 * PUT 	/app/user/
	 * @deprecated
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE, params={"mine"})
	public IdentityReadAdapter self(UserAuthentication userAuthentication, @RequestBody IdentityReadAdapter command) {
		return userCommandService.self(userAuthentication, command);
	}
	
	/**
	 * @deprecated
	 */
	@ModelAttribute("modalBody")
	public String getForm(){
		return "form-identity";
	}
	
	/**
	 * @deprecated
	 */
	@ModelAttribute("appellations")
	public Appellation[] getAppellations() {
		return Appellation.values();
	}

	/**
	 * @deprecated
	 */
	@ModelAttribute("notifications")
	public Notification[] getNotifications() {
		return Notification.values();
	}

	/**
	 * @deprecated
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params={"type"})
	public IdentityType[] getIdentityTypes() {
		return new IdentityType[] {
				IdentityType.NOT_ADDRESSABLE
				, IdentityType.ORGANIZATIONAL_EMAIL
				, IdentityType.PERSONAL_EMAIL
		};
	}

	/**
	 * @deprecated
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params={"type2"})
	public IdentityType[] getIdentityLegacyTypes() {
		return IdentityType.values();
	}

	/**
	 * @deprecated
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params={"gender"})
	public Gender[] getGenders() {
		return Gender.values();
	}

}
