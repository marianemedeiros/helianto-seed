package org.helianto.entity.service;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.City;
import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.Lead;
import org.helianto.core.domain.Operator;
import org.helianto.core.domain.Signup;
import org.helianto.core.repository.CityRepository;
import org.helianto.core.repository.EntityRepository;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.repository.LeadRepository;
import org.helianto.core.repository.OperatorRepository;
import org.helianto.install.service.EntityInstallStrategy;
import org.helianto.install.service.UserInstallService;
import org.helianto.security.domain.UserAuthority;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.security.repository.UserAuthorityRepository;
import org.helianto.security.service.EntityInstallService;
import org.helianto.user.domain.User;
import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserGroupRepository;
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
    private CityRepository cityRepository;

	@Inject
	private OperatorRepository operatorRepository;	
	
	@Inject 
	private	IdentityRepository identityRepository;
	
	@Inject
	private UserGroupRepository userGroupRepository;

	@Inject
	private UserAuthorityRepository userAuthorityRepository;

	@Inject
	private LeadRepository leadRepository;

	@Inject 
	private UserInstallService userInstallService;

	@Inject 
	private	EntityInstallStrategy entityInstallStrategy;
	
	@Inject
	private	EntityInstallService entityInstallService;
	
//	/**
//	 * Save an Entity.
//	 * 
//	 * @param userAuthentication
//	 * @param command
//	 */
//	public Entity saveOrUpdate2(UserAuthentication userAuthentication, Entity command ) {
//		logger.debug("saving Entity {}.", command);
//		Operator context = operatorRepository.findOne(userAuthentication.getContextId());
//		Entity target = entityRepository.findByOperatorAndAlias(context, command.getAlias());
//		Identity identity = identityRepository.findOne(userAuthentication.getIdentityId());
//		if(target==null){
//			Signup signup = new Signup(command);
//			signup.setDomain(command.getAlias());
//			List<Entity> prototypes = entityInstallStrategy.generateEntityPrototypes(signup);
//			createEntities(context, prototypes, identity);
//		}
//		else{
////			TODO merge on Entity to update mode?
////			target = target.merge(command);
//		}
//		return entityRepository.findByOperatorAndAlias(context, command.getAlias());
//	}
//	
	/**
	 * Save an Entity.
	 * 
	 * @param contextId
     * @param identityId
	 * @param command
	 */
	public Entity saveOrUpdate(int contextId, int identityId, Entity command ) {
        command.setEntityDomain(command.getAlias());
		Entity target = null;
		if(command.getId()==0){
			Operator context = operatorRepository.findOne(contextId);
			if (context==null) {
				throw new IllegalArgumentException("Unable to find context");
			}
			Entity entity = entityRepository.findByContextNameAndAlias(context.getOperatorName(), command.getAlias());
			if (entity!=null) {
				logger.debug("Found existing entity for context {} and alias {}.", context.getOperatorName(), command.getAlias());
				throw new IllegalArgumentException("Existing entity");
			}
            City city = cityRepository.findOne(command.getCityId());
            if (city==null) {
                logger.error("Unable to find city with id {}.", command.getCityId());
                throw new IllegalArgumentException("Unable to find city to create entity");
            }
            target = new Entity(context, command.getAlias());
            target.setCity(city);
			Identity identity = identityRepository.findOne(identityId);
            target = entityRepository.saveAndFlush(target.merge(command));
            entityInstallService.createUser(target, identity);
            installAuthorities(target.getId());
            return target;
		}
		else {
			target = entityRepository.findByOperator_IdAndAlias(contextId, command.getAlias());
            return entityRepository.saveAndFlush(target.merge(command));
		}
	}

	public void installAuthorities(int entityId) {
		// Root authorities
		UserGroup admin = userGroupRepository.findByEntity_IdAndUserKey(entityId, "ADMIN");

		// TODO pay attention to the hard coded ADMIN group here; see contextGroups for a better solution

		if (admin==null) {
			throw new IllegalArgumentException("Unable to install context, ADMIN group not found; check your contextGroups definition to ensure proper installation.");
		}

        // Root privileges
        UserAuthority rootAuthority = userAuthorityRepository.findByUserGroupAndServiceCode(admin, "ADMIN");
        if (rootAuthority==null) {
            rootAuthority = new UserAuthority(admin, "ADMIN");
            rootAuthority.setServiceExtension("READ,WRITE,MANAGER");
            userAuthorityRepository.saveAndFlush(rootAuthority);
        }
        logger.debug("Mandatory ADMIN authority is {}", rootAuthority);

        // User privileges
        UserAuthority userAuthority = userAuthorityRepository.findByUserGroupAndServiceCode(admin, "USER");
        if (userAuthority==null) {
            userAuthority = new UserAuthority(admin, "USER");
            userAuthority.setServiceExtension("READ,WRITE");
            userAuthorityRepository.saveAndFlush(userAuthority);
        }
        logger.debug("Mandatory USER authority is {}", userAuthority);
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
