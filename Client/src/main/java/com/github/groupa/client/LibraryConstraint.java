package com.github.groupa.client;
import java.util.Set;

public class LibraryConstraint {
	public static final int HAS_IMAGE = 1;
	public static final int HAS_TAG = 2;
	public static final int HAS_DESCRIPTION = 3;

	public static boolean satisfied(Set<LibraryConstraint> constraints,
			ImageObject img) {
		for (LibraryConstraint constraint : constraints) {
			if (!constraint.isSatisfied(img))
				return false;
		}
		return true;
	}

	private int type;
	private Object criteria = null;
	private boolean invertResult = false;

	public LibraryConstraint(int type) {
		this.type = type;
	}

	public LibraryConstraint(int type, boolean invertResult) {
		this.type = type;
		this.invertResult = invertResult;
	}

	public LibraryConstraint(int type, Object criteria) {
		this.type = type;
		this.criteria = criteria;
	}

	public LibraryConstraint(int type, Object criteria, boolean invertResult) {
		this.type = type;
		this.criteria = criteria;
		this.invertResult = invertResult;
	}

	public boolean isSatisfied(ImageObject img) {
		if (img == null) {
			System.err.println("WTF NULL PTR !");
		}
		if (type == HAS_IMAGE) {
			return invertResult != img.hasImageRaw();
		} else if (type == HAS_TAG && criteria != null) {
			return invertResult != img.hasTag((String) criteria);
		} else if (type == HAS_DESCRIPTION) {
			return invertResult != (img.getDescription() != null);
		}
		throw new RuntimeException("Invalid library constraint usage (" + type + ")");
	}
}
