package com.github.groupa.client.servercommunication;

import java.awt.Rectangle;
import java.net.ConnectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.client.Response;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.google.inject.Inject;

public class ModifyImage {
	private static final Logger logger = LoggerFactory
			.getLogger(ModifyImage.class);
	private RESTService restService;

	@Inject
	public ModifyImage(RESTService restService) {
		this.restService = restService;
	}

	public void rotate(final Callback<ImageObject> callback,
			final ImageObject image, final int angle) {
		new Thread(new Runnable() {
			public void run() {
				Response response = null;
				try {
					response = restService.rotateImage(image.getId(), angle);
					if (response.getStatus() == 200) {
						image.refreshImage();
						callback.success(image);
						return;
					}
				} catch (ConnectException e) {
					logger.warn("Could not connect to server to rotate image");
				}
				callback.failure();
			}
		}).start();
	}

	public void crop(final Callback<ImageObject> callback,
			final ImageObject image, final Rectangle rectangle) {
		new Thread(new Runnable() {
			public void run() {
				Response response = null;
				try {
					response = restService.cropImage(image.getId(),
							rectangle.x, rectangle.y, rectangle.width,
							rectangle.height);
					if (response.getStatus() == 200) {
						image.refreshImage();
						callback.success(image);
						return;
					}
				} catch (ConnectException e) {
					logger.warn("Could not connect to server to crop image");
				}
				callback.failure();
			}
		}).start();
	}
}
