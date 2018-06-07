/**
 * 
 */
package com.emc.ecs.metadata.controllers;

import java.text.ParseException;

import org.apache.log4j.Logger;
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
@RequestMapping(value = Constants.URL_OBJECT)
public class ObjectController {
	
	private static final Logger LOG = Logger.getLogger(ObjectController.class);
	private static final String OBJECT_DATAS = "/objectDatas";
	private S3ObjectService s3ObjectService;
	
	@RequestMapping(value = OBJECT_DATAS, method = RequestMethod.POST)
	public void postObjectData(@RequestParam(value = "collectionTime", required = false) String collectionTime)
			throws ParseException {
		s3ObjectService.postObjectData(Constants.DATA_DATE_FORMAT.parse(collectionTime));
	}
}
