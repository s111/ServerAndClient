package com.github.groupa.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Library {
	private static List<ImageObject> images = new ArrayList<>();
	private static int modCount = 0;

	public enum ConstraintType {
		HAS_IMAGE, HAS_TAG,
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

	private List<Constraint> constraints = new LinkedList<>();

	public Library addConstraint(ConstraintType type) {
		if (type == ConstraintType.HAS_IMAGE) {
			constraints.add(new Constraint(type, null, true));
		} else {
			throw new RuntimeException("Invalid usage");
		}
		refresh();
		return this;
	}

	public Library addConstraint(ConstraintType type, boolean wanted) {
		if (type == ConstraintType.HAS_IMAGE) {
			constraints.add(new Constraint(type, null, wanted));
		} else {
			throw new RuntimeException("Invalid usage");
		}
		refresh();
		return this;
	}

	public Library addConstraint(ConstraintType type, Object crit) {
		if (type == ConstraintType.HAS_TAG) {
			constraints.add(new Constraint(type, crit, true));
		} else {
			throw new RuntimeException("Invalid usage");
		}
		refresh();
		return this;
	}

	public Library addConstraint(ConstraintType type, Object crit,
			boolean wanted) {
		if (type == ConstraintType.HAS_TAG) {
			constraints.add(new Constraint(type, crit, wanted));
		} else {
			throw new RuntimeException("Invalid usage");
		}
		refresh();
		return this;
	}

	private List<ImageObject> selectedImages = new ArrayList<>();
	private int expectedModCount;
	private int activeImage = 0;

	public static ImageObject add(ImageObject img) {
		images.add(img);
		modCount++;
		return img;
	}

	public static int size() {
		return images.size();
	}

	public Library() {
		refresh();
	}

	public ImageObject getImage(int idx) {
		return get(idx);
	}
	
	public ImageObject getImage() {
		return get(activeImage);
	}

	public ImageObject getNextImage() {
		return get(activeImage - 1);
	}

	public ImageObject getPrevImage() {
		return get(activeImage + 1);
	}

	public int imageCount() {
		ensureSync();
		return selectedImages.size();
	}

	public Library refresh() {
		expectedModCount = Library.modCount;
		this.selectedImages.clear();
		this.selectedImages.addAll(Library.images);
		if (constraints.isEmpty())
			return this;
		Iterator<ImageObject> itr = selectedImages.iterator();
		while (itr.hasNext()) {
			ImageObject img = itr.next();
			for (Constraint constraint : constraints) {
				if (!constraint.isSatisfied(img)) {
					itr.remove();
					break;
				}
			}
		}
		return this;
	}

	private ImageObject get(int num) {
		ensureSync();
		int count = imageCount();
		if (count == 0)
			return null;
		activeImage = (num + count) % count;
		return selectedImages.get(activeImage);
	}

	private void ensureSync() {
		if (expectedModCount != Library.modCount) {
			refresh();
		}
	}
}
