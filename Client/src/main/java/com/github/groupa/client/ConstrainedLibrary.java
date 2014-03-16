package com.github.groupa.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.github.groupa.client.events.LibraryAddEvent;
import com.google.common.eventbus.Subscribe;

public class ConstrainedLibrary implements Library {
	public static final int HAS_IMAGE = 1;
	public static final int HAS_TAG = 2;

	private List<ImageObject> images = new ArrayList<>();
	private Library parent;

	private List<Constraint> constraints = new ArrayList<>();

	public ConstrainedLibrary(Library parent) {
		this.parent = parent;
		images.addAll(parent.getImages());
	}
	
	@Subscribe
	public void imageChangedListener(ImageInfoChangedEvent event) {
		ImageObject img = event.getImageObject();
		if (parent.hasImage(img)) {
			boolean satisfied = constraintsSatisfied(img);
			boolean hasImage = hasImage(img);
			if (!hasImage && satisfied) images.add(img);
			if (hasImage && !satisfied) images.remove(img);
		}
	}
	
	@Subscribe
	public void libaryAddListener(LibraryAddEvent event) {
		if (!event.getLibrary().equals(parent)) return;
		ImageObject img = event.getImage();
		if (constraintsSatisfied(img)) images.add(img);
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

	private boolean constraintsSatisfied(ImageObject img) {
		for (Constraint constraint : constraints) {
			if (!constraint.isSatisfied(img))
				return false;
		}
		return true;
	}

	private void checkCurrentImages() {
		Iterator<ImageObject> itr = images.iterator();
		while (itr.hasNext()) {
			ImageObject img = itr.next();
			if (!constraintsSatisfied(img)) {
				itr.remove();
			}
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
