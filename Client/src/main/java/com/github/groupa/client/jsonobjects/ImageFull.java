package com.github.groupa.client.jsonobjects;

import java.sql.Timestamp;
import java.util.List;

public class ImageFull {
	private long id;

	private List<Long> ids;

	private int rating;

	private String description;

	private List<String> tags;

	private Timestamp date;

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

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}
