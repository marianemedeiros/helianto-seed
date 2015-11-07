package org.helianto.seed;

import org.helianto.core.config.HeliantoServiceConfig;
import org.helianto.sender.service.UserConfirmationSender;
import org.helianto.sendgrid.config.SendGridConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@ContextConfiguration(classes={HeliantoServiceConfig.class
		, SendGridConfig.class
		, OAuth2ClientConfig.class
		, MultiHttpSecurityConfig.class})
	@EnableJpaRepositories(
	    basePackages={"org.helianto.*.repository"})
@PropertySource("classpath:/META-INF/app.properties")
public class TestConfig extends AbstractRootContextConfig {

    @Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.queryableText(env.getProperty("security.encryptPassword", "password"), env.getProperty("security.encryptSalt", "00"));
	}

    @Bean
	public UserConfirmationSender userConfirmationSender(Environment env) {
		return new UserConfirmationSender(env);
	}

}
