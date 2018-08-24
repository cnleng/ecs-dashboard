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
import com.emc.ecs.metadata.dao.ObjectDAO;
import com.emc.ecs.metadata.dao.mqtt.MqttDAOConfig;
import com.emc.ecs.metadata.dao.mqtt.MqttS3ObjectDAO;
import com.emc.ecs.metadata.rest.bo.BillingBO;
import com.emc.ecs.metadata.rest.bo.S3ObjectBO;
import com.emc.ecs.metadata.services.S3ObjectService;
import com.emc.ecs.metadata.tasks.S3ObjectTask;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
@Service("mqttS3ObjectService")
public class MqttS3ObjectService implements S3ObjectService {

	@Autowired
	private MqttConfiguration mqttConfiguration;
	@Autowired
	private EcsConfiguration ecsConfiguration;
	@Autowired
	private TaskExecutor ecsPoolTaskExecutor;

	@Override
	public void postObjectData(Date collectionTime) {
		final BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(),
				ecsConfiguration.getEcsMgmtSecretKey(), ecsConfiguration.getEcsHosts(),
				ecsConfiguration.getEcsMgmtPort(), null);

		// Instantiate DAO
		ObjectDAO objectDAO = null;
		// Instantiate mqtt
		final MqttDAOConfig mqttDAOConfig = new MqttDAOConfig();
		mqttDAOConfig.setHost(mqttConfiguration.getHost());
		mqttDAOConfig.setPort(mqttConfiguration.getPort());
		mqttDAOConfig.setCollectionTime(collectionTime);
		try {
			objectDAO = new MqttS3ObjectDAO(mqttDAOConfig);
			S3ObjectBO objectBO = new S3ObjectBO(billingBO, ecsConfiguration.getEcsHosts(), objectDAO);
			S3ObjectTask task = new S3ObjectTask(collectionTime, objectBO, TaskType.S3ObjectsData);
			ecsPoolTaskExecutor.execute(task);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void postObjectVersions(Date collectionTime) {
		final BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(),
				ecsConfiguration.getEcsMgmtSecretKey(), ecsConfiguration.getEcsHosts(),
				ecsConfiguration.getEcsMgmtPort(), null);

		// Instantiate DAO
		ObjectDAO objectDAO = null;
		// Instantiate mqtt
		final MqttDAOConfig mqttDAOConfig = new MqttDAOConfig();
		mqttDAOConfig.setHost(mqttConfiguration.getHost());
		mqttDAOConfig.setPort(mqttConfiguration.getPort());
		mqttDAOConfig.setCollectionTime(collectionTime);
		try {
			objectDAO = new MqttS3ObjectDAO(mqttDAOConfig);
			S3ObjectBO objectBO = new S3ObjectBO(billingBO, ecsConfiguration.getEcsHosts(), objectDAO);
			S3ObjectTask task = new S3ObjectTask(collectionTime, objectBO, TaskType.S3ObjectVersions);
			ecsPoolTaskExecutor.execute(task);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void postObjectModified(Date collectionTime,	Integer numberOfDays) {
		final BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(),
				ecsConfiguration.getEcsMgmtSecretKey(), ecsConfiguration.getEcsHosts(),
				ecsConfiguration.getEcsMgmtPort(), null);

		// Instantiate DAO
		ObjectDAO objectDAO = null;
		// Instantiate mqtt
		final MqttDAOConfig mqttDAOConfig = new MqttDAOConfig();
		mqttDAOConfig.setHost(mqttConfiguration.getHost());
		mqttDAOConfig.setPort(mqttConfiguration.getPort());
		mqttDAOConfig.setCollectionTime(collectionTime);
		try {
			objectDAO = new MqttS3ObjectDAO(mqttDAOConfig);
			S3ObjectBO objectBO = new S3ObjectBO(billingBO, ecsConfiguration.getEcsHosts(), objectDAO);
			S3ObjectTask task = new S3ObjectTask(collectionTime, objectBO, TaskType.S3ObjectsModified, numberOfDays);
			ecsPoolTaskExecutor.execute(task);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void postObjectDataByNamespace(Date collectionTime, String namespace) {
		final BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(),
				ecsConfiguration.getEcsMgmtSecretKey(), ecsConfiguration.getEcsHosts(),
				ecsConfiguration.getEcsMgmtPort(), null);

		// Instantiate DAO
		ObjectDAO objectDAO = null;
		// Instantiate mqtt
		final MqttDAOConfig mqttDAOConfig = new MqttDAOConfig();
		mqttDAOConfig.setHost(mqttConfiguration.getHost());
		mqttDAOConfig.setPort(mqttConfiguration.getPort());
		mqttDAOConfig.setCollectionTime(collectionTime);
		
		try {
			objectDAO = new MqttS3ObjectDAO(mqttDAOConfig);
			S3ObjectBO objectBO = new S3ObjectBO(billingBO, ecsConfiguration.getEcsHosts(), objectDAO, namespace);
			S3ObjectTask task = new S3ObjectTask(collectionTime, objectBO, TaskType.S3ObjectsDataByNamespace);
			ecsPoolTaskExecutor.execute(task);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void postObjectDataByBucket(Date collectionTime, String namespace, String bucketname) {
		final BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(),
				ecsConfiguration.getEcsMgmtSecretKey(), ecsConfiguration.getEcsHosts(),
				ecsConfiguration.getEcsMgmtPort(), null);

		// Instantiate DAO
		ObjectDAO objectDAO = null;
		// Instantiate mqtt
		final MqttDAOConfig mqttDAOConfig = new MqttDAOConfig();
		mqttDAOConfig.setHost(mqttConfiguration.getHost());
		mqttDAOConfig.setPort(mqttConfiguration.getPort());
		mqttDAOConfig.setCollectionTime(collectionTime);
		
		try {
			objectDAO = new MqttS3ObjectDAO(mqttDAOConfig);
			S3ObjectBO objectBO = new S3ObjectBO(billingBO, ecsConfiguration.getEcsHosts(), objectDAO, namespace, bucketname);
			S3ObjectTask task = new S3ObjectTask(collectionTime, objectBO, TaskType.S3ObjectsDataByBucket);
			ecsPoolTaskExecutor.execute(task);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
