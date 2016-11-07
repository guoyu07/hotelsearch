package com.agoda.hotel.data;

public class Room {

	private String roomType;

	private double price;

	public Room(String roomType, double price) {
		this.roomType = roomType;
		this.price = price;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
