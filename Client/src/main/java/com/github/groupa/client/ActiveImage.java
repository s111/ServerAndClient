package com.github.groupa.client;

import javax.inject.Inject;

import com.github.groupa.client.events.ActiveLibraryChangedEvent;
import com.google.common.eventbus.EventBus;

public class ActiveImage {
	private int currentImageIndex = -1;

	private EventBus eventBus;

	private Library library;

	@Inject
	public ActiveImage(EventBus eventBus, Library library) {
		this.eventBus = eventBus;
		this.library = library;
	}

	public void setActiveLibrary(Library library) {
		this.library = library;

		eventBus.post(new ActiveLibraryChangedEvent(library));
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
