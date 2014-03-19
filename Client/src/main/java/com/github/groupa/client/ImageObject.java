package com.github.groupa.client;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.client.Response;

import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.github.groupa.client.jsonobjects.ImageFull;
import com.github.groupa.client.jsonobjects.ImageInfo;
import com.github.groupa.client.servercommunication.RESTService;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;

public class ImageObject {
	private static final Logger logger = LoggerFactory
			.getLogger(ImageObject.class);

	private static final Object staticLock = new Object();

	private RESTService restService;

	private long id;

	private Image imageRaw;

	private ImageInfo imageInfo;

	private ImageFull imageFull;

	private EventBus eventBus;

	private Map<String, Integer> thumbSize = new HashMap<>();
	private Map<String, Image> thumbs = new HashMap<>();

	@Inject
	public ImageObject(EventBus eventBus, RESTService restService,
			@Assisted long id) {
		this.eventBus = eventBus;
		this.restService = restService;
		this.id = id;
		thumbSize.put("xs", 40);
		thumbSize.put("s", 80);
		thumbSize.put("m", 140);
		thumbSize.put("l", 220);
		thumbSize.put("xl", 260);
	}

	public boolean hasImageRaw() {
		return imageRaw != null;
	}

	public boolean hasImageInfo() {
		return imageInfo != null;
	}

	public Image getImageRaw() {
		if (!hasImageRaw()) {
			loadImage();
		}

		return imageRaw;
	}

	public void loadImageWithCallback(final Callback<Image> callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (!hasImageRaw()) {
					loadImage();
				}

				callback.success(imageRaw);
			}
		}).start();
	}

	private Image getThumb(String size, boolean halfSize) {
		return thumbs.get(size + halfSize);
	}

	private void downloadThumb(String size) {
		if (!hasImageRaw()) {
			loadImage();
		}
		if (hasImageRaw()) {
			thumbs.put(size + false, imageRaw.getScaledInstance(
					thumbSize.get(size), -1, Image.SCALE_FAST));

			thumbs.put(size + true, imageRaw.getScaledInstance(
					thumbSize.get(size) / 2, -1, Image.SCALE_FAST));
		}
	}

	public void loadThumbWithCallback(final Callback<Image> callback,
			final String size, final boolean halfSize) {
		new Thread(new Runnable() { // This should use actual thumbs from server
									// later on
					@Override
					public void run() {
						Image image = getThumb(size, halfSize);
						if (image == null) {
							downloadThumb(size);
							image = getThumb(size, halfSize);
							if (image == null)
								callback.failure();
						}
						callback.success(image);
					}
				}).start();
	}

	public ImageInfo getImageInfo() {
		// We need to check with the server for actual changes, for now just
		// always load image info
		loadImageInfo();

		return imageInfo;
	}

	private void loadImage() {
		Response imageRawResponse = null;

		try {
			imageRawResponse = restService.getImageRaw(id);
		} catch (ConnectException e) {
			logger.warn("Could not get raw image for id: " + id);
		}

		if (imageRawResponse == null) {
			return;
		}

		try {
			InputStream imageRawStream = imageRawResponse.getBody().in();

			synchronized (staticLock) {
				imageRaw = ImageIO.read(imageRawStream);
			}

			imageRawStream.close();
		} catch (IOException exception) {
			logger.warn("Failed to load image: " + exception.getMessage());
		}
	}

	private void loadImageInfo() {
		try {
			imageInfo = restService.getImageInfo(id);
		} catch (ConnectException e) {
			logger.warn("Could not load image info for image with id: " + id);
		}

		imageFull = imageInfo.getImage();
	}

	public long getId() {
		return id;
	}

	public int getRating() {
		loadImageInfo();

		return imageFull.getRating();
	}

	public String getDescription() {
		loadImageInfo();

		return imageFull.getDescription();
	}

	public List<String> getTags() {
		loadImageInfo();

		return imageFull.getTags();
	}

	/**
	 * @param rating
	 *            0 < rating <= 5
	 */
	public void rate(int rating) {
		try {
			restService.rateImage(id, rating);

			eventBus.post(new ImageInfoChangedEvent(this));
		} catch (ConnectException e) {
			logger.warn("Could not connect to server and rate image");
		}
	}

	public void describe(String description) {
		try {
			restService.describeImage(id, description);

			eventBus.post(new ImageInfoChangedEvent(this));
		} catch (ConnectException e) {
			logger.warn("Could not connect to server and describe image");
		}
	}

	public void addTag(String tag) {
		try {
			restService.tagImage(id, tag);

			eventBus.post(new ImageInfoChangedEvent(this));
		} catch (ConnectException e) {
			logger.warn("Could not connect to server and tag image");
		}
	}

	public boolean hasTag(String tag) {
		loadImageInfo();

		return imageFull.getTags().contains(tag);
	}

	public boolean hasTags(List<String> tags) {
		loadImageInfo();

		return imageFull.getTags().containsAll(tags);
	}
}
