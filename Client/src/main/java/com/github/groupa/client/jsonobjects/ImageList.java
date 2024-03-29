package com.github.groupa.client.jsonobjects;

import java.util.List;

public class ImageList {
	private String href;
	private String first;
	private String next;
	private String previous;
	private String last;
	
	private int offset;
	private int limit;
	
	private List<ImageShort> images;

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

	public int getOffset() {
		return offset;
	}

	public int getLimit() {
		return limit;
	}

	public List<ImageShort> getImages() {
		return images;
	}
}
