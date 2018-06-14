/**
 * 
 */
package com.emc.ecs.metadata.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.emc.ecs.management.entity.Attribute;
import com.emc.ecs.management.entity.NamespaceDetail;
import com.emc.ecs.management.entity.NamespaceQuota;
import com.emc.ecs.management.entity.UserMapping;
import com.emc.ecs.management.entity.Vdc;

/**
 * @author nlengc
 *
 */
public class Constants {

	// REST End Points
	public static final String URL_NAMESPACE = "/namespaces";
	public static final String URL_BILLING = "/billings";
	public static final String URL_OBJECT = "/objects";
	public static final String URL_VDC = "/vdc";
	public static final String PARAM_RELATIVE_DAY_SHIFT = "/relativeDayShift";

	public static final String DATA_DATE_PATTERN = "yyyy-MM-dd";
	public static final SimpleDateFormat DATA_DATE_FORMAT = new SimpleDateFormat(DATA_DATE_PATTERN);

	/**
	 * 
	 * @return
	 */
	public static final NamespaceDetail getDummyNamespaceDetail() {
		final String URL = "http://localhost:8080";
		final NamespaceDetail detail = new NamespaceDetail();
		try {
			final URI URI = new URI(URL);
			final List<java.net.URI> URI_LIST = Arrays.asList(URI);

			// set attributes
			final Attribute attribute = new Attribute();
			attribute.setKey("key");
			attribute.setValues(Arrays.asList("values"));

			// set user mappings
			final UserMapping userMapping = new UserMapping();
			userMapping.setAttributes(Arrays.asList(attribute));
			userMapping.setDomain("domain");
			userMapping.setGroups(Arrays.asList("groups"));

			// set vdc
			final Vdc vdc = new Vdc();
			vdc.setId(URI);
			vdc.setLink("link");

			detail.setAllowedVPools(URI_LIST);
			detail.setCreationTime(new Date());
			detail.setDefaultBucketBlockSize(4096L);
			detail.setDefaultDataServicesVPool(URI);
			detail.setDisallowedVPools(URI_LIST);
			detail.setExternalGroupAdmins("externalGroupAdmins");
			detail.setGlobal(true);
			detail.setId(URI);
			detail.setInactive(true);
			detail.setInternal(true);
			detail.setIsComplianceEnabled(true);
			detail.setIsEncryptionEnabled(true);
			detail.setIsStaledAllowed(true);
			detail.setLink("link");
			detail.setName("name");
			detail.setNamespaceAdmins("namespaceAdmins");
			detail.setRemote(true);
			detail.setUserMappings(Arrays.asList(userMapping));
			detail.setVdc(vdc);
			return detail;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final NamespaceQuota getDummyNamespaceQuota() {
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public static final Date getCollectionTime(Integer relativeDayShift) {
		Date collectionTime = new Date(System.currentTimeMillis());
		if (relativeDayShift != null && relativeDayShift != 0) {
			Long epochTime = collectionTime.getTime();
			Long daysShift = TimeUnit.DAYS.toMillis(relativeDayShift);
			return new Date(epochTime - daysShift);
		} else {
			return collectionTime;
		}
	}

}
