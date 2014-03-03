package com.github.groupa.client;

import static org.junit.Assert.*;

import org.junit.Test;

import servercommunication.Requester;

public class ImageObjectTest {

	@Test
	public void test() {
		Requester requester = new FakeRequester();
		ImageObject img = new ImageObject(1, requester);

		assertNotNull(img.getImage());
		assertNotNull(img.getThumbXS());
		assertNotNull(img.getThumbS());
		assertNotNull(img.getThumbM());
		assertNotNull(img.getThumbL());
		assertNotNull(img.getThumbXL());
	}

}
