package org.helianto.user.service;

import java.util.List;

import javax.inject.Inject;

import org.helianto.user.domain.User;
import org.helianto.user.domain.UserAssociation;
import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserAssociationRepository;
import org.helianto.user.repository.UserGroupNameAdapter;
import org.helianto.user.repository.UserGroupRepository;
import org.helianto.user.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * User association command service.
 * 
 * @author mauriciofernandesdecastro
 */
@Service
public class UserAssociationCommandService {
	
	@Inject 
	private UserRepository userRepository;

	@Inject 
	private UserGroupRepository userGroupRepository;

	@Inject 
	private UserAssociationRepository userAssociationRepository;
	
	@Inject 
	private UserAssociationQueryService userAssociationQueryService;

	/**
	 * Associate (or de-associate) groups and users.
	 * 
	 * @param entityId
	 * @param userId
	 * @param checked
	 * @param unchecked
	 */
	public List<UserGroupNameAdapter> associate(int entityId, Integer userId, Integer[] checked, Integer[] unchecked) {
		User user = userRepository.findOne(userId);
		if (user.getEntity().getId()!=entityId) {
			throw new IllegalArgumentException("Inconsistent userId");
		}
		for (Integer id: checked) {
			if (id>0) {
				UserAssociation association = userAssociationRepository.findByParentIdAndChildId(id, userId);
				if (association==null) {
					UserGroup parent = userGroupRepository.findOne(id);
					if (parent==null) {
						throw new IllegalArgumentException("Inconsistent user parent");
					}
					userAssociationRepository.saveAndFlush(new UserAssociation(parent, user));
				}
			}
		}
		for (Integer id: unchecked) {
			if (id>0) {
				UserAssociation association = userAssociationRepository.findByParentIdAndChildId(id, userId);
				if (association!=null) {
					UserGroup parent = userGroupRepository.findOne(id);
					if (!parent.isSystemGroup()) {
						userAssociationRepository.delete(association);
					}
				}
			}
		}
		return userAssociationQueryService.parentNameList(entityId, userId);
	}


}
