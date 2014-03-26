package models;

import java.util.HashSet;
import java.util.Set;

public class Tag implements Comparable<Tag> {
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
	public int compareTo(Tag otherTag) {
		return name.compareTo(otherTag.getName());
	}
}
