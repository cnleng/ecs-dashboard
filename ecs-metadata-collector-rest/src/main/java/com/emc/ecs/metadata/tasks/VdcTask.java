/**
 * 
 */
package com.emc.ecs.metadata.tasks;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.emc.ecs.metadata.rest.bo.VdcBO;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
public class VdcTask implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(VdcTask.class);
	private Date collectionTime;
	private VdcBO vdcBO;
	private TaskType taskType;
	private AtomicLong objectCount = new AtomicLong(0L);

	public VdcTask(Date collectionTime, VdcBO vdcBO, TaskType taskType) {
		this.collectionTime = collectionTime;
		this.vdcBO = vdcBO;
		this.taskType = taskType;
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
			case VdcDetails:
				vdcBO.collectVdcDetails(this.collectionTime, this.objectCount);
				break;
			case BucketOwners:
				vdcBO.collectBucketOwner(this.collectionTime, this.objectCount);
				break;
			default:
				break;
			}
			Long objectCollectionFinish = System.currentTimeMillis();
			Double deltaTime = Double.valueOf((objectCollectionFinish - collectionTime.getTime())) / 1000 ;
			LOGGER.info("Collected " + objectCount.get() + " objects");
			LOGGER.info("Total collection time: " + deltaTime + " seconds");
		} finally {
			vdcBO.shutdown();
		}
	}

}
