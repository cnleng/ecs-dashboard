/**
 * 
 */
package com.emc.ecs.metadata.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author nlengc
 *
 */
@Configuration
@ConfigurationProperties(prefix = "ecsConfiguration")
public class EcsConfiguration {
	
	private static final Integer DEFAULT_ECS_MGMT_PORT = 4443;
	
	private String ecsMgmtAccessKey;
	private String ecsMgmtSecretKey;
	private Integer ecsMgmtPort;
	private String ecsHosts;
//	@Value("#{'${ecsConfiguration.hosts}'.split(',')}")
	//private List<String> ecsHosts;

	/**
	 * 
	 */
	public EcsConfiguration() {
	}

	public final List<String> getEcsHosts() {
		return Arrays.asList(ecsHosts.split(","));
	}

	public final void setEcsHosts(String ecsHosts) {
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
		return (ecsMgmtPort != null) ? ecsMgmtPort : DEFAULT_ECS_MGMT_PORT;
	}

	public final void setEcsMgmtPort(Integer ecsMgmtPort) {
		this.ecsMgmtPort = ecsMgmtPort;
	}

	public static final Integer getDefaultEcsMgmtPort() {
		return DEFAULT_ECS_MGMT_PORT;
	}
	
//	@Bean
//	public ECSConfiguration ecsConfiguration() {
//		ECSConfiguration configuration = new ECSConfiguration();
//		configuration.setEcsHosts(this.getEcsHosts());
//		configuration.setEcsMgmtAccessKey(this.getEcsMgmtAccessKey());
//		configuration.setEcsMgmtSecretKey(this.getEcsMgmtSecretKey());
//		configuration.setEcsMgmtPort(getEcsMgmtPort());
//		return configuration;
//	}

}
