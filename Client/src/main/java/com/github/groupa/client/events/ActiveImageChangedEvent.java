package com.github.groupa.client.events;

import com.github.groupa.client.ImageObject;

public class ActiveImageChangedEvent {
	private ImageObject image;

	public ActiveImageChangedEvent(ImageObject image) {
		this.image = image;
	}

	public ImageObject getImageObject() {
		return image;
	}

}
