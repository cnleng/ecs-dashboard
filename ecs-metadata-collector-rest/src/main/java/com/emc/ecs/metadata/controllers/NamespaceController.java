/**
 * 
 */
package com.emc.ecs.metadata.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@RequestMapping(value = NAMESPACE_DETAILS, method = RequestMethod.POST)
	public void postNamespaceDetails(@RequestParam(value="relativeDayShift", required=false) Integer relativeDayShift)  {
		namespaceService.postNamespaceDetails(Constants.getCollectionTime(relativeDayShift));
	}
	
	@RequestMapping(value = NAMESPACE_QUOTAS, method = RequestMethod.POST)
	public void postNamespaceQuotas(@RequestParam(value="relativeDayShift", required=false) Integer relativeDayShift) {
		namespaceService.postNamespaceQuotas(Constants.getCollectionTime(relativeDayShift));
	}
}
