package com.google.codeu.data;

import java.util.ArrayList;
import java.util.UUID;

public class Saving {
	private String user;
	private ArrayList<String> post; // this stores the post id
	private long timestamp;

	public Saving(String user, ArrayList<String> post, long timestamp) {
		this.user = user;
		this.post = post;
		this.timestamp = timestamp;
	}

	public Saving(String user, ArrayList<String> post) {
		this(user, post, System.currentTimeMillis());
	}


	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ArrayList<String> getPost() {
		return post;
	}

	public void setPost(ArrayList<String> post) {
		this.post = post;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
