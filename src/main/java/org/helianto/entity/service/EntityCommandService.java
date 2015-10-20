package org.helianto.entity.service;

import java.util.Date;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.Operator;
import org.helianto.core.repository.EntityRepository;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.repository.OperatorRepository;
import org.helianto.install.service.UserInstallService;
import org.helianto.security.internal.UserAuthentication;
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
	private UserInstallService userInstallService;
	
	@Inject 
	private	IdentityRepository identityRepository;
	
	/**
	 * Save an Entity.
	 * 
	 * @param userAuthentication
	 * @param command
	 */
	public Entity saveEntity(UserAuthentication userAuthentication, Entity command ) {
		logger.debug("saving Entity {}.", command);
		Operator context = operatorRepository.findOne(userAuthentication.getContextId());
		Entity target = entityRepository.findByOperatorAndAlias(context, command.getAlias());
		if(target==null){
			command.setOperator(context);
			command.setInstallDate(new Date());
			target = entityRepository.saveAndFlush(command);
			Identity identity = identityRepository.findOne(userAuthentication.getIdentityId());
			userInstallService.installUser(command, identity.getPrincipal());
		}else{
//			TODO merge on Entity to update mode?
//			target = target.merge(command);
		}
		return entityRepository.saveAndFlush(target);
	}
	
}
