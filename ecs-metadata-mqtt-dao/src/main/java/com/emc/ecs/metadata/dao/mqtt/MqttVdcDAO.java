/**
 * 
 */
package com.emc.ecs.metadata.dao.mqtt;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.emc.ecs.management.entity.BucketOwner;
import com.emc.ecs.management.entity.VdcDetails;
import com.emc.ecs.metadata.dao.VdcDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Vdc.VdcDetail;

/**
 * @author nlengc
 *
 */
public class MqttVdcDAO extends MqttDAO implements VdcDAO {
	private static final Logger LOG = Logger.getLogger(MqttVdcDAO.class);
	private static final String ECS_VDC_DETAILS = "ecs_vdc_details";
	private static final String ECS_BUCKET_OWNERS = "ecs_bucket_owners";

	/**
	 * @param config
	 */
	public MqttVdcDAO(MqttDAOConfig config) throws URISyntaxException {
		super(config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.emc.ecs.metadata.dao.VdcDAO#initIndexes(java.util.Date)
	 */
	@Override
	public void initIndexes(Date collectionTime) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.ecs.metadata.dao.VdcDAO#insert(com.emc.ecs.management.entity.
	 * VdcDetails, java.util.Date)
	 */
	@Override
	public void insert(VdcDetails vdcDetails, Date collectionTime) {
		try {
			final String json = this.convertVdcDEtailsToJson(vdcDetails, collectionTime);
			this.postData(ECS_VDC_DETAILS, json);
		} catch (JsonProcessingException e) {
			LOG.error("An error occured while parsing Json for vdc details ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.emc.ecs.metadata.dao.VdcDAO#insert(java.util.List,
	 * java.util.Date)
	 */
	@Override
	public void insert(List<BucketOwner> bucketOwners, Date collectionTime) {
		try {
			final String json = this.convertBucketOwnerToJson(bucketOwners, collectionTime);
			this.postData(ECS_BUCKET_OWNERS, json);
		} catch (JsonProcessingException e) {
			LOG.error("An error occured while parsing Json for bucket owners ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.ecs.metadata.dao.VdcDAO#purgeOldData(com.emc.ecs.metadata.dao.
	 * VdcDAO.VdcDataType, java.util.Date)
	 */
	@Override
	public Long purgeOldData(VdcDataType type, Date thresholdDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param bucketOwners
	 * @return
	 * @throws JsonProcessingException
	 */
	private String convertBucketOwnerToJson(List<BucketOwner> bucketOwners, Date collectionTime) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final List<Map<String, Object>> jsonMap = new ArrayList<>();
		if (bucketOwners != null && !bucketOwners.isEmpty()) {
			for (BucketOwner bucketOwner : bucketOwners) {
				final Map<String, Object> map = new HashMap<>();
				map.put("vdcid", bucketOwner.getVdcId());
				map.put("bucketkey", bucketOwner.getBucketKey());
				map.put("collectiontime", collectionTime);
				jsonMap.add(map);
			}
		}
		return mapper.writeValueAsString(jsonMap);
	}

	/**
	 * 
	 * @param quota
	 * @return
	 * @throws JsonProcessingException
	 */
	private String convertVdcDEtailsToJson(VdcDetails vdcDetails, Date collectionTime) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		List<VdcDetail> vdcDetailList = vdcDetails.getVdcDetails();
		final List<Map<String, Object>> jsonMap = new ArrayList<>();
		if (vdcDetailList != null && !vdcDetailList.isEmpty()) {
			for (VdcDetail detail : vdcDetailList) {
				final Map<String, Object> map = new HashMap<>();
				map.put("vdcid", detail.getVdcId());
				map.put("vdcname", detail.getVdcName());
				map.put("intervdcendpoints", detail.getInterVdcEndPoints());
				map.put("intervdccmdendpoints", detail.getInterVdcCmdEndPoints());
				map.put("secretkeys", detail.getSecretKeys());
				map.put("permanentlyfailed", detail.getPermanentlyFailed());
				map.put("local", detail.getLocal());
				map.put("managementendpoints", detail.getManagementEndPoints());
				map.put("name", detail.getName());
				map.put("id", detail.getId());
				map.put("link", detail.getLink());
				map.put("creationtime", detail.getCreationTime());
				map.put("inactive", detail.getInactive());
				map.put("global", detail.getGlobal());
				map.put("remote", detail.getRemote());
				map.put("vdc", detail.getVdc());
				map.put("internal", detail.getInternal());
				map.put("collectiontime", collectionTime);
				jsonMap.add(map);
			}
		}
		return mapper.writeValueAsString(jsonMap);
	}
	
	@Override
	public void close() {
	}
}
