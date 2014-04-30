package com.github.groupa.client.events;

import com.github.groupa.client.ImageObject;

public class ImageAvailableEvent {
	private ImageObject image;
	private String size;

	public ImageAvailableEvent(ImageObject image, String size) {
		this.image = image;
		this.size = size;
	}

	public ImageObject getImageObject() {
		return image;
	}

	public String getSize() {
		return size;
	}
}
