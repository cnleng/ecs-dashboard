/**
 * 
 */
package com.emc.ecs.metadata.controllers.mqtt;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emc.ecs.metadata.services.S3ObjectService;
import com.emc.ecs.metadata.utils.Constants;

/**
 * @author nlengc
 *
 */
@RestController
@RequestMapping(value = Constants.MQTT + Constants.URL_OBJECT)
public class MqttObjectController {

	private static final String OBJECT_DATAS = "/datas";
	private static final String OBJECT_VERSIONS = "/versions";
	private static final String OBJECT_MODIFIED = "/modified";

	@Autowired
	@Qualifier("mqttS3ObjectService")
	private S3ObjectService s3ObjectService;

	@RequestMapping(value = OBJECT_DATAS, method = RequestMethod.POST)
	public void postObjectData(@RequestParam(value = "relativeDayShift", required = false) Integer relativeDayShift,
			@RequestParam(value = "namespace", required = false) String namespace,
			@RequestParam(value = "bucket", required = false) String bucket)
			throws ParseException {
		if (namespace != null) {
			if (bucket != null) {
				s3ObjectService.postObjectDataByBucket(Constants.getCollectionTime(relativeDayShift), namespace,
						bucket);
			} else {
				s3ObjectService.postObjectDataByNamespace(Constants.getCollectionTime(relativeDayShift), namespace);
			}
		} else {
			s3ObjectService.postObjectData(Constants.getCollectionTime(relativeDayShift));
		}
	}

	@RequestMapping(value = OBJECT_VERSIONS, method = RequestMethod.POST)
	public void postObjectVersions(@RequestParam(value = "relativeDayShift", required = false) Integer relativeDayShift)
			throws ParseException {
		s3ObjectService.postObjectVersions(Constants.getCollectionTime(relativeDayShift));
	}

	@RequestMapping(value = OBJECT_MODIFIED, method = RequestMethod.POST)
	public void postObjectModified(@RequestParam(value = "numberOfDays", required = true) Integer numberOfDays) throws ParseException {
		s3ObjectService.postObjectModified(Constants.getCollectionTime(null), numberOfDays);
	}
	
}
