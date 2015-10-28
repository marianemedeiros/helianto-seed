package org.helianto.user.service;

import java.util.Locale;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.repository.EntityRepository;
import org.helianto.core.repository.IdentityReadAdapter;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.install.service.IdentityInstallService;
import org.helianto.install.service.UserInstallService;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.user.domain.User;
import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserGroupRepository;
import org.helianto.user.repository.UserReadAdapter;
import org.helianto.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
* User command service.
* 
* @author Eldevan Nery Junior
*/
@Service
public class UserCommandService {

	private static final Logger logger = LoggerFactory.getLogger(UserCommandService.class);
	
	@Inject
	private EntityRepository entityRepository;
	
	@Inject
	private UserGroupRepository userGroupRepository;
	
	@Inject 
	private UserRepository userRepository;
	
	@Inject 
	private IdentityRepository identityRepository;
	
	@Inject 
	private IdentityInstallService identityInstallService;
	
	@Inject 
	private UserInstallService userInstallService;
	
	//TODO Replace ReadAdapter
	public User user(int entityId, UserReadAdapter command) {
		User target = null;
		if (command.getUserId()==0) {
			target = newUserAdapter(command.getIdentityId(), entityId).build().merge();
			Integer existing = userRepository.findIdByEntity_IdAndUserKey(entityId, target.getUserKey());
			if (existing!=null) {
				throw new RuntimeException("USER_NOT_UNIQUE");
			}
		}
		else{
			target = userRepository.findOne(command.getUserId());
		}
		target = userRepository.saveAndFlush(command.setAdaptee(target).build().merge());
		
		return userRepository.findOne(target.getId());
	}
	
	public UserReadAdapter newUserAdapter(Integer identityId, Integer entityId){
		return new UserReadAdapter(createUser(identityId, entityId));
	}
	
	/**
	 * Cria um Usuário.
	 * 
	 * @param identity
	 * @param entity
	 */
	public User createUser(Integer identityId, Integer entityId) {
		Identity identity = identityRepository.findOne(identityId);
		Entity entity = entityRepository.findOne(entityId);
		User user = null;
		if (identity!=null) {
			user = new User(entity, identity);
			user.setUserName(identity.getIdentityFirstName()+" "+ identity.getIdentityLastName());
			user.setLocale(Locale.getDefault());
			user.setUserType('I');
			user.setUserState('A');
			user.setAccountNonExpired(true);
			return user;
		}
		throw new IllegalArgumentException("Identity not informed!");
	}
	
	//identity

	//TODO Replace ReadAdapter
	public IdentityReadAdapter newIdentity(){
		return new IdentityReadAdapter();
	}
	
	//TODO Replace ReadAdapter
	public IdentityReadAdapter identity(int entityId, IdentityReadAdapter command){
//		IdentitySecret secret = identityInstallService.installIdentity(command.getPrincipal());
		logger.debug("Creating Identity and User");
		Identity target = new Identity();
		if (command.getId()==0) {
			target = command.setAdaptee(target).merge();
			target = identityRepository.saveAndFlush(target);
		}else{
			target = identityRepository.findOne(command.getId());
		}
		Entity entity = entityRepository.findOne(entityId);
		userInstallService.installUser(entity, target.getPrincipal());
		identityInstallService.installIdentity(target.getPrincipal());
		return command.setAdaptee(target).build();
	}
	
	//TODO Replace ReadAdapter
	public IdentityReadAdapter self(UserAuthentication userAuthentication, IdentityReadAdapter command){
		Identity target = identityRepository.findOne(userAuthentication.getIdentityId());
		target = command.setAdaptee(target).merge();
		return command.setAdaptee(identityRepository.saveAndFlush(target)).build();
	}
	
	//TODO Replace ReadAdapter
	public User user(UserAuthentication userAuthentication, UserReadAdapter command) {
		User target = null;
		if (command.getUserId()==0) {
			target = newUserAdapter(command.getIdentityId(), userAuthentication.getEntityId()).build().merge();
			//TODO?
//			Integer existing = userRepository.findByEntityIdAndUserKey(userAuthentication.getEntityId(), target.getUserKey());
//			if (existing!=null) {
//				throw new SaveEntityException(existing, "USER_NOT_UNIQUE", 404);
//			}
		}else{
			target = userRepository.findOne(command.getUserId());
		}
		UserGroup group = userGroupRepository.findOne(command.getUserGroupId());
		
		target = userRepository.saveAndFlush(command.setAdaptee(target).build().merge());
		System.err.println(target.getUserName());
		return userRepository.findAdapter(target.getId());
	}
	
	public User userActivate(UserAuthentication userAuthentication, User command) {
		User user = userRepository.findOne(command.getId());
		if (user==null) {
			throw new IllegalArgumentException("Unable to activate/deactivate null user.");
		}
		user.setUserState(command.getUserState());
		return userRepository.saveAndFlush(user);
	}
	
	
}
