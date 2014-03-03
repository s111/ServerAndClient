package com.github.groupa.client;

import java.awt.Image;
import java.io.IOException;

import com.github.groupa.client.jsonobjects.ImageList;

import com.github.groupa.client.servercommunication.Requester;

public class FakeRequester implements Requester {
	@Override
	public Image getImage(long id, String s) throws IOException {
		return new FakeImage();
	}

	@Override
	public ImageList getImageList(int limit) throws IOException {

		return null;
	}

	@Override
	public boolean rateImage(long id, int stars) throws IOException {
		if (0 < stars && stars <= 5)
			return true;
		return false;
	}
}
