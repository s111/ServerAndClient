package com.github.groupa.client.jsonobjects;

import java.util.List;

public class ImageFull {
	private long id;
	
	private int rating;
	
	private String description;
	
	private List<String> tags;

	public long getId() {
		return id;
	}

	public int getRating() {
		return rating;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getTags() {
		return tags;
	}
}
