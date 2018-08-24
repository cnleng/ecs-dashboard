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
import com.emc.ecs.metadata.dao.BillingDAO;
import com.emc.ecs.metadata.dao.mqtt.MqttBillingDAO;
import com.emc.ecs.metadata.dao.mqtt.MqttDAOConfig;
import com.emc.ecs.metadata.rest.bo.BillingBO;
import com.emc.ecs.metadata.services.BillingService;
import com.emc.ecs.metadata.tasks.BillingTask;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
@Service("mqttBillingService")
public class MqttBillingService implements BillingService {
	
	@Autowired
	private MqttConfiguration mqttConfiguration;
	@Autowired
	private EcsConfiguration ecsConfiguration;
	@Autowired
	private TaskExecutor ecsPoolTaskExecutor;

	@Override
	public void postNamespaceBillingInfo(Date collectionTime) {
		BillingDAO billingDAO = null;
		// Instantiate ServiceNow
		final MqttDAOConfig mqttDAOConfig = new MqttDAOConfig();
		mqttDAOConfig.setHost(mqttConfiguration.getHost());
		mqttDAOConfig.setPort(mqttConfiguration.getPort());
		mqttDAOConfig.setCollectionTime(collectionTime);
		try {
			billingDAO = new MqttBillingDAO(mqttDAOConfig);
			// instantiate billing BO
			BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
					ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), billingDAO);
			BillingTask billingTask = new BillingTask(collectionTime, billingBO, TaskType.NamespaceBillingInfos);
			ecsPoolTaskExecutor.execute(billingTask);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void postObjectBuckets(Date collectionTime) {
		BillingDAO billingDAO = null;
		// Instantiate ServiceNow
		final MqttDAOConfig mqttDAOConfig = new MqttDAOConfig();
		mqttDAOConfig.setHost(mqttConfiguration.getHost());
		mqttDAOConfig.setPort(mqttConfiguration.getPort());
		mqttDAOConfig.setCollectionTime(collectionTime);
		try {
			billingDAO = new MqttBillingDAO(mqttDAOConfig);
			// instantiate billing BO
			BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
					ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), billingDAO);
			BillingTask billingTask = new BillingTask(collectionTime, billingBO, TaskType.ObjectBuckets);
			ecsPoolTaskExecutor.execute(billingTask);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

}