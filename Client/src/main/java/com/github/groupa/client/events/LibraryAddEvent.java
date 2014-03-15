package com.github.groupa.client.events;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;

public class LibraryAddEvent {
	private Library library;
	private ImageObject image;
	
	public LibraryAddEvent(Library library, ImageObject image) {
		this.library = library;
		this.image = image;
	}

	public ImageObject getImage() {
		return image;
	}
	
	public Library getLibrary() {
		return library;
	}
}
