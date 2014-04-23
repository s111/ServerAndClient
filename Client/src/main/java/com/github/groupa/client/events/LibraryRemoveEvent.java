package com.github.groupa.client.events;

import java.util.List;

import com.github.groupa.client.ImageObject;

public class LibraryRemoveEvent {
	private ImageObject image = null;
	private List<ImageObject> images = null;
	
	public LibraryRemoveEvent(ImageObject image) {
		this.image = image;
	}

	public LibraryRemoveEvent(List<ImageObject> images) {
		this.images = images;
	}

	public ImageObject getImage() {
		return image;
	}
	
	public List<ImageObject> getImages() {
		return images;
	}
}
