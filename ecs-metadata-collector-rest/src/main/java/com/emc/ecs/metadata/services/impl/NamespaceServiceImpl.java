/**
 * 
 */
package com.emc.ecs.metadata.services.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.emc.ecs.metadata.configuration.EcsConfiguration;
import com.emc.ecs.metadata.configuration.ServiceNowConfiguration;
import com.emc.ecs.metadata.dao.NamespaceDAO;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowDAOConfig;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowNamespaceDAO;
import com.emc.ecs.metadata.rest.bo.NamespaceBO;
import com.emc.ecs.metadata.services.NamespaceService;
import com.emc.ecs.metadata.tasks.NamespaceTask;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
@Service
public class NamespaceServiceImpl implements NamespaceService {

	@Autowired
	private ServiceNowConfiguration serviceNowConfiguration;
	@Autowired
	private EcsConfiguration ecsConfiguration;
	@Autowired
	private TaskExecutor ecsPoolTaskExecutor;

	@Override
	public void postNamespaceDetails(Date collectionTime) {
		NamespaceDAO namespaceDAO = null;
		// Instantiate ServiceNow
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setApi(serviceNowConfiguration.getApi());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		namespaceDAO = new ServiceNowNamespaceDAO(serviceNowDAOConfig);

		// instantiate namespace BO
		NamespaceBO namespaceBO = new NamespaceBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
				ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), namespaceDAO);
		
		NamespaceTask namespaceTask = new NamespaceTask(collectionTime, namespaceBO, TaskType.NamespaceDetails);
		ecsPoolTaskExecutor.execute(namespaceTask);
	}

	@Override
	public void postNamespaceQuotas(Date collectionTime) {
		NamespaceDAO namespaceDAO = null;
		// Instantiate ServiceNow
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setApi(serviceNowConfiguration.getApi());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		namespaceDAO = new ServiceNowNamespaceDAO(serviceNowDAOConfig);

		// instantiate billing BO
		NamespaceBO namespaceBO = new NamespaceBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
				ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), namespaceDAO);

		NamespaceTask namespaceTask = new NamespaceTask(collectionTime, namespaceBO, TaskType.NamespaceQuotas);
		ecsPoolTaskExecutor.execute(namespaceTask);
	}

}
