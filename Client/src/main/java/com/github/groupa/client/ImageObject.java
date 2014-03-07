package com.github.groupa.client;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import com.github.groupa.client.servercommunication.RESTService;

public class ImageObject {
	private Image image = null;
	private long id = -1;
	private int rating = 0;
	private String description = null;
	private RESTService restService;
	private HashMap<String, Integer> sizeMap = new HashMap<>();
	private List<String> tags = new LinkedList<>();

	public ImageObject(long id, RESTService restService) {
		this.id = id;
		this.restService = restService;

		sizeMap.put("xs", 48);
		sizeMap.put("s", 64);
		sizeMap.put("m", 128);
		sizeMap.put("l", 192);
		sizeMap.put("xl", 256);
	}

	public boolean hasImage() {
		return image != null;
	}

	public Image getImage() {
		try {
			return (image == null) ? (image = ImageIO.read(restService.getThumbnailXLarge(id).getBody().in()))
					: image;
		} catch (IOException e) {
			return null;
		}
	}

	public long getId() {
		return id;
	}

	/***
	 * Valid value for rating is 0 < rating <= 5
	 * 
	 * @param rating
	 * @return
	 */
	public void rate(int rating) {
		restService.rateImage(id, rating);
	}

	public int getRating() {
		return rating;
	}

	public void describe(String description) {
		restService.describeImage(id, description);
	}

	public String getDescription() {
		return description;
	}

	public List<String> getTags() {
		return tags;
	}

	public void addTag(String tag) {
		restService.tagImage(id, tag);
	}

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	public boolean hasTags(List<String> tags) {
		return this.tags.containsAll(tags);
	}
}
