/**
 * 
 */
package com.emc.ecs.metadata.rest.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.emc.ecs.management.client.ManagementClient;
import com.emc.ecs.management.client.ManagementClientConfig;
import com.emc.ecs.management.client.VdcManagementClient;
import com.emc.ecs.management.client.VdcManagementClientConfig;
import com.emc.ecs.management.entity.BucketOwner;
import com.emc.ecs.management.entity.VdcDetails;
import com.emc.ecs.metadata.dao.VdcDAO;

/**
 * @author nlengc
 *
 */
public class VdcBO {

	private final static Logger LOGGER = Logger.getLogger(VdcBO.class);
	private ManagementClient client;
	private VdcDAO vdcDAO;
	private AtomicLong objectCount;

	public VdcBO(String mgmtAccessKey, String mgmtSecretKey, List<String> hosts, Integer port, VdcDAO vdcDAO,
			AtomicLong objectCount) {
		// client config
		ManagementClientConfig clientConfig = new ManagementClientConfig(mgmtAccessKey, mgmtSecretKey, port, hosts);
		this.client = new ManagementClient(clientConfig);
		this.vdcDAO = vdcDAO;
		this.objectCount = objectCount;
	}
	
	public VdcBO(String mgmtAccessKey, String mgmtSecretKey, List<String> hosts, Integer port, Integer alternativePort, VdcDAO vdcDAO,
			AtomicLong objectCount) {
		// client config
		ManagementClientConfig clientConfig = new VdcManagementClientConfig(mgmtAccessKey, mgmtSecretKey, port, alternativePort, hosts);
		this.client = new VdcManagementClient(clientConfig);
		this.vdcDAO = vdcDAO;
		this.objectCount = objectCount;
	}

	public List<VdcDetails> collectVdcDetails(Date collectionTime) {
		long objCounter = 0;
		LOGGER.info("Collecting all VDC on cluster. ");
		VdcDetails vdcDetails = client.getVdcDetails();
		List<VdcDetails> vdcDetailsList = new ArrayList<>();
		if (vdcDetails == null || vdcDetails.getVdcDetails() == null || vdcDetails.getVdcDetails().isEmpty()) {
			return vdcDetailsList;
		}
		objCounter = objCounter + vdcDetails.getVdcDetails().size();
		// Push collected details into datastore
		if (vdcDAO != null) {
			vdcDetailsList.add(vdcDetails);
			vdcDAO.insert(vdcDetails, collectionTime);
		}
		// peg global counter
		this.objectCount.getAndAdd(objCounter);
		return vdcDetailsList;
	}

	public List<BucketOwner> collectBucketOwner(Date collectionTime) {
		long objCounter = 0;
		LOGGER.info("Collecting all bucket owner on cluster. ");
		List<BucketOwner> bucketOwners = client.getBucketOwner();
		if (bucketOwners == null || bucketOwners.isEmpty()) {
			return new ArrayList<>();
		}
		objCounter = objCounter + bucketOwners.size();
		// Push collected details into datastore
		if (vdcDAO != null) {
			LOGGER.info("Pushing all bucket owner to ServiceNow. ");
			vdcDAO.insert(bucketOwners, collectionTime);
		}
		// peg global counter
		this.objectCount.getAndAdd(objCounter);
		return bucketOwners;
	}

	public void shutdown() {
		if (this.client != null) {
			client.shutdown();
		}
	}
}
