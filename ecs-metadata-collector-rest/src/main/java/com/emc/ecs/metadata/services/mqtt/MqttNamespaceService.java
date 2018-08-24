/**
 * 
 */
package com.emc.ecs.metadata.services.mqtt;

import java.net.URISyntaxException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.emc.ecs.metadata.configuration.EcsConfiguration;
import com.emc.ecs.metadata.configuration.MqttConfiguration;
import com.emc.ecs.metadata.dao.NamespaceDAO;
import com.emc.ecs.metadata.dao.mqtt.MqttDAOConfig;
import com.emc.ecs.metadata.dao.mqtt.MqttNamespaceDAO;
import com.emc.ecs.metadata.rest.bo.NamespaceBO;
import com.emc.ecs.metadata.services.NamespaceService;
import com.emc.ecs.metadata.tasks.NamespaceTask;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
@Service("mqttNamespaceService")
public class MqttNamespaceService implements NamespaceService {

	@Autowired
	private MqttConfiguration mqttConfiguration;
	@Autowired
	private EcsConfiguration ecsConfiguration;
	@Autowired
	private TaskExecutor ecsPoolTaskExecutor;

	@Override
	public void postNamespaceDetails(Date collectionTime) {
		NamespaceDAO namespaceDAO = null;
		// Instantiate mqtt
		final MqttDAOConfig mqttDAOConfig = new MqttDAOConfig();
		mqttDAOConfig.setHost(mqttConfiguration.getHost());
		mqttDAOConfig.setPort(mqttConfiguration.getPort());
		mqttDAOConfig.setCollectionTime(collectionTime);
		
		try {
			namespaceDAO = new MqttNamespaceDAO(mqttDAOConfig);
			// instantiate namespace BO
			NamespaceBO namespaceBO = new NamespaceBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
					ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), namespaceDAO);
			
			NamespaceTask namespaceTask = new NamespaceTask(collectionTime, namespaceBO, TaskType.NamespaceDetails);
			ecsPoolTaskExecutor.execute(namespaceTask);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void postNamespaceQuotas(Date collectionTime) {
		NamespaceDAO namespaceDAO = null;
		// Instantiate mqtt
		final MqttDAOConfig mqttDAOConfig = new MqttDAOConfig();
		mqttDAOConfig.setHost(mqttConfiguration.getHost());
		mqttDAOConfig.setPort(mqttConfiguration.getPort());
		mqttDAOConfig.setCollectionTime(collectionTime);
		
		try {
			namespaceDAO = new MqttNamespaceDAO(mqttDAOConfig);
			// instantiate billing BO
			NamespaceBO namespaceBO = new NamespaceBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
					ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), namespaceDAO);

			NamespaceTask namespaceTask = new NamespaceTask(collectionTime, namespaceBO, TaskType.NamespaceQuotas);
			ecsPoolTaskExecutor.execute(namespaceTask);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

}
