/**
 * 
 */
package com.emc.eccs.metadata.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author nlengc
 *
 */
@Configuration
@ConfigurationProperties(prefix = "ecsConfiguration")
public class ECSConfiguration {
	
	private static final Integer DEFAULT_ECS_MGMT_PORT = 4443;
	
	private String ecsMgmtAccessKey;
	private String ecsMgmtSecretKey;
	private Integer ecsMgmtPort;
	private List<String> ecsHosts = new ArrayList<>();

	/**
	 * 
	 */
	public ECSConfiguration() {
	}

	public final List<String> getEcsHosts() {
		return ecsHosts;
	}

	public final void setEcsHosts(List<String> ecsHosts) {
		this.ecsHosts = ecsHosts;
	}

	public final String getEcsMgmtAccessKey() {
		return ecsMgmtAccessKey;
	}

	public final void setEcsMgmtAccessKey(String ecsMgmtAccessKey) {
		this.ecsMgmtAccessKey = ecsMgmtAccessKey;
	}

	public final String getEcsMgmtSecretKey() {
		return ecsMgmtSecretKey;
	}

	public final void setEcsMgmtSecretKey(String ecsMgmtSecretKey) {
		this.ecsMgmtSecretKey = ecsMgmtSecretKey;
	}

	public final Integer getEcsMgmtPort() {
		return ecsMgmtPort;
	}

	public final void setEcsMgmtPort(Integer ecsMgmtPort) {
		this.ecsMgmtPort = ecsMgmtPort;
	}

	public static final Integer getDefaultEcsMgmtPort() {
		return DEFAULT_ECS_MGMT_PORT;
	}

}
