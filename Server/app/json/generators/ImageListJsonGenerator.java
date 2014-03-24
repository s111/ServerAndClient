package json.generators;

import java.util.ArrayList;
import java.util.List;

import json.objects.ImageList;
import json.objects.ImageShort;
import models.Image;
import url.generators.ImageInfoURLGenerator;
import url.generators.ImageListURLGenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImageListJsonGenerator {
	private List<Image> images;

	private ImageListURLGenerator imageListURLGenerator;
	private ImageInfoURLGenerator imageInfoURLGenerator;

	private int offset;
	private int limit;

	public ImageListJsonGenerator(List<Image> images,
			ImageListURLGenerator imageListURLGenerator,
			ImageInfoURLGenerator imageInfoURLGenerator) {
		this.images = images;
		this.imageListURLGenerator = imageListURLGenerator;
		this.imageInfoURLGenerator = imageInfoURLGenerator;

		offset = imageListURLGenerator.getOffset();
		limit = imageListURLGenerator.getLimit();
	}

	public JsonNode toJson() {
		ImageList imageList = createImageListObject();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode imageListNode = mapper.convertValue(imageList, JsonNode.class);

		return imageListNode;
	}

	private ImageList createImageListObject() {
		ImageList imageList = new ImageList();
		imageList.setOffset(offset);
		imageList.setLimit(limit);
		imageList.setHref(imageListURLGenerator.getURL());
		imageList.setNext(imageListURLGenerator.getNextURL());
		imageList.setPrevious(imageListURLGenerator.getPreviousURL());
		imageList.setFirst(imageListURLGenerator.getFirstURL());
		imageList.setLast(imageListURLGenerator.getLastURL());

		List<ImageShort> images = imageToImageShort();

		imageList.setImages(images);

		return imageList;
	}

	private List<ImageShort> imageToImageShort() {
		List<ImageShort> shortImages = new ArrayList<>();

		for (Image image : images) {
			ImageShort imageShort = new ImageShort();

			long id = image.getId();

			imageShort.setHref(imageInfoURLGenerator.getURL(id));
			imageShort.setId(id);

			shortImages.add(imageShort);
		}

		return shortImages;
	}
}
