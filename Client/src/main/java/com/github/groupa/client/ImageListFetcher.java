package com.github.groupa.client;

import java.net.ConnectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.groupa.client.factories.ImageObjectFactory;
import com.github.groupa.client.jsonobjects.ImageList;
import com.github.groupa.client.jsonobjects.ImageShort;
import com.github.groupa.client.library.Library;
import com.github.groupa.client.servercommunication.RESTService;
import com.google.inject.Inject;

public class ImageListFetcher {
	private static final Logger logger = LoggerFactory
			.getLogger(ImageListFetcher.class);
	private RESTService restService;
	private ImageObjectFactory imageObjectFactory;
	private Library library;

	@Inject
	public ImageListFetcher(RESTService restService, Library library, ImageObjectFactory imageObjectFactory) {
		this.restService = restService;
		this.library = library;
		this.imageObjectFactory = imageObjectFactory;
	}

	public void importAllImages() {
		ImageList imageList = null;

		try {
			imageList = restService.getImageList(0, 0);
		} catch (ConnectException exception) {
			logger.error("Could not connect to the server: "
					+ exception.getMessage());
		} catch (Exception e) {
			logger.error("Unknown error when attempting to import images from server: "
					+ e.getMessage());
		}

		if (imageList == null) {
			logger.error("Unknown problem getting images");

			return;
		}

		for (ImageShort image : imageList.getImages()) {
			library.add(imageObjectFactory.create(image.getId()));
		}
	}
}