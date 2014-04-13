package com.github.groupa.client.library;

import java.util.Set;

import com.github.groupa.client.ImageObject;

public abstract class LibraryConstraint {
	public static boolean satisfied(Set<LibraryConstraint> constraints,
			ImageObject img) {
		for (LibraryConstraint constraint : constraints) {
			if (!constraint.satisfied(img))
				return false;
		}
		return true;
	}

	abstract public boolean satisfied(ImageObject img);
}
