package com.bjss.pricebasket.service;

/**
 * Service for retrieving resource properties for the System's Locale.
 * 
 * @author Leon Danser
 * 
 */
public interface MsgService {

	/**
	 * Returns a localised property from a resource bundle
	 * 
	 * @param key
	 *            the property identifier
	 * @param params
	 *            values to inject into a parameterised resource entry
	 * @return the localised resource message
	 */
	String getMessage(String key, Object... params);

}
