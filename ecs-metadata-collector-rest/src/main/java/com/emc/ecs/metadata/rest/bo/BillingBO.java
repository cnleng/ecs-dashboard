/**
 * 
 */
package com.emc.ecs.metadata.rest.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.emc.ecs.management.client.ManagementClient;
import com.emc.ecs.management.client.ManagementClientConfig;
import com.emc.ecs.management.entity.BucketBillingInfo;
import com.emc.ecs.management.entity.ListNamespaceRequest;
import com.emc.ecs.management.entity.ListNamespacesResult;
import com.emc.ecs.management.entity.Namespace;
import com.emc.ecs.management.entity.NamespaceBillingInfo;
import com.emc.ecs.management.entity.NamespaceRequest;
import com.emc.ecs.management.entity.ObjectBucket;
import com.emc.ecs.management.entity.ObjectBuckets;
import com.emc.ecs.management.entity.ObjectUser;
import com.emc.ecs.management.entity.ObjectUserDetails;
import com.emc.ecs.management.entity.ObjectUserSecretKeys;
import com.emc.ecs.management.entity.ObjectUsers;
import com.emc.ecs.management.entity.ObjectUsersRequest;
import com.emc.ecs.metadata.dao.BillingDAO;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowDAO;

/**
 * @author nlengc
 *
 */
public class BillingBO {

	private final static Logger LOGGER = Logger.getLogger(BillingBO.class);
	private ManagementClient client;
	private BillingDAO billingDAO;

	public BillingBO(String mgmtAccessKey, String mgmtSecretKey, List<String> hosts, Integer port,
			BillingDAO billingDAO) {
		// client config
		ManagementClientConfig clientConfig = new ManagementClientConfig(mgmtAccessKey, mgmtSecretKey, port, hosts);
		this.client = new ManagementClient(clientConfig);
		this.billingDAO = billingDAO;
	}

	/**
	 * Retrieve Object uid and secret keys
	 * 
	 * @return ObjectUserDetails
	 */
	public List<ObjectUserDetails> getObjectUserSecretKeys() {

		List<ObjectUserDetails> userDetails = new ArrayList<ObjectUserDetails>();
		// Collect all uids in order to collect secret keys after
		List<ObjectUser> objectUserList = new ArrayList<ObjectUser>();

		// first batch
		ObjectUsersRequest objectUsersRequest = new ObjectUsersRequest();
		ObjectUsers objectUsersResult = client.getObjectUsersUid(objectUsersRequest);

		if (objectUsersResult != null) {
			if (objectUsersResult.getBlobUser() != null) {
				objectUserList.addAll(objectUsersResult.getBlobUser());
			}
			objectUsersRequest.setMarker(objectUsersResult.getNextMarker());
			// Subsequent batches
			while (objectUsersResult.getNextMarker() != null) {
				objectUsersResult = client.getObjectUsersUid(objectUsersRequest);
				if (objectUsersResult != null) {
					objectUserList.addAll(objectUsersResult.getBlobUser());
					objectUsersRequest.setMarker(objectUsersResult.getNextMarker());
				} else {
					break;
				}
			}
		}

		// Collect secret keys
		for (ObjectUser objectUser : objectUserList) {
			ObjectUserSecretKeys objectUserSecretKeys = client
					.getObjectUserSecretKeys(objectUser.getUserId().toString(), objectUser.getNamespace().toString());
			if (objectUserSecretKeys != null) {
				userDetails.add(new ObjectUserDetails(objectUser, objectUserSecretKeys));
			}
		}
		return userDetails;
	}

