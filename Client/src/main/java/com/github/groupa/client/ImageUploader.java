package com.github.groupa.client;

import java.net.ConnectException;

import javax.swing.JOptionPane;

import library.Library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.mime.TypedFile;

import com.github.groupa.client.events.UploadImageEvent;
import com.github.groupa.client.factories.ImageObjectFactory;
import com.github.groupa.client.jsonobjects.ImageInfo;
import com.github.groupa.client.main.Main;
import com.github.groupa.client.servercommunication.RESTService;
import com.google.common.eventbus.Subscribe;
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

	@Subscribe
	public void uploadFile(UploadImageEvent event) {
		if (library == null)
			throw new NullPointerException("library not initialized");
		ImageInfo imageInfo = null;

		try {
			imageInfo = restService.uploadImage(new TypedFile("image/*", event
					.getFile()));
		} catch (ConnectException e) {
			logger.warn("Could not connect to server and upload image");

			return;
		}

		ImageObjectFactory imageObjectFactory = Main.injector
				.getInstance(ImageObjectFactory.class);
		ImageObject imageObject = imageObjectFactory.create(imageInfo
				.getImage().getId());

		library.add(imageObject);

		JOptionPane.showMessageDialog(null, "Image uploaded!");
	}

}
