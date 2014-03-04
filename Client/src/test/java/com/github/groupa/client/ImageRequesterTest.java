package com.github.groupa.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.awt.Image;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.groupa.client.servercommunication.Requester;

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

		Requester requester = new FakeRequester();

		try {
			image = requester.getImage(0, "");
		} catch (IOException e) {
			fail("Threw IOException");
		}

		assertNotNull(image);
	}
}
