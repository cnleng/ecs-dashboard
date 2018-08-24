/**
 * 
 */
package com.emc.ecs.metadata.rest.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.emc.ecs.management.entity.ObjectBucket;
import com.emc.ecs.management.entity.ObjectUserDetails;
import com.emc.ecs.metadata.dao.ObjectDAO;
import com.emc.object.Protocol;
import com.emc.object.s3.S3Config;
import com.emc.object.s3.jersey.S3JerseyClient;
import com.emc.rest.smart.ecs.Vdc;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;

/**
 * @author nlengc
 *
 */
public class S3ObjectBO {

	private final static Logger LOGGER = Logger.getLogger(S3ObjectBO.class);
	private BillingBO billingBO;
	private List<String> ecsObjectHosts;
	private ObjectDAO objectDAO;
	private String namespace;
	private String bucketname;

	public S3ObjectBO(BillingBO billingBO, List<String> ecsObjectHosts, ObjectDAO objectDAO) {
		this.billingBO = billingBO;
		this.ecsObjectHosts = ecsObjectHosts;
		this.objectDAO = objectDAO;
	}

	public S3ObjectBO(BillingBO billingBO, List<String> ecsObjectHosts, ObjectDAO objectDAO, String namespace) {
		this(billingBO, ecsObjectHosts, objectDAO);
		this.namespace = namespace;
	}
	
	public S3ObjectBO(BillingBO billingBO, List<String> ecsObjectHosts, ObjectDAO objectDAO, String namespace, String bucketname) {
		this(billingBO, ecsObjectHosts, objectDAO);
		this.namespace = namespace;
		this.bucketname = bucketname;
	}
	
	public void collectObjectData(Date collectionTime,ThreadPoolExecutor threadPoolExecutor, Queue<Future<?>> futures,AtomicLong objectCount) {
		collectObjectData(collectionTime, threadPoolExecutor, futures, null, objectCount);
	}

	public void collectObjectData(Date collectionTime, ThreadPoolExecutor threadPoolExecutor, Queue<Future<?>> futures, String queryCriteria, AtomicLong objectCount) {

		// collect S3 user Id and credentials
		List<ObjectUserDetails> objectUserDetailsList = billingBO.getObjectUserSecretKeys();
		// Collect bucket details
		Map<NamespaceBucketKey, ObjectBucket> objectBucketMap = new HashMap<>();
		billingBO.getObjectBucketData(objectBucketMap, objectCount);
		Map<String, S3JerseyClient> s3ObjectClientMap = null;

		try {
			// create all required S3 jersey clients for very S3 users
			s3ObjectClientMap = createS3ObjectClients(objectUserDetailsList, this.ecsObjectHosts);

			// collect objects for all users
			for (ObjectUserDetails objectUserDetails : objectUserDetailsList) {
				if (objectUserDetails.getObjectUser().getUserId() == null
						|| objectUserDetails.getSecretKeys().getSecretKey1() == null) {
					continue;
				}

				String userId = objectUserDetails.getObjectUser().getUserId().toString();
				S3JerseyClient s3JerseyClient = s3ObjectClientMap.get(userId);
				String namespace = objectUserDetails.getObjectUser().getNamespace().toString();

				if (s3JerseyClient != null && namespace != null) {
					ObjectCollectionConfig collectionConfig = new ObjectCollectionConfig(s3JerseyClient, namespace,
							this.objectDAO, objectBucketMap, collectionTime, objectCount, threadPoolExecutor, futures,
							queryCriteria);
					NamespaceObjectCollection namespaceObjectCollection = new NamespaceObjectCollection(
							collectionConfig);

					try {
						// submit namespace collection to thread pool
						futures.add(threadPoolExecutor.submit(namespaceObjectCollection));
					} catch (RejectedExecutionException e) {
						// Thread pool didn't accept bucket collection
						// running in the current thread
						LOGGER.error("Thread pool didn't accept bucket collection - running in current thread");
						try {
							namespaceObjectCollection.call();
						} catch (Exception e1) {
							LOGGER.error("Error occured during namespace object collection operation - message: "
									+ e.getLocalizedMessage());
						}
					}
				}
			}

		} finally {
			// ensure to clean up S3 jersey clients
			if (s3ObjectClientMap != null) {
				for (S3JerseyClient s3JerseyClient : s3ObjectClientMap.values()) {
					s3JerseyClient.destroy();
				}
			}
		}

	}

