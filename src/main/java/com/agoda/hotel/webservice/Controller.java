package com.agoda.hotel.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agoda.hotel.config.APIAccessStatus;
import com.agoda.hotel.config.APIKeyDetails;
import com.agoda.hotel.config.APIKeyStore;
import com.agoda.hotel.data.Hotel;
import com.agoda.hotel.data.SearchRequest;
import com.agoda.hotel.data.SearchRequest.SortOrder;
import com.agoda.hotel.service.HotelSearchService;
import com.agoda.hotel.service.HotelSearchServiceImpl;

/**
 * This is the controller class which has request url mapping.
 * / --> welcome message
 * /search --> expects city name and sort order(optional) as inpu and return sorted hotel list for this city.
 *
 * @author Shivam Khare
 */
@EnableAutoConfiguration
@RestController
@ComponentScan(basePackages = { "com.agoda.hotel.config" })
@SpringBootApplication
public class Controller {

	private APIKeyDetails apikeyDetails;
	private HotelSearchService searchService;
	private static final Log log = LogFactory.getLog(Controller.class);
	private static boolean initialized = false;

	@RequestMapping("/")
	public ResponseEntity<Map<String, String>> welcome(@RequestHeader(value = "api-key") String apiKey) {
		
		if(!initialized) {
			init();
		}
		Map<String, String> response = new HashMap<String, String>();
		APIAccessStatus permission = apikeyDetails.apiPermission(apiKey);
		if (permission.isAllowed()) {
			response.put("msg", "Service is running..");
		} else {
			response.put("error", permission.getStatus().name());
		}

		ResponseEntity<Map<String, String>> x = new ResponseEntity<Map<String, String>>(response, permission.getStatus().getHttpStatus());
		return x;
	}

	@RequestMapping("/search")
	public ResponseEntity<Map<String, Object>> search(@RequestHeader(value = "api-key") String apiKey, @RequestParam(value = "city") String city,
			@RequestParam(value = "sort", defaultValue = "ASC") String sort) {

		
		if(!initialized) {
			init();
		}

		APIAccessStatus permission = apikeyDetails.apiPermission(apiKey);

		Map<String, Object> response = new HashMap<String, Object>();
		if (permission.isAllowed()) {
			log.info("City: " + city + ", SortOrder: " + sort);
			SearchRequest searchCriteria = new SearchRequest(city, SortOrder.valueOf(sort));
			List<Hotel> hotels = null;
			try {
				hotels = searchService.search(searchCriteria);
			} catch (Exception e) {
				hotels = new ArrayList<Hotel>();
			}
			if (hotels != null) {
				response.put("hotels", hotels);
			} else {
				response.put("error", "No hotels found for this city");
			}
		} else {
			response.put("error", permission.getStatus().name());
		}
		ResponseEntity<Map<String, Object>> httpResponse = new ResponseEntity<Map<String, Object>>(response, permission.getStatus().getHttpStatus());
		return httpResponse;
	}

	public static void main(String[] args) {
		log.info("Starting spring application... .");

	//	Controller obj = new Controller();
	//	obj.init();
		SpringApplication.run(Controller.class, args);
		// obj.execute();

	}

	public void init() {

		initialized = true;

		APIKeyStore apiKeyStore = new APIKeyStore();
		apiKeyStore.loadAPIKeys();
		apikeyDetails = new APIKeyDetails();
		apikeyDetails.setApikeyStore(apiKeyStore);
		apikeyDetails.initialize();
		searchService = new HotelSearchServiceImpl();
	}
	/*
	 * public void execute() {
	 * 
	 * String apiKey = "2d206762274548303c4c317243423762734f5d6c4f276b695141384d6a";
	 * 
	 * String city = "Amsterdam";
	 * 
	 * String sort = "DESC";
	 * 
	 * processSearch(apiKey, sort, city); processSearch(apiKey, sort, city); processSearch(apiKey, sort, city);
	 * 
	 * }
	 * 
	 * private void processSearch(String apiKey, String sort, String city) { APIAccessStatus permit = apikeyDetails.apiPermission(apiKey);
	 * 
	 * Map<String, Object> response = new HashMap<String, Object>(); if (permit.isAllowed()) { log.info("searching for city " + city); SearchRequest searchCriteria = new SearchRequest(city,
	 * SortOrder.valueOf(sort)); List<Hotel> hotels = null; try { hotels = searchService.search(searchCriteria); } catch (Exception e) { hotels = new ArrayList<Hotel>(); } if (hotels != null) {
	 * response.put("hotels", hotels); } else { response.put("error", "No Such City in Database"); } } else { response.put("error", permit.getStatus().name()); } for (String key : response.keySet()) {
	 * System.out.println(key); if (response.get(key) instanceof ArrayList<?>) { for (Hotel h : (List<Hotel>) response.get(key)) { System.out.println(h.getRoom().getRoomType() + "  " +
	 * h.getRoom().getPrice());
	 * 
	 * } } else if (response.get(key) instanceof String) { System.out.println(response.get(key)); } } }
	 */
}
