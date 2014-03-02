package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.ImageModel;
import models.TagModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class TagController extends Controller {
	public static Result getImages(String tag, int offset, int limit) {
		if (isNotWithinBoundaries(tag, offset, limit))
			return badRequest();

		ObjectNode rootNode = createJSONForImages(tag, offset, limit);

		List<ImageModel> imageModels = TagModel.get(tag).images;

		ArrayNode images = createJSONArrayFromImageModels(imageModels);

		rootNode.put("images", images);

		return ok(rootNode);
	}
	
	private static ArrayNode createJSONArrayFromImageModels(
			List<ImageModel> imageModels) {
		ObjectMapper mapper = new ObjectMapper();

		ArrayNode images = mapper.convertValue(imageModels, ArrayNode.class);

		for (JsonNode image : images) {
			long id = image.get("id").asLong();

			((ObjectNode) image).removeAll();
			((ObjectNode) image).put("id", id);
			((ObjectNode) image).put("href", routes.GetImage.info(id)
					.absoluteURL(request()));
		}

		return images;
	}
	
	private static ObjectNode createJSONForImages(String tag, int offset, int limit) {
		int lastOffset = calculateLastOffset(tag, limit);
		int previousOffset = calculatePreviousOffset(offset, limit);
		int nextOffset = calculateNextOffset(offset, limit);

		String next = getAbsoluteURLToImageListOrNull(nextOffset, limit);
		String href = getAbsoluteURLToImageList(offset, limit);
		String first = getAbsoluteURLToImageList(0, 25);
		String previous = getAbsoluteURLToImageListOrNull(previousOffset, limit);
		String last = getAbsoluteURLToImageList(lastOffset, limit);

		ObjectNode rootNode = Json.newObject();
		rootNode.put("href", href);
		rootNode.put("offset", offset);
		rootNode.put("limit", limit);
		rootNode.put("first", first);
		rootNode.put("next", next);
		rootNode.put("previous", previous);
		rootNode.put("last", last);

		return rootNode;
	}
	
	private static int calculateLastOffset(String tag, int limit) {
		int numRows = TagModel.get(tag).images.size();
		int pages = numRows / limit;
		int lastOffset = pages * limit;

		return lastOffset;
	}

	private static int calculateNextOffset(int offset, int limit) {
		int numRows = ImageModel.getRowCount();

		int nextOffset = offset + limit;

		if (nextOffset > numRows - 1) {
			nextOffset = -1;
		}

		return nextOffset;
	}

	private static int calculatePreviousOffset(int offset, int limit) {
		int previousOffset = offset - limit;

		if (previousOffset < 0) {
			previousOffset = 0;
		}

		return previousOffset;
	}
	
	private static String getAbsoluteURLToImageListOrNull(int offset, int limit) {
		return offset >= 0 ? getAbsoluteURLToImageList(offset, limit) : null;
	}

	private static String getAbsoluteURLToImageList(int offset, int limit) {
		return routes.ImageController.getImages(offset, limit).absoluteURL(
				request());
	}
	
	private static boolean isNotWithinBoundaries(String tag, int offset, int limit) {
		int numRows = TagModel.get(tag).images.size();

		return (offset < 0 || limit < 0 || offset > numRows);
	}
}
