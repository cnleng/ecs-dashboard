/**
 * 
 */
package com.emc.ecs.metadata.services;

import java.util.Date;
import java.util.List;

import com.emc.ecs.management.entity.NamespaceBillingInfo;
import com.emc.ecs.management.entity.ObjectBuckets;

/**
 * @author nlengc
 *
 */
public interface BillingService {

	/**
	 * 
	 * @param collectionTime
	 * @return
	 */
	List<NamespaceBillingInfo> getNamespaceBillingInfo(Date collectionTime);
	
	/**
	 * 
	 * @param collectionTime
	 * @return
	 */
	List<ObjectBuckets> getObjectBuckets(Date collectionTime);
}