	/**
	 * Collects Billing metadata for all namespace defined on a cluster
	 * 
	 * @param collectionTime
	 *            - Collection Time
	 */
	public List<NamespaceBillingInfo> collectBillingData(Date collectionTime, AtomicLong objectCount ) {

		// Collect the object bucket data first in order to use some of
		// the fields from object bucket
		Map<NamespaceBucketKey, ObjectBucket> objectBuckets = new HashMap<NamespaceBucketKey, ObjectBucket>();
		Map<String, Set<ObjectBucket>> objectBucketsPerNamespace = new HashMap<String, Set<ObjectBucket>>();

		// Collect bucket data and write to datastore
		// secondly returns classified data per namespace
		// and also per namespace+bucketname
		collectObjectBucketData(objectBucketsPerNamespace, objectBuckets, collectionTime, objectCount);

		// Start collecting billing data from ECS systems
		List<Namespace> namespaceList = getNamespaces();

		long objCounter = 0;

		final List<NamespaceBillingInfo> namespaceBillingInfos = new ArrayList<>();
		for (Namespace namespace : namespaceList) {
			// Initial billing request for current namespace
			NamespaceRequest namespaceRequest = new NamespaceRequest();
			namespaceRequest.setName(namespace.getName());
			if (objectBucketsPerNamespace.get(namespace.getName()) != null
					&& !objectBucketsPerNamespace.get(namespace.getName()).isEmpty()) {
				// There are buckets in that namespace
				// indicate to client to include bucket data
				namespaceRequest.setIncludeBuckets(true);
			}
			NamespaceBillingInfo namespaceBillingResponse = client.getNamespaceBillingInfo(namespaceRequest);

			if (namespaceBillingResponse == null) {
				continue;
			}

			// add object bucket attributes
			for (BucketBillingInfo bucketBillingInfo : namespaceBillingResponse.getBucketBillingInfo()) {

				NamespaceBucketKey namespaceBucketKey = new NamespaceBucketKey(namespace.getName(),
						bucketBillingInfo.getName());
				ObjectBucket objectBucket = objectBuckets.get(namespaceBucketKey);

				if (objectBucket != null) {
					// set api type
					bucketBillingInfo.setApiType(objectBucket.getApiType());
					// set namespace
					bucketBillingInfo.setNamespace(namespace.getName());
				} else {
					// set api type
					bucketBillingInfo.setApiType("unknown");
					// set namespace
					bucketBillingInfo.setNamespace(namespace.getName());
				}
				objCounter++;
			}

			// Push collected info into datastore
			namespaceBillingInfos.add(namespaceBillingResponse);
			if (this.billingDAO != null) {
				// insert something
				billingDAO.insert(namespaceBillingResponse, collectionTime);
			}

			// collect n subsequent pages
			while (namespaceRequest.getNextMarker() != null) {
				namespaceBillingResponse = client.getNamespaceBillingInfo(namespaceRequest);

				if (namespaceBillingResponse != null) {
					namespaceRequest.setNextMarker(namespaceBillingResponse.getNextMarker());

					// add object bucket attributes
					for (BucketBillingInfo bucketBillingInfo : namespaceBillingResponse.getBucketBillingInfo()) {
						ObjectBucket objectBucket = objectBuckets.get(bucketBillingInfo.getName());

						if (objectBucket != null) {
							// set api type
							bucketBillingInfo.setApiType(objectBucket.getApiType());
							// set namespace
							bucketBillingInfo.setNamespace(namespace.getName());
						}
						objCounter++;
					}

					// Push collected info into datastore
					namespaceBillingInfos.add(namespaceBillingResponse);
					if (this.billingDAO != null) {
						// insert something
						billingDAO.insert(namespaceBillingResponse, collectionTime);
					}
				} else {
					namespaceRequest.setNextMarker(null);
				}
			}
		}

		// peg global counter
		objectCount.getAndAdd(objCounter);
		return namespaceBillingInfos;

	}

	/**
	 * Collects Bucket metadata for all namespace defined on a cluster
	 * 
	 * @param objectBucketMap
	 *            - Object Bucket Map
	 */
	public void getObjectBucketData(Map<NamespaceBucketKey, ObjectBucket> objectBucketMap, AtomicLong objectCount) {

		// no namespace map required
		// no collection time required
		// no DAO required
		collectObjectBucketData(null, objectBucketMap, null, objectCount);

	}

	/**
	 * 
	 * @param collectionTime
	 * @return
	 */
	public List<ObjectBuckets> collectObjectBuckets(Date collectionTime, AtomicLong objectCount ) {
		return this.collectObjectBucketData(null, null, collectionTime, objectCount);
	}

