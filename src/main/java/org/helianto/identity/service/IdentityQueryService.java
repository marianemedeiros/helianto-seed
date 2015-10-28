package org.helianto.identity.service;

import javax.inject.Inject;

import org.helianto.core.domain.Identity;
import org.helianto.core.repository.EntityRepository;
import org.helianto.core.repository.IdentityReadAdapter;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Service
public class IdentityQueryService {

	@Inject 
	private UserRepository userRepository;

	@Inject 
	private IdentityRepository identityRepository;	
	
	@Inject 
	private EntityRepository entityRepository;	

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
	
	//TODO replace readAdapter.
	public IdentityReadAdapter identityOne(Integer identityId) {
		Identity target = identityRepository.findOne(identityId);
		if (target==null) {
			target = new Identity(); 
		}
		return new IdentityReadAdapter().setAdaptee(target).build();
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
