package com.github.groupa.client.servercommunication;

import java.awt.Image;
import java.io.IOException;

import com.github.groupa.client.jsonobjects.ImageList;

public interface Requester {
	public Image getImage(long id, String size) throws IOException;
	
	public ImageList getImageList(int limit) throws IOException;
	
	public boolean rateImage(long id, int stars) throws IOException;
	
	public boolean describeImage(long id, String description) throws IOException;
}
