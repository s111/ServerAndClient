package com.github.groupa.client;

import java.awt.Image;

public class ImageObject {
	private Image image;
	private long id;
	
	public ImageObject(long id, Image image) {
		this.id = id;
		this.image = image;
	}
	
	public Image getImage() { return image; }
	public long getId() { return id; }
	
}
