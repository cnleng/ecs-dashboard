/**
 * 
 */
package com.emc.ecs.metadata.tasks;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import com.emc.ecs.metadata.rest.bo.BillingBO;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
public class BillingTask implements Runnable {
	
	private static AtomicLong objectCount = new AtomicLong(0L);
	private Date collectionTime;
	private BillingBO billingBO;
	private TaskType taskType;

	public BillingTask(Date collectionTime, BillingBO billingBO, TaskType taskType) {
		this.collectionTime = collectionTime;
		this.taskType = taskType;
		this.billingBO = billingBO;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		switch (this.taskType) {
		case NamespaceBillingInfos:
			billingBO.collectBillingData(this.collectionTime);
			break;
		case ObjectBuckets:
			billingBO.collectObjectBuckets(this.collectionTime);
			break;
		default:
			break;
		}
		billingBO.shutdown();
	}

}
