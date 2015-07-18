package org.helianto.user.service;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.internal.QualifierAdapter;
import org.helianto.core.internal.SimpleCounter;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserKeyNameAdapterArray;
import org.helianto.user.repository.UserRepository;
import org.helianto.user.repository.UserStatsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
* User query service.
* 
* @author mauriciofernandesdecastro
*/
@Service
public class UserQueryService {

	@Inject 
	private UserRepository userTmpRepository;

	@Inject
	private UserKeyNameAdapterArray keyNameAdapterArray;

	@Inject 
	protected UserStatsRepository userStatsRepository;
	
	/**
	 * List qualifiers.
	 * 
	 * @param userAuthentication
	 */
	public List<QualifierAdapter> qualifierList(int entityId) {
		List<QualifierAdapter> qualifierList 
			= QualifierAdapter.qualifierAdapterList(keyNameAdapterArray.values());
		qualifierCount(entityId, qualifierList);
		return qualifierList;
	}
	
	/**
	 * Helper method to count qualifiers.
	 * 
	 * @param entityId
	 * @param qualifierList
	 */
	protected void qualifierCount(int entityId, List<QualifierAdapter> qualifierList) {
		List<SimpleCounter> counterListAll 
			= userStatsRepository.countActiveUsersGroupByType(entityId);
		for (QualifierAdapter qualifier: qualifierList) {
			qualifier.setCountItems(counterListAll);
		}
	}
	
	/**
	 * Page users.
	 * 
	 * @param entityId
	 * @param userType
	 * @param userStates
	 * @param pageNumber
	 */
	public Page<User> userList(int entityId, Character userType, String userStates, Integer pageNumber) {
		Pageable page = new PageRequest(pageNumber, 20, Direction.ASC, "userName");
		return userTmpRepository.findByParentUserType(entityId, userType, userStates.toCharArray(), page);
	}

}
