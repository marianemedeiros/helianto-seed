package org.helianto.sender.service;

import javax.inject.Inject;

import org.helianto.sendgrid.message.sender.AbstractTemplateSender;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Standard notification sender.
 * 
 * @author mauriciofernandesdecastro
 */
@Component
public class NotificationSender 
	extends AbstractTemplateSender
{
	
	/**
	 * Constructor.
	 */
	@Inject
	public NotificationSender(Environment env) {
		super(env.getProperty("sender.noReplyEmail"), env.getProperty("sender.rootFullName"), "notificationSender");
	}
	
}
