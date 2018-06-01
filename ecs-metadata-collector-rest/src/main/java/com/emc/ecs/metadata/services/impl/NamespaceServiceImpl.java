/**
 * 
 */
package com.emc.ecs.metadata.services.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emc.ecs.management.entity.NamespaceDetail;
import com.emc.ecs.management.entity.NamespaceQuota;
import com.emc.ecs.metadata.configuration.EcsConfiguration;
import com.emc.ecs.metadata.configuration.ServiceNowConfiguration;
import com.emc.ecs.metadata.dao.NamespaceDAO;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowDAOConfig;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowNamespaceDAO;
import com.emc.ecs.metadata.rest.bo.NamespaceBO;
import com.emc.ecs.metadata.services.NamespaceService;

/**
 * @author nlengc
 *
 */
@Service
public class NamespaceServiceImpl implements NamespaceService {

	private static AtomicLong objectCount = new AtomicLong(0L);
	@Autowired
	private ServiceNowConfiguration serviceNowConfiguration;
	@Autowired
	private EcsConfiguration ecsConfiguration;

	@Override
	public List<NamespaceDetail> getNamespaceDetails(Date collectionTime) {
		NamespaceDAO namespaceDAO = null;
		// Instantiate ServiceNow
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		namespaceDAO = new ServiceNowNamespaceDAO(serviceNowDAOConfig);

		// instantiate namespace BO
		NamespaceBO namespaceBO = new NamespaceBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
				ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), namespaceDAO, objectCount);

		// Start collection
		List<NamespaceDetail> namespaceDetails = namespaceBO.collectNamespaceDetails(collectionTime);
		namespaceBO.shutdown();
		return namespaceDetails;
	}

	@Override
	public List<NamespaceQuota> getNamespaceQuotas(Date collectionTime) {
		NamespaceDAO namespaceDAO = null;
		// Instantiate ServiceNow
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		namespaceDAO = new ServiceNowNamespaceDAO(serviceNowDAOConfig);

		// instantiate billing BO
		NamespaceBO namespaceBO = new NamespaceBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
				ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), namespaceDAO, objectCount);

		// Start collection
		List<NamespaceQuota> namespaceQuotas = namespaceBO.collectNamespaceQuota(collectionTime);
		namespaceBO.shutdown();
		return namespaceQuotas;
	}

}
