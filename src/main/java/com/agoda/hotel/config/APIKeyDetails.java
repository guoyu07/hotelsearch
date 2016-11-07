package com.agoda.hotel.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agoda.hotel.config.APIAccessStatus.APIStatus;
import com.agoda.hotel.ratelimit.RateLimiter;

/**
 * This reads the keys.properties and keeps it in memory for later use.
 *
 * @author Shivam Khare
 */
public class APIKeyDetails {

	private static final Log logger = LogFactory.getLog(APIKeyDetails.class);

	private static final int SUSPENSION_TIME = 5 * 60 * 1000; // suspended for 5 mins
	private static final String WIN_SUFFIX = ".window.sec";
	private static final String REQ_THRESHOLD_SUFFIX = ".request.threshold";

	private Map<String, List<Rules>> rules;
	private Map<String, RateLimiter> apiKeyRateLimiter;
	private Map<String, Long> suspendedKeys;

	private APIKeyStore apiKeyStore;

	private Properties properties = null;

	public APIKeyDetails() {
		rules = new HashMap<String, List<Rules>>();
		apiKeyRateLimiter = new HashMap<String, RateLimiter>();
		suspendedKeys = new HashMap<String, Long>();
	}

	public void initialize() {
		Set<String> apiKeys = apiKeyStore.getApiKeys();

		try {

			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("keys.properties");
			properties = new Properties();
			properties.load(inputStream);
			for (String key : apiKeys) {
				String temp = properties.getProperty(getKeyThresholdPropertyName(key));
				int threshold = (temp == null) ? getDefaultThreshold() : Integer.parseInt(temp);

				temp = properties.getProperty(getKeyWindowSizePropertyName(key));
				int window = (temp == null) ? getDefaultWindowSize() : Integer.parseInt(temp);

				RateLimitRules rateLimitRule = new RateLimitRules(window, threshold);

				RateLimiter rateLimiter = new RateLimiter(rateLimitRule);
				apiKeyRateLimiter.put(key, rateLimiter);
				applyRules(key, rateLimitRule);
			}
		} catch (Exception e) {
			logger.error("Error while reading keys.properties, applying default for all");
			Rules defaultRateLimitRules = new RateLimitRules(getDefaultThreshold(), getDefaultWindowSize());
			for (String key : apiKeys) {
				apiKeyRateLimiter.put(key, new RateLimiter((RateLimitRules) defaultRateLimitRules));
				applyRules(key, defaultRateLimitRules);
			}
		}
	}

	private String getKeyWindowSizePropertyName(String key) {
		return String.format("%s%s", key, WIN_SUFFIX);
	}

	private int getDefaultWindowSize() {
		return Integer.parseInt(properties.getProperty("default.window.sec"));
	}

	private int getDefaultThreshold() {
		return Integer.parseInt(properties.getProperty("default.request.threshold"));
	}

	private String getKeyThresholdPropertyName(String key) {
		return String.format("%s%s", key, REQ_THRESHOLD_SUFFIX);
	}

	private void applyRules(String apikey, Rules rule) {
		List<Rules> apiKeyRules = null;
		if (rules.containsKey(apikey)) {
			apiKeyRules = rules.get(apikey);
		} else {
			apiKeyRules = new ArrayList<Rules>();
			rules.put(apikey, apiKeyRules);
		}
		apiKeyRules.add(rule);
	}

	private void suspendKey(String apiKey) {
		long fiveMinutes = System.currentTimeMillis() + SUSPENSION_TIME;
		suspendedKeys.put(apiKey, fiveMinutes);
	}

	public boolean isKeyInSuspenstion(String apiKey) {
		boolean suspended = false;
		if (suspendedKeys.containsKey(apiKey)) {
			if (System.currentTimeMillis() < suspendedKeys.get(apiKey)) {
				suspended = true;
			} else {
				suspendedKeys.remove(apiKey);
			}
		}
		return suspended;
	}

	public APIAccessStatus apiPermission(String apiKey) {
		APIAccessStatus status = new APIAccessStatus();
        if (apiKeyStore.isValidKey(apiKey)) {
            if (isKeyInSuspenstion(apiKey)) {
            	status.setStatus(APIStatus.SUSPENDED);
            } else {
                if (canAccessNow(apiKey)) {
                    status.setAllowed(true);
                    status.setStatus(APIStatus.ACTIVE);
                } else {
                    suspendKey(apiKey);
                    status.setStatus(APIStatus.SUSPENDED);
                }

            }
        } else {
            status.setStatus(APIStatus.INVALID_KEY);
        }
        return status;
    }
	
	private boolean canAccessNow(String apiKey) {
        RateLimiter rateLimmiter = apiKeyRateLimiter.get(apiKey);
        return rateLimmiter.access();
    }

	public void setApikeyStore(APIKeyStore apikeyStore) {
		this.apiKeyStore = apikeyStore;
	}
}
