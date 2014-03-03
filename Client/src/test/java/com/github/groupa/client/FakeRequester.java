package com.github.groupa.client;

import java.awt.Image;
import java.io.IOException;

import servercommunication.Requester;

public class FakeRequester implements Requester {
	@Override
	public Image getImage(long id, String s) throws IOException {
		return new FakeImage();
	}
}
