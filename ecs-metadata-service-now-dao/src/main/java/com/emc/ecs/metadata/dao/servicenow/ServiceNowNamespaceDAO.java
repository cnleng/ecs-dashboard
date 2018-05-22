/**
 * 
 */
package com.emc.ecs.metadata.dao.servicenow;

import java.util.Date;

import org.apache.log4j.Logger;

import com.emc.ecs.management.entity.NamespaceDetail;
import com.emc.ecs.management.entity.NamespaceQuota;
import com.emc.ecs.metadata.dao.NamespaceDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author nlengc
 *
 */
public class ServiceNowNamespaceDAO extends ServiceNowDAO implements NamespaceDAO {

	private static final Logger LOG = Logger.getLogger(ServiceNowNamespaceDAO.class);
	private static final String ECS_NAMESPACE_DETAILS = "/ecs_namespace_details";
	private static final String ECS_NAMESPACE_QUOTA = "/ecs_namespace_quota";

	public ServiceNowNamespaceDAO(ServiceNowDAOConfig config) {
		super(config);
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.NamespaceDAO#initIndexes(java.util.Date)
	 */
	@Override
	public void initIndexes(Date collectionTime) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.NamespaceDAO#insert(com.emc.ecs.management.entity.NamespaceDetail, java.util.Date)
	 */
	@Override
	public void insert(NamespaceDetail namespaceDetail, Date collectionTime) {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final String json = mapper.writeValueAsString(namespaceDetail);
			this.postData(this.url + ECS_NAMESPACE_DETAILS, json);
		} catch (JsonProcessingException e) {
			LOG.error("An error occured while parsing Json for namespace details ",e);
		}
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.NamespaceDAO#insert(com.emc.ecs.management.entity.NamespaceQuota, java.util.Date)
	 */
	@Override
	public void insert(NamespaceQuota namespacequota, Date collectionTime) {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final String json = mapper.writeValueAsString(namespacequota);
			this.postData(this.url + ECS_NAMESPACE_QUOTA, json);
		} catch (JsonProcessingException e) {
			LOG.error("An error occured while parsing Json for namespace quota ",e);
		}
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.NamespaceDAO#purgeOldData(com.emc.ecs.metadata.dao.NamespaceDAO.NamespaceDataType, java.util.Date)
	 */
	@Override
	public Long purgeOldData(NamespaceDataType type, Date thresholdDate) {
		return null;
	}

}
