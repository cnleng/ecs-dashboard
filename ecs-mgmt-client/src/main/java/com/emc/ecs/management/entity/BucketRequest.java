/**
 * 
 */
package com.emc.ecs.management.entity;

/**
 * @author nlengc
 *
 */
public class BucketRequest {
	private String nextMarker;
	private String name;
	private String namespace;
	private boolean includeBuckets = false;

	public String getNextMarker() {
		return nextMarker;
	}

	public void setNextMarker(String nextMarker) {
		this.nextMarker = nextMarker;
	}

	public final String getNamespace() {
		return namespace;
	}

	public final void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIncludeBuckets() {
		return includeBuckets;
	}

	public void setIncludeBuckets(boolean includeBuckets) {
		this.includeBuckets = includeBuckets;
	}
}
