/**
 * 
 */
package com.emc.ecs.metadata.rest.bo;

/**
 * @author nlengc
 *
 */
public class NamespaceBucketKey {
	// ========================
	// Private members
	// ========================
	private String namespace;
	private String bucketName;

	// ========================
	// Constructor
	// ========================
	public NamespaceBucketKey(String namespace, String bucket) {
		this.namespace = namespace;
		this.bucketName = bucket;
	}

	public final String getNamespace() {
		return namespace;
	}

	public final void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public final String getBucketName() {
		return bucketName;
	}

	public final void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucketName == null) ? 0 : bucketName.hashCode());
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamespaceBucketKey other = (NamespaceBucketKey) obj;
		if (bucketName == null) {
			if (other.bucketName != null)
				return false;
		} else if (!bucketName.equals(other.bucketName))
			return false;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		return true;
	}

}
