/**
 * 
 */
package com.emc.ecs.metadata.controllers.mqtt;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emc.ecs.metadata.services.BillingService;
import com.emc.ecs.metadata.utils.Constants;

/**
 * @author nlengc
 *
 */
@RestController
@RequestMapping(value = Constants.MQTT + Constants.URL_BILLING)
public class MqttBillingController {

	private static final Logger LOG = Logger.getLogger(MqttBillingController.class);
	private static final String NAMESPACE_BILLING_INFOS = "/billinginfos";
	private static final String OBJECT_BUCKETS = "/objectbuckets";

	@Autowired
	@Qualifier("mqttBillingService")
	private BillingService billingService;

	@RequestMapping(value = NAMESPACE_BILLING_INFOS, method = RequestMethod.POST)
	public void postNamespaceBillingInfo(
			@RequestParam(value = "relativeDayShift", required = false) Integer relativeDayShift) {
		billingService.postNamespaceBillingInfo(Constants.getCollectionTime(relativeDayShift));
	}

	@RequestMapping(value = OBJECT_BUCKETS, method = RequestMethod.POST)
	public void postObjectBuckets(
			@RequestParam(value = "relativeDayShift", required = false) Integer relativeDayShift) {
		billingService.postObjectBuckets(Constants.getCollectionTime(relativeDayShift));
	}
}
