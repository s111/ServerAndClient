package com.github.groupa.client.jsonobjects;

import java.util.List;

public class ImageList {
	public String href;
	public String first;
	public String next;
	public String previous;
	public String last;
	
	public int offset;
	public int limit;
	
	public List<Image> images;
}
