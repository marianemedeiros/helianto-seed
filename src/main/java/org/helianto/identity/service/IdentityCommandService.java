package org.helianto.identity.service;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.repository.EntityRepository;
import org.helianto.core.repository.IdentityReadAdapter;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.install.service.IdentityInstallService;
import org.helianto.install.service.UserInstallService;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.security.service.IdentityCryptoService;
import org.helianto.user.domain.User;
import org.helianto.user.domain.UserAssociation;
import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserAssociationRepository;
import org.helianto.user.repository.UserGroupRepository;
import org.helianto.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IdentityCommandService {
	private static final Logger logger = LoggerFactory.getLogger(IdentityCommandService.class);

	@Inject
	private EntityRepository entityRepository;
	
	@Inject 
	private IdentityRepository identityRepository;
	
	@Inject 
	private IdentityInstallService identityInstallService;

	@Inject 
	private UserInstallService userInstallService;

	@Inject
	private IdentityCryptoService identityCrypto;
	
	@Inject 
	private UserRepository userRepository;
	
	@Inject
	private UserGroupRepository userGroupRepository;
	
	@Inject
	private UserAssociationRepository userAssociationRepository;

	
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
	
	/**
	 * Update identity.
	 * 
	 * @param entityId
	 * @param command
	 */
	public Identity update(int entityId, Identity command) {
		Identity identity = null;
		if (command==null) {
			throw new IllegalArgumentException("Unable to update null identity.");
		}
		if (command.getPrincipal()==null || command.getPrincipal().isEmpty()) {
			throw new IllegalArgumentException("Unable to update non-existing identity principal.");
		}
		identity = identityRepository.findByPrincipal(command.getPrincipal());
		if (identity==null) {
			identity = identityRepository.saveAndFlush(command);
		}
		else {
			identity = identityRepository.saveAndFlush(identity.merge(command));
		}
		identityInstallService.installSecurity(identity);
		if (command.isPasswordChanging()) {
			identityCrypto.changeIdentitySecret(command.getPrincipal(), command.getPasswordToChange());
			logger.debug("New password updated.");
		}
		installUser(entityId, identity);
		return identity;
	}
	
	/**
	 * Local user installation.
	 * 
	 * @param entityId
	 * @param identity
	 */
	protected void installUser(int entityId, Identity identity) {
		@SuppressWarnings("deprecation")
		User user = userRepository.findByEntity_IdAndIdentity_Id(entityId, identity.getId());
		if (user==null) {
			Entity entity = entityRepository.findOne(entityId);
			logger.info("Will install user for entity {} and principal {}.", entity.getAlias(), identity.getPrincipal());
			user = new User(entity, identity);
			user.setUserType('I');
			user.setUserName(identity.getIdentityFirstName()+ " " + identity.getIdentityLastName());
			user.setAccountNonExpired(true);
			user = userRepository.saveAndFlush(user);
			logger.info("Installed user for entity {} and principal {}.", entity.getAlias(), identity.getPrincipal());
			UserGroup userGroup = userGroupRepository.findByEntity_IdAndUserKey(entity.getId(), "USER");
			if (userGroup==null) {
				throw new IllegalArgumentException("Unable to create user, USER group not found!");
			}
			UserAssociation association = userAssociationRepository.saveAndFlush(new UserAssociation(userGroup, user));
			logger.info("Associated user as {}.", association);
		}
	}
}
