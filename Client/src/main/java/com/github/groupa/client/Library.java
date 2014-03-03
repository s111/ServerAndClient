package com.github.groupa.client;

import java.util.ArrayList;
import java.util.List;

public class Library {
	private static List<ImageObject> images = new ArrayList<>();
	private static int modCount = 0;

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
		addSelectedImages();
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

	/***
	 * Default is to make all images available
	 */
	private void addSelectedImages() {
		expectedModCount = Library.modCount;
		this.selectedImages.clear();
		this.selectedImages.addAll(Library.images);
	}

	private ImageObject get(int num) {
		ensureSync();
		int count = imageCount();
		if (count == 0) return null;
		activeImage = (num + count) % count;
		return selectedImages.get(activeImage);
	}
	
	private void ensureSync() {
		if (expectedModCount != Library.modCount) {
			addSelectedImages();
		}
	}
}
