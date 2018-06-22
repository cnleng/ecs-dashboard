/**
 * 
 */
package com.emc.ecs.metadata.tasks;

import java.util.Date;

import com.emc.ecs.metadata.rest.bo.VdcBO;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
public class VdcTask implements Runnable {

	private Date collectionTime;
	private VdcBO vdcBO;
	private TaskType taskType;

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
				vdcBO.collectVdcDetails(this.collectionTime);
				break;
			case BucketOwners:
				vdcBO.collectBucketOwner(collectionTime);
				break;
			default:
				break;
			}
		} finally {
			vdcBO.shutdown();
		}
	}

}
