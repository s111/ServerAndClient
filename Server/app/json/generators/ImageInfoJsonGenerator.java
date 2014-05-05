package json.generators;

import java.util.ArrayList;
import java.util.List;

import json.objects.ImageFull;
import json.objects.ImageInfo;
import models.Image;
import models.Tag;
import url.generators.ImageInfoURLGenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImageInfoJsonGenerator {
	private Image image;

	private ImageInfoURLGenerator absoluteURLGenerator;

	public ImageInfoJsonGenerator(Image image,
			ImageInfoURLGenerator absoluteURLGenerator) {
		this.image = image;
		this.absoluteURLGenerator = absoluteURLGenerator;
	}

	public JsonNode toJson() {
		ImageInfo imageInfo = createImageInfoObject();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode imageInfoNode = mapper.convertValue(imageInfo, JsonNode.class);

		return imageInfoNode;
	}

	private ImageInfo createImageInfoObject() {
		ImageInfo imageInfo = new ImageInfo();
		imageInfo.setHref(absoluteURLGenerator.getURL(image.getId()));
		imageInfo.setNext(absoluteURLGenerator.getNextURL(image.getId()));
		imageInfo
				.setPrevious(absoluteURLGenerator.getPreviousURL(image.getId()));
		imageInfo.setFirst(absoluteURLGenerator.getFirstURL());
		imageInfo.setLast(absoluteURLGenerator.getLastURL());
		imageInfo.setImage(imageToImageFull());

		return imageInfo;
	}

	private ImageFull imageToImageFull() {
		ImageFull imageFull = new ImageFull();
		imageFull.setId(image.getId());

		Integer rating = image.getRating();

		imageFull.setRating(rating == null ? 0 : rating);
		imageFull.setDescription(image.getDescription());
		imageFull.setDateTaken(image.getDateTaken());

		List<String> tagsAsString = new ArrayList<>();

		for (Tag tag : image.getTags()) {
			tagsAsString.add(tag.getName());
		}

		imageFull.setTags(tagsAsString);

		return imageFull;
	}
}
