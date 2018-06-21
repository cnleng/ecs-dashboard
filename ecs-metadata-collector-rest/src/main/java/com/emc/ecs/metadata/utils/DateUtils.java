/**
 * 
 */
package com.emc.ecs.metadata.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author nlengc
 *
 */
public final class DateUtils {
	/**
	 * 
	 * @return
	 */
	public static final Date getCollectionTime(Integer relativeDayShift) {
		Date collectionTime = new Date(System.currentTimeMillis());
		if (relativeDayShift != null && relativeDayShift != 0) {
			Long epochTime = collectionTime.getTime();
			Long daysShift = TimeUnit.DAYS.toMillis(relativeDayShift);
			return new Date(epochTime - daysShift);
		} else {
			return collectionTime;
		}
	}
}
