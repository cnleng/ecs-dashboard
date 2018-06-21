/**
 * 
 */
package com.emc.ecs.metadata.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.emc.ecs.metadata.rest.bo.S3ObjectBO;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
public class S3ObjectTask implements Runnable {
	
	private static final String            DATA_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final SimpleDateFormat  DATA_DATE_FORMAT = new  SimpleDateFormat(DATA_DATE_PATTERN);
	private static final String ECS_OBJECT_LAST_MODIFIED_MD_KEY  = "LastModified";

	private Date collectionTime;
	private S3ObjectBO objectBO;
	private TaskType taskType;
	private Integer numberOfDays;

	public S3ObjectTask(Date collectionTime, S3ObjectBO objectBO, TaskType taskType) {
		this.collectionTime = collectionTime;
		this.objectBO = objectBO;
		this.taskType = taskType;
	}
	
	public S3ObjectTask(Date collectionTime, S3ObjectBO objectBO, TaskType taskType, Integer numberOfDays) {
		this(collectionTime, objectBO, taskType);
		this.numberOfDays = numberOfDays;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		switch (this.taskType) {
		case S3ObjectsData:
			objectBO.collectObjectData(this.collectionTime);
			break;
		case S3ObjectVersions:
			objectBO.collectObjectVersionData(this.collectionTime);
			break;
		case S3ObjectsModified:
			// query criteria should look like ( LastModified >= 'since date' )
			Date sinceDate = new Date( (collectionTime.getTime() - (TimeUnit.MILLISECONDS.convert(this.numberOfDays, TimeUnit.DAYS)) ));
			String yesterdayDateTime = DATA_DATE_FORMAT.format( sinceDate );
			String queryCriteria = "( " + ECS_OBJECT_LAST_MODIFIED_MD_KEY + " >= '" + yesterdayDateTime + "' )";
			objectBO.collectObjectData(this.collectionTime, queryCriteria);
			break;
		default:
			break;
		}
		objectBO.shutdown();
	}

}
