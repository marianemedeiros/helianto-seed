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
}
