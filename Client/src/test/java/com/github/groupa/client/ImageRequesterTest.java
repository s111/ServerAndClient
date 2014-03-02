package com.github.groupa.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.awt.Image;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class ImageRequesterTest {
	@BeforeClass
	public static void setUp() {
		/*
		 * Quick and simple way to configure slf4j Should probably use a config
		 * as we expand
		 */
		BasicConfigurator.configure();
	}

	@Test
	public void requestImage() {
		Image image = null;
		
		Requester requester = new FakeImageRequester();

		try {
			image = requester.requestImage("");
		} catch (IOException e) {
			fail("Threw IOException");
		}

		assertNotNull(image);
	}
}
