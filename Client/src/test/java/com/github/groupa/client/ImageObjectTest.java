package com.github.groupa.client;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.groupa.client.servercommunication.Requester;

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
		
		assertFalse(img.rate(0));
		assertFalse(img.rate(6));
		assertTrue(img.rate(4));
		assertEquals(img.getRating(), 4);
		assertNotEquals(img.getRating(), 3);
	}

}
