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

import com.emc.ecs.management.entity.BucketBillingInfo;
import com.emc.ecs.management.entity.NamespaceBillingInfo;
import com.emc.ecs.management.entity.ObjectBucket;
import com.emc.ecs.management.entity.ObjectBuckets;
import com.emc.ecs.metadata.dao.BillingDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author nlengc
 *
 */
public class MqttBillingDAO extends MqttDAO implements BillingDAO  {

	private static final Logger LOG = Logger.getLogger(MqttBillingDAO.class);
	private static final String _NAMESPACE_BILLING_INFOS =  "ecs_billing_infos";
	private static final String _OBJECT_BUCKETS = "ecs_object_buckets";

	public MqttBillingDAO(MqttDAOConfig config) throws URISyntaxException {
		super(config);
	}
	
	@Override
	public void initIndexes(Date collectionTime) {
		// TODO Auto-generated method stub
	}

	@Override
	public void insert(NamespaceBillingInfo billingData, Date collectionTime) {
		try {
			final String json = this.convertBillingInfoToJson(billingData, collectionTime);
			this.postData(_NAMESPACE_BILLING_INFOS, json);
		} catch (JsonProcessingException e) {
			LOG.error("An error occured while parsing Json for namespace billing infos ", e);
		}
	}

	@Override
	public void insert(ObjectBuckets bucketResponse, Date collectionTime) {
		try {
			final String json = this.convertObjectBucketToJson(bucketResponse, collectionTime);
			this.postData(_OBJECT_BUCKETS, json);
		} catch (JsonProcessingException e) {
			LOG.error("An error occured while parsing Json for Object Buckets ", e);
		}
	}

	@Override
	public Long purgeOldData(ManagementDataType type, Date collectionTime) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String convertObjectBucketToJson(ObjectBuckets bucket, Date collectionTime) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		List<ObjectBucket> objectBuckets = bucket.getObjectBucket();
		final List<Map<String, Object>> jsonMap = new ArrayList<>();
		if (objectBuckets != null && !objectBuckets.isEmpty()) {
			for (ObjectBucket objectBucket : objectBuckets) {
				final Map<String, Object> map = new HashMap<>();
				map.put("maxbuckets", bucket.getMaxBuckets());
				map.put("nextmarker", bucket.getNextMarker());
				map.put("filter", bucket.getFilter());
				map.put("nextpagelink", bucket.getNextPageLink());
				map.put("namespace", objectBucket.getNamespace());
				map.put("softquota", objectBucket.getSoftQuota());
				map.put("fsaccessenabled", objectBucket.getFsAccessEnabled());
				map.put("internal", objectBucket.getInternal());
				map.put("vdc", objectBucket.getVdc());
				map.put("remote", objectBucket.getRemote());
				map.put("global", objectBucket.getGlobal());
				map.put("inactive", objectBucket.getInactive());
				map.put("creationtime", objectBucket.getCreationTime());
				map.put("link", objectBucket.getLink());
				map.put("id", objectBucket.getId());
				map.put("name", objectBucket.getName());
				map.put("searchmetadata", objectBucket.getSearchMetadata());
				map.put("defaultgroup", objectBucket.getDefaultGroup());
				map.put("defaultgroupdirexecutepermission", objectBucket.getDefaultGroupDirExecutePermission());
				map.put("defaultgroupdirwritepermission", objectBucket.getDefaultGroupDirWritePermission());
				map.put("defaultgroupdirreadpermission", objectBucket.getDefaultGroupDirReadPermission());
				map.put("defaultgroupfileexecutepermission", objectBucket.getDefaultGroupFileExecutePermission());
				map.put("defaultgroupfilewritepermission", objectBucket.getDefaultGroupFileWritePermission());
				map.put("defaultgroupfilereadpermission", objectBucket.getDefaultGroupFileReadPermission());
				map.put("retention", objectBucket.getRetention());
				map.put("tagset", objectBucket.getTagSet());
				map.put("apitype", objectBucket.getApiType());
				map.put("notificationsize", objectBucket.getNotificationSize());
				map.put("blocksize", objectBucket.getBlockSize());
				map.put("defaultRetention", objectBucket.getDefaultRetention());
				map.put("isencryptionenabled", objectBucket.getIsEncryptionEnabled());
				map.put("isstaleallowed", objectBucket.getIsStaleAllowed());
				map.put("owner", objectBucket.getOwner());
				map.put("vpool", objectBucket.getVpool());
				map.put("locked", objectBucket.getLocked());
				map.put("collectiontime", collectionTime);
				jsonMap.add(map);
			}
		} else {
			final Map<String, Object> map = new HashMap<>();
			map.put("maxbuckets", bucket.getMaxBuckets());
			map.put("nextmarker", bucket.getNextMarker());
			map.put("filter", bucket.getFilter());
			map.put("nextpagelink", bucket.getNextPageLink());
			jsonMap.add(map);
		}
		return mapper.writeValueAsString(jsonMap);
	}
	
	private String convertBillingInfoToJson(NamespaceBillingInfo billingData, Date collectionTime) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		List<BucketBillingInfo> bucketBillingInfos = billingData.getBucketBillingInfo();
		final List<Map<String, Object>> jsonMap = new ArrayList<>();
		if (bucketBillingInfos != null && !bucketBillingInfos.isEmpty()) {
			for (BucketBillingInfo bucketBillingInfo : bucketBillingInfos) {
				final Map<String, Object> map = new HashMap<>();
				map.put("nextmarker", billingData.getNextMarker());
				map.put("totalsize", billingData.getTotalSize());
				map.put("totalsizeunit", billingData.getTotalSizeUnit());
				map.put("totalobjects", billingData.getTotalObjects());
				map.put("namespace", billingData.getNamespace());
				map.put("sampletime", billingData.getSampleTime());
				map.put("name", bucketBillingInfo.getName());
				map.put("vpoolid", bucketBillingInfo.getVpoolId());
				map.put("tagset", bucketBillingInfo.getTagSet());
				map.put("apitype", bucketBillingInfo.getApiType());
				map.put("collectiontime", collectionTime);
				jsonMap.add(map);
			}
		} else {
			final Map<String, Object> map = new HashMap<>();
			map.put("nextmarker", billingData.getNextMarker());
			map.put("totalsize", billingData.getTotalSize());
			map.put("totalsizeunit", billingData.getTotalSizeUnit());
			map.put("totalobjects", billingData.getTotalObjects());
			map.put("namespace", billingData.getNamespace());
			map.put("sampletime", billingData.getSampleTime());
			map.put("collectiontime", collectionTime);
			jsonMap.add(map);
		}
		return mapper.writeValueAsString(jsonMap);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
