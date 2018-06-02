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

import com.emc.ecs.management.entity.BucketOwner;
import com.emc.ecs.management.entity.VdcDetails;
import com.emc.ecs.metadata.services.VdcService;
import com.emc.ecs.metadata.utils.Constants;

/**
 * @author nlengc
 *
 */
@RestController
@RequestMapping(value = Constants.URL_VDC)
public class VdcController {
	private static final Logger LOG = Logger.getLogger(VdcController.class);
	private static final String VDC_DETAILS = "/vdcDetails";
	private static final String BUCKET_OWNERS = "/bucketOwners";
	
	@Autowired
	private VdcService vdcService;
	
	@RequestMapping(value = VDC_DETAILS, method = RequestMethod.GET)
	public List<VdcDetails> getVdcDetails(@RequestParam(value="collectionTime", required=false) String collectionTime) throws ParseException {
		return vdcService.getVdcDetails(Constants.DATA_DATE_FORMAT.parse(collectionTime));
	}
	
	/**
	 * 
	 * @param collectionTime
	 * @return
	 */
	@RequestMapping(value = BUCKET_OWNERS, method = RequestMethod.GET)
	List<BucketOwner> getBucketOwners(@RequestParam(value="collectionTime", required=false) String collectionTime) throws ParseException {
		return vdcService.getBucketOwners(Constants.DATA_DATE_FORMAT.parse(collectionTime));
	}
}
