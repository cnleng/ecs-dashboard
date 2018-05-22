/**
 * 
 */
package com.emc.ecs.metadata.services.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emc.eccs.metadata.configuration.ECSConfiguration;
import com.emc.eccs.metadata.configuration.ServiceNowConfiguration;
import com.emc.ecs.management.entity.NamespaceDetail;
import com.emc.ecs.management.entity.NamespaceQuota;
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
	private ServiceNowConfiguration servicenowConfiguration;
	@Autowired
	private ECSConfiguration ecsConfiguration;

	@Override
	public List<NamespaceDetail> getNamespaceDetails(Date collectionTime) {
		NamespaceDAO namespaceDAO = null;
		// Instantiate ServiceNow
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(servicenowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setUsername(servicenowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(servicenowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		namespaceDAO = new ServiceNowNamespaceDAO(serviceNowDAOConfig);

		// instantiate billing BO
		NamespaceBO namespaceBO = new NamespaceBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
				ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), namespaceDAO, objectCount);
//
//		// Start collection
		namespaceBO.collectNamespaceDetails(collectionTime);
		namespaceBO.shutdown();
		return null;
	}

	@Override
	public List<NamespaceQuota> getNamespaceQuotas(Date collectionTime) {
		// TODO Auto-generated method stub
		return null;
	}

}
