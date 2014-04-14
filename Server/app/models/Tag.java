package models;

import java.util.HashSet;
import java.util.Set;

public class Tag {
	private String name;

	private Set<Image> images = new HashSet<>();

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

	@Override
	public boolean equals(Object other) {
		return name.equals(((Tag) other).getName());
	}

	@Override
	public int hashCode() {
		return 31 + (name == null ? 0 : name.hashCode());
	}
}
