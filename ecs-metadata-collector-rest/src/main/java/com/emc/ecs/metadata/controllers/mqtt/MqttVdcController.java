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

import com.emc.ecs.metadata.services.VdcService;
import com.emc.ecs.metadata.utils.Constants;

/**
 * @author nlengc
 *
 */
@RestController
@RequestMapping(value = Constants.MQTT + Constants.URL_VDC)
public class MqttVdcController {
	
	private static final Logger LOG = Logger.getLogger(MqttVdcController.class);
	private static final String VDC_DETAILS = "/vdcDetails";
	private static final String BUCKET_OWNERS = "/bucketOwners";

	@Autowired
	@Qualifier("mqttVdcService")
	private VdcService vdcService;

	@RequestMapping(value = VDC_DETAILS, method = RequestMethod.POST)
	public void postVdcDetails(@RequestParam(value = "relativeDayShift", required = false) Integer relativeDayShift) {
		vdcService.postVdcDetails(Constants.getCollectionTime(relativeDayShift));
	}

	@RequestMapping(value = BUCKET_OWNERS, method = RequestMethod.POST)
	public void postBucketOwners(@RequestParam(value = "relativeDayShift", required = false) Integer relativeDayShift) {
		vdcService.postBucketOwners(Constants.getCollectionTime(relativeDayShift));
	}
}
