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

import com.emc.ecs.management.entity.NamespaceDetail;
import com.emc.ecs.management.entity.NamespaceQuota;
import com.emc.ecs.metadata.services.NamespaceService;
import com.emc.ecs.metadata.utils.Constants;

/**
 * @author nlengc
 *
 */
@RestController
@RequestMapping(value = Constants.URL_NAMESPACE)
public class NamespaceController {
	
	private static final Logger LOG = Logger.getLogger(NamespaceController.class);
	private static final String NAMESPACE_DETAILS = "/details";
	private static final String NAMESPACE_QUOTAS = "/quotas";
	
	@Autowired
	private NamespaceService namespaceService;
	
	@RequestMapping(value = NAMESPACE_DETAILS, method = RequestMethod.GET)
	public List<NamespaceDetail> getNamespaceDetails(@RequestParam(value="collectionTime", required=false) String collectionTime) throws ParseException {
		return namespaceService.getNamespaceDetails(Constants.DATA_DATE_FORMAT.parse(collectionTime));
	}
	
	/**
	 * 
	 * @param collectionTime
	 * @return
	 */
	@RequestMapping(value = NAMESPACE_QUOTAS, method = RequestMethod.GET)
	List<NamespaceQuota> getNamespaceQuotas(@RequestParam(value="collectionTime", required=false) String collectionTime) throws ParseException {
		return namespaceService.getNamespaceQuotas(Constants.DATA_DATE_FORMAT.parse(collectionTime));
	}
	
}
