/**
 * 
 */
package com.emc.ecs.metadata.dao.servicenow;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * @author nlengc
 *
 */
public abstract class ServiceNowDAO {
	private static final Logger LOG = Logger.getLogger(ServiceNowDAO.class);
	protected CloseableHttpClient client;
	protected String url;

	public ServiceNowDAO(ServiceNowDAOConfig config) {
		final CredentialsProvider provider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(config.getUsername(),
				config.getPassword());
		provider.setCredentials(AuthScope.ANY, credentials);
		this.client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		this.url = config.getInstanceUrl();
	}
	
	
	/**
	 * @param httpPost
	 * @param json
	 */
	protected void postData(final String endPoint, final String json) {
		try {
			final HttpPost httpPost = new HttpPost(endPoint);
			final HttpEntity body = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(body);
			httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			CloseableHttpResponse response = this.client.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} catch (IOException e) {
			LOG.error("An error occured while posting data to service-now instance: ",e);
		}
	}
}
