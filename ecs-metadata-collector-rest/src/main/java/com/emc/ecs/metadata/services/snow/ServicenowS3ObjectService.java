/**
 * 
 */
package com.emc.ecs.metadata.services.snow;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.emc.ecs.metadata.configuration.EcsConfiguration;
import com.emc.ecs.metadata.configuration.ServiceNowConfiguration;
import com.emc.ecs.metadata.dao.ObjectDAO;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowDAOConfig;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowS3ObjectDAO;
import com.emc.ecs.metadata.rest.bo.BillingBO;
import com.emc.ecs.metadata.rest.bo.S3ObjectBO;
import com.emc.ecs.metadata.services.S3ObjectService;
import com.emc.ecs.metadata.tasks.S3ObjectTask;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
@Service("servicenowS3ObjectService")
public class ServicenowS3ObjectService implements S3ObjectService {

	@Autowired
	private TaskExecutor ecsPoolTaskExecutor;
	@Autowired
	private ServiceNowConfiguration serviceNowConfiguration;
	@Autowired
	private EcsConfiguration ecsConfiguration;

	@Override
	public void postObjectData(Date collectionTime) {
		final BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(),
				ecsConfiguration.getEcsMgmtSecretKey(), ecsConfiguration.getEcsHosts(),
				ecsConfiguration.getEcsMgmtPort(), null);

		// Instantiate DAO
		ObjectDAO objectDAO = null;
		// Instantiate ServiceNow DAO
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setApi(serviceNowConfiguration.getApi());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		objectDAO = new ServiceNowS3ObjectDAO(serviceNowDAOConfig);

		S3ObjectBO objectBO = new S3ObjectBO(billingBO, ecsConfiguration.getEcsHosts(), objectDAO);
		S3ObjectTask task = new S3ObjectTask(collectionTime, objectBO, TaskType.S3ObjectsData);
		ecsPoolTaskExecutor.execute(task);
		
	}

	@Override
	public void postObjectVersions(Date collectionTime) {
		final BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(),
				ecsConfiguration.getEcsMgmtSecretKey(), ecsConfiguration.getEcsHosts(),
				ecsConfiguration.getEcsMgmtPort(), null);

		// Instantiate DAO
		ObjectDAO objectDAO = null;
		// Instantiate ServiceNow DAO
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setApi(serviceNowConfiguration.getApi());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		objectDAO = new ServiceNowS3ObjectDAO(serviceNowDAOConfig);

		S3ObjectBO objectBO = new S3ObjectBO(billingBO, ecsConfiguration.getEcsHosts(), objectDAO);
		S3ObjectTask task = new S3ObjectTask(collectionTime, objectBO, TaskType.S3ObjectVersions);
		ecsPoolTaskExecutor.execute(task);
	}

	@Override
	public void postObjectModified(Date collectionTime,	Integer numberOfDays) {
		final BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(),
				ecsConfiguration.getEcsMgmtSecretKey(), ecsConfiguration.getEcsHosts(),
				ecsConfiguration.getEcsMgmtPort(), null);

		// Instantiate DAO
		ObjectDAO objectDAO = null;
		// Instantiate ServiceNow DAO
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setApi(serviceNowConfiguration.getApi());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		objectDAO = new ServiceNowS3ObjectDAO(serviceNowDAOConfig);

		S3ObjectBO objectBO = new S3ObjectBO(billingBO, ecsConfiguration.getEcsHosts(), objectDAO);
		S3ObjectTask task = new S3ObjectTask(collectionTime, objectBO, TaskType.S3ObjectsModified, numberOfDays);
		ecsPoolTaskExecutor.execute(task);
		
	}

	@Override
	public void postObjectDataByNamespace(Date collectionTime, String namespace) {
		final BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(),
				ecsConfiguration.getEcsMgmtSecretKey(), ecsConfiguration.getEcsHosts(),
				ecsConfiguration.getEcsMgmtPort(), null);

		// Instantiate DAO
		ObjectDAO objectDAO = null;
		// Instantiate ServiceNow DAO
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setApi(serviceNowConfiguration.getApi());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		objectDAO = new ServiceNowS3ObjectDAO(serviceNowDAOConfig);
		S3ObjectBO objectBO = new S3ObjectBO(billingBO, ecsConfiguration.getEcsHosts(), objectDAO, namespace);
		S3ObjectTask task = new S3ObjectTask(collectionTime, objectBO, TaskType.S3ObjectsDataByNamespace);
		ecsPoolTaskExecutor.execute(task);
	}

	@Override
	public void postObjectDataByBucket(Date collectionTime, String namespace, String bucketname) {
		final BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(),
				ecsConfiguration.getEcsMgmtSecretKey(), ecsConfiguration.getEcsHosts(),
				ecsConfiguration.getEcsMgmtPort(), null);

		// Instantiate DAO
		ObjectDAO objectDAO = null;
		// Instantiate ServiceNow DAO
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setApi(serviceNowConfiguration.getApi());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		objectDAO = new ServiceNowS3ObjectDAO(serviceNowDAOConfig);

		S3ObjectBO objectBO = new S3ObjectBO(billingBO, ecsConfiguration.getEcsHosts(), objectDAO, namespace, bucketname);
		S3ObjectTask task = new S3ObjectTask(collectionTime, objectBO, TaskType.S3ObjectsDataByBucket);
		ecsPoolTaskExecutor.execute(task);
	}
	
}
