package com.github.groupa.client;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.groupa.client.events.ImageAvailableEvent;
import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.github.groupa.client.events.ImageModifiedEvent;
import com.github.groupa.client.jsonobjects.ImageInfo;
import com.github.groupa.client.servercommunication.ServerConnection;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;

public class ImageObject {
	public static Map<String, Integer> thumbSize = new HashMap<>();
	static {
		thumbSize.put("xs", 48);
		thumbSize.put("s", 64);
		thumbSize.put("m", 128);
		thumbSize.put("l", 192);
		thumbSize.put("xl", 256);
	}

	private static final Logger logger = LoggerFactory
			.getLogger(ImageObject.class);

	private EventBus eventBus;
	private ServerConnection serverConnection;
	private long id;

	private ImageInfo imageInfo;
	private Map<String, BufferedImage> images = new HashMap<>();

	private boolean valid = true;

	private BackgroundImageFetcher backgroundImageFetcher;

	@Inject
	public ImageObject(EventBus eventBus, ServerConnection serverConnection,
			BackgroundImageFetcher backgroundImageFetcher, @Assisted long id) {
		this.eventBus = eventBus;
		this.serverConnection = serverConnection;
		this.backgroundImageFetcher = backgroundImageFetcher;
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Date getDateTaken() {
		if (loadImage())
			return imageInfo.getImage().getDateTaken();
		return null;
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
			return (List<String>) Collections.unmodifiableList(imageInfo
					.getImage().getTags());
		return new ArrayList<String>();
	}

	public boolean hasTag(String tag) {
		if (loadImage())
			return imageInfo.getImage().getTags().contains(tag);
		return false;
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

	public boolean hasImage(String size) {
		return _hasImage(size);
	}

	public BufferedImage getImage(String size) {
		return _getImage(size);
	}

	public BufferedImage getThumb(String size) {
		return _getImage(size);
	}

	public void loadImage(final String size, int priority) {
		if (!_hasImage(size)) {
			BackgroundImageFetch job = new BackgroundImageFetch(new Runnable() {
				public void run() {
					BufferedImage image = serverConnection.getImage(id, size);
					if (image != null) {
						ImageObject.this.images.put(size, image);
						eventBus.post(new ImageAvailableEvent(ImageObject.this,
								size));
					}
				}
			}, this, size);
			BackgroundJob oldJob = backgroundImageFetcher.getJob(job);
			if (oldJob == null) {
				job.setPriority(priority);
				backgroundImageFetcher.addJob(job);
			}
		}
	}

	public void rate(final int rating) {
		if (imageInfo == null)
			loadImage();
		else
			imageInfo.getImage().setRating(rating);
		eventBus.post(new ImageInfoChangedEvent(this));
	}

	public void describe(final String description) {
		if (imageInfo == null)
			loadImage();
		else
			imageInfo.getImage().setDescription(description);
		eventBus.post(new ImageInfoChangedEvent(this));
	}

	public void addTag(final String tag) {
		if (imageInfo == null)
			loadImage();
		else
			imageInfo.getImage().getTags().add(tag);
		eventBus.post(new ImageInfoChangedEvent(this));
	}

	public void deleteTag(final String tag) {
		if (imageInfo == null)
			loadImage();
		else
			imageInfo.getImage().getTags().remove(tag);
		eventBus.post(new ImageInfoChangedEvent(this));
	}

	private boolean _hasImage(String img) {
		return images.containsKey(img);
	}

	private BufferedImage _getImage(String img) {
		return images.get(img);
	}

	private boolean loadImage() {
		if (imageInfo != null)
			return true;
		imageInfo = serverConnection.getImageInfo(id);
		return imageInfo != null;
	}

	public void reloadImageInfo() {
		ImageInfo newImageInfo = serverConnection.getImageInfo(id);
		if (newImageInfo != null) {
			imageInfo = newImageInfo;
			eventBus.post(new ImageInfoChangedEvent(this));
		}
	}

	public boolean equals(Object o) {
		if (o == null)
			return false;
		return ((ImageObject) o).getId() == getId();
	}

	public int hashCode() {
		return (int) (this.id ^ (this.id >>> 32));
	}

	public void refreshImages() {
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
