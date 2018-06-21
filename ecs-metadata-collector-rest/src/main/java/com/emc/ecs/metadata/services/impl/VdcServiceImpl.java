/**
 * 
 */
package com.emc.ecs.metadata.services.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.emc.ecs.management.entity.BucketOwner;
import com.emc.ecs.management.entity.VdcDetails;
import com.emc.ecs.metadata.configuration.EcsConfiguration;
import com.emc.ecs.metadata.configuration.ServiceNowConfiguration;
import com.emc.ecs.metadata.dao.VdcDAO;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowDAOConfig;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowVdcDAO;
import com.emc.ecs.metadata.rest.bo.VdcBO;
import com.emc.ecs.metadata.services.VdcService;
import com.emc.ecs.metadata.tasks.VdcTask;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
@Service
public class VdcServiceImpl implements VdcService {
	
	private static AtomicLong objectCount = new AtomicLong(0L);
	@Autowired
	private ServiceNowConfiguration serviceNowConfiguration;
	@Autowired
	private EcsConfiguration ecsConfiguration;
	@Autowired
	private TaskExecutor ecsPoolTaskExecutor;

	@Override
	public void postVdcDetails(Date collectionTime) {
		VdcDAO vdcDAO = null;
		// Instantiate ServiceNow
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setApi(serviceNowConfiguration.getApi());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		vdcDAO = new ServiceNowVdcDAO(serviceNowDAOConfig);

		// instantiate BO
		VdcBO vdcBO = new VdcBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
				ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), vdcDAO, objectCount);

		// Start collection
		VdcTask vdcTask = new VdcTask(collectionTime, vdcBO, TaskType.VdcDetails);
		ecsPoolTaskExecutor.execute(vdcTask);
	}

	@Override
	public void postBucketOwners(Date collectionTime) {
		VdcDAO vdcDAO = null;
		// Instantiate ServiceNow
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setApi(serviceNowConfiguration.getApi());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		vdcDAO = new ServiceNowVdcDAO(serviceNowDAOConfig);

		// instantiate BO
		VdcBO vdcBO = new VdcBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
				ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), ecsConfiguration.getEcsAlternativePort(), vdcDAO, objectCount);

		// Start collection
		VdcTask vdcTask = new VdcTask(collectionTime, vdcBO, TaskType.BucketOwners);
		ecsPoolTaskExecutor.execute(vdcTask);
	}

}
