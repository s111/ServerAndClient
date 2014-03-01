package controllers;

import java.io.File;
import java.util.List;

import models.ImageModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ImageController extends Controller {
	public static Result getImageInfo(long id) {
		ImageModel imageModel = ImageModel.get(id);

		if (imageModel == null)
			return badRequest();

		ObjectMapper mapper = new ObjectMapper();

		ObjectNode rootNode = Json.newObject();
		rootNode.put("href", routes.ImageController.getImageInfo(id)
				.absoluteURL(request()));

		ImageModel nextImageModel = ImageModel.getNext(id);
		ImageModel previousImageModel = ImageModel.getPrevious(id);
		ImageModel firstImageModel = ImageModel.getFirst();
		ImageModel lastImageModel = ImageModel.getLast();

		long nextId = -1;
		long previousId = -1;
		long firstId = -1;
		long lastId = -1;

		if (nextImageModel != null) {
			nextId = nextImageModel.id;
		}

		if (previousImageModel != null) {
			previousId = previousImageModel.id;
		}

		if (firstImageModel != null) {
			firstId = firstImageModel.id;
		}

		if (lastImageModel != null) {
			lastId = lastImageModel.id;
		}

		rootNode.put("next", getAbsoluteURLToImageOrNull(nextId));

		rootNode.put("previous", getAbsoluteURLToImageOrNull(previousId));

		rootNode.put("first", getAbsoluteURLToImageOrNull(firstId));

		rootNode.put("last", getAbsoluteURLToImageOrNull(lastId));

		/*
		 * This document should also contain a first, next, previous, last
		 * pointing to the appropriate images
		 */

		ObjectNode image = mapper.convertValue(imageModel, ObjectNode.class);

		rootNode.put("image", image);

		return ok(rootNode);
	}

	private static String getAbsoluteURLToImageOrNull(long previousId) {
		return previousId > 0 ? routes.ImageController.getImageInfo(previousId)
				.absoluteURL(request()) : null;
	}

	public static Result getImage(long id) {
		ImageModel imageModel = ImageModel.get(id);

		if (imageModel == null)
			return badRequest();

		File image = new File(imageModel.filename);

		if (image.exists()) {
			return ok(image, true);
		} else {
			return notFound();
		}
	}

	public static Result getImages(int offset, int limit) {
		if (isNotWithinBoundaries(offset, limit))
			return badRequest();

		ObjectNode rootNode = createJSONForImages(offset, limit);

		List<ImageModel> imageModels = ImageModel.getPageList(offset, limit);

		ArrayNode images = createJSONArrayFromImageModels(imageModels);

		rootNode.put("images", images);

		return ok(rootNode);
	}

	private static ObjectNode createJSONForImages(int offset, int limit) {
		int lastOffset = calculateLastOffset(limit);
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

	private static String getAbsoluteURLToImageListOrNull(int offset, int limit) {
		return offset >= 0 ? getAbsoluteURLToImageList(offset,
				limit) : null;
	}

	private static String getAbsoluteURLToImageList(int offset, int limit) {
		return routes.ImageController.getImages(offset, limit).absoluteURL(
				request());
	}

	private static ArrayNode createJSONArrayFromImageModels(
			List<ImageModel> imageModels) {
		ObjectMapper mapper = new ObjectMapper();

		ArrayNode images = mapper.convertValue(imageModels, ArrayNode.class);

		for (JsonNode image : images) {
			long id = image.get("id").asLong();

			((ObjectNode) image).put("href", routes.ImageController
					.getImageInfo(id).absoluteURL(request()));
		}

		return images;
	}

	private static int calculateLastOffset(int limit) {
		int numRows = ImageModel.getRowCount();
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

	private static boolean isNotWithinBoundaries(int offset, int limit) {
		int numRows = ImageModel.getRowCount();

		return (offset < 0 || limit < 0 || offset > numRows);
	}
}
