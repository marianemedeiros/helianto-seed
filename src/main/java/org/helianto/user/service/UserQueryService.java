package org.helianto.user.service;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.Identity;
import org.helianto.core.internal.KeyNameAdapter;
import org.helianto.core.internal.QualifierAdapter;
import org.helianto.core.internal.SimpleCounter;
import org.helianto.core.repository.EntityRepository;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserRepository;
import org.helianto.user.repository.UserStatsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
* User query service.
* 
* @author mauriciofernandesdecastro
*/
public class UserQueryService {

	@Inject 
	private UserRepository userRepository;

	@Inject 
	protected UserStatsRepository userStatsRepository;
	
	@Inject 
	private IdentityRepository identityRepository;	
	
	@Inject 
	private EntityRepository entityRepository;	
	
	private KeyNameAdapter[] keyNameAdapter;
	
	/**
	 * KeyNameAdapter array constructor.
	 * 
	 * @param keyNameAdapter
	 */
	public UserQueryService(KeyNameAdapter[] keyNameAdapter) {
		super();
		this.keyNameAdapter = keyNameAdapter;
	}

	/**
	 * List qualifiers.
	 * 
	 * @param userAuthentication
	 */
	public List<QualifierAdapter> qualifierList(int entityId) {
		List<QualifierAdapter> qualifierList = 
				QualifierAdapter.qualifierAdapterList(keyNameAdapter);
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
	 * @param itemsPerPage
	 */
	public Page<User> userList(int entityId, Character userType, String userStates, Integer pageNumber, Integer itemsPerPage) {
		Pageable page = new PageRequest(pageNumber, itemsPerPage, Direction.ASC, "userName");
		return userRepository.findByParentUserType(entityId, userType, userStates.toCharArray(), page);
	}
	
	/**
	 * Get user.
	 * 
	 * @param userId
	 */
	public User user(int entityId, Integer userId) {
		User user = userRepository.findAdapter(userId);
		return user;
	}
	
	public User userOne(Integer userId) {
		return userRepository.findAdapter(userId);
	}
	
	public IdentityUserCreateResponse createNewUser(String search, Integer entityId){
		Identity identity = identityRepository.findByPrincipal(search);
		IdentityUserCreateResponse response = new IdentityUserCreateResponse();
		if (identity!=null) {
			response.setIdentityId(identity.getId());
		}
		User user = userRepository.findByEntityAndUserKey(entityRepository.findOne(entityId), search);
		if (user!=null) {
			response.setUserId(user.getId());
		}
		response.setUserName(search);
		return response;
	}
	
	/**
	 * <h2>Decision class of user/identity creation.</h2>
	 * <ul>
	 * 	<li>(identityId = 0 & userId = 0) : User and identity creation.</li>
	 *  <li>(identityId > 0 & userId = 0) : Just user creation.</li>
	 *  <li>(identityId > 0 & userId > 0) : Cannot create user and identity.</li>
	 * </ul>
	 * 
	 * @author Eldevan Nery Jr.
	 *	
	 */
	public class IdentityUserCreateResponse{
		
		private String userName = ""; 
		private Integer identityId = 0;
		private Integer userId = 0;
		
		public IdentityUserCreateResponse(String userName, Integer identityId, Integer userId) {
			super();
			this.userName = userName;
			this.identityId = identityId;
			this.userId = userId;
		} 
		
		public IdentityUserCreateResponse() {

		}
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		
		public Integer getIdentityId() {
			return identityId;
		}
		public void setIdentityId(Integer identityId) {
			this.identityId = identityId;
		}
		
		public Integer getUserId() {
			return userId;
		}
		public void setUserId(Integer userId) {
			this.userId = userId;
		}
		
		@JsonSerialize
		public Boolean cannotCreate(){
			return getIdentityId()>0 && getUserId()>0;
		}
		
		@JsonSerialize
		public Boolean createIdentity(){
			return getIdentityId()==0;
		}
		
		@JsonSerialize
		public Boolean createUser(){
			return getUserId()==0;
		}
		
	}

}
