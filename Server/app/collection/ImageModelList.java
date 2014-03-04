package collection;

import java.util.List;

import play.libs.Json;
import play.mvc.Http.Request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import controllers.routes;
import models.ImageModel;

public class ImageModelList {
	List<ImageModel> imageModels;
	
	Request request;
	
	int offset;
	int limit;
	
	public ImageModelList(Request request, int offset, int limit) {
		this.request = request;
		this.offset = offset;
		this.limit = limit;
		
		imageModels = ImageModel.getPageList(offset, limit);
	}
	
	public ObjectNode generateJSON() {
		int lastOffset = calculateLastOffset();
		int previousOffset = calculatePreviousOffset();
		int nextOffset = calculateNextOffset();

		String next = getAbsoluteURLToImageListOrNull(nextOffset);
		String href = getAbsoluteURLToImageList(offset);
		String first = getAbsoluteURLToImageList(0);
		String previous = getAbsoluteURLToImageListOrNull(previousOffset);
		String last = getAbsoluteURLToImageList(lastOffset);

		ObjectNode rootNode = Json.newObject();
		rootNode.put("href", href);
		rootNode.put("offset", offset);
		rootNode.put("limit", limit);
		rootNode.put("first", first);
		rootNode.put("next", next);
		rootNode.put("previous", previous);
		rootNode.put("last", last);
		rootNode.put("images", generateImageListJSON());

		return rootNode;
	}
	
	private ArrayNode generateImageListJSON() {
		ObjectMapper mapper = new ObjectMapper();

		ArrayNode images = mapper.convertValue(imageModels, ArrayNode.class);

		for (JsonNode image : images) {
			long id = image.get("id").asLong();

			((ObjectNode) image).removeAll();
			((ObjectNode) image).put("id", id);
			((ObjectNode) image).put("href", routes.GetImage.info(id)
					.absoluteURL(request));
		}

		return images;
	}
	
	private int calculateLastOffset() {
		int numRows = ImageModel.getRowCount();
		int pages = numRows / limit;
		int lastOffset = pages * limit;

		return lastOffset;
	}

	private int calculateNextOffset() {
		int numRows = ImageModel.getRowCount();

		int nextOffset = offset + limit;

		if (nextOffset > numRows - 1) {
			nextOffset = -1;
		}

		return nextOffset;
	}

	private int calculatePreviousOffset() {
		int previousOffset = offset - limit;

		if (previousOffset < 0) {
			previousOffset = 0;
		}

		return previousOffset;
	}
	
	private String getAbsoluteURLToImageListOrNull(int offset) {
		return offset >= 0 ? getAbsoluteURLToImageList(offset) : null;
	}

	private String getAbsoluteURLToImageList(int offset) {
		return routes.ImageController.getImages(offset, limit).absoluteURL(
				request);
	}
}
