package com.github.groupa.client.image;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/***
 * Instances should store:
 * Actual Image objects cloned from synchronizedImageObject (Can be null if unavailable)
 * Instances should provide methods to:
 * Retrieve Image object with specific size
 * Make temporary changes to images
 * 
 * Commit temporary changes to all ImageSource objects thus making them permanent
 * 
 * Check if ImageSource objects are synchronized
 */
public class ImageObject implements ImageManipulationInterface {
	private LinkedList<String> changeLog = new LinkedList<>();
	private Map<String, Image> images = new HashMap<>();
	
	public boolean isViewable() { return false; }
	public boolean isModified() { return !changeLog.isEmpty(); }
	
	public boolean hasRemoteSource() {
		for (ImageSource src : imageSources) {
			if (src instanceof RemoteImageSource) return true;
		}
		return false;
	}
	
	private LinkedList<ImageSource> imageSources = new LinkedList<>();
	
	// The synchronizedImageObject forces other ImageSources to be synchronized with it
	private ImageSource synchronizedImageSource;
	
	public ImageObject (ImageSource obj) {
		imageSources.add(obj);
		synchronizedImageSource = obj;
	}
	
	/***
	 * Add a new ImageSource, and synchronize it from synchronizedImageSource
	 * @param src
	 */
	public boolean addImageSource(ImageSource src) {
		if (!src.synchronizeFrom(synchronizedImageSource)) {
			// Error synchronizing
		} else { // Success
			imageSources.add(src);
			return true;
		}
		return false;
	}

	/***
	 * Set synchronizedImageSource to src
	 * @param src
	 */
	public boolean setSynchronizedSource(ImageSource src) {
		if (imageSources.contains(src) || addImageSource(src)) {
			synchronizedImageSource = src;
			return true;
		}
		return false;
	}

	public boolean isSynchronized() {
		for (ImageSource src : imageSources) {
			if (!src.isSynchronized()) {
				return false;
			}
		}
		return true;
	}
	
	/***
	 * Rotates image 90 degrees clockwise
	 */
	public void rotate90CW() {
		if (changeLog.getLast().equals(ROTATE90CCW)) {
			changeLog.removeLast();
		}
		else {
			changeLog.add(ROTATE90CW);
		}
		modifyImages(images, ROTATE90CW);
	}
	
	/***
	 * Rotates image 90 degrees counter-clockwise
	 */
	public void rotate90CCW() {
		if (changeLog.getLast().equals(ROTATE90CW)) {
			changeLog.removeLast();
		}
		else {
			changeLog.add(ROTATE90CCW);
		}
		modifyImages(images, ROTATE90CCW);
	}
	
	/***
	 * Commit changes and ensure synchronizedImageObject is synchronized
	 */
	public void commit() {
		if (changeLog.isEmpty()) return; // No changes are to be made
		for (ImageSource obj : imageSources) {
			obj.commit(changeLog); // Tell source to modify available images (This do
		}
		synchronizedImageSource.synchronize();
	}
	
	public void undoLastChange() {
		changeLog.removeLast();
		images.clear();
	}
	
	/***
	 * Reset all changes. This will clear image cache, so GUI should ask for a new one afterwards
	 */
	public void resetChanges() {
		changeLog.clear();
		images.clear();
	}

	private void modifyImages(Map<String, Image> images, String change) {
		if (ROTATE90CW.equals(change)) {
			for (Image image : images.values()) image.rotate90CW();
		}
		else if (ROTATE90CCW.equals(change)) {
			for (Image image : images.values()) image.rotate90CCW();
		}
		else throw new RuntimeException("Unknown operation: " + change);
	}
}
