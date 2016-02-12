package org.helianto.entity.service;

import javax.inject.Inject;

import org.helianto.core.domain.City;
import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.Operator;
import org.helianto.core.repository.CityRepository;
import org.helianto.core.repository.EntityRepository;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.repository.OperatorRepository;
import org.helianto.security.domain.UserAuthority;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.security.repository.UserAuthorityRepository;
import org.helianto.security.service.EntityInstallService;
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
	private OperatorRepository operatorRepository;	
	
	@Inject 
	private	IdentityRepository identityRepository;
	
	@Inject
	private UserGroupRepository userGroupRepository;

	@Inject
	private UserAuthorityRepository userAuthorityRepository;
	
	@Inject
	private CityRepository cityRepository;
	
	@Inject
	private EntityInstallService entityInstallService;

	
	
	/**
	 * Save an Entity.
	 * 
	 * @param userAuthentication
	 * @param command
	 */
	public Entity saveOrUpdate(UserAuthentication userAuthentication, Entity command ) {
	    // TODO: resolver CNPJ, etc
        command.setEntityDomain(command.getAlias());
		Entity target = null;
		if(command.getId()==0){
			Operator context = operatorRepository.findOne(userAuthentication.getContextId());
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
			Identity identity = identityRepository.findOne(userAuthentication.getIdentityId());
            target = entityRepository.saveAndFlush(target.merge(command));
            entityInstallService.createUser(target, identity);
 //           installAuthorities(target.getId());
            return target;
		}
		else {
			target = entityRepository.findByOperator_IdAndAlias(userAuthentication.getContextId(), command.getAlias());
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


}
