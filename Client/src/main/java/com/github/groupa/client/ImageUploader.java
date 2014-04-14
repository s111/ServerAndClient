package com.github.groupa.client;

import java.io.File;
import java.net.ConnectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.mime.TypedFile;

import com.github.groupa.client.factories.ImageObjectFactory;
import com.github.groupa.client.jsonobjects.ImageInfo;
import com.github.groupa.client.library.Library;
import com.github.groupa.client.main.Main;
import com.github.groupa.client.servercommunication.RESTService;
import com.google.inject.Inject;

public class ImageUploader {
	private static final Logger logger = LoggerFactory
			.getLogger(ImageUploader.class);

	private Library library;
	private RESTService restService;

	@Inject
	public ImageUploader(RESTService restService, Library library) {
		this.restService = restService;
		this.library = library;
	}

	public void uploadImages(File[] files) {
		for (File file : files) {
			uploadImage(file);
		}
	}
	
	public void uploadImage(File file) {
		ImageInfo imageInfo = null;

		try {
			imageInfo = restService.uploadImage(new TypedFile("image/*", file));
		} catch (ConnectException e) {
			logger.warn("Could not connect to server and upload image");

			return;
		}

		ImageObjectFactory imageObjectFactory = Main.injector
				.getInstance(ImageObjectFactory.class);
		ImageObject imageObject = imageObjectFactory.create(imageInfo
				.getImage().getId());

		library.add(imageObject);
	}

}
