/**
 * 
 */
package com.emc.ecs.metadata.services;

import java.util.Date;

/**
 * @author nlengc
 *
 */
public interface S3ObjectService {

	/**
	 * 
	 * @param collectionTime
	 */
	void postObjectData(Date collectionTime);

	/**
	 * 
	 * @param collectionTime
	 */
	void postObjectVersions(Date collectionTime);

	/**
	 * 
	 * @param collectionTime
	 */
	void postObjectModified(Date collectionTime, Integer numberOfDays);
}
