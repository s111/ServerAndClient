package com.github.groupa.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.groupa.client.events.LibraryAddEvent;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

public class SingleLibrary implements Library {
	private List<ImageObject> images = new ArrayList<>();
	private EventBus eventBus = null;

	public void clear() {
		images.clear();
	}

	@Override
	public ImageObject add(ImageObject img) {
		if (!images.contains(img)) {
			images.add(img);

			if (eventBus != null) {
				eventBus.post(new LibraryAddEvent(this, img));
			}
		}

		return img;
	}

	@Inject
	public SingleLibrary(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public SingleLibrary() {
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

	public void addAll(Library lib) {
		List<ImageObject> images = lib.getImages();
		Iterator<ImageObject> itr = images.iterator();
		while (itr.hasNext()) {
			ImageObject image = itr.next();
			if (hasImage(image.getId()))
				itr.remove();
		}
		if (images.size() > 0) {
			this.images.addAll(images);
			if (eventBus != null)
				eventBus.post(new LibraryAddEvent(this, images));
		}
	}

	private boolean hasImage(long id) {
		for (ImageObject image : images) {
			if (image.getId() == id)
				return true;
		}
		return false;
	}
}
