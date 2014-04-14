package com.github.groupa.client.library;

import com.github.groupa.client.ImageObject;

public class RatingConstraint extends LibraryConstraint {
	public static RatingConstraint create() {
		return new RatingConstraint(1);
	}
	
	private int min = 0;
	private int max = 5;
	
	public RatingConstraint(int min) {
		this.min = min;
	}
	
	public RatingConstraint(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public boolean satisfied(ImageObject image) {
		int rating = image.getRating();
		return min <= rating && rating <= max;
	}
	
	public String toString() {
		if (min == 0) {
			if (max == 5) {
				return "Any rating";
			}
			return "Rated at most " + max;
		}
		if (max == 5) {
			return "Rated at least " + min;
		}
		return "Rated between " + min + "-" + max;
	}
}
