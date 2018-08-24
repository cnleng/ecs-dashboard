/**
 * 
 */
package com.emc.ecs.metadata.dao.mqtt;

import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

/**
 * @author nlengc
 *
 */
public abstract class MqttDAO {
	
	private static final Logger LOG = Logger.getLogger(MqttDAO.class);
	protected MQTT mqtt;
	protected String host;
	protected int port;
	
	public MqttDAO(MqttDAOConfig config) throws URISyntaxException {
		this.host = config.getHost();
		this.port = config.getPort();
		this.mqtt = new MQTT();
		this.mqtt.setHost(this.host, this.port);
	}
	
	/**
	 * 
	 * @param endPoint
	 * @param json
	 */
	protected void postData(final String topic, final String json) {
		try {
			final BlockingConnection connection = this.mqtt.blockingConnection();
			connection.connect();
			connection.publish(topic, json.getBytes(), QoS.AT_LEAST_ONCE, false);
		} catch (Exception e) {
			LOG.error("An error occured while posting data to service-now instance: ", e);
		}
	}
}
