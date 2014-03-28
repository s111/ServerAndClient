package com.github.groupa.client.gui;

import javax.inject.Inject;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;

public class ActiveImage {
	private int currentImageIndex = -1;

	private Library library;

	@Inject
	public ActiveImage(Library library) {
		this.library = library;
	}

	public void setActiveLibrary(Library library) {
		this.library = library;
	}

	public int getCurrentImageIndex() {
		return currentImageIndex;
	}

	public void setCurrentImageIndex(int currentImageIndex) {
		this.currentImageIndex = currentImageIndex;
	}

	public ImageObject getImage() {
		return library.getImage(currentImageIndex);
	}
}
