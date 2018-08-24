/**
 * 
 */
package com.emc.ecs.metadata.dao.mqtt;

import java.util.Date;

/**
 * @author nlengc
 *
 */
public class MqttDAOConfig {

	private String host;
	private int port;
	private Date collectionTime;

	public final String getHost() {
		return host;
	}

	public final void setHost(String host) {
		this.host = host;
	}

	public final int getPort() {
		return port;
	}

	public final void setPort(int port) {
		this.port = port;
	}

	public final Date getCollectionTime() {
		return collectionTime;
	}

	public final void setCollectionTime(Date collectionTime) {
		this.collectionTime = collectionTime;
	}

}
