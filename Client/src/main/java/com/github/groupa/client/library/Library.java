package com.github.groupa.client.library;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.groupa.client.image.ImageObject;

/***
 * Stores all pictures(remote and local) and provides methods to store and retrieve them
 *
 */
public class Library {
	private static List<ImageObject> images = new ArrayList<>();

	public static int imageCount() { return images.size(); }
	public static ImageObject get(int idx) { return images.get(idx); }
	public static ImageObject remove(int idx) { return images.remove(idx); }
	public static boolean add(ImageObject img) { return images.add(img); }
	public static boolean remove(ImageObject img) { return images.remove(img); }
	public static boolean contains(ImageObject img) { return images.contains(img); }
	public static int indexOf(ImageObject img) { return images.indexOf(img); }
	public static boolean isEmpty() { return images.isEmpty(); }
	public static Iterator<ImageObject> iterator() { return images.iterator(); }
	
	/***
	 * @return list of images that exists on server
	 */
	public static List<ImageObject> getRemote() {
		List<ImageObject> list = new ArrayList<>();
		for (ImageObject img : images) {
			if (img.isUploaded()) list.add(img);
		}
		return list;
	}
	/***
	 * @return list of images that do not exist on server
	 */
	public static List<ImageObject> getLocal() {
		List<ImageObject> list = new ArrayList<>();
		for (ImageObject img : images) {
			if (!img.isUploaded()) list.add(img);
		}
		return list;
	}
	/***
	 * @return list of images that are viewable
	 */
	public static List<ImageObject> getViewable() {
		List<ImageObject> list = new ArrayList<>();
		for (ImageObject img : images) {
			if (img.isViewable()) list.add(img);
		}
		return list;
	}
	/***
	 * @return list of images that has uncommited changes
	 */
	public static List<ImageObject> getUnsaved() {
		List<ImageObject> list = new ArrayList<>();
		for (ImageObject img : images) {
			if (img.isModified()) list.add(img);
		}
		return list;
	}
}
