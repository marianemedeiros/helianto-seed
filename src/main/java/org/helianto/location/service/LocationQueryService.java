package org.helianto.location.service;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.City;
import org.helianto.core.domain.Operator;
import org.helianto.core.domain.State;
import org.helianto.core.repository.CityRepository;
import org.helianto.core.repository.OperatorRepository;
import org.helianto.core.repository.StateRepository;
import org.helianto.security.internal.UserAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
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
public class LocationQueryService {

	private static final Logger logger = LoggerFactory.getLogger(LocationQueryService.class);

	@Inject
	private OperatorRepository operatorRepository;	
	
	@Inject 
	private StateRepository stateRepository;
	
	@Inject 
	private CityRepository cityRepository;
	
	@Inject 
	private Environment environment;
	
	public List<State> getStates(UserAuthentication userAuthentication){
		String contextName = environment.getProperty("helianto.defaultContextName", "DEFAULT");
		Operator context =  operatorRepository.findByOperatorName(contextName);
		if(userAuthentication!=null && userAuthentication.getContextId()>0){
			context = operatorRepository.findOne(userAuthentication.getContextId());
		}
		return stateRepository.findByContext(context, new Sort(Direction.ASC, "stateCode", "stateName"));
	}
	
	public List<City> getCities(Integer stateId){
		return cityRepository.findByState_Id(stateId);
	}
	
	
	
}