	public void collectObjectVersionData(Date collectionTime, ThreadPoolExecutor threadPoolExecutor, Queue<Future<?>> futures, AtomicLong objectCount) {

		// collect S3 user Id and credentials
		List<ObjectUserDetails> objectUserDetailsList = billingBO.getObjectUserSecretKeys();

		// Collect bucket details
		Map<NamespaceBucketKey, ObjectBucket> objectBucketMap = new HashMap<>();
		billingBO.getObjectBucketData(objectBucketMap, objectCount);
		Map<String, S3JerseyClient> s3ObjectClientMap = null;

		try {
			// create all required S3 jersey clients for very S3 users
			s3ObjectClientMap = createS3ObjectClients(objectUserDetailsList, this.ecsObjectHosts);

			// collect objects for all users
			for (ObjectUserDetails objectUserDetails : objectUserDetailsList) {

				if (objectUserDetails.getObjectUser().getUserId() == null
						|| objectUserDetails.getSecretKeys().getSecretKey1() == null) {
					continue;
				}

				String userId = objectUserDetails.getObjectUser().getUserId().toString();
				S3JerseyClient s3JerseyClient = s3ObjectClientMap.get(userId);
				String namespace = objectUserDetails.getObjectUser().getNamespace().toString();

				if (s3JerseyClient != null && namespace != null) {

					ObjectCollectionConfig collectionConfig = new ObjectCollectionConfig(s3JerseyClient, namespace,
							objectDAO, objectBucketMap, collectionTime, objectCount, threadPoolExecutor, futures, null);
					NamespaceObjectVersionCollection namespaceObjectVersionCollection = new NamespaceObjectVersionCollection(
							collectionConfig);

					try {
						// submit namespace collection to thread pool
						futures.add(threadPoolExecutor.submit(namespaceObjectVersionCollection));
					} catch (RejectedExecutionException e) {
						// Thread pool didn't accept bucket collection
						// running in the current thread
						LOGGER.error("Thread pool didn't accept bucket collection - running in current thread");
						try {
							namespaceObjectVersionCollection.call();
						} catch (Exception e1) {
							LOGGER.error(
									"Error occured during namespace object version collection operation - message: "
											+ e.getLocalizedMessage());
						}
					}
				}
			}

		} finally {
			// ensure to clean up S3 jersey clients
			if (s3ObjectClientMap != null) {
				for (S3JerseyClient s3JerseyClient : s3ObjectClientMap.values()) {
					s3JerseyClient.destroy();
				}
			}
		}
	}

	private Map<String, S3JerseyClient> createS3ObjectClients(List<ObjectUserDetails> objectUserDetailsList,
			List<String> ecsObjectHosts) {

		Map<String, S3JerseyClient> s3JerseyClientList = new HashMap<String, S3JerseyClient>();
		// collect objects for all users
		for (ObjectUserDetails objectUserDetails : objectUserDetailsList) {
			if (objectUserDetails.getObjectUser().getUserId() == null
					|| objectUserDetails.getSecretKeys().getSecretKey1() == null) {
				continue;
			}

			// Create object client user
			Vdc vdc = new Vdc((String[]) this.ecsObjectHosts.toArray());
			S3Config s3config = new S3Config(Protocol.HTTP, vdc);
			// in all cases, you need to provide your credentials
			s3config.withIdentity(objectUserDetails.getObjectUser().getUserId().toString())
					.withSecretKey(objectUserDetails.getSecretKeys().getSecretKey1());
			s3config.setSmartClient(true);
			URLConnectionClientHandler urlHandler = new URLConnectionClientHandler();
			S3JerseyClient s3JerseyClient = new S3JerseyClient(s3config, urlHandler);
			s3JerseyClientList.put(objectUserDetails.getObjectUser().getUserId().toString(), s3JerseyClient);
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			LOGGER.error(e.getLocalizedMessage());
		} // wait for poll to complete

		return s3JerseyClientList;
	}
	
