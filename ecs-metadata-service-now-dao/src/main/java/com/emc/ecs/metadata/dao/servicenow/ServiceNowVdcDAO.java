/**
 * 
 */
package com.emc.ecs.metadata.dao.servicenow;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.emc.ecs.management.entity.BucketOwner;
import com.emc.ecs.management.entity.VdcDetails;
import com.emc.ecs.metadata.dao.VdcDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author nlengc
 *
 */
public class ServiceNowVdcDAO extends ServiceNowDAO implements VdcDAO {
	
	private static final Logger LOG = Logger.getLogger(ServiceNowVdcDAO.class);
	private static final String ECS_VDC_DETAILS = "/ecs_vdc_details";
	private static final String ECS_BUCKET_OWNERS = "/ecs_bucket_owners";

	/**
	 * @param config
	 */
	public ServiceNowVdcDAO(ServiceNowDAOConfig config) {
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
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final String json = mapper.writeValueAsString(vdcDetails);
			this.postData(this.url + ECS_VDC_DETAILS, json);
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
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final String json = mapper.writeValueAsString(bucketOwners);
			this.postData(this.url + ECS_BUCKET_OWNERS, json);
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

}
