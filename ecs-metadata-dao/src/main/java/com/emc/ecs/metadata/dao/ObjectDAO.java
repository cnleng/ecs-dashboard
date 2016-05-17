package com.emc.ecs.metadata.dao;

import java.util.Date;

import com.emc.object.s3.bean.ListObjectsResult;
import com.emc.object.s3.bean.QueryObjectsResult;

public interface ObjectDAO {

	
	public void insert( ListObjectsResult listObjectsResult, String namespace,
						String bucketName, Date collectionTime );
	
	public void insert( QueryObjectsResult queryObjectsResult, String namespace,
						String bucketName, Date collectionTime );
	
	
}
