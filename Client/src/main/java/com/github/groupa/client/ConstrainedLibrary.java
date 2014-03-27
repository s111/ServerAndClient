package com.github.groupa.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.LibraryRemoveEvent;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ConstrainedLibrary implements Library {
	public static final int HAS_IMAGE = 1;
	public static final int HAS_TAG = 2;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Comparator<ImageObject> SORT_RATING_ASC = new Comparator() {
		public int compare(Object o1, Object o2) {
			ImageObject i1 = (ImageObject) o1;
			ImageObject i2 = (ImageObject) o2;
			return ComparisonChain
					.start()
					.compare(i1.getRating(), i2.getRating())
					.compare(i1.getId(), i1.getId(),
							Ordering.natural().nullsLast()).result();
		}
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Comparator<ImageObject> SORT_RATING_DESC = new Comparator() {
		public int compare(Object o1, Object o2) {
			ImageObject i1 = (ImageObject) o1;
			ImageObject i2 = (ImageObject) o2;
			return ComparisonChain
					.start()
					.compare(i2.getRating(), i1.getRating())
					.compare(i1.getId(), i1.getId(),
							Ordering.natural().nullsLast()).result();
		}
	};

	private Comparator<ImageObject> comparator = null;

	private List<ImageObject> images = new ArrayList<>();
	private Library parent;

	private List<Constraint> constraints = new ArrayList<>();
	private EventBus eventBus;

	public ConstrainedLibrary(EventBus eventBus, Library parent) {
		this.parent = parent;
		this.eventBus = eventBus;
		eventBus.register(this);
		images.addAll(parent.getImages());
	}

	@Subscribe
	public void imageChangedListener(ImageInfoChangedEvent event) {
		ImageObject img = event.getImageObject();
		if (parent.hasImage(img)) {
			boolean satisfied = constraintsSatisfied(img);
			boolean hasImage = hasImage(img);
			if (!hasImage && satisfied)
				addImage(img);
			if (hasImage && !satisfied)
				removeImage(img);
		}
	}

	@Subscribe
	public void libaryAddListener(LibraryAddEvent event) {
		if (!event.getLibrary().equals(parent))
			return;
		ImageObject img = event.getImage();
		if (constraintsSatisfied(img)) {
			addImage(img);
		}
	}

	public void sort(Comparator<ImageObject> cmp) {
		this.comparator = cmp;
		sort();
	}

	public Comparator<ImageObject> getComparator() {
		return comparator;
	}

	public ConstrainedLibrary addConstraint(int type) {
		return addConstraint(type, true);
	}

	public ConstrainedLibrary addConstraint(int type, boolean invertResult) {
		if (type == HAS_IMAGE) {
			constraints.add(new Constraint(type, null, invertResult));
		} else {
			throw new RuntimeException("Invalid usage");
		}
		checkCurrentImages();
		return this;
	}

	public ConstrainedLibrary addConstraint(int type, Object criteria) {
		return addConstraint(type, criteria, true);
	}

	public ConstrainedLibrary addConstraint(int type, Object criteria,
			boolean invertResult) {
		if (type == HAS_TAG) {
			constraints.add(new Constraint(type, criteria, invertResult));
		} else {
			throw new RuntimeException("Invalid usage");
		}
		checkCurrentImages();
		return this;
	}

	@Override
	public ImageObject add(ImageObject img) {
		return parent.add(img);
	}

	@Override
	public List<ImageObject> getImages() {
		return images;
	}

	@Override
	public int imageCount() {
		return images.size();
	}

	@Override
	public ImageObject getImage(int idx) {
		return images.get(idx);
	}

	@Override
	public boolean hasImage(ImageObject image) {
		return images.contains(image);
	}

	@Override
	public int indexOf(ImageObject img) {
		return images.indexOf(img);
	}

	private void addImage(ImageObject img) {
		images.add(img);
		eventBus.post(new LibraryAddEvent(this, img));
		sort();
	}

	private void removeImage(ImageObject img) {
		images.remove(img);
		eventBus.post(new LibraryRemoveEvent(this, img));
	}

	private void sort() {
		if (comparator == null)
			return;
		List<ImageObject> old = new ArrayList<>();
		old.addAll(images);
		Collections.sort(images, comparator);
		int l1 = old.size();
		int l2 = images.size();
		if (l1 == l2) {
			for (int i = 0; i < l1; i++) {
				if (!old.get(i).equals(images.get(i))) {

					break;
				}
			}
		}
	}

	private boolean constraintsSatisfied(ImageObject img) {
		for (Constraint constraint : constraints) {
			if (!constraint.isSatisfied(img))
				return false;
		}
		return true;
	}

	private void checkCurrentImages() {
		boolean changed = false;
		Iterator<ImageObject> itr = images.iterator();
		while (itr.hasNext()) {
			ImageObject img = itr.next();
			if (!constraintsSatisfied(img)) {
				itr.remove();
				changed = true;
			}
		}
		if (changed) {
			sort();
		}
	}

	private class Constraint {
		private int type;
		private Object criteria;
		private boolean invertResult;

		public Constraint(int type, Object criteria, boolean invertResult) {
			this.type = type;
			this.criteria = criteria;
			this.invertResult = invertResult;
		}

		public boolean isSatisfied(ImageObject img) {
			if (type == HAS_IMAGE) {
				return invertResult == img.hasImageRaw();
			} else if (type == HAS_TAG) {
				return invertResult == img.hasTag((String) criteria);
			}
			throw new RuntimeException("Invalid library constraint usage");
		}
	}
}
