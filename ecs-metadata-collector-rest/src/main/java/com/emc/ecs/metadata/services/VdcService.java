/**
 * 
 */
package com.emc.ecs.metadata.services;

import java.util.Date;

/**
 * @author nlengc
 *
 */
public interface VdcService {

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
