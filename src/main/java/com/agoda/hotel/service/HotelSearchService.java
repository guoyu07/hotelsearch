package com.agoda.hotel.service;

import java.util.List;

import com.agoda.hotel.data.Hotel;
import com.agoda.hotel.data.SearchRequest;
import com.agoda.hotel.exceptions.InvalidInputParameterException;
import com.agoda.hotel.exceptions.ResourceNotFoundException;

/**
 * Hotel search interface.
 *
 * @author Shivam Khare
 */
public interface HotelSearchService {

	List<Hotel> search(SearchRequest searchRequest) throws ResourceNotFoundException, InvalidInputParameterException;

}
