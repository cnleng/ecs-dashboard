/**
 * 
 */
package com.emc.ecs.metadata.dao.servicenow;

import java.util.Date;

import org.apache.log4j.Logger;

import com.emc.ecs.management.entity.NamespaceBillingInfo;
import com.emc.ecs.management.entity.ObjectBuckets;
import com.emc.ecs.metadata.dao.BillingDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author nlengc
 *
 */
public class ServiceNowBillingDAO extends ServiceNowDAO implements BillingDAO {
	
	private static final Logger LOG = Logger.getLogger(ServiceNowBillingDAO.class);
	private static final String ECS_NAMESPACE_BILLING_INFOS = "/ecs_namespace_billing_infos";
	private static final String ECS_OBJECT_BUCKETS = "/ecs_object_buckets";

	public ServiceNowBillingDAO(ServiceNowDAOConfig config) {
		super(config);
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.BillingDAO#initIndexes(java.util.Date)
	 */
	@Override
	public void initIndexes(Date collectionTime) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.BillingDAO#insert(com.emc.ecs.management.entity.NamespaceBillingInfo, java.util.Date)
	 */
	@Override
	public void insert(NamespaceBillingInfo billingData, Date collectionTime) {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final String json = mapper.writeValueAsString(billingData);
			this.postData(this.url + ECS_NAMESPACE_BILLING_INFOS, json);
		} catch (JsonProcessingException e) {
			LOG.error("An error occured while parsing Json for namespace billing infos ", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.BillingDAO#insert(com.emc.ecs.management.entity.ObjectBuckets, java.util.Date)
	 */
	@Override
	public void insert(ObjectBuckets bucketResponse, Date collectionTime) {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final String json = mapper.writeValueAsString(bucketResponse);
			this.postData(this.url + ECS_NAMESPACE_BILLING_INFOS, json);
		} catch (JsonProcessingException e) {
			LOG.error("An error occured while parsing Json for Object Buckets ", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.BillingDAO#purgeOldData(com.emc.ecs.metadata.dao.BillingDAO.ManagementDataType, java.util.Date)
	 */
	@Override
	public Long purgeOldData(ManagementDataType type, Date collectionTime) {
		// TODO Auto-generated method stub
		return null;
	}

}
