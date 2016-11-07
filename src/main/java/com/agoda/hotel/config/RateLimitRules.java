package com.agoda.hotel.config;

public class RateLimitRules implements Rules {

	private int window;

	private int threshold;

	public RateLimitRules(int window, int threshold) {
		this.window = window;
		this.threshold = threshold;
	}

	public int getWindow() {
		return window;
	}

	public int getThreshold() {
		return threshold;
	}

}
