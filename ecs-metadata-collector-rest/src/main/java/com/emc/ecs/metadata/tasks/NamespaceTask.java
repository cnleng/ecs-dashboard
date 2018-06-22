/**
 * 
 */
package com.emc.ecs.metadata.tasks;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import com.emc.ecs.metadata.rest.bo.NamespaceBO;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
public class NamespaceTask implements Runnable {

	private static AtomicLong objectCount = new AtomicLong(0L);
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
				namespaceBO.collectNamespaceDetails(this.collectionTime);
				break;
			case NamespaceQuotas:
				namespaceBO.collectNamespaceQuota(this.collectionTime);
				break;
			default:
				break;
			}
		} finally {
			namespaceBO.shutdown();
		}
	}

}
