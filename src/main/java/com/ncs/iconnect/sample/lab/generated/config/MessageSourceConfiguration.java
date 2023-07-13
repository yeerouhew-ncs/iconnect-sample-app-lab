package com.ncs.iconnect.sample.lab.generated.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfiguration {
	
	public final String[] msg = {"i18n.messages","i18n.itrust-messages"};
	
	@Bean(name="messageSource")
	public ResourceBundleMessageSource getResourceBundler() {
		ResourceBundleMessageSource res = new ResourceBundleMessageSource();
		res.addBasenames(msg);
		return res;
	}
}
