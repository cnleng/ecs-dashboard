/**
 * 
 */
package com.emc.ecs.metadata.controllers;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emc.ecs.management.entity.NamespaceBillingInfo;
import com.emc.ecs.management.entity.ObjectBuckets;
import com.emc.ecs.metadata.services.BillingService;
import com.emc.ecs.metadata.utils.Constants;

/**
 * @author nlengc
 *
 */
@RestController
@RequestMapping(value = Constants.URL_BILLING)
public class BillingController {
	
	private static final Logger LOG = Logger.getLogger(BillingController.class);
	private static final String NAMESPACE_BILLING_INFOS = "/billingInfos";
	private static final String OBJECT_BUCKETS = "/objectBuckets";
	
	@Autowired
	private BillingService billingService;
	
	@RequestMapping(value = NAMESPACE_BILLING_INFOS, method = RequestMethod.GET)
	public List<NamespaceBillingInfo> getNamespaceBillingInfo(@RequestParam(value="collectionTime", required=false) String collectionTime) throws ParseException {
		return billingService.getNamespaceBillingInfo(Constants.DATA_DATE_FORMAT.parse(collectionTime));
	}
	
	/**
	 * 
	 * @param collectionTime
	 * @return
	 */
	@RequestMapping(value = OBJECT_BUCKETS, method = RequestMethod.GET)
	List<ObjectBuckets> getObjectBuckets(@RequestParam(value="collectionTime", required=false) String collectionTime) throws ParseException {
		return billingService.getObjectBuckets(Constants.DATA_DATE_FORMAT.parse(collectionTime));
	}
}
