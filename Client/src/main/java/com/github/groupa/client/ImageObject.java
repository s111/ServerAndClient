package com.github.groupa.client;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class ImageObject {
	private Image image = null;
	private Image thumb = null;
	private long id = -1;
	private Requester requester = null;
	private HashMap<String, Integer> sizeMap = new HashMap<>();

	private String thumbSize = null;

	public ImageObject(long id, Requester requester) {
		this.id = id;
		this.requester = requester;

		sizeMap.put("xs", 32);
		sizeMap.put("s", 64);
		sizeMap.put("m", 96);
		sizeMap.put("l", 128);
		sizeMap.put("xl", 192);
	}

	public Image getImage() {
		try {
			return (image == null) ? (image = requester.getImage(id, "raw"))
					: image;
		} catch (IOException e) {
			return null;
		}
	}

	private Image getThumb(String size) {
		if (thumbSize == null) { // Need a thumb
			if (image == null) { // Request from server
				try {
					thumb = requester.getImage(id, size);
				} catch (IOException e) {
					return null;
				}
			} else { // Generate from full-size image
				thumb = scaleImage(image, size);
			}
			thumbSize = size;
		} else if (sizeMap.get(size) < sizeMap.get(thumbSize)) { // Scale down existing thumb
			return scaleImage(thumb, size);
		} else if (sizeMap.get(size) == sizeMap.get(thumbSize)) { // Have the thumb
			return thumb;
		}
		if (sizeMap.get(size) > sizeMap.get(thumbSize)) { // Need a bigger thumb
			if (image == null) {
				try {
					thumb = requester.getImage(id, size);
				} catch (IOException e) {
					return null;
				}
			} else {
				thumb = scaleImage(image, size);
			}
			thumbSize = size;
		}
		return thumb;
	}

	private Image scaleImage(Image img, String size) {
		return scaleImage(img, sizeMap.get(size));
	}

	private Image scaleImage(Image img, int size) {
		return image.getScaledInstance(size, size, BufferedImage.SCALE_SMOOTH);
	}

	public Image getThumbXS() {
		return getThumb("xs");
	}

	public Image getThumbS() {
		return getThumb("s");
	}

	public Image getThumbM() {
		return getThumb("m");
	}

	public Image getThumbL() {
		return getThumb("l");
	}

	public Image getThumbXL() {
		return getThumb("xl");
	}

	public long getId() {
		return id;
	}
}
