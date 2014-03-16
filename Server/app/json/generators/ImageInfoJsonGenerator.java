package json.generators;

import java.util.ArrayList;
import java.util.List;

import url.generators.ImageInfoURLGenerator;
import json.objects.ImageFull;
import json.objects.ImageInfo;
import models.ImageModel;
import models.TagModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImageInfoJsonGenerator {
	private ImageModel imageModel;

	private ImageInfoURLGenerator absoluteURLGenerator;

	public ImageInfoJsonGenerator(ImageModel imageModel,
			ImageInfoURLGenerator absoluteURLGenerator) {
		this.imageModel = imageModel;
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
		imageInfo.setHref(absoluteURLGenerator.getURL(imageModel));
		imageInfo.setNext(absoluteURLGenerator.getNextURL(imageModel));
		imageInfo.setPrevious(absoluteURLGenerator.getPreviousURL(imageModel));
		imageInfo.setFirst(absoluteURLGenerator.getFirstURL());
		imageInfo.setLast(absoluteURLGenerator.getLastURL());
		imageInfo.setImage(imageModelToImageFull());

		return imageInfo;
	}

	private ImageFull imageModelToImageFull() {
		ImageFull imageFull = new ImageFull();
		imageFull.setId(imageModel.id);
		imageFull.setRating(imageModel.rating);
		imageFull.setDescription(imageModel.description);

		List<String> tagsAsString = new ArrayList<>();

		for (TagModel tag : imageModel.tags) {
			tagsAsString.add(tag.name);
		}

		imageFull.setTags(tagsAsString);

		return imageFull;
	}
}
