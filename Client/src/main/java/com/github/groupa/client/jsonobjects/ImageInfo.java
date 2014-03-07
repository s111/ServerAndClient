package com.github.groupa.client.jsonobjects;

public class ImageInfo {
	private String href;
	private String first;
	private String next;
	private String previous;
	private String last;
	
	private ImageFull image;

	public String getHref() {
		return href;
	}

	public String getFirst() {
		return first;
	}

	public String getNext() {
		return next;
	}

	public String getPrevious() {
		return previous;
	}

	public String getLast() {
		return last;
	}

	public ImageFull getImage() {
		return image;
	}
}
