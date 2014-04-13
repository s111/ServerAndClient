package com.github.groupa.client.events;

import java.util.List;

import library.Library;

import com.github.groupa.client.ImageObject;

public class LibraryRemoveEvent {
	private Library library;
	private ImageObject image = null;
	private List<ImageObject> images = null;
	
	public LibraryRemoveEvent(Library library, ImageObject image) {
		this.library = library;
		this.image = image;
	}

	public LibraryRemoveEvent(Library library, List<ImageObject> images) {
		this.library = library;
		this.images = images;
	}

	public ImageObject getImage() {
		return image;
	}
	
	public List<ImageObject> getImages() {
		return images;
	}
	
	public Library getLibrary() {
		return library;
	}
}
