package com.github.groupa.client.events;

import com.github.groupa.client.ImageObject;

public class DisplayedImageChangedEvent {
	private ImageObject image;

	public DisplayedImageChangedEvent(ImageObject image) {
		this.image = image;
	}

	public ImageObject getImageObject() {
		return image;
	}

}
