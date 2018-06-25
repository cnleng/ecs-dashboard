/**
 * 
 */
package com.emc.ecs.metadata.tasks;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.emc.ecs.metadata.rest.bo.NamespaceBO;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
public class NamespaceTask implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(NamespaceTask.class);
	private AtomicLong objectCount = new AtomicLong(0L);
	private Date collectionTime;
	private NamespaceBO namespaceBO;
	private TaskType taskType;

	public NamespaceTask(Date collectionTime, NamespaceBO namespaceBO, TaskType taskType) {
		this.collectionTime = collectionTime;
		this.taskType = taskType;
		this.namespaceBO = namespaceBO;
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
			case NamespaceDetails:
				namespaceBO.collectNamespaceDetails(this.collectionTime, this.objectCount);
				break;
			case NamespaceQuotas:
				namespaceBO.collectNamespaceQuota(this.collectionTime, this.objectCount);
				break;
			default:
				break;
			}
			Long objectCollectionFinish = System.currentTimeMillis();
			Double deltaTime = Double.valueOf((objectCollectionFinish - collectionTime.getTime())) / 1000 ;
			LOGGER.info("Collected " + objectCount.get() + " objects");
			LOGGER.info("Total collection time: " + deltaTime + " seconds");
		} finally {
			namespaceBO.shutdown();
		}
	}

}
