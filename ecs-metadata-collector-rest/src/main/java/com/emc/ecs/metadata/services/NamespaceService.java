/**
 * 
 */
package com.emc.ecs.metadata.services;

import java.util.Date;
import java.util.List;

import com.emc.ecs.management.entity.NamespaceDetail;
import com.emc.ecs.management.entity.NamespaceQuota;

/**
 * @author nlengc
 *
 */
public interface NamespaceService {
	
	/**
	 * 
	 * @param collectionTime
	 * @return
	 */
	List<NamespaceDetail> getNamespaceDetails(Date collectionTime);

	/**
	 * 
	 * @param collectionTime
	 * @return
	 */
	List<NamespaceQuota> getNamespaceQuotas(Date collectionTime);

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
