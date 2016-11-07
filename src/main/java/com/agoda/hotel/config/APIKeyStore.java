package com.agoda.hotel.config;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class loads the valid api keys and keeps it in memory.
 *
 * @author Shivam Khare
 */
public class APIKeyStore {

	private final static Log logger = LogFactory.getLog(APIKeyStore.class);
	private Set<String> apiKeys = new HashSet<String>();

	public void loadAPIKeys() {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("APIKeys.store");

		try {
			String input = IOUtils.toString(inputStream);
			String keys[] = input.split("\n");

			for (String key : keys) {
				apiKeys.add(key.trim());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public Set<String> getApiKeys() {
		return apiKeys;
	}

	public boolean isValidKey(String key) {
		return key == null ? false : apiKeys.contains(key);
	}
}
