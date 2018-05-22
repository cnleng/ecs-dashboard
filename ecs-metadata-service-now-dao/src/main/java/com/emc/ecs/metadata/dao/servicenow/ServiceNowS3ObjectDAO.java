/**
 * 
 */
package com.emc.ecs.metadata.dao.servicenow;

import java.util.Date;

import org.apache.log4j.Logger;

import com.emc.ecs.metadata.dao.ObjectDAO;
import com.emc.object.s3.bean.ListObjectsResult;
import com.emc.object.s3.bean.ListVersionsResult;
import com.emc.object.s3.bean.QueryObjectsResult;

/**
 * @author nlengc
 *
 */
public class ServiceNowS3ObjectDAO extends ServiceNowDAO implements ObjectDAO {
	
	private static final Logger LOG = Logger.getLogger(ServiceNowS3ObjectDAO.class);
	private static final String ECS_S3_LIST_OBJECTS = "/ecs_s3_objects_list";
	private static final String ECS_S3_QUERY_OBJECTS = "/ecs_s3_objects_query";
	
	/**
	 * @param config
	 */
	public ServiceNowS3ObjectDAO(ServiceNowDAOConfig config) {
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
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.ObjectDAO#insert(com.emc.object.s3.bean.QueryObjectsResult, java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	public void insert(QueryObjectsResult queryObjectsResult, String namespace, String bucketName,
			Date collectionTime) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.ObjectDAO#insert(com.emc.object.s3.bean.ListVersionsResult, java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	public void insert(ListVersionsResult listVersionsResult, String namespace, String name, Date collectionTime) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.emc.ecs.metadata.dao.ObjectDAO#purgeOldData(com.emc.ecs.metadata.dao.ObjectDAO.ObjectDataType, java.util.Date)
	 */
	@Override
	public Long purgeOldData(ObjectDataType type, Date collectionTime) {
		// TODO Auto-generated method stub
		return null;
	}

}
