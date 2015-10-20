package org.helianto.entity.service;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.City;
import org.helianto.core.domain.Operator;
import org.helianto.core.domain.State;
import org.helianto.core.repository.CityRepository;
import org.helianto.core.repository.EntityRepository;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.repository.OperatorRepository;
import org.helianto.core.repository.StateRepository;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.security.service.EntityInstallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * Entity query service.
 * 
 * @author Eldevan Nery Junior
 *
 */
@Service
public class EntityQueryService {

	private static final Logger logger = LoggerFactory.getLogger(EntityQueryService.class);

	@Inject
	private EntityRepository entityRepository;
	
	@Inject
	private OperatorRepository operatorRepository;	
	
	@Inject 
	private	IdentityRepository identityRepository;
	
	@Inject 
	private	EntityInstallService entityInstallService;
	
	@Inject 
	private StateRepository stateRepository;
	
	@Inject 
	private CityRepository cityRepository;
	
	public List<State> getStates(UserAuthentication userAuthentication){
		return stateRepository.findByContext(operatorRepository.findOne(userAuthentication.getContextId()), new Sort(Direction.ASC, "stateCode", "stateName"));
	}
	
	public List<City> getCities(Integer stateId){
		return cityRepository.findByState_Id(stateId);
	}
	
	
	
}
