package com.github.groupa.client;

import java.awt.Image;
import java.io.IOException;

public class ImageObject {
	private Image image = null;
	private long id = -1;
	private Requester requester = null;

	public ImageObject(long id, Requester requester) {
		this.id = id;
		this.requester = requester;
	}

	public Image getImage() {
		try {
			return (image == null) ? (image = requester.getImage(id, "raw"))
					: image;
		} catch (IOException e) {
			return null;
		}
	}

	public long getId() {
		return id;
	}
}
