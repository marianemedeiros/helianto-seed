package org.helianto.entity.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.repository.EntityReadAdapter;
import org.helianto.core.repository.EntityRepository;
import org.helianto.entity.service.EntityCommandService;
import org.helianto.security.domain.UserAuthority;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.security.repository.UserAuthorityRepository;
import org.helianto.user.domain.User;
import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserGroupRepository;
import org.helianto.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Entity controller.
 * 
 * @author mauriciofernandesdecastro
 */
@RestController
@RequestMapping("/api/entity")
@PreAuthorize("isAuthenticated()")
public class EntityController {

	private static final Logger logger = LoggerFactory.getLogger(EntityController.class);

	@Inject
	private EntityRepository entityRepository;
	
	@Inject
	private UserGroupRepository userGroupRepository;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private UserAuthorityRepository userAuthorityRepository;
	
	@Inject
	private EntityCommandService entityCommandService ;	
	
	/**
	 * Get the current entity.
	 * 
	 * GET /api/entity
	 * 
	 * Retrieves entity data using the entityId stored within the authenticated user. Please, note
	 * that the entityId is not part of the request. It is, instead, taken from the Secure context
	 * created by Spring Security and inject as a method parameter here via AOP.
	 *   
	 * @param userAuthentication
	 */
	@RequestMapping(method= RequestMethod.GET)
	public EntityReadAdapter entity(UserAuthentication userAuthentication) {
		EntityReadAdapter adapter = entityRepository.findAdapter(userAuthentication.getEntityId());
		logger.debug("Entity found: {}.", adapter);
		return adapter;
	}
	
	/**
	 * Create entity.
	 * 
	 * @param userAuthentication
	 */
	@RequestMapping(method= RequestMethod.POST)
	public Entity createEntity(UserAuthentication userAuthentication) {
		Entity entity = new Entity();
		logger.debug("Creating Entity {}.", entity);
		return entity;
	}
	
	@RequestMapping(method= RequestMethod.PUT)
	public Entity saveEntity(UserAuthentication userAuthentication, @RequestBody Entity entity ) {
		return entityCommandService.saveOrUpdate(userAuthentication, entity);
	}
	
	/**
	 * Get the current user.
	 * 
	 * GET /api/entity/user
	 */
	@RequestMapping(value="/user", method= RequestMethod.GET)
	public User user(UserAuthentication userAuthentication) {
		User adapter = userRepository.findAdapter(userAuthentication.getUserId());
		logger.debug("User found: {}.", adapter);
		return adapter;
	}
	
	/**
	 * Get user authorities for the current user.
	 *
	 * GET /api/entity/auth
	 */
	@RequestMapping(value={"/auth"}, method=RequestMethod.GET)
	public List<UserAuthority> auth(UserAuthentication userAuthentication) {
		return auth(userAuthentication.getUserId(), 0);
	}

	/**
	 * Get user authorities for a given user.
	 *
	 * GET /api/entity/auth?userId
	 */
	@RequestMapping(value={"/auth"}, method=RequestMethod.GET, params="userId")
	public List<UserAuthority> auth(UserAuthentication userAuthentication
			, @RequestParam Integer userId
			, @RequestParam(required = false, defaultValue = "0") Integer pageNumber) {
		return auth(userId, pageNumber);
	}
	
	/**
	 * Internal authorities.
	 * 
	 * @param userId
	 * @param pageNumber
	 */
	private List<UserAuthority> auth(Integer userId, Integer pageNumber) {
		List<UserGroup> parentGroups = userGroupRepository.findParentsByChildId(userId);
		List<UserAuthority> authList = new ArrayList<>();
		authList.addAll(userAuthorityRepository.findByUserGroupIdOrderByServiceCodeAsc(parentGroups));
		authList.add(new UserAuthority(0, 0, "USER", "READ", ""));
		return authList;
	}
	
}
