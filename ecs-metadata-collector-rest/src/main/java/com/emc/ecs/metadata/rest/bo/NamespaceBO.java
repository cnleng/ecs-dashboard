/**
 * 
 */
package com.emc.ecs.metadata.rest.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.emc.ecs.management.client.ManagementClient;
import com.emc.ecs.management.client.ManagementClientConfig;
import com.emc.ecs.management.entity.ListNamespaceRequest;
import com.emc.ecs.management.entity.ListNamespacesResult;
import com.emc.ecs.management.entity.Namespace;
import com.emc.ecs.management.entity.NamespaceDetail;
import com.emc.ecs.management.entity.NamespaceQuota;
import com.emc.ecs.management.entity.NamespaceRequest;
import com.emc.ecs.metadata.dao.NamespaceDAO;
import com.emc.ecs.metadata.dao.servicenow.ServiceNowDAO;

/**
 * @author nlengc
 *
 */
public class NamespaceBO {

	private final static Logger LOGGER = Logger.getLogger(NamespaceBO.class);
	private ManagementClient client;
	private NamespaceDAO namespaceDAO;

	public NamespaceBO(String mgmtAccessKey, String mgmtSecretKey, List<String> hosts, Integer port,
			NamespaceDAO namespaceDAO) {
		// client config
		ManagementClientConfig clientConfig = new ManagementClientConfig(mgmtAccessKey, mgmtSecretKey, port, hosts);
		this.client = new ManagementClient(clientConfig);
		this.namespaceDAO = namespaceDAO;
	}

	/**
	 * Gathers all namespaces present on a cluster
	 * 
	 * @return List - List of namespace
	 */
	public List<Namespace> getNamespaces() {

		List<Namespace> namespaceList = new ArrayList<Namespace>();
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

	/**
	 * Gathers all namespaces details present on a cluster
	 * 
	 * @return List - List of namespace details
	 */
	public List<NamespaceDetail> collectNamespaceDetails(Date collectionTime, AtomicLong objectCount) {

		// Start collecting namespace data details from ECS systems
		List<Namespace> namespaceList = getNamespaces();
		// At this point we should have all the namespace supported by the ECS
		// system
		List<NamespaceDetail> namespaceDetails = new ArrayList<>();
		long objCounter = 0;
		for (Namespace namespace : namespaceList) {
			LOGGER.info("Collecting Details for namespace: " + namespace.getName());
			NamespaceDetail namespaceDetail = client.getNamespaceDetails(namespace.getId());

			if (namespaceDetail == null) {
				continue;
			}
			objCounter++;
			// Push collected details into datastore
			namespaceDetails.add(namespaceDetail);
			if (namespaceDAO != null) {
				// insert something
				namespaceDAO.insert(namespaceDetail, collectionTime);
			}
		}
		
		// peg global counter
		objectCount.getAndAdd(objCounter);
		return namespaceDetails;
	}
	
	/**
	 * Gathers all namespaces quota present on a cluster
	 * 
	 * @return List - List of namespace quota
	 */
	public List<NamespaceQuota> collectNamespaceQuota(Date collectionTime, AtomicLong objectCount) {

		// Start collecting namespace data quota from ECS systems
		List<Namespace> namespaceList = getNamespaces();
		List<NamespaceQuota> namespaceQuotas = new ArrayList<>();
		long objCounter = 0;

		for (Namespace namespace : namespaceList) {
			
			NamespaceRequest namespaceRequest = new NamespaceRequest();
			namespaceRequest.setName(namespace.getName());
			LOGGER.info("Collecting Quota Details for namespace: " + namespace.getName());
			NamespaceQuota namespaceQuota = client.getNamespaceQuota(namespaceRequest);

			if (namespaceQuota == null) {
				continue;
			}
			objCounter++;

			// Push collected details into datastore
			if (namespaceDAO != null) {
				namespaceQuotas.add(namespaceQuota);
				// insert something
				namespaceDAO.insert(namespaceQuota, collectionTime);
			}

		}

		// peg global counter
		objectCount.getAndAdd(objCounter);
		return namespaceQuotas;
	}

	public void shutdown() {
		if(this.client != null) {
			client.shutdown();
		}
		if (namespaceDAO!=null) {
			namespaceDAO.close();
		}
	}

}
