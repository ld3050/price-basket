package com.bjss.pricebasket.service.mock;

import java.util.HashMap;

import com.bjss.pricebasket.service.MsgService;

public class MockMsgService implements MsgService {

	private HashMap<String, String> messageMap = new HashMap<String, String>();

	@Override
	public String getMessage(String key, Object... params) {
		String messageValue = messageMap.get(key);
		if (params.length > 0) {
			return String.format(messageValue, params);
		}
		return messageValue;
	}

	/**
	 * For simplicity, unlike the real resource bundle, parameters in the key
	 * are specified with %s instead of {0}, {1}, etc.
	 * 
	 * @param key
	 * @param value
	 */
	public void addMessage(String key, String value) {
		messageMap.put(key, value);
	}

}
