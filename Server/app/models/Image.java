package models;

import java.util.HashSet;
import java.util.Set;

public class Image {
	private Long id;

	private Integer rating;

	private String filename;
	private String description;

	private Set<Tag> tags = new HashSet<>();

	private Set<Thumbnail> thumbnails = new HashSet<>();

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Set<Thumbnail> getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(Set<Thumbnail> thumbnails) {
		this.thumbnails = thumbnails;
	}
}
