/**
 * 
 */
package com.emc.ecs.metadata.services;

import java.util.Date;
import java.util.List;

import com.emc.ecs.management.entity.BucketOwner;
import com.emc.ecs.management.entity.VdcDetails;

/**
 * @author nlengc
 *
 */
public interface VdcService {

	/**
	 * 
	 * @param collectionTime
	 * @return
	 */
	List<BucketOwner> getBucketOwners(Date collectionTime);
	
	/**
	 * 
	 * @param collectionTime
	 * @return
	 */
	List<VdcDetails> getVdcDetails(Date collectionTime);

	/**
	 * 
	 * @param collectionTime
	 */
	void postVdcDetails(Date collectionTime);

	/**
	 * 
	 * @param collectionTime
	 */
	void postBucketOwners(Date collectionTime);
}