	/**
	 * Collects Bucket metadata for all namespace defined on a cluster
	 * 
	 * @param objectBucketMap
	 *            - Object Bucket Map
	 * @param collectionTime
	 *            - Collection Time
	 * @param billDAO
	 *            - Billing DAO Object
	 */
	private List<ObjectBuckets> collectObjectBucketData(Map<String, Set<ObjectBucket>> objectPerNamespaceMap,
			Map<NamespaceBucketKey, ObjectBucket> objectBucketMap, Date collectionTime, AtomicLong objectCount) {

		// Start collecting billing data from ECS systems
		List<Namespace> namespaceList = getNamespaces();

		// At this point we should have all the namespace supported by the ECS
		// system

		long objCounter = 0;

		final List<ObjectBuckets> objectBuckets = new ArrayList<>();
		for (Namespace namespace : namespaceList) {

			// ===============================================
			// Initial billing request for current namespace
			// ===============================================

			NamespaceRequest namespaceRequest = new NamespaceRequest();
			namespaceRequest.setName(namespace.getName());
			ObjectBuckets objectBucketsResponse = client.getNamespaceBucketInfo(namespaceRequest);

			if (objectBucketsResponse == null) {
				continue;
			}

			LOGGER.info("Collect Billing Data for namespace: " + namespace.getName());

			objCounter += (objectBucketsResponse.getObjectBucket() != null)
					? objectBucketsResponse.getObjectBucket().size() : 0;

			// Push collected info into datastore
			objectBuckets.add(objectBucketsResponse);
			if (this.billingDAO != null) {
				this.billingDAO.insert(objectBucketsResponse, collectionTime);
			}

			// Add to return map per namespace and bucket key
			if (objectBucketsResponse.getObjectBucket() != null && objectBucketMap != null) {
				for (ObjectBucket objectBucket : objectBucketsResponse.getObjectBucket()) {
					NamespaceBucketKey key = new NamespaceBucketKey(namespace.getName(), objectBucket.getName());
					objectBucketMap.put(key, objectBucket);
				}
			}

			// Add to return map per namespace key
			if (objectBucketsResponse.getObjectBucket() != null && objectPerNamespaceMap != null) {
				Set<ObjectBucket> objectSet = objectPerNamespaceMap.get(namespace.getName());
				if (objectSet == null) {
					// there isn;t already a set present for that namespace
					// create one
					objectSet = new HashSet<ObjectBucket>();
					// add reference of set to map
					objectPerNamespaceMap.put(namespace.getName(), objectSet);
				}
				for (ObjectBucket objectBucket : objectBucketsResponse.getObjectBucket()) {
					// add all object to set
					objectSet.add(objectBucket);
				}
			}

			// collect n subsequent pages
			while (namespaceRequest.getNextMarker() != null) {
				objectBucketsResponse = client.getNamespaceBucketInfo(namespaceRequest);
				if (objectBucketsResponse != null) {
					objCounter += (objectBucketsResponse.getObjectBucket() != null)
							? objectBucketsResponse.getObjectBucket().size() : 0;
					namespaceRequest.setNextMarker(objectBucketsResponse.getNextMarker());

					// Push collected info into datastore
					objectBuckets.add(objectBucketsResponse);
					if (this.billingDAO != null) {
						// insert something
						this.billingDAO.insert(objectBucketsResponse, collectionTime);
					}

					// Add to return map
					if (objectBucketsResponse.getObjectBucket() != null && objectBucketMap != null) {
						for (ObjectBucket objectBucket : objectBucketsResponse.getObjectBucket()) {
							NamespaceBucketKey key = new NamespaceBucketKey(namespace.getName(),
									objectBucket.getName());
							objectBucketMap.put(key, objectBucket);
						}
					}

					// Add to return map per namespace key
					if (objectBucketsResponse.getObjectBucket() != null && objectPerNamespaceMap != null) {
						Set<ObjectBucket> objectSet = objectPerNamespaceMap.get(namespace.getName());
						if (objectSet == null) {
							// there isn;t already a set present for that
							// namespace
							// create one
							objectSet = new HashSet<ObjectBucket>();
							// add reference of set to map
							objectPerNamespaceMap.put(namespace.getName(), objectSet);
						}
						for (ObjectBucket objectBucket : objectBucketsResponse.getObjectBucket()) {
							// add all object to set
							objectSet.add(objectBucket);
						}
					}

				} else {
					// stop the loop
					namespaceRequest.setNextMarker(null);
				}
			}
		}

		// peg global counter
		objectCount.getAndAdd(objCounter);
		return objectBuckets;
	}

	public void shutdown() {
		if (this.client != null) {
			client.shutdown();
		}
		if (billingDAO!=null) {
			((ServiceNowDAO)billingDAO).close();
		}
	}

	/**
	 * Gathers all namespaces present on a cluster
	 * 
	 * @return List - List of namespace
	 */
	public List<Namespace> getNamespaces() {

		// Start collecting billing data from ECS systems
		List<Namespace> namespaceList = new ArrayList<Namespace>();
		// collect namespace names
		ListNamespaceRequest listNamespaceRequest = new ListNamespaceRequest();
		// first batch
		ListNamespacesResult namespacesResult = client.listNamespaces(listNamespaceRequest);
		namespaceList.addAll(namespacesResult.getNamespaces());

		// n subsequent batches
		while (namespacesResult.getNextMarker() != null) {
			listNamespaceRequest.setNextMarker(namespacesResult.getNextMarker());
			namespacesResult = client.listNamespaces(listNamespaceRequest);
			if (namespacesResult.getNamespaces() != null) {
				namespaceList.addAll(namespacesResult.getNamespaces());
			}
		}

		return namespaceList;
	}
}
