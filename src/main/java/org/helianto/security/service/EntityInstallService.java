package org.helianto.security.service;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.Lead;
import org.helianto.core.domain.Operator;
import org.helianto.core.domain.Signup;
import org.helianto.core.repository.LeadRepository;
import org.helianto.core.repository.SignupRepository;
import org.helianto.install.service.EntityInstallStrategy;
import org.helianto.install.service.UserInstallService;
import org.helianto.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service class for Entity operations.
 * 
 * @author Eldevan Nery Junior.
 * @deprecated
 */

//@Service
public class EntityInstallService {

	private static final Logger logger = LoggerFactory.getLogger(EntityInstallService.class);

	@Inject
	private LeadRepository leadRepository;

	@Inject 
	private UserInstallService userInstallService;

	@Inject
	private EntityInstallStrategy entityInstallStrategy;

	@Inject
	private SignupRepository signupRepository;

	/**
	 * Retrieve previous signup.
	 * 
	 * @param contextId
	 * @param identity
	 */
	public Signup getSignup(Integer contextId, Identity identity) {
		return signupRepository.findByContextIdAndPrincipal(contextId, identity.getPrincipal());
	}

	/**
	 * Generate entity prototypes.
	 * 
	 * @param identity
	 */
	public List<Entity> generateEntityPrototypes(Signup signup){
		return entityInstallStrategy.generateEntityPrototypes(signup);
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
