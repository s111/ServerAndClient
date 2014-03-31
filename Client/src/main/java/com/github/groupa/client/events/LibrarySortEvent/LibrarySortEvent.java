package com.github.groupa.client.events.LibrarySortEvent;

import com.github.groupa.client.Library;

public class LibrarySortEvent {
	private Library library;

	public LibrarySortEvent(Library library) {
		this.library = library;
	}

	public Library getLibrary() {
		return library;
	}
}
