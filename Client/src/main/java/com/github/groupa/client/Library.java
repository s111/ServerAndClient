package com.github.groupa.client;

import java.util.List;

public interface Library {
	public ImageObject add(ImageObject img);
	public List<ImageObject> getImages();
	public int imageCount();
	public ImageObject getImage(int idx);
	public boolean hasImage(ImageObject image);
	public int indexOf(ImageObject img);
}
