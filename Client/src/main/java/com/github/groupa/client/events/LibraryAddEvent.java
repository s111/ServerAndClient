package com.github.groupa.client.events;

import java.util.List;

import com.github.groupa.client.ImageObject;

public class LibraryAddEvent {
	private ImageObject image = null;
	private List<ImageObject> images = null;

	public LibraryAddEvent(ImageObject image) {
		this.image = image;
	}

	public LibraryAddEvent(List<ImageObject> images) {
		this.images = images;
	}

	public ImageObject getImage() {
		return image;
	}

	public List<ImageObject> getImages() {
		return images;
	}

	public String toString() {
		return (image == null) ? ("" + images.size() + " images") : "1 image";
	}
}
