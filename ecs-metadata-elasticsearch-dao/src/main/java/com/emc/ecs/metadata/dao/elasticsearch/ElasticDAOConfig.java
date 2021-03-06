/*

The MIT License (MIT)

Copyright (c) 2016 EMC Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/


package com.emc.ecs.metadata.dao.elasticsearch;

import java.util.Date;
import java.util.List;

import com.emc.ecs.metadata.dao.EcsCollectionType;


/**
 * Configuration class holding config parameters
 * for interacting with ElasticSearch
 * @author carone1
 *
 */
public class ElasticDAOConfig {
	
	//==========================
	// Private members
	//==========================
	private List<String>      hosts;
	private Integer           port;
	private String            clusterName;
	private Date              collectionTime;
	private EcsCollectionType collectionType;
	
	private String xpackUser;
	private String xpackPassword;
	private String xpackSslKey;
	private String xpackSslCertificate;
	private String xpackSslCertificateAuthorities;
	
	//==========================
	// Public Methods
	//==========================	
	public List<String> getHosts() {
		return hosts;
	}
	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}
	
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	
	public Date getCollectionTime() {
		return collectionTime;
	}
	public void setCollectionTime(Date collectionTime) {
		this.collectionTime = collectionTime;
	}
	
	public EcsCollectionType getCollectionType() {
		return this.collectionType;
	}
	
	public void setCollectionType(EcsCollectionType collectionType) {
		this.collectionType = collectionType;
	}
	public final String getXpackUser() {
		return xpackUser;
	}
	public final void setXpackUser(String xpackUser) {
		this.xpackUser = xpackUser;
	}
	public final String getXpackPassword() {
		return xpackPassword;
	}
	public final void setXpackPassword(String xpackPassword) {
		this.xpackPassword = xpackPassword;
	}
	public final String getXpackSslKey() {
		return xpackSslKey;
	}
	public final void setXpackSslKey(String xpackSslKey) {
		this.xpackSslKey = xpackSslKey;
	}
	public final String getXpackSslCertificate() {
		return xpackSslCertificate;
	}
	public final void setXpackSslCertificate(String xpackSslCertificate) {
		this.xpackSslCertificate = xpackSslCertificate;
	}
	public final String getXpackSslCertificateAuthorities() {
		return xpackSslCertificateAuthorities;
	}
	public final void setXpackSslCertificateAuthorities(String xpackSslCertificateAuthorities) {
		this.xpackSslCertificateAuthorities = xpackSslCertificateAuthorities;
	}
	
}