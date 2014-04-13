package com.github.groupa.client.events;

import library.Library;

public class LibrarySortEvent {
	private Library library;

	public LibrarySortEvent(Library library) {
		this.library = library;
	}

	public Library getLibrary() {
		return library;
	}
}
