package com.github.groupa.client.jsonobjects;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ImageFull {
	private long id;

	private int rating;

	private String description = "";

	private List<String> tags = new ArrayList<>();

	private Timestamp dateTaken;

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

	public Timestamp getDateTaken() {
		return dateTaken;
	}

	public void setDateTaken(Timestamp dateTaken) {
		this.dateTaken = dateTaken;
	}
}