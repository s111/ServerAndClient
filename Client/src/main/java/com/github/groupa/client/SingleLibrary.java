package com.github.groupa.client;

import java.util.ArrayList;
import java.util.List;

import com.github.groupa.client.events.LibraryAddEvent;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

public class SingleLibrary implements Library {
	private List<ImageObject> images = new ArrayList<>();
	private int activeImage = 0;
	private EventBus eventBus;

	private ImageObject get(int num) {
		int count = imageCount();
		if (count == 0)
			return null;
		return images.get(num);
	}

	public void clear() {
		images.clear();
	}

	@Override
	public ImageObject add(ImageObject img) {
		if (!images.contains(img)) {
			int idx = imageCount();
			images.add(img);
			eventBus.post(new LibraryAddEvent(this, img, idx));
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
		return get(idx);
	}

	@Override
	public ImageObject getActiveImage() {
		return get(activeImage);
	}

	@Override
	public ImageObject getNextImage() {
		setActiveImage(wrapAround(activeImage + 1));
		return get(activeImage);
	}

	@Override
	public ImageObject getPrevImage() {
		setActiveImage(wrapAround(activeImage - 1));
		return get(activeImage);
	}

	@Override
	public void setActiveImage(ImageObject image) {
		int count = imageCount();
		for (int i = 0; i < count; i++) {
			if (image.equals(get(i))) {
				activeImage = i;
				return;
			}
		}
		throw new IndexOutOfBoundsException(
				"Tried setting non-existing image active");
	}

	@Override
	public void setActiveImage(int idx) {
		int count = imageCount();
		if (idx < 0 || idx >= count) {
			throw new IndexOutOfBoundsException(
					"Tried setting non-existing image active");
		}
		activeImage = idx;
	}

	@Override
	public int imageCount() {
		return images.size();
	}

	private int wrapAround(int idx) {
		int count = imageCount();

		return (count + idx) % count;
	}
}