	/**
	 * 
	 * @param collectionTime
	 * @param namespace
	 * @param threadPoolExecutor
	 * @param futures
	 * @param objectCount
	 */
	public void collectObjectDataByNamespace(Date collectionTime, ThreadPoolExecutor threadPoolExecutor, Queue<Future<?>> futures, AtomicLong objectCount) {
		
		// collect S3 user Id and credentials
		// Collect bucket details
		List<ObjectUserDetails> objectUserDetailsList = billingBO.getObjectUserSecretKeys();
		Map<NamespaceBucketKey, ObjectBucket> objectBucketMap = new HashMap<>();
		billingBO.getObjectBucketDataByNamespace(objectBucketMap, this.namespace, objectCount);
		Map<String, S3JerseyClient> s3ObjectClientMap = null;
		
		try {
			// create all required S3 jersey clients for very S3 users
			s3ObjectClientMap = createS3ObjectClients(objectUserDetailsList, this.ecsObjectHosts);
			
			// collect objects for all users
			for( ObjectUserDetails objectUserDetails : objectUserDetailsList ) {

				if (objectUserDetails.getObjectUser().getUserId() == null
						|| objectUserDetails.getSecretKeys().getSecretKey1() == null
						|| objectUserDetails.getObjectUser().getNamespace() == null
						|| !this.namespace.equals(objectUserDetails.getObjectUser().getNamespace().toString())) {
					continue;
				}

				String userId = objectUserDetails.getObjectUser().getUserId().toString();
				S3JerseyClient s3JerseyClient = s3ObjectClientMap.get(userId);

				if (s3JerseyClient != null) {

					ObjectCollectionConfig collectionConfig = new ObjectCollectionConfig(s3JerseyClient, this.namespace,
							this.objectDAO, objectBucketMap, collectionTime, objectCount, threadPoolExecutor, futures,
							null);
					NamespaceObjectCollection namespaceObjectCollection = new NamespaceObjectCollection(
							collectionConfig);

					try {
						// submit namespace collection to thread pool
						futures.add(threadPoolExecutor.submit(namespaceObjectCollection));
					} catch (RejectedExecutionException e) {
						// Thread pool didn't accept bucket collection
						// running in the current thread
						LOGGER.error("Thread pool didn't accept bucket collection - running in current thread");
						try {
							namespaceObjectCollection.call();
						} catch (Exception e1) {
							LOGGER.error("Error occured during namespace object collection operation - message: "
									+ e.getLocalizedMessage());
						}
					}
				}
			}
			
		} finally {
			// ensure to clean up S3 jersey clients
			if(s3ObjectClientMap != null ) {
				for( S3JerseyClient s3JerseyClient : s3ObjectClientMap.values() ) {
					s3JerseyClient.destroy();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param collectionTime
	 * @param threadPoolExecutor
	 * @param futures
	 * @param objectCount
	 */
	public void collectObjectDataByBucket(Date collectionTime, ThreadPoolExecutor threadPoolExecutor, Queue<Future<?>> futures, AtomicLong objectCount) {
		// collect S3 user Id and credentials
		List<ObjectUserDetails> objectUserDetailsList = billingBO.getObjectUserSecretKeys();
		
		// Collect bucket details
		Map<NamespaceBucketKey, ObjectBucket> objectBucketMap = new HashMap<>();
		billingBO.getObjectBucketDataByBucket(objectBucketMap, this.namespace, this.bucketname, objectCount);
		Map<String, S3JerseyClient> s3ObjectClientMap = null;
		
		try {
			// create all required S3 jersey clients for very S3 users
			s3ObjectClientMap = createS3ObjectClients(objectUserDetailsList, this.ecsObjectHosts);
			
			// collect objects for all users
			for( ObjectUserDetails objectUserDetails : objectUserDetailsList ) {

				if (objectUserDetails.getObjectUser().getUserId() == null
						|| objectUserDetails.getSecretKeys().getSecretKey1() == null
						|| objectUserDetails.getObjectUser().getNamespace() == null
						|| !namespace.equals(objectUserDetails.getObjectUser().getNamespace().toString())) {
					continue;
				}

				String userId = objectUserDetails.getObjectUser().getUserId().toString();
				S3JerseyClient s3JerseyClient = s3ObjectClientMap.get(userId);

				if (s3JerseyClient != null) {

					ObjectCollectionConfig collectionConfig = new ObjectCollectionConfig(s3JerseyClient, this.namespace, this.bucketname,
							this.objectDAO, objectBucketMap, collectionTime, objectCount, threadPoolExecutor, futures, null);
					NamespaceObjectCollection namespaceObjectCollection = new NamespaceObjectCollection(
							collectionConfig);
					try {
						// submit namespace collection to thread pool
						futures.add(threadPoolExecutor.submit(namespaceObjectCollection));
					} catch (RejectedExecutionException e) {
						LOGGER.error("Thread pool didn't accept bucket collection - running in current thread");
						try {
							namespaceObjectCollection.call();
						} catch (Exception e1) {
							LOGGER.error("Error occured during namespace object collection operation - message: "
									+ e.getLocalizedMessage());
						}
					}
				}
			}
		} finally {
			// ensure to clean up S3 jersey clients
			if(s3ObjectClientMap != null ) {
				for( S3JerseyClient s3JerseyClient : s3ObjectClientMap.values() ) {
					s3JerseyClient.destroy();
				}
			}
		}
	}

	public void shutdown() {
		billingBO.shutdown();
	}

}
