package com.github.groupa.client;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.client.Response;

import com.github.groupa.client.jsonobjects.ImageFull;
import com.github.groupa.client.jsonobjects.ImageInfo;
import com.github.groupa.client.servercommunication.RESTService;

public class ImageObject {
	private static final Logger logger = LoggerFactory
			.getLogger(ImageObject.class);

	private RESTService restService;

	private long id;

	private Image imageRaw;

	private ImageInfo imageInfo;
	
	private ImageFull imageFull;

	public ImageObject(long id, RESTService restService) {
		this.id = id;
		this.restService = restService;
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

	public ImageInfo getImageInfo() {
		if (!hasImageInfo()) {
			loadImageInfo();
		}

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

			imageRaw = ImageIO.read(imageRawStream);
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
		return imageFull.getRating();
	}

	public String getDescription() {
		return imageFull.getDescription();
	}

	public List<String> getTags() {
		return imageFull.getTags();
	}

	/**
	 * @param rating 0 < rating <= 5
	 */
	public void rate(int rating) {
		restService.rateImage(id, rating);
	}

	public void describe(String description) {
		restService.describeImage(id, description);
	}

	public void addTag(String tag) {
		restService.tagImage(id, tag);
	}

	public boolean hasTag(String tag) {
		return imageFull.getTags().contains(tag);
	}

	public boolean hasTags(List<String> tags) {
		return imageFull.getTags().containsAll(tags);
	}
}
