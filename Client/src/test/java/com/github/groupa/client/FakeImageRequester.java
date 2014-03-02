package com.github.groupa.client;

import java.awt.Image;
import java.io.IOException;

public class FakeImageRequester implements Requester {
	@Override
	public Image requestImage(long id) throws IOException {
		return new FakeImage();
	}
}
