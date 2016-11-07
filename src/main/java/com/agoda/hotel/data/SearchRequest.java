package com.agoda.hotel.data;

/**
 * This is a bean which stores our search request parameters.
 *
 * @author Shivam Khare
 */
public class SearchRequest {

	public enum SortOrder {
		ASC, DESC;
	}

	private String city;

	private SortOrder sortOrder;

	public SearchRequest(String city, SortOrder sortOrder) {
		this.city = city;
		this.sortOrder = sortOrder;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

}
