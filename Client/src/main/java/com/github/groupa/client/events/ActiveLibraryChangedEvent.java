package com.github.groupa.client.events;

import com.github.groupa.client.Library;

public class ActiveLibraryChangedEvent {
	private Library library;

	public ActiveLibraryChangedEvent(Library library) {
		this.library = library;
	}

	public Library getLibrary() {
		return library;
	}
}
