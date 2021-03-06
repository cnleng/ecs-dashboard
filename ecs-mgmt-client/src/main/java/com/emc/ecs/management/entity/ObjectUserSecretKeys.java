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


package com.emc.ecs.management.entity;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "user_secret_keys")
@XmlType(propOrder = {"secretKey1", "keyTimestamp1", "keyExpiryTimestamp1", "secretKey2", "keyTimestamp2", "keyExpiryTimestamp2", "link"})
public class ObjectUserSecretKeys {
	
	public final static String SECRET_KEY_1   		  = "secret_key_1";
	public final static String KEY_TIMESTAMP_1 		  = "key_timestamp_1";
	public final static String KEY_EXPIRY_TIMESTAMP_1 = "key_expiry_timestamp_1";
	public final static String SECRET_KEY_2   		  = "secret_key_2";
	public final static String KEY_TIMESTAMP_2 		  = "key_timestamp_2";
	public final static String KEY_EXPIRY_TIMESTAMP_2 = "key_expiry_timestamp_2";
	public final static String LINK 				  = "link";
	
	private String secretKey1;
	private String keyTimestamp1;
	private String keyExpiryTimestamp1;
	private String secretKey2;
    private String keyTimestamp2;
    private String keyExpiryTimestamp2;
    private String link;
    

	//private String 
    @XmlElement(name = SECRET_KEY_1)
	public String getSecretKey1() {
		return secretKey1;
	}
	public void setSecretKey1(String secretKey1) {
		this.secretKey1 = secretKey1;
	}
	
	@XmlElement(name = KEY_TIMESTAMP_1)
	public String getKeyTimestamp1() {
		return keyTimestamp1;
	}
	public void setKeyTimestamp1(String keyTimestamp1) {
		this.keyTimestamp1 = keyTimestamp1;
	}

	@XmlElement(name = KEY_EXPIRY_TIMESTAMP_1)
	public String getKeyExpiryTimestamp1() {
		return keyExpiryTimestamp1;
	}
	public void setKeyExpiryTimestamp1(String keyExpiryTimestamp1) {
		this.keyExpiryTimestamp1 = keyExpiryTimestamp1;
	}
	
	 @XmlElement(name = SECRET_KEY_2)
	public String getSecretKey2() {
		return secretKey2;
	}
	public void setSecretKey2(String secretKey2) {
		this.secretKey2 = secretKey2;
	}
	
	@XmlElement(name = KEY_TIMESTAMP_2)
	public String getKeyTimestamp2() {
		return keyTimestamp2;
	}
	public void setKeyTimestamp2(String keyTimestamp2) {
		this.keyTimestamp2 = keyTimestamp2;
	}
	
	 @XmlElement(name = KEY_EXPIRY_TIMESTAMP_2)
	public String getKeyExpiryTimestamp2() {
		return keyExpiryTimestamp2;
	}
	 
	public void setKeyExpiryTimestamp2(String keyExpiryTimestamp2) {
		this.keyExpiryTimestamp2 = keyExpiryTimestamp2;
	}
    
	@XmlElement(name = LINK)
    public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

   
}
