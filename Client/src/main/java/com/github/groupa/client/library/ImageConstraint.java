package com.github.groupa.client.library;

import com.github.groupa.client.ImageObject;

public class ImageConstraint extends LibraryConstraint {
	private boolean hasImage = true;
	
	public ImageConstraint(boolean hasImage) {
		this.hasImage = hasImage;
	}

	public boolean satisfied(ImageObject image) {
		return hasImage == image.hasImage();
	}
}
