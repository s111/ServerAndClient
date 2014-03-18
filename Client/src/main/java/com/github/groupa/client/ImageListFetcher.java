package com.github.groupa.client;

import java.net.ConnectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.groupa.client.factories.ImageObjectFactory;
import com.github.groupa.client.jsonobjects.ImageList;
import com.github.groupa.client.jsonobjects.ImageShort;
import com.github.groupa.client.servercommunication.RESTService;
import com.google.inject.Inject;

public class ImageListFetcher {
	private static final Logger logger = LoggerFactory
			.getLogger(ImageListFetcher.class);
	private RESTService restService;

	@Inject
	public ImageListFetcher(RESTService restService) {
		this.restService = restService;
	}

	public Library importAllImages() {
		Library library = new SingleLibrary();
		ImageList imageList = null;

		try {
			imageList = restService.getImageList(0, 0);
		} catch (ConnectException exception) {
			logger.error("Could not connect to the server: "
					+ exception.getMessage());
			return null;
		}

		if (imageList == null) {
			logger.error("Unknown problem getting images");
			return null;
		}

		ImageObjectFactory imageObjectFactory = Main.injector
				.getInstance(ImageObjectFactory.class);

		for (ImageShort image : imageList.getImages()) {
			library.add(imageObjectFactory.create(image.getId()));
		}
		return library;
	}
}