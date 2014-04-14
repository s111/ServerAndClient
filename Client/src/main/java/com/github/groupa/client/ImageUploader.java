package com.github.groupa.client;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.groupa.client.factories.ImageObjectFactory;
import com.github.groupa.client.jsonobjects.ImageInfo;
import com.github.groupa.client.library.Library;
import com.github.groupa.client.servercommunication.ServerConnection;
import com.google.inject.Inject;

public class ImageUploader {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(ImageUploader.class);

	private Library library;

	private ImageObjectFactory imageObjectFactory;

	private ServerConnection serverConnection;

	@Inject
	public ImageUploader(ServerConnection serverConnection,
			ImageObjectFactory imageObjectFactory, Library library) {
		this.serverConnection = serverConnection;
		this.imageObjectFactory = imageObjectFactory;
		this.library = library;
	}

	public void uploadImages(File[] files) {
		for (File file : files) {
			uploadImage(file);
		}
	}

	public void uploadImage(File file) {
		ImageInfo imageInfo = serverConnection.uploadImage(file);
		if (imageInfo != null) {
			ImageObject imageObject = imageObjectFactory.create(imageInfo
					.getImage().getId());

			library.add(imageObject);
		}
	}
}
