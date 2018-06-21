/**
 * 
 */
package com.emc.ecs.metadata.services;

import java.util.Date;

/**
 * @author nlengc
 *
 */
public interface BillingService {

	/**
	 * 
	 * @param parse
	 */
	void postNamespaceBillingInfo(Date parse);

	/**
	 * 
	 * @param parse
	 */
	void postObjectBuckets(Date parse);
}
