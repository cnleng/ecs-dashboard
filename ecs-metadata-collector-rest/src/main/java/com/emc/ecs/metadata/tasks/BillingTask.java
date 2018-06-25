/**
 * 
 */
package com.emc.ecs.metadata.tasks;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.emc.ecs.metadata.rest.bo.BillingBO;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
public class BillingTask implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(BillingTask.class);
	private AtomicLong objectCount = new AtomicLong(0L);
	private Date collectionTime;
	private BillingBO billingBO;
	private TaskType taskType;

	public BillingTask(Date collectionTime, BillingBO billingBO, TaskType taskType) {
		this.collectionTime = collectionTime;
		this.taskType = taskType;
		this.billingBO = billingBO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			switch (this.taskType) {
			case NamespaceBillingInfos:
				billingBO.collectBillingData(this.collectionTime, this.objectCount);
				break;
			case ObjectBuckets:
				billingBO.collectObjectBuckets(this.collectionTime, this.objectCount);
				break;
			default:
				break;
			}
			Long objectCollectionFinish = System.currentTimeMillis();
			Double deltaTime = Double.valueOf((objectCollectionFinish - collectionTime.getTime())) / 1000 ;
			LOGGER.info("Collected " + objectCount.get() + " objects");
			LOGGER.info("Total collection time: " + deltaTime + " seconds");
		} finally {
			billingBO.shutdown();
		}
	}

}
