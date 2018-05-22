/**
 * 
 */
package com.emc.ecs.metadata.dao.servicenow;

import java.util.Date;

/**
 * @author nlengc
 *
 */
public class ServiceNowDAOConfig {
	
	private String instanceUrl;
	private String username;
	private String password;
	private Date collectionTime;

	public final String getInstanceUrl() {
		return instanceUrl;
	}

	public final void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}

	public final String getUsername() {
		return username;
	}

	public final void setUsername(String username) {
		this.username = username;
	}

	public final String getPassword() {
		return password;
	}

	public final void setPassword(String password) {
		this.password = password;
	}

	public final Date getCollectionTime() {
		return collectionTime;
	}

	public final void setCollectionTime(Date collectionTime) {
		this.collectionTime = collectionTime;
	}

}
