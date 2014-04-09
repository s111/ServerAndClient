package com.github.groupa.client.servercommunication;

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

	public void rotate(final Callback<ImageObject> callback, final ImageObject image, final int angle) {
		new Thread(new Runnable() {
			public void run() {
				Response response = null;
				try {
					response = restService.rotateImage(image.getId(), angle);
					if (response.getStatus() == 202) {
						image.refreshImage();
						callback.success(image);
					}
				} catch (ConnectException e) {
					logger.warn("Could not connect to server to rate image");
				}
				callback.failure();
			}
		}).start();
	}
}
