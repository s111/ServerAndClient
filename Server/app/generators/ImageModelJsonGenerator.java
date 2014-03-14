package generators;

import java.util.ArrayList;
import java.util.List;

import jsonobjects.ImageFull;
import jsonobjects.ImageInfo;
import models.ImageModel;
import models.TagModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImageModelJsonGenerator {
	private ImageModel imageModel;

	private AbsoluteURLGenerator absoluteURLGenerator;

	public ImageModelJsonGenerator(ImageModel imageModel,
			AbsoluteURLGenerator absoluteURLGenerator) {
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
		imageInfo.setHref(absoluteURLGenerator.getURL());
		imageInfo.setNext(absoluteURLGenerator.getNextURL());
		imageInfo.setPrevious(absoluteURLGenerator.getPreviousURL());
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
