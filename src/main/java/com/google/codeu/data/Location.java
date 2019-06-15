package com.google.codeu.data;

import java.util.UUID;

public class Location {
	private UUID id;
	public String user;
	public String country;

	public Location(String user, String country) {
		this(UUID.randomUUID(), user, country);
	}

	public Location(UUID id, String user, String country) {
		this.id = id;
		this.user = user;
		this.country = country;
	}

	public UUID getId() {
		return id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
