package org.helianto.user.service;

import java.util.List;

import javax.inject.Inject;

import org.helianto.user.repository.UserAssociationAdapterRepository;
import org.helianto.user.repository.UserGroupNameAdapter;
import org.springframework.stereotype.Service;

/**
 * User association query service.
 * 
 * @author mauriciofernandesdecastro
 */
@Service
public class UserAssociationQueryService {
	
	@Inject 
	private UserAssociationAdapterRepository userTmpRepository;
	
	/**
	 * List parents from an user.
	 * 
	 * @param entityId
	 * @param userId
	 */
	public List<UserGroupNameAdapter> parentNameList(int entityId, int userId) {
		List<UserGroupNameAdapter> parentNameList = userTmpRepository.findParentNamesByChildId(userId);
		parentNameList.addAll(userTmpRepository.findParentNamesByChildId(entityId, userId));
		return parentNameList;
	}


}
