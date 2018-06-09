/**
 * 
 */
package com.emc.ecs.metadata.dao.servicenow;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
		try {
			final String json = this.convertDetailsObjectToJson(namespaceDetail);
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
		try {
			final String json = this.convertQuotaObjectToJson(namespacequota);
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
	
	/**
	 * 
	 * @param quota
	 * @return
	 * @throws JsonProcessingException
	 */
	private String convertQuotaObjectToJson(NamespaceQuota quota) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("namespace", quota.getNamespace());
		jsonMap.put("blocksize", quota.getBlockSize());
		jsonMap.put("notificationsize", quota.getNotificationSize());
		return mapper.writeValueAsString(jsonMap);
	}

	/**
	 * 
	 * @param namespaceDetail
	 * @return
	 * @throws JsonProcessingException
	 */
	private String convertDetailsObjectToJson(NamespaceDetail namespaceDetail) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("id", namespaceDetail.getId());
		jsonMap.put("link", namespaceDetail.getLink());
		jsonMap.put("namespaceadmins", namespaceDetail.getNamespaceAdmins());
		jsonMap.put("defaultdataservicesvpool", namespaceDetail.getDefaultDataServicesVPool());
		jsonMap.put("allowedvpools", namespaceDetail.getAllowedVPools());
		jsonMap.put("disallowedVPools", namespaceDetail.getDisallowedVPools());
		jsonMap.put("isencryptionenabled", namespaceDetail.getIsEncryptionEnabled());
		jsonMap.put("isstaledallowed", namespaceDetail.getIsStaledAllowed());
		jsonMap.put("iscomplianceenabled", namespaceDetail.getIsComplianceEnabled());
		jsonMap.put("global", namespaceDetail.getGlobal());
		jsonMap.put("inactive", namespaceDetail.getInactive());
		jsonMap.put("remote", namespaceDetail.getRemote());
		jsonMap.put("internal", namespaceDetail.getInternal());
		jsonMap.put("vdc", namespaceDetail.getVdc());
		jsonMap.put("userMappings", namespaceDetail.getUserMappings());
		jsonMap.put("creationtime", namespaceDetail.getCreationTime());
		jsonMap.put("defaultbucketblocksize", namespaceDetail.getDefaultBucketBlockSize());
		jsonMap.put("externalgroupadmins", namespaceDetail.getExternalGroupAdmins());
		return mapper.writeValueAsString(jsonMap);
	}
}
