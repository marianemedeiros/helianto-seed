package org.helianto.user.service;

import java.util.List;

import javax.inject.Inject;

import org.helianto.user.repository.UserAssociationAdapterRepository;
import org.helianto.user.repository.UserGroupNameAdapter;
import org.springframework.stereotype.Service;

/**
 * Serviço de associação de usuários.
 * 
 * @author mauriciofernandesdecastro
 */
@Service
public class UserAssociationQueryService {
	
	@Inject 
	private UserAssociationAdapterRepository userTmpRepository;
	
	public List<UserGroupNameAdapter> parentNameList(int entityId, int userId) {
		List<UserGroupNameAdapter> parentNameList = userTmpRepository.findParentNamesByChildId(userId);
		parentNameList.addAll(userTmpRepository.findParentNamesByChildId(entityId, userId));
		return parentNameList;
	}


}
