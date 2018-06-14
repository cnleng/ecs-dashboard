/**
 * 
 */
package com.emc.ecs.metadata.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author nlengc
 *
 */
@Configuration
@ConfigurationProperties(prefix = "serviceNowConfiguration")
public class ServiceNowConfiguration {

	private String instanceUrl;
	private String api;
	private String username;
	private String password;

	public ServiceNowConfiguration() {
	}

	/**
	 * @return the instanceUrl
	 */
	public final String getInstanceUrl() {
		return instanceUrl;
	}

	/**
	 * @param instanceUrl
	 *            the instanceUrl to set
	 */
	public final void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}

	public final String getApi() {
		return api;
	}

	public final void setApi(String api) {
		this.api = api;
	}

	/**
	 * @return the username
	 */
	public final String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public final void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public final void setPassword(String password) {
		this.password = password;
	}

	// @Bean
	// public ServiceNowConfiguration serviceNowConfiguration() {
	// ServiceNowConfiguration configuration = new ServiceNowConfiguration();
	// configuration.setInstanceUrl(this.getInstanceUrl());
	// configuration.setPassword(this.getPassword());
	// configuration.setUsername(this.getUsername());
	// return configuration;
	// }
	//
	// @Bean
	// public ServiceNowDAOConfig serviceNowDAOConfig() throws Exception {
	// final ServiceNowDAOConfig serviceNowDAOConfig = new
	// ServiceNowDAOConfig();
	// serviceNowDAOConfig.setInstanceUrl(this.getInstanceUrl());
	// serviceNowDAOConfig.setUsername(this.getUsername());
	// serviceNowDAOConfig.setPassword(this.getPassword());
	// return serviceNowDAOConfig;
	// }

}
