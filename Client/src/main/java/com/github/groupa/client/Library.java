package com.github.groupa.client;

import java.util.HashMap;
import java.util.Map;
import java.awt.Image;

public class Library {
	private static Map<Long, Image> images = new HashMap<>();

	public static int size() { return images.size(); }
	public static Image get(long id) { return images.get(id); }
	//public static Image remove(long id) { return images.remove(id); }
	public static void add(long id, Image img) {
		if (img == null) {
			throw new NullPointerException("Image was null");
		}
		images.put(id, img);
	}
}
