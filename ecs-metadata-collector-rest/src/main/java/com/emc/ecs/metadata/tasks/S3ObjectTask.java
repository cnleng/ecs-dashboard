/**
 * 
 */
package com.emc.ecs.metadata.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.emc.ecs.metadata.rest.bo.S3ObjectBO;
import com.emc.ecs.metadata.utils.Constants.TaskType;

/**
 * @author nlengc
 *
 */
public class S3ObjectTask implements Runnable {

	private static final String DATA_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final SimpleDateFormat DATA_DATE_FORMAT = new SimpleDateFormat(DATA_DATE_PATTERN);
	private static final String ECS_OBJECT_LAST_MODIFIED_MD_KEY = "LastModified";

	private final static Logger LOGGER = Logger.getLogger(S3ObjectTask.class);
	private ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private Queue<Future<?>> futures = new ConcurrentLinkedQueue<Future<?>>();

	private Date collectionTime;
	private S3ObjectBO objectBO;
	private TaskType taskType;
	private Integer numberOfDays;
	private AtomicLong objectCount = new AtomicLong(0L);

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
		try {
			switch (this.taskType) {
			case S3ObjectsData:
				objectBO.collectObjectData(this.collectionTime, this.threadPoolExecutor, this.futures, this.objectCount);
				break;
			case S3ObjectsDataByNamespace:
				objectBO.collectObjectDataByNamespace(this.collectionTime, this.threadPoolExecutor, this.futures, this.objectCount);
				break;
			case S3ObjectsDataByBucket:
				objectBO.collectObjectDataByBucket(this.collectionTime, this.threadPoolExecutor, this.futures, this.objectCount);
				break;				
			case S3ObjectVersions:
				objectBO.collectObjectVersionData(this.collectionTime, this.threadPoolExecutor, this.futures, this.objectCount);
				break;
			case S3ObjectsModified:
				// Query criteria should look like ( LastModified >= 'since date' )
				Date sinceDate = new Date(
						(this.collectionTime.getTime() - (TimeUnit.MILLISECONDS.convert(this.numberOfDays, TimeUnit.DAYS))));
				String yesterdayDateTime = DATA_DATE_FORMAT.format(sinceDate);
				String queryCriteria = "( " + ECS_OBJECT_LAST_MODIFIED_MD_KEY + " >= '" + yesterdayDateTime + "' )";
				objectBO.collectObjectData(this.collectionTime, this.threadPoolExecutor, this.futures, queryCriteria, this.objectCount);
				break;
			default:
				break;
			}
			
			// wait for all threads to complete their work
			while ( !futures.isEmpty() ) {
			    try {
					Future<?> future = this.futures.poll();
					if(future != null){
						future.get();
					}
				} catch (InterruptedException e) {
					LOGGER.error(e.getLocalizedMessage());
				} catch (ExecutionException e) {
					LOGGER.error(e.getLocalizedMessage());
				}
			}
			
			Long objectCollectionFinish = System.currentTimeMillis();
			Double deltaTime = Double.valueOf((objectCollectionFinish - this.collectionTime.getTime())) / 1000 ;
			LOGGER.info("Collected " + this.objectCount.get() + " objects");
			LOGGER.info("Total collection time: " + deltaTime + " seconds");
			
			// take everything down once all threads have completed their work
			this.threadPoolExecutor.shutdown();
			
			// wait for all threads to terminate
			boolean termination = false; 
			do {
				try {
					termination = this.threadPoolExecutor.awaitTermination(2, TimeUnit.MINUTES);
				} catch (InterruptedException e) {
					LOGGER.error(e.getLocalizedMessage());
					termination = true;
				}
			} while(!termination);
		} finally {
			objectBO.shutdown();
		}
	}

}
