package com.agoda.hotel.data;


public class Hotel {

	private long id;

	private String city;

	Room room = null;

	public Hotel(long id, String city, Room room) {
		this.id = id;
		this.city = city;
		this.room = room;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
}
