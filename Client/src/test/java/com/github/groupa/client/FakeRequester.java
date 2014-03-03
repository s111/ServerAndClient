package com.github.groupa.client;

import java.awt.Image;
import java.io.IOException;

public class FakeRequester implements Requester {
	@Override
	public Image getImage(long id, String s) throws IOException {
		return new FakeImage();
	}
}
