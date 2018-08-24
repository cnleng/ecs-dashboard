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
	protected final MQTT mqtt;
	protected final String host;
	protected final int port;
	
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
		BlockingConnection connection = null;
		try {
			connection = this.mqtt.blockingConnection();
			connection.connect();
			connection.publish(topic, json.getBytes(), QoS.AT_LEAST_ONCE, false);
		} catch (Exception e) {
			LOG.error("An error occured while posting data to MQTT Broker: ", e);
		} finally {
			if (connection!=null) {
				try {
					connection.disconnect();
				} catch (Exception e) {
					LOG.error("An error occured while closing MQTT connection: ", e);
				}
			}
		}
	}
	
}
