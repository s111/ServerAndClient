package com.github.groupa.client;

import javax.swing.JOptionPane;

import retrofit.mime.TypedFile;

import com.github.groupa.client.events.UploadImageEvent;
import com.github.groupa.client.factories.ImageObjectFactory;
import com.github.groupa.client.jsonobjects.ImageInfo;
import com.github.groupa.client.servercommunication.RESTService;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

public class ImageUploader {
	private Library library;
	private RESTService restService;

	@Inject
	public ImageUploader(RESTService restService, SingleLibrary library) {
		this.restService = restService;
		this.library = library;
	}

	@Subscribe
	public void uploadFile(UploadImageEvent event) {
		if (library == null)
			throw new NullPointerException("library not initialized");
		ImageInfo imageInfo = restService.uploadImage(new TypedFile("image/*",
				event.getFile()));

		ImageObjectFactory imageObjectFactory = Main.injector
				.getInstance(ImageObjectFactory.class);
		ImageObject imageObject = imageObjectFactory.create(imageInfo
				.getImage().getId());

		library.add(imageObject);

		JOptionPane.showMessageDialog(null, "Image uploaded!");
	}

}
