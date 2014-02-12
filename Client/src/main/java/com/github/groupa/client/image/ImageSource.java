package com.github.groupa.client.image;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class ImageSource {
	public static final String MAIN_IMAGE = "Main";
	public static final String COMPRESSED_IMAGE = "Compressed";
	
	protected LinkedList<String> changeLog = new LinkedList<>();
	protected Map<String, Image> images = new HashMap<>();

	abstract public boolean isSynchronized();
	abstract public void synchronize();
	abstract protected void readImages();
	public boolean isModified() { return !changeLog.isEmpty(); }

	abstract public boolean makeAvailable(String key);
	abstract public boolean synchronizeFrom(ImageSource src);
	
	
	public Image getImage(String key) {
		if (makeAvailable(key)) {
			return images.get(key);
		}
		return null;
	}
	
	public void commit(LinkedList<String> changes) {
		readImages();
		for (String change : changes) {
			modifyImages(images, change);
		}
	}
	
	private void modifyImages(Map<String, Image> images, String change) {
		if (ImageManipulationInterface.ROTATE90CW.equals(change)) {
			for (Image image : images.values()) image.rotate90CW();
		}
		else if (ImageManipulationInterface.ROTATE90CCW.equals(change)) {
			for (Image image : images.values()) image.rotate90CCW();
		}
		else throw new RuntimeException("Unknown operation: " + change);
	}
}
