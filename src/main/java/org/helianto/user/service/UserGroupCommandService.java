package org.helianto.user.service;

import java.util.UUID;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.repository.CategoryRepository;
import org.helianto.core.repository.EntityRepository;
import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
* User group command service.
* 
* @author mauriciofernandesdecastro
*/
@Service
public class UserGroupCommandService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserGroupCommandService.class);

	@Inject
	private EntityRepository entityRepository;
	
	@Inject
	private UserGroupRepository userGroupRepository;

	@Inject 
	private CategoryRepository categoryRepository;
	
	public UserGroup newGroup(Character qualifierValue) {
		if (qualifierValue.equals(null)) {
			throw new IllegalArgumentException("A group type is required");
		}
		if (qualifierValue.equals("G")) {
			logger.error("RESERVED GROUP TYPE 'G' for administrators does not accept new groups after installation!");
			throw new IllegalArgumentException("Reserved group type does not accept new groups after installation!");
		}
		if (qualifierValue.equals("A")) {
			logger.error("RESERVED GROUP TYPE 'A' for users does not accept new groups after installation!");
			throw new IllegalArgumentException("Reserved group type does not accept new groups after installation!");
		}
		UserGroup userGroup = new UserGroup();
		userGroup.setUserType(qualifierValue);
		return userGroup;
	}

	public UserGroup saveOrUpdate(int entityId, UserGroup command) {
		UserGroup target = null;
		if (command.getId()==0) {
			Entity entity = entityRepository.findOne(entityId);
			target = new UserGroup(entity, UUID.randomUUID().toString());
			target.setUserType(command.getUserType());
		}
		else {
			target = userGroupRepository.findOne(command.getId());
		}
		if(target==null) {
			throw new IllegalArgumentException("UserGroup cannot be null");	
		}
		if (command.getCategoryId()!=null && command.getCategoryId()>0) {
			target.setCategory(categoryRepository.findOne(command.getCategoryId()));
		}
		return userGroupRepository.saveAndFlush(target.merge(command));
	}

}
