package com.google.codeu.data;

public class Location {
	public String user;
	public String country;

	public Location(String user, String country) {
		this.user = user;
		this.country = country;
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
