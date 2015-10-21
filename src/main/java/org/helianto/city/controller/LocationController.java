package org.helianto.city.controller;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.City;
import org.helianto.core.domain.State;
import org.helianto.entity.service.EntityQueryService;
import org.helianto.security.internal.UserAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * City and State controller.
 * 
 * @author Eldevan Nery Junior
 */
@RestController
@RequestMapping("/api/location")
@PreAuthorize("permitAll()")
public class LocationController {

	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

	@Inject
	private EntityQueryService entityQueryService;	
	
	/**
	
	/**
	 * Retrieve state from user context.
	 * 
	 * @param userAuthentication
	 * 
	 */
	@RequestMapping(value={"/state"}, method=RequestMethod.GET)
	public List<State> states(UserAuthentication userAuthentication) {
		return entityQueryService.getStates(userAuthentication);
	}

	/**
	 * Get Cities given State.
	 * 
	 * @param stateId
	 * 
	 */
	@RequestMapping(value={"/city"}, method=RequestMethod.GET, params="stateId")
	public List<City> cities(@RequestParam Integer stateId) {
		return entityQueryService.getCities(stateId);
	}
	
}
