package com.github.groupa.client.events;

import com.github.groupa.client.ImageObject;

public class ImageModifiedEvent {
	private ImageObject image;

	public ImageModifiedEvent(ImageObject image) {
		this.image = image;
	}

	public ImageObject getImageObject() {
		return image;
	}
}
