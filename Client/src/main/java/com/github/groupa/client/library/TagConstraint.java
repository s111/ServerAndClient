package com.github.groupa.client.library;

import com.github.groupa.client.ImageObject;

public class TagConstraint extends LibraryConstraint {
	private boolean invert;
	private String tag;
	
	public TagConstraint(String tag) {
		this(tag, false);
	}
	
	public TagConstraint(String tag, boolean invert) {
		this.tag = tag;
		this.invert = invert;
	}

	public boolean satisfied(ImageObject image) {
		return invert != image.hasTag(tag);
	}
	
	public String toString() {
		if (invert) return "Is not tagged " + tag;
		return "Is tagged " + tag;
	}
}
