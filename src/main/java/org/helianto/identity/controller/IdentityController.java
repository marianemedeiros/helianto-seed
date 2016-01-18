package org.helianto.identity.controller;

import javax.inject.Inject;

import org.helianto.core.def.Gender;
import org.helianto.core.def.IdentityType;
import org.helianto.core.domain.Identity;
import org.helianto.identity.service.IdentityCommandService;
import org.helianto.identity.service.IdentityQueryService;
import org.helianto.security.internal.UserAuthentication;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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

	@Inject
	private IdentityCommandService identityCommandService;
	
	@Inject
	private IdentityQueryService identityQueryService;

	/**
	 * New identity.
	 *
	 * POST		/app/identity/?principal
	 */
	@RequestMapping(method=RequestMethod.POST, params={"principal"})
	public Identity identityNew(@RequestParam String principal) {
		return new Identity(principal);
	}
	
	/**
	 * Identity.
	 *
	 * GET 	/app/identity/?identityId
	 */
	@RequestMapping(method=RequestMethod.GET, params={"identityId"})
	public Identity identity(@RequestParam Integer identityId) {
		return identityQueryService.identityOne(identityId);
	}

	/**
	 * My identity.
	 * 
	 * @param userAuthentication
	 */
	@RequestMapping(method=RequestMethod.GET, params={"mine"})
	public Identity identityOne(UserAuthentication userAuthentication) {
		return identityQueryService.identityOne(userAuthentication.getIdentityId());
	}

	/**
	 * Update identity.
	 *
	 * PUT 	/app/identity/
	 */
	@RequestMapping(method=RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Identity user(UserAuthentication userAuthentication, @RequestBody Identity command) {
		return identityCommandService.update(userAuthentication.getEntityId(), command);
	}

	/**
	 * Update my identity.
	 *
	 * PUT 	/app/identity/mine
	 */
	@RequestMapping(method=RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE, params={"mine"})
	public Identity self(UserAuthentication userAuthentication, @RequestBody Identity command) {
		return identityCommandService.update(userAuthentication.getEntityId(), command);
	}
	
	/**
	 * Get identity types (limited).
	 * 
	 * GET /app/identity/?type
	 */
	@RequestMapping(method=RequestMethod.GET, params={"type"})
	public IdentityType[] getIdentityTypes() {
		return new IdentityType[] {
				IdentityType.NOT_ADDRESSABLE
				, IdentityType.ORGANIZATIONAL_EMAIL
				, IdentityType.PERSONAL_EMAIL
		};
	}

	/**
	 * Get gender types.
	 * 
	 * GET /app/identity/?gender
	 */
	@RequestMapping(method=RequestMethod.GET, params={"gender"})
	public Gender[] getGenders() {
		return Gender.values();
	}

}
