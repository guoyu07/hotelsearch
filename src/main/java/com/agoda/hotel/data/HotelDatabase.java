package com.agoda.hotel.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agoda.hotel.exceptions.InvalidInputParameterException;
import com.agoda.hotel.exceptions.ResourceNotFoundException;

/**
 * This is a class has method parseCsv() which parse hotels.csv file and keeps the data in memory for real time access.
 * 
 * @author Shivam Khare
 */
public class HotelDatabase {

	private static final Log logger = LogFactory.getLog(HotelDatabase.class);

	private static HotelDatabase instance = null;
	private List<Hotel> hotels;

	private Map<Integer, Hotel> hotelsMap;
	private Map<String, List<Hotel>> cityHotels;

	private HotelDatabase() {
		hotels = new ArrayList<Hotel>();
		cityHotels = new HashMap<String, List<Hotel>>();
		hotelsMap = new HashMap<Integer, Hotel>();
		parseCsv();
	}

	public static HotelDatabase getInstance() {
		if(instance == null) {
			instance = new HotelDatabase();
		}
		return instance;
	}

	protected void parseCsv() {
		// take file name from properties.
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("hotels.csv");
		try {
			String data = IOUtils.toString(inputStream);
			//String data = "Bangkok,1,Deluxe,1000|Amsterdam,2,Superior,2000|Ashburn,3,Sweet Suite,1300|Amsterdam,4,Deluxe,2200|Ashburn,5,Sweet Suite,1200|Bangkok,6,Superior,2000|Ashburn,7,Deluxe,1600|Bangkok,8,Superior,2400|Amsterdam,9,Sweet Suite,30000|Ashburn,10,Superior,1100|Bangkok,11,Deluxe,60|Ashburn,12,Deluxe,1800|Amsterdam,13,Superior,1000|Bangkok,14,Sweet Suite,25000|Bangkok,15,Deluxe,900|Ashburn,16,Superior,800|Ashburn,17,Deluxe,2800|Bangkok,18,Sweet Suite,5300|Ashburn,19,Superior,1000|Ashburn,20,Superior,4444|Ashburn,21,Deluxe,7000|Ashburn,22,Sweet Suite,14000|Amsterdam,23,Deluxe,5000|Ashburn,24,Superior,1400|Ashburn,25,Deluxe,1900|Amsterdam,26,Deluxe,2300";
			String[] rows = data.split("\n");

			for (int i = 0; i < rows.length; i++) {
				String[] column = rows[i].split(",");

				String city = column[0].trim();
				int hotelId = Integer.parseInt(column[1].trim());
				String roomType = column[2].trim();
				double price = Double.parseDouble(column[3].trim());

				Room room = new Room(roomType, price);
				Hotel hotel = new Hotel(hotelId, city, room);

				List<Hotel> hotelList = null;
				if (cityHotels.containsKey(city)) {
					hotelList = cityHotels.get(city);
				} else {
					hotelList = new ArrayList<Hotel>();
					cityHotels.put(city, hotelList);
				}
				hotelsMap.put(hotelId, hotel);
				hotelList.add(hotel);
				hotels.add(hotel);
			}
		} catch (Exception e) {
			logger.error("Error while parsing csv");
		}

	/*	for (String key : cityHotels.keySet()) {

			List<Hotel> hotell = cityHotels.get(key);
			System.out.println(key);
			for (Hotel h : hotell) {
				System.out.println(h.getRoom().getRoomType() + "  " + h.getRoom().getPrice());
			}
			System.out.println("---------------------------");
		}*/
	}

	public List<Hotel> getCityHotels(String city) throws ResourceNotFoundException, InvalidInputParameterException {

		if (city == null) {
			throw new InvalidInputParameterException();
		}

		List<Hotel> cityHotelList = cityHotels.get(city);

		if (cityHotelList == null) {
			throw new ResourceNotFoundException();
		}

		return cityHotelList;
	}

	public Hotel getHotel(int id) throws ResourceNotFoundException {

		Hotel hotel = null;

		hotel = hotelsMap.get(id);

		if (hotel == null) {
			throw new ResourceNotFoundException();
		}

		return hotel;
	}
}
