package com.github.groupa.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Library {
	public enum ConstraintType {
		HAS_IMAGE, HAS_TAG,
	}

	private int modCount = 0;
	private List<ImageObject> images = new ArrayList<>();
	private int activeImage = 0;

	private Library parent;

	private List<Constraint> constraints = new LinkedList<>();

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

	private ImageObject get(int num) {
		ensureSync();
		int count = imageCount();
		if (count == 0)
			return null;
		activeImage = (num + count) % count;
		return images.get(activeImage);
	}

	private void ensureSync() {
		if (parent != null && modCount != parent.getModCount()) {
			refresh();
		}
	}

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

	public ImageObject add(ImageObject img) {
		if (parent == null) {
			images.add(img);
			modCount++;
		} else {
			parent.add(img);
			ensureSync();
		}
		return img;
	}

	public Library() {
		parent = null;
	}

	public Library(Library parent) {
		this.parent = parent;
		this.modCount = parent.getModCount();
		refresh();
	}

	public int getModCount() {
		ensureSync();
		return modCount;
	}

	public List<ImageObject> getImages() {
		ensureSync();
		return images;
	}

	public ImageObject getImage(int idx) {
		return get(idx);
	}

	public ImageObject getImage() {
		return get(activeImage);
	}

	public ImageObject getNextImage() {
		return get(activeImage + 1);
	}

	public ImageObject getPrevImage() {
		return get(activeImage - 1);
	}

	public int imageCount() {
		ensureSync();
		return images.size();
	}

	public Library refresh() {
		if (parent == null)
			return this;
		images.clear();
		modCount = parent.getModCount();
		images.addAll(parent.getImages());
		if (constraints.isEmpty())
			return this;
		Iterator<ImageObject> itr = images.iterator();
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
}
