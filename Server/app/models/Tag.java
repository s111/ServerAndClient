package models;

import java.util.HashSet;
import java.util.Set;

public class Tag {
	private Long id;

	private String name;

	private Set<Image> images = new HashSet<>();

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}
}
