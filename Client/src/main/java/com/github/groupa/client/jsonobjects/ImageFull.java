package com.github.groupa.client.jsonobjects;

import java.util.ArrayList;
import java.util.List;

public class ImageFull {
	private long id;

	private List<Long> ids;

	private int rating = 0;

	private String description = "";

	private List<String> tags = new ArrayList<>();

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

	public void setId(long id) {
		this.id = id;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
}