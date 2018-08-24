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
@ConfigurationProperties(prefix = "mqttConfiguration")
public class MqttConfiguration {

	private String host;
	private int port;

	public MqttConfiguration() {
	}

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

}
