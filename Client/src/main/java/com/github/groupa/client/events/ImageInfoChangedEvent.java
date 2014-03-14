package com.github.groupa.client.events;

import com.github.groupa.client.ImageObject;

public class ImageInfoChangedEvent {
	private ImageObject image;

	public ImageInfoChangedEvent(ImageObject image) {
		this.image = image;
	}

	public ImageObject getImageObject() {
		return image;
	}
}
