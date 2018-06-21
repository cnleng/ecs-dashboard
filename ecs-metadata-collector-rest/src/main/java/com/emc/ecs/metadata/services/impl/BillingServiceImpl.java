/**
 * 
 */
package com.emc.ecs.metadata.services.impl;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.emc.ecs.metadata.configuration.EcsConfiguration;
import com.emc.ecs.metadata.configuration.ServiceNowConfiguration;
import com.emc.ecs.metadata.dao.BillingDAO;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowBillingDAO;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowDAOConfig;
import com.emc.ecs.metadata.rest.bo.BillingBO;
import com.emc.ecs.metadata.services.BillingService;
import com.emc.ecs.metadata.tasks.BillingTask;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
@Service
public class BillingServiceImpl implements BillingService {
	
	private static AtomicLong objectCount = new AtomicLong(0L);
	@Autowired
	private ServiceNowConfiguration serviceNowConfiguration;
	@Autowired
	private EcsConfiguration ecsConfiguration;
	@Autowired
	private TaskExecutor ecsPoolTaskExecutor;

	@Override
	public void postNamespaceBillingInfo(Date collectionTime) {
		BillingDAO billingDAO = null;
		// Instantiate ServiceNow
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setApi(serviceNowConfiguration.getApi());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		billingDAO = new ServiceNowBillingDAO(serviceNowDAOConfig);

		// instantiate billing BO
		BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
				ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), billingDAO, objectCount);

		BillingTask billingTask = new BillingTask(collectionTime, billingBO, TaskType.NamespaceBillingInfos);
		ecsPoolTaskExecutor.execute(billingTask);
	}

	@Override
	public void postObjectBuckets(Date collectionTime) {
		BillingDAO billingDAO = null;
		// Instantiate ServiceNow
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setApi(serviceNowConfiguration.getApi());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		billingDAO = new ServiceNowBillingDAO(serviceNowDAOConfig);

		// instantiate billing BO
		BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
				ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), billingDAO, objectCount);

		// Start collection
		BillingTask billingTask = new BillingTask(collectionTime, billingBO, TaskType.ObjectBuckets);
		ecsPoolTaskExecutor.execute(billingTask);
	}

}