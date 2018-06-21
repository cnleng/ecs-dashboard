/**
 * 
 */
package com.emc.ecs.metadata.services;

import java.util.Date;

/**
 * @author nlengc
 *
 */
public interface NamespaceService {
	
	/**
	 * 
	 * @param collectionTime
	 */
	void postNamespaceDetails(Date collectionTime);

	/**
	 * 
	 * @param collectionTime
	 */
	void postNamespaceQuotas(Date collectionTime);

}
