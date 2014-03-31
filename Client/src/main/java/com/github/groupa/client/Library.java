package com.github.groupa.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.LibraryRemoveEvent;
import com.github.groupa.client.events.LibrarySortEvent.LibrarySortEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

public class Library {
	private Comparator<ImageObject> comparator = null;
	private Library parent = null;
	private EventBus eventBus;

	private List<ImageObject> images = Collections
			.synchronizedList(new ArrayList<ImageObject>());

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

	public void sort(Comparator<ImageObject> cmp) {
		this.comparator = cmp;
		sort();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public Comparator<ImageObject> getComparator() {
		return comparator;
	}

	public List<ImageObject> getImages() {
		List<ImageObject> list = new ArrayList<>();
		list.addAll(images);
		return list;
	}

	public void clear() {
		images.clear();
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

	public int imageCount() {
		return images.size();
	}

	public ImageObject getImage(int idx) {
		if (idx < 0 || idx >= imageCount())
			return null;
		return images.get(idx);
	}

	public boolean hasImage(ImageObject image) {
		return images.contains(image);
	}

	public int indexOf(ImageObject img) {
		return images.indexOf(img);
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

	private void tryAddImages(List<ImageObject> list) {
		List<ImageObject> newImages = new ArrayList<>();
		for (ImageObject img : list) {
			if (!images.contains(img)
					&& LibraryConstraint.satisfied(constraints, img)) {
				newImages.add(img);
			}
		}
		if (newImages.size() > 0) {
			synchronized (images) {
				images.addAll(newImages);
			}
			eventBus.post(new LibraryAddEvent(this, newImages));
			sort();
		}
	}

	private void tryAddImage(ImageObject img) {
		boolean success = false;
		synchronized (images) {
			if ((success = !images.contains(img))
					&& LibraryConstraint.satisfied(constraints, img)) {
				images.add(img);
			}
		}
		if (success) {
			eventBus.post(new LibraryAddEvent(this, img));
			sort();
		}
	}

	private void tryRemoveImage(ImageObject img) {
		synchronized (images) {
			if (images.contains(img)) {
				images.remove(img);
			}
		}
		eventBus.post(new LibraryRemoveEvent(this, img));
	}

	private void tryRemoveImages(List<ImageObject> list) {
		synchronized (images) {
			images.removeAll(list);
		}
		eventBus.post(new LibraryRemoveEvent(this, list));
	}

	private synchronized void sort() {
		if (comparator != null) {
			if (LibrarySort.sort(images, comparator)) {
				eventBus.post(new LibrarySortEvent(this));
			}
		}
	}
}
