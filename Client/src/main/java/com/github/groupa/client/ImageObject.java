package com.github.groupa.client;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.github.groupa.client.events.ImageModifiedEvent;
import com.github.groupa.client.jsonobjects.ImageInfo;
import com.github.groupa.client.servercommunication.ServerConnection;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;

public class ImageObject {
	public static Map<String, Integer> thumbSize = new HashMap<>();
	static {
		thumbSize.put("xs", 40);
		thumbSize.put("s", 80);
		thumbSize.put("m", 140);
		thumbSize.put("l", 220);
		thumbSize.put("xl", 260);
	}

	private static final Logger logger = LoggerFactory
			.getLogger(ImageObject.class);

	private EventBus eventBus;
	private ServerConnection serverConnection;
	private long id;

	private ImageInfo imageInfo;
	private Map<String, BufferedImage> images = new HashMap<>();

	private boolean valid = true;

	@Inject
	public ImageObject(EventBus eventBus, ServerConnection serverConnection,
			@Assisted long id) {
		this.eventBus = eventBus;
		this.serverConnection = serverConnection;
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public boolean hasDescription() {
		if (loadImage())
			return imageInfo.getImage().getDescription().length() > 0;
		return false;
	}

	public String getDescription() {
		if (loadImage())
			return imageInfo.getImage().getDescription();
		return "";
	}

	public boolean hasRating() {
		if (loadImage())
			return imageInfo.getImage().getRating() != 0;
		return false;
	}

	public int getRating() {
		if (loadImage())
			return imageInfo.getImage().getRating();
		return 0;
	}

	public List<String> getTags() {
		if (loadImage())
			return imageInfo.getImage().getTags();
		return new ArrayList<String>();
	}

	public boolean hasTag(String tag) {
		if (loadImage())
			return imageInfo.getImage().getTags().contains(tag);
		return false;
	}

	public void addTag(String tag) {
		if (tag.length() == 0 || tag.contains(","))
			error("Invalid tag: " + tag);
		_addTag(tag);
	}

	public void rate(int rating) {
		if (rating < 0 || rating > 5)
			error("Invalid rating: " + rating);
		_setRating(rating);
	}

	public void describe(String description) {
		_setDescription(description);
	}

	public boolean hasImageRaw() {
		return _hasImage("raw");
	}

	public BufferedImage getImageRaw() {
		return _getImage("raw");
	}

	public boolean hasImage() {
		return _hasImage("compressed") || _hasImage("raw");
	}

	public BufferedImage getImage() {
		if (_hasImage("raw"))
			return _getImage("raw");
		return _getImage("compressed");
	}

	public boolean hasThumb(String size) {
		return _hasImage(size);
	}

	public BufferedImage getThumb(String size) {
		return _getImage(size);
	}

	public void loadImage(final Callback<BufferedImage> callback,
			final String size) {
		if (_hasImage(size)) {
			callback.success(_getImage(size));
		} else {
			new Thread(new Runnable() {
				public void run() {
					BufferedImage image = serverConnection.getImage(id, size);
					if (image != null) {
						ImageObject.this.images.put(size, image);
						callback.success(image);
					} else
						callback.failure();
					// TODO: Send event
				}
			}).start();
		}
	}

	private boolean _hasImage(String img) {
		return images.containsKey(img);
	}

	private BufferedImage _getImage(String img) {
		return images.get(img);
	}

	private void _addTag(final String tag) {
		new Thread(new Runnable() {
			public void run() {
				if (serverConnection.addTag(id, tag)) {
					if (imageInfo == null)
						loadImage();
					else
						imageInfo.getImage().getTags().add(tag);
					eventBus.post(new ImageInfoChangedEvent(ImageObject.this));
				}
			}
		}).start();
	}

	private void _setRating(final int rating) {
		new Thread(new Runnable() {
			public void run() {
				if (serverConnection.rate(id, rating)) {
					if (imageInfo == null)
						loadImage();
					else
						imageInfo.getImage().setRating(rating);
					eventBus.post(new ImageInfoChangedEvent(ImageObject.this));
				}
			}
		}).start();
	}

	private void _setDescription(final String description) {
		new Thread(new Runnable() {
			public void run() {
				if (serverConnection.describe(id, description)) {
					if (imageInfo == null)
						loadImage();
					else
						imageInfo.getImage().setDescription(description);
					eventBus.post(new ImageInfoChangedEvent(ImageObject.this));
				}
			}
		}).start();
	}

	private boolean loadImage() {
		if (imageInfo != null)
			return true;
		imageInfo = serverConnection.getImageInfo(id);
		return imageInfo != null;
	}

	private void error(String string) {
		logger.error(string);
		throw new RuntimeException(string);
	}

	public boolean equals(Object o) {
		if (o == null)
			return false;
		return ((ImageObject) o).getId() == getId();
	}
	
	public int hashCode() {
		return (int)(this.id^(this.id>>>32));
	}

	public void refreshImage() {
		valid = false;
		Set<String> sizes = images.keySet();
		images.clear();
		Map<String, BufferedImage> newImages = new HashMap<>();
		for (String size : sizes) {
			BufferedImage image = serverConnection.getImage(id, size);
			if (image != null) {
				newImages.put(size, image);
			} else {
				logger.warn("Error refreshing images");
			}
		}
		images.putAll(newImages);
		valid = true;
		eventBus.post(new ImageModifiedEvent(this));
	}

	public boolean isValid() {
		return valid;
	}
}
