package com.github.groupa.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.google.common.eventbus.Subscribe;

public class ImageFinder implements Library {
	private Library parent;
	private List<ImageObject> images;
	
	public enum ConstraintType {
		HAS_IMAGE, HAS_TAG,
	}

	private List<Constraint> constraints = new LinkedList<>();

	public ImageFinder(Library parent) {
		this.parent = parent;
		createInitialLibrary();
	}

	private void createInitialLibrary() {
		images = new ArrayList<>();
		for (ImageObject img : parent.getImages()) {
			if (constraintsSatisfied(img)) {
				images.add(img);
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

	@Subscribe
	public void imageInfoChangedListener(ImageInfoChangedEvent event) {
		ImageObject image = event.getImageObject();
		if (parent.hasImage(image) && constraintsSatisfied(image)
				&& !images.contains(image)) {
			images.add(image);
		} else if (images.contains(image))
			images.remove(image);
	}

	public ImageFinder addConstraint(ConstraintType type) {
		if (type == ConstraintType.HAS_IMAGE) {
			constraints.add(new Constraint(type, null, true));
		} else {
			throw new RuntimeException("Invalid usage");
		}
		return this;
	}

	public ImageFinder addConstraint(ConstraintType type, boolean wanted) {
		if (type == ConstraintType.HAS_IMAGE) {
			constraints.add(new Constraint(type, null, wanted));
		} else {
			throw new RuntimeException("Invalid usage");
		}
		return this;
	}

	public ImageFinder addConstraint(ConstraintType type, Object crit) {
		if (type == ConstraintType.HAS_TAG) {
			constraints.add(new Constraint(type, crit, true));
		} else {
			throw new RuntimeException("Invalid usage");
		}
		return this;
	}

	public ImageFinder addConstraint(ConstraintType type, Object crit,
			boolean wanted) {
		if (type == ConstraintType.HAS_TAG) {
			constraints.add(new Constraint(type, crit, wanted));
		} else {
			throw new RuntimeException("Invalid usage");
		}
		return this;
	}

	private class Constraint {
		private ConstraintType type;
		private Object crit;
		private boolean wanted;

		public Constraint(ConstraintType type, Object crit, boolean wanted) {
			this.type = type;
			this.crit = crit;
			this.wanted = wanted;
		}

		public boolean isSatisfied(ImageObject img) {
			if (type == ConstraintType.HAS_IMAGE) {
				return wanted == img.hasImageRaw();
			} else if (type == ConstraintType.HAS_TAG) {
				return wanted == img.hasTag((String) crit);
			}
			throw new RuntimeException("Invalid library constraint usage");
		}
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
		return get(idx);
	}

	@Override
	public boolean hasImage(ImageObject image) {
		return images.contains(image);
	}

}
