package com.github.groupa.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.LibraryRemoveEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

public class Library {
	private Library parent = null;
	private EventBus eventBus;

	private Set<ImageObject> images = new HashSet<>();

	private Set<LibraryConstraint> constraints = new HashSet<>();

	@Inject
	public Library(EventBus eventBus) {
		this.eventBus = eventBus;
		eventBus.register(this);
	}

	public Library(Library parent) {
		this.parent = parent;
		this.eventBus = parent.getEventBus();
		eventBus.register(this);
		tryAddImages(parent.getImages());
	}

	public Library(EventBus eventBus, List<ImageObject> initialImages) {
		this.eventBus = eventBus;
		images.addAll(initialImages);
	}

	public Library addConstraint(LibraryConstraint constraint) {
		constraints.add(constraint);
		ArrayList<ImageObject> list = new ArrayList<>();
		synchronized (images) {
			for (ImageObject img : images) {
				if (img != null) {
					if (!constraint.isSatisfied(img)) {
						list.add(img);
					}
				}
			}
		}
		tryRemoveImages(list);
		return this;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public List<ImageObject> getImages() {
		List<ImageObject> list = new ArrayList<>();
		list.addAll(images);
		return list;
	}

	public void add(ImageObject img) {
		if (parent == null) {
			tryAddImage(img);
		} else {
			parent.add(img);
		}
	}

	public void addAll(List<ImageObject> list) {
		if (parent == null) {
			tryAddImages(list);
		} else {
			parent.addAll(list);
		}
	}

	public int size() {
		return images.size();
	}

	public boolean hasImage(ImageObject image) {
		return images.contains(image);
	}

	@Subscribe
	public void imageChangedListener(ImageInfoChangedEvent event) {
		ImageObject img = event.getImageObject();
		boolean satisfied = LibraryConstraint.satisfied(constraints, img);
		boolean hasImage = hasImage(img);
		if (parent != null) {
			satisfied = parent.hasImage(img) && satisfied;
		}

		if (!hasImage && satisfied) {
			tryAddImage(img);
		}
		if (hasImage && !satisfied) {
			tryRemoveImage(img);
		}
	}

	@Subscribe
	public void libaryAddListener(LibraryAddEvent event) {
		if (parent == null || !event.getLibrary().equals(parent))
			return;
		ImageObject img = event.getImage();
		if (img == null) {
			tryAddImages(event.getImages());
		} else {
			tryAddImage(img);
		}
	}

	@Subscribe
	public void libaryRemoveListener(LibraryRemoveEvent event) {
		if (parent == null && !event.getLibrary().equals(parent)
				&& !event.getLibrary().equals(this)) {
			return;
		}
		ImageObject img = event.getImage();
		if (img == null) {
			tryRemoveImages(event.getImages());
		} else {
			tryRemoveImage(img);
		}
	}

	private void tryAddImages(List<ImageObject> list) {
		List<ImageObject> newImages = new ArrayList<>();
		newImages.addAll(list);
		Iterator<ImageObject> itr = newImages.iterator();
		while (itr.hasNext()) {
			ImageObject img = itr.next();
			if (images.contains(img)
					|| !LibraryConstraint.satisfied(constraints, img)) {
				itr.remove();
			}
		}
		if (newImages.size() > 0) {
			synchronized (images) {
				images.addAll(newImages);
			}
			eventBus.post(new LibraryAddEvent(this, newImages));
		}
	}

	private void tryAddImage(ImageObject img) {
		boolean success = false;
		synchronized (images) {
			if (!images.contains(img)
					&& LibraryConstraint.satisfied(constraints, img)) {
				success = images.add(img);
			}
		}
		if (success) {
			eventBus.post(new LibraryAddEvent(this, img));
		}
	}

	private void tryRemoveImage(ImageObject img) {
		boolean success = false;
		synchronized (images) {
			if ((success = images.contains(img))) {
				images.remove(img);
			}
		}
		if (success) {
			eventBus.post(new LibraryRemoveEvent(this, img));
		}
	}

	private void tryRemoveImages(List<ImageObject> list) {
		List<ImageObject> retainedList = new ArrayList<>(list.size());
		retainedList.addAll(list);
		retainedList.retainAll(images);
		synchronized (images) {
			images.removeAll(list);
		}
		if (!retainedList.isEmpty()) {
			eventBus.post(new LibraryRemoveEvent(this, retainedList));
		}
	}
}
