package com.agoda.hotel.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.agoda.hotel.data.Hotel;
import com.agoda.hotel.data.SearchRequest;
import com.agoda.hotel.data.SearchRequest.SortOrder;
import com.agoda.hotel.exceptions.InvalidInputParameterException;
import com.agoda.hotel.exceptions.ResourceNotFoundException;

@Ignore
public class TestSearchService {

	@Test
	public void nullCityTest() throws Exception {
		HotelSearchService searchService = new HotelSearchServiceImpl();
		SearchRequest searchRequest = new SearchRequest(null, SortOrder.DESC);
		List<Hotel> search = searchService.search(searchRequest);
		assertNull(search);
	}

	@Test
	public void validCityTest() throws ResourceNotFoundException, InvalidInputParameterException {
		HotelSearchService searchService = new HotelSearchServiceImpl();
		SearchRequest searchRequest = new SearchRequest("Amsterdam", SortOrder.ASC);
		List<Hotel> search = searchService.search(searchRequest);
		assertEquals(6, search.size());
	}

	@Test
	public void inValidCityTest() throws Exception {
		HotelSearchService searchService = new HotelSearchServiceImpl();
		SearchRequest searchRequest = new SearchRequest("Delhi", SortOrder.DESC);
		List<Hotel> search = searchService.search(searchRequest);
		assertNull(search);
	}

}
