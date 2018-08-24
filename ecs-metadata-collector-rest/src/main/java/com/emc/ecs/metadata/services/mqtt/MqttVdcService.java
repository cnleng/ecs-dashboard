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
import com.emc.ecs.metadata.dao.VdcDAO;
import com.emc.ecs.metadata.dao.mqtt.MqttDAOConfig;
import com.emc.ecs.metadata.dao.mqtt.MqttVdcDAO;
import com.emc.ecs.metadata.rest.bo.VdcBO;
import com.emc.ecs.metadata.services.VdcService;
import com.emc.ecs.metadata.tasks.VdcTask;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
@Service("mqttVdcService")
public class MqttVdcService implements VdcService {
	
	@Autowired
	private MqttConfiguration mqttConfiguration;
	@Autowired
	private EcsConfiguration ecsConfiguration;
	@Autowired
	private TaskExecutor ecsPoolTaskExecutor;

	@Override
	public void postVdcDetails(Date collectionTime) {
		VdcDAO vdcDAO = null;
		// Instantiate mqtt
		final MqttDAOConfig mqttDAOConfig = new MqttDAOConfig();
		mqttDAOConfig.setHost(mqttConfiguration.getHost());
		mqttDAOConfig.setPort(mqttConfiguration.getPort());
		mqttDAOConfig.setCollectionTime(collectionTime);
		
		try {
			vdcDAO = new MqttVdcDAO(mqttDAOConfig);
			// instantiate BO
			VdcBO vdcBO = new VdcBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
					ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), vdcDAO);
			// Start collection
			VdcTask vdcTask = new VdcTask(collectionTime, vdcBO, TaskType.VdcDetails);
			ecsPoolTaskExecutor.execute(vdcTask);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void postBucketOwners(Date collectionTime) {
		VdcDAO vdcDAO = null;
		// Instantiate mqtt
		final MqttDAOConfig mqttDAOConfig = new MqttDAOConfig();
		mqttDAOConfig.setHost(mqttConfiguration.getHost());
		mqttDAOConfig.setPort(mqttConfiguration.getPort());
		mqttDAOConfig.setCollectionTime(collectionTime);
		
		try {
			vdcDAO = new MqttVdcDAO(mqttDAOConfig);
			// instantiate BO
			VdcBO vdcBO = new VdcBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
					ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), ecsConfiguration.getEcsAlternativePort(), vdcDAO);

			// Start collection
			VdcTask vdcTask = new VdcTask(collectionTime, vdcBO, TaskType.BucketOwners);
			ecsPoolTaskExecutor.execute(vdcTask);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
