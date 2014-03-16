package generators;

import java.util.ArrayList;
import java.util.List;

import jsonobjects.ImageList;
import jsonobjects.ImageShort;
import models.ImageModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImageListJsonGenerator {
	private List<ImageModel> imageModels;

	private ImageListURLGenerator imageListURLGenerator;
	private ImageInfoURLGenerator imageInfoURLGenerator;

	private int offset;
	private int limit;

	public ImageListJsonGenerator(List<ImageModel> imageModels,
			ImageListURLGenerator imageListURLGenerator,
			ImageInfoURLGenerator imageInfoURLGenerator) {
		this.imageModels = imageModels;
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

		List<ImageShort> images = imageModelsToImageShort();

		imageList.setImages(images);

		return imageList;
	}

	private List<ImageShort> imageModelsToImageShort() {
		List<ImageShort> images = new ArrayList<>();

		for (ImageModel imageModel : imageModels) {
			ImageShort imageShort = new ImageShort();
			imageShort.setHref(imageInfoURLGenerator.getURL(imageModel));
			imageShort.setId(imageModel.id);

			images.add(imageShort);
		}

		return images;
	}
}
