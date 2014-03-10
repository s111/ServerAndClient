package com.github.groupa.client.events;

import com.github.groupa.client.jsonobjects.ImageFull;
import com.github.groupa.client.jsonobjects.ImageInfo;

public class ImageInfoChangedEvent {
	private ImageInfo imageInfo;

	public ImageInfoChangedEvent(ImageInfo imageInfo) {
		this.imageInfo = imageInfo;
	}

	public ImageFull getImageFull() {
		return imageInfo.getImage();
	}
}
