package com.github.groupa.client.events;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;

public class LibraryAddEvent {
	private Library library;
	private ImageObject image;
	private int idx;
	
	public LibraryAddEvent(Library library, ImageObject image, int idx) {
		this.library = library;
		this.image = image;
		this.idx = idx;
	}

	public ImageObject getImage() {
		return image;
	}
	
	public Library getLibrary() {
		return library;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

}
