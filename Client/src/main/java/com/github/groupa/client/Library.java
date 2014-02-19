package com.github.groupa.client;

import java.util.ArrayList;
import java.util.List;
import java.awt.Image;

public class Library {
	private static List<ImageObject> images = new ArrayList<>();

	public static int size() { return images.size(); }
	/***
	 * 
	 * @param id
	 * @return null if image can not be found
	 */
	public static ImageObject get(long id) {
		for (ImageObject o : images) {
			if (id == o.getId()) { return o; }
		}
		return null;
	}
	
	public static void add(long id, Image img) {
		if (img == null) {
			throw new NullPointerException("Image was null");
		}
		images.add(new ImageObject(id, img));
	}
}
