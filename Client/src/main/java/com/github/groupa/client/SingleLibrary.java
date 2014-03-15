package com.github.groupa.client;

import java.util.ArrayList;
import java.util.List;

import com.github.groupa.client.events.LibraryAddEvent;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

public class SingleLibrary implements Library {
	private List<ImageObject> images = new ArrayList<>();
	private EventBus eventBus;

	public void clear() {
		images.clear();
	}

	@Override
	public ImageObject add(ImageObject img) {
		if (!images.contains(img)) {
			images.add(img);
			eventBus.post(new LibraryAddEvent(this, img));
		}

		return img;
	}

	@Inject
	public SingleLibrary(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public List<ImageObject> getImages() {
		return images;
	}

	@Override
	public ImageObject getImage(int idx) {
		int count = imageCount();
		return (count == 0 || idx < 0 || idx >= count) ? null : images.get(idx);
	}

	@Override
	public int imageCount() {
		return images.size();
	}

	@Override
	public boolean hasImage(ImageObject image) {
		return images.contains(image);
	}

	@Override
	public int indexOf(ImageObject img) {
		return images.indexOf(img);
	}
}
