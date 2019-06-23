package com.google.codeu.data;

import java.util.UUID;

public class PlaceRating {
	private UUID id;
	private String place;
	private float rating;

	public PlaceRating(String place, float rating) {
		this(UUID.randomUUID(), place, rating);
	}

	public PlaceRating(UUID id, String place, float rating) {
		this.id = id;
		this.place = place;
		this.rating = rating;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}
}
