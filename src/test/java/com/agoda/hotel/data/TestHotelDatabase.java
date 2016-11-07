package com.agoda.hotel.data;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.Ignore;

import com.agoda.hotel.exceptions.InvalidInputParameterException;
import com.agoda.hotel.exceptions.ResourceNotFoundException;

@Ignore
public class TestHotelDatabase {

    @Test
    public void test() throws InvalidInputParameterException, ResourceNotFoundException {
    	HotelDatabase instance = HotelDatabase.getInstance();
        assertNotNull(instance.getHotel(1));
        assertEquals(instance.getHotel(1).getRoom().getPrice() , 1000d,0.001);
        List<Hotel> cityHotels = instance.getCityHotels("Amsterdam");
        assertEquals(cityHotels.size(),6);
    }
    
}
