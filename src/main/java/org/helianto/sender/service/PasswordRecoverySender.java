package org.helianto.sender.service;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Send confirmation e-mail.
 * 
 * @author mauriciofernandesdecastro
 */
@Component
public class PasswordRecoverySender 
	extends AbstractBodyTemplateSender 
{

	/**
	 * Constructor.
	 */
	@Inject
	public PasswordRecoverySender(Environment env) {
		super(env.getProperty("sender.noReplyEmail"), env.getProperty("sender.rootFullName"), "passwordRecovery");
	}
	
}
