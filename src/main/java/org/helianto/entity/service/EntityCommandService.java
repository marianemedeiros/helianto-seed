package org.helianto.entity.service;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.Lead;
import org.helianto.core.domain.Operator;
import org.helianto.core.domain.Signup;
import org.helianto.core.repository.EntityRepository;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.repository.LeadRepository;
import org.helianto.core.repository.OperatorRepository;
import org.helianto.install.service.EntityInstallStrategy;
import org.helianto.install.service.UserInstallService;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.user.domain.User;
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
	private LeadRepository leadRepository;

	@Inject 
	private UserInstallService userInstallService;

	@Inject 
	private	EntityInstallStrategy entityInstallStrategy;
	
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
			Signup signup = new Signup(command);
			signup.setDomain(command.getAlias());
			List<Entity> prototypes = entityInstallStrategy.generateEntityPrototypes(signup);
			createEntities(context, prototypes, identity);
		}
		else{
//			TODO merge on Entity to update mode?
//			target = target.merge(command);
		}
		return entityRepository.findByOperatorAndAlias(context, command.getAlias());
	}
	
	/**
	 * Create entities.
	 * 
	 * @param prototypes
	 * @param identity
	 */
	public void createEntities(Operator context, List<Entity> prototypes, Identity identity) {
		Entity entity = null;
		for (Entity prototype: prototypes) {
			entity = entityInstallStrategy.installEntity(context, prototype);
			if(entity!=null){
				createUser(entity, identity);
			}
		}
	}
	/**
	 * Create new user.
	 * 
	 * @param entity
	 * @param form
	 * @param formBinding
	 */
	public User createUser(Entity entity, Identity identity) {
		try {
			String principal = identity.getPrincipal();
			User user = userInstallService.installUser(entity,  principal);
			removeLead(principal);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Remove temporary lead.
	 * 
	 * @param leadPrincipal
	 */
	public final String removeLead(String leadPrincipal){
		List<Lead> leads = leadRepository.findByPrincipal(leadPrincipal);	
		for (Lead lead : leads) {
			leadRepository.delete(lead);
		}
		return leadPrincipal;
	}


}
