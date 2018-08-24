package com.emc.ecs.metadata.dao.mqtt;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.emc.ecs.metadata.dao.ObjectDAO;
import com.emc.object.s3.bean.AbstractVersion;
import com.emc.object.s3.bean.DeleteMarker;
import com.emc.object.s3.bean.ListObjectsResult;
import com.emc.object.s3.bean.ListVersionsResult;
import com.emc.object.s3.bean.QueryObject;
import com.emc.object.s3.bean.QueryObjectsResult;
import com.emc.object.s3.bean.S3Object;
import com.emc.object.s3.bean.Version;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MqttS3ObjectDAO extends MqttDAO implements ObjectDAO {

	private static final Logger LOG = Logger.getLogger(MqttS3ObjectDAO.class);
	private static final String ECS_S3_LIST_OBJECTS = "ecs_s3_objects_list";
	private static final String ECS_S3_QUERY_OBJECTS = "ecs_s3_objects_query";
	private static final String ECS_S3_OBJECT_VERSIONS = "ecs_s3_objects_version";
	
	/**
	 * @param config
	 */
	public MqttS3ObjectDAO(MqttDAOConfig config) throws URISyntaxException {
		super(config);
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.ObjectDAO#initIndexes(java.util.Date)
	 */
	@Override
	public void initIndexes(Date collectionTime) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.ObjectDAO#insert(com.emc.object.s3.bean.ListObjectsResult, java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	public void insert(ListObjectsResult listObjectsResult, String namespace, String bucketName, Date collectionTime) {
		if (listObjectsResult == null || listObjectsResult.getObjects() == null
				|| listObjectsResult.getObjects().isEmpty()) {
			return;
		}
		try {
			final String json = this.convertListObjectsResultToJson(listObjectsResult, namespace, bucketName,
					collectionTime);
			this.postData(ECS_S3_LIST_OBJECTS, json);
		} catch (JsonProcessingException e) {
			LOG.error("An error occured while parsing Json for ListObjectsResult ", e);
		}
	}
	
	/**
	 * 
	 * @param results
	 * @param namespace
	 * @param bucketName
	 * @param collectionTime
	 * @return
	 * @throws JsonProcessingException
	 */
	private String convertListObjectsResultToJson(ListObjectsResult results, String namespace, String bucketName, Date collectionTime) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final List<Map<String, Object>> jsonMap = new ArrayList<>();
		List<S3Object> s3Objects = results.getObjects();
		if (s3Objects != null && !s3Objects.isEmpty()) {
			for (S3Object s3Object : s3Objects) {
				final Map<String, Object> map = new HashMap<>();
				map.put("etag", s3Object.getETag());
				map.put("key", s3Object.getKey());
				map.put("lastmodified", s3Object.getLastModified());
				map.put("owner", s3Object.getOwner());
				map.put("rawetag", s3Object.getRawETag());
				map.put("size", s3Object.getSize());
				map.put("storageclass", s3Object.getStorageClass());
				map.put("bucketname", bucketName);
				map.put("namespace", namespace);
				map.put("collectiontime", collectionTime);
				jsonMap.add(map);
			}
		}
		return mapper.writeValueAsString(jsonMap);
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.ObjectDAO#insert(com.emc.object.s3.bean.QueryObjectsResult, java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	public void insert(QueryObjectsResult queryObjectsResult, String namespace, String bucketName,
			Date collectionTime) {
		if (queryObjectsResult == null || queryObjectsResult.getObjects() == null
				|| queryObjectsResult.getObjects().isEmpty()) {
			return;
		}
		try {
			final String json = this.convertQueryObjectsResultToJson(queryObjectsResult, namespace, bucketName,
					collectionTime);
			this.postData(ECS_S3_QUERY_OBJECTS, json);
		} catch (JsonProcessingException e) {
			LOG.error("An error occured while parsing Json for ListObjectsResult ", e);
		}
	}
	
	/**
	 * 
	 * @param results
	 * @param namespace
	 * @param bucketName
	 * @param collectionTime
	 * @return
	 * @throws JsonProcessingException
	 */
	private String convertQueryObjectsResultToJson(QueryObjectsResult results, String namespace, String bucketName, Date collectionTime) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final List<Map<String, Object>> jsonMap = new ArrayList<>();
		List<QueryObject> queryObjects = results.getObjects();
		if (queryObjects != null && !queryObjects.isEmpty()) {
			for (QueryObject s3Object : queryObjects) {
				final Map<String, Object> map = new HashMap<>();
				map.put("objectid", s3Object.getObjectId());
				map.put("objectname", s3Object.getObjectName());
				map.put("metadatas", s3Object.getQueryMds());
				map.put("versionid", s3Object.getVersionId());
				map.put("bucketname", bucketName);
				map.put("namespace", namespace);
				map.put("collectiontime", collectionTime);
				jsonMap.add(map);
			}
		}
		return mapper.writeValueAsString(jsonMap);
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.ObjectDAO#insert(com.emc.object.s3.bean.ListVersionsResult, java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	public void insert(ListVersionsResult listVersionsResult, String namespace, String bucketName,
			Date collectionTime) {
		if (listVersionsResult == null || listVersionsResult.getVersions() == null
				|| listVersionsResult.getVersions().isEmpty()) {
			return;
		}
		try {
			final String json = this.convertListVersionsResultToJson(listVersionsResult, namespace, bucketName,
					collectionTime);
			this.postData(ECS_S3_OBJECT_VERSIONS, json);
		} catch (JsonProcessingException e) {
			LOG.error("An error occured while parsing Json for ListVersionsResult ", e);
		}
	}
	
	/**
	 * 
	 * @param results
	 * @param namespace
	 * @param bucketName
	 * @param collectionTime
	 * @return
	 * @throws JsonProcessingException
	 */
	private String convertListVersionsResultToJson(ListVersionsResult results, String namespace, String bucketName, Date collectionTime) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final List<Map<String, Object>> jsonMap = new ArrayList<>();
		final List<AbstractVersion> versions = results.getVersions();
		if (versions != null && !versions.isEmpty()) {
			for (AbstractVersion abstractVersion : versions) {
				final Map<String, Object> map = new HashMap<>();
				if (abstractVersion instanceof Version) {
					Version version = (Version) abstractVersion;
					map.put("lastmodified", version.getLastModified());
					map.put("key", version.getKey());
					map.put("owner", version.getOwner());
					map.put("versionid", version.getVersionId());
					map.put("etag", version.getETag());
					map.put("islatest", version.isLatest());
				} else if (abstractVersion instanceof DeleteMarker) {
					DeleteMarker version = (DeleteMarker) abstractVersion;
					map.put("lastmodified", version.getLastModified());
					map.put("key", version.getKey());
					map.put("owner", version.getOwner());
					map.put("versionid", version.getVersionId());
					map.put("islatest", version.isLatest());
				}
				map.put("bucketname", bucketName);
				map.put("namespace", namespace);
				map.put("collectiontime", collectionTime);
				jsonMap.add(map);
			}
		}
		return mapper.writeValueAsString(jsonMap);
	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.ObjectDAO#purgeOldData(com.emc.ecs.metadata.dao.ObjectDAO.ObjectDataType, java.util.Date)
	 */
	@Override
	public Long purgeOldData(ObjectDataType type, Date collectionTime) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void close() {
	}
}
