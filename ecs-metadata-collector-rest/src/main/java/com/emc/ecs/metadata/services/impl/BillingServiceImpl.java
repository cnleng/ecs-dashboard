/**
 * 
 */
package com.emc.ecs.metadata.services.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emc.ecs.management.entity.NamespaceBillingInfo;
import com.emc.ecs.management.entity.ObjectBuckets;
import com.emc.ecs.metadata.configuration.EcsConfiguration;
import com.emc.ecs.metadata.configuration.ServiceNowConfiguration;
import com.emc.ecs.metadata.dao.BillingDAO;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowBillingDAO;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowDAOConfig;
import com.emc.ecs.metadata.rest.bo.BillingBO;
import com.emc.ecs.metadata.services.BillingService;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.ecs.metadata.services.BillingService#getNamespaceBillingInfo(java
	 * .util.Date)
	 */
	@Override
	public List<NamespaceBillingInfo> getNamespaceBillingInfo(Date collectionTime) {
		BillingDAO billingDAO = null;
		// Instantiate ServiceNow
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		billingDAO = new ServiceNowBillingDAO(serviceNowDAOConfig);

		// instantiate billing BO
		BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
				ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), billingDAO, objectCount);

		// Start collection
		List<NamespaceBillingInfo> namespaceBillingInfos = billingBO.collectBillingData(collectionTime); 
		billingBO.shutdown();
		return namespaceBillingInfos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.ecs.metadata.services.BillingService#getObjectBuckets(java.util.
	 * Date)
	 */
	@Override
	public List<ObjectBuckets> getObjectBuckets(Date collectionTime) {
		BillingDAO billingDAO = null;
		// Instantiate ServiceNow
		final ServiceNowDAOConfig serviceNowDAOConfig = new ServiceNowDAOConfig();
		serviceNowDAOConfig.setInstanceUrl(serviceNowConfiguration.getInstanceUrl());
		serviceNowDAOConfig.setUsername(serviceNowConfiguration.getUsername());
		serviceNowDAOConfig.setPassword(serviceNowConfiguration.getPassword());
		serviceNowDAOConfig.setCollectionTime(collectionTime);
		billingDAO = new ServiceNowBillingDAO(serviceNowDAOConfig);

		// instantiate billing BO
		BillingBO billingBO = new BillingBO(ecsConfiguration.getEcsMgmtAccessKey(), ecsConfiguration.getEcsMgmtSecretKey(),
				ecsConfiguration.getEcsHosts(), ecsConfiguration.getEcsMgmtPort(), billingDAO, objectCount);

		// Start collection
		List<ObjectBuckets> objectBuckets = billingBO.collectObjectBuckets(collectionTime);
		billingBO.shutdown();
		return objectBuckets;
	}

}