package com.github.groupa.client.library;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.github.groupa.client.events.KnownTagsChangedEvent;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.LibraryRemoveEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

public class Library {
	private EventBus eventBus;

	private Set<ImageObject> constrainedImages = new HashSet<>();
	private Set<ImageObject> allImages = new HashSet<>();
	private Set<LibraryConstraint> constraints = new HashSet<>();

	@Inject
	public Library(EventBus eventBus) {
		this.eventBus = eventBus;
		eventBus.register(this);
	}

	public Set<LibraryConstraint> getConstraints() {
		Set<LibraryConstraint> set = new HashSet<>();
		set.addAll(constraints);
		return set;
	}

	public void addConstraint(LibraryConstraint constraint) {
		if (constraints.contains(constraint))
			return;
		ArrayList<ImageObject> list = new ArrayList<>();
		synchronized (this) {
			constraints.add(constraint);
			for (ImageObject img : constrainedImages) {
				if (!constraint.satisfied(img)) {
					list.add(img);
				}
			}
		}
		removeImages(list);
	}

	public void removeConstraint(LibraryConstraint constraint) {
		if (!constraints.contains(constraint))
			return;
		ArrayList<ImageObject> list = new ArrayList<>();
		synchronized (this) {
			constraints.remove(constraint);
			for (ImageObject img : allImages) {
				if (!constrainedImages.contains(img)
						&& LibraryConstraint.satisfied(constraints, img)) {
					list.add(img);
				}
			}
		}
		addImages(list);
	}

	public void checkTags(ImageObject TODO) {
		Set<String> set = new HashSet<String>();
		synchronized (allImages) {
			for (ImageObject img : allImages) {
				set.addAll(img.getTags());
			}
		}
		eventBus.post(new KnownTagsChangedEvent(set));
	}

	public List<ImageObject> getImages() {
		List<ImageObject> list = new ArrayList<>();
		list.addAll(constrainedImages);
		return list;
	}

	public void add(ImageObject img) {
		if (!allImages.contains(img)) {
			allImages.add(img);
			addImage(img);
		}
		checkTags(img);
	}

	public void addAll(List<ImageObject> list) {
		List<ImageObject> newList = new ArrayList<>();
		newList.addAll(list);
		newList.removeAll(allImages);
		allImages.addAll(newList);
		addImages(newList);
		for (ImageObject img : list) {
			checkTags(img);
		}
	}
	
	public void remove(ImageObject img) {
		if (allImages.contains(img)) {
			allImages.remove(img);
			removeImage(img);
		}
		checkTags(img);
	}
	
	public void removeAll(List<ImageObject> list) {
		List<ImageObject> newList = new ArrayList<>();
		newList.addAll(list);
		newList.retainAll(allImages);
		if (!newList.isEmpty()) {
			allImages.removeAll(newList);
			removeImages(newList);
		}
		for (ImageObject img : list) {
			checkTags(img);
		}
	}

	public int allImagesCount() {
		return allImages.size();
	}

	public int constrainedImagesCount() {
		return constrainedImages.size();
	}

	public boolean libraryContains(ImageObject image) {
		return allImages.contains(image);
	}

	@Subscribe
	public void imageChangedListener(ImageInfoChangedEvent event) {
		ImageObject img = event.getImageObject();
		boolean satisfied = LibraryConstraint.satisfied(constraints, img);
		boolean hasImage = constrainedImages.contains(img);
		if (!hasImage && satisfied) {
			addImage(img);
		}
		if (hasImage && !satisfied) {
			removeImage(img);
		}
		checkTags(img);
	}

	private void addImages(List<ImageObject> list) {
		list.removeAll(constrainedImages);
		if (!list.isEmpty()) {
			constrainedImages.addAll(list);
			eventBus.post(new LibraryAddEvent(list));
		}
	}

	private void addImage(ImageObject img) {
		if (!constrainedImages.contains(img)) {
			constrainedImages.add(img);
			eventBus.post(new LibraryAddEvent(img));
		}
	}

	private void removeImages(List<ImageObject> list) {
		list.retainAll(constrainedImages);
		if (!list.isEmpty()) {
			constrainedImages.removeAll(list);
			eventBus.post(new LibraryRemoveEvent(list));
		}
	}

	private void removeImage(ImageObject img) {
		if (!constrainedImages.contains(img)) {
			constrainedImages.remove(img);
			eventBus.post(new LibraryRemoveEvent(img));
		}
	}
}
