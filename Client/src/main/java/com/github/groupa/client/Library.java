package com.github.groupa.client;

import java.util.List;

public interface Library {
	public ImageObject add(ImageObject img);
	public List<ImageObject> getImages();
	public int imageCount();
	public ImageObject getImage(int idx);
	public ImageObject getNextImage();
	public ImageObject getPrevImage();
	public ImageObject getActiveImage();
	public void setActiveImage(ImageObject image);
	public void setActiveImage(int idx);
	public boolean hasImage(ImageObject image);
}
