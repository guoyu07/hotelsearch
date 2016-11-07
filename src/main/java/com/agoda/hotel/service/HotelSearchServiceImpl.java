package com.agoda.hotel.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.agoda.hotel.data.Hotel;
import com.agoda.hotel.data.HotelDatabase;
import com.agoda.hotel.data.SearchRequest;
import com.agoda.hotel.exceptions.InvalidInputParameterException;
import com.agoda.hotel.exceptions.ResourceNotFoundException;

/**
 * HotelServiceImpl implements the hotel search for a given search request.
 * 
 * @author Shivam Khare
 */
public class HotelSearchServiceImpl implements HotelSearchService {

	private HotelDatabase hotelDatabase;

	public HotelSearchServiceImpl() {
		this.hotelDatabase = HotelDatabase.getInstance();
	}

	@Override
	public List<Hotel> search(SearchRequest searchRequest) throws ResourceNotFoundException, InvalidInputParameterException {
		List<Hotel> cityHotels = null;
		
		cityHotels = hotelDatabase.getCityHotels(searchRequest.getCity());
		final int sortFactor = SearchRequest.SortOrder.DESC == searchRequest.getSortOrder() ? -1 : 1;
		Collections.sort(cityHotels, new Comparator<Hotel>(){

			@Override
			public int compare(Hotel h1, Hotel h2) {
				return sortFactor * ((h1.getRoom().getPrice() - h2.getRoom().getPrice()) < 0 ? -1:1);
			}
			
		});
		return cityHotels;
	}
	
}
