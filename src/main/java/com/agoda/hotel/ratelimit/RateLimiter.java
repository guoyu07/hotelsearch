package com.agoda.hotel.ratelimit;

import java.util.concurrent.atomic.AtomicInteger;

import com.agoda.hotel.config.RateLimitRules;

/**
 * RateLimiter uses atomic integer to maintain count for number of access.
 * Synchronized block is used when checking for the counter value and incrementing it. 
 * This will prevent multiple threads trying to access at same time. 
 *
 * @author Shivam Khare
 */
public class RateLimiter {

	private AtomicInteger counter;
	private int threshold;
	private int window;
	private long windowStart;
	private long windowEnd;
	
	public RateLimiter(RateLimitRules rule) {
		this(rule.getThreshold(), rule.getWindow());
	}

	public RateLimiter(int threshold, int window) {
		this.threshold = threshold;
		this.window = window;
		counter = new AtomicInteger();
		createWindow();
	}

	private void createWindow() {
		windowStart = System.currentTimeMillis();
        windowEnd = windowStart + window * 1000;
        counter.set(0);
	}
	
	public boolean access() {
        boolean access = false;
        long now = System.currentTimeMillis();
        synchronized (this) {
            if (windowStart <= now && windowEnd >= now) {
                int currentCount = counter.incrementAndGet();
                if (currentCount <= threshold) {
                    access = true;
                }
            } else {
                createWindow();
                access = access();
            }
        }
        return access;
    }
}
