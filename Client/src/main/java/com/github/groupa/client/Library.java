package com.github.groupa.client;

import java.util.ArrayList;
import java.util.List;

public class Library {
	private static List<ImageObject> images = new ArrayList<>();
	private static int modCount = 0;

	private List<ImageObject> selectedImages = new ArrayList<>();
	private int expectedModCount;
	private long activeImage = 0l;

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

	public ImageObject getImage(long id) {
		ImageObject img = get(id);
		if (img != null) {
			this.activeImage = id;
		}
		return img;
	}

	public ImageObject getImage() {
		return get(activeImage);
	}

	public ImageObject getNextImage() {
		ImageObject img = get(++activeImage);
		if (img == null)
			activeImage--;
		return img;
	}

	public ImageObject getPrevImage() {
		ImageObject img = get(--activeImage);
		if (img == null)
			activeImage++;
		return img;
	}

	public int imageCount() {
		return selectedImages.size();
	}

	/***
	 * Default is to make all images available
	 */
	private void addSelectedImages() {
		expectedModCount = Library.modCount;
		this.selectedImages.addAll(Library.images);
	}

	private ImageObject get(long id) {
		if (expectedModCount != Library.modCount) {
			addSelectedImages();
		}
		for (ImageObject o : selectedImages) {
			if (id == o.getId()) {
				return o;
			}
		}
		return null;
	}
}
