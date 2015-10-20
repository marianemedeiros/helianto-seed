package org.helianto.entity.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.Operator;
import org.helianto.core.domain.Signup;
import org.helianto.core.repository.EntityRepository;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.repository.OperatorRepository;
import org.helianto.install.service.EntityInstallStrategy;
import org.helianto.install.service.UserInstallService;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.security.service.EntityInstallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Entity Command Service.
 * 
 * @author Eldevan Nery Junior
 *
 */
@Service
public class EntityCommandService {

	private static final Logger logger = LoggerFactory.getLogger(EntityCommandService.class);

	@Inject
	private EntityRepository entityRepository;
	
	@Inject
	private OperatorRepository operatorRepository;	
	
	@Inject 
	private	IdentityRepository identityRepository;
	
	@Inject 
	private	EntityInstallService entityInstallService;
	
	/**
	 * Save an Entity.
	 * 
	 * @param userAuthentication
	 * @param command
	 */
	public Entity saveOrUpdate(UserAuthentication userAuthentication, Entity command ) {
		logger.debug("saving Entity {}.", command);
		Operator context = operatorRepository.findOne(userAuthentication.getContextId());
		Entity target = entityRepository.findByOperatorAndAlias(context, command.getAlias());
		Identity identity = identityRepository.findOne(userAuthentication.getIdentityId());
		if(target==null){
			Signup signup = new Signup();
			signup.setDomain(command.getAlias());
			signup.setCityId(command.getCityId());
			List<Entity> prototypes = entityInstallService.generateEntityPrototypes(signup);
			entityInstallService.createEntities(context, prototypes, identity);
		}
		else{
//			TODO merge on Entity to update mode?
//			target = target.merge(command);
		}
		return entityRepository.saveAndFlush(target);
	}
	
}
