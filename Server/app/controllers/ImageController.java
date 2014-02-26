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
	public static Result getImage(long id) {
		ImageModel imageModel = ImageModel.getImageModel(id);

		if (imageModel == null)
			return badRequest();

		File image = new File(imageModel.filename);

		if (image.exists()) {
			return ok(image, true);
		} else {
			return badRequest();
		}
	}

	public static Result getImages(int offset, int limit) throws Exception {
		int numRows = ImageModel.getRowCount();
		
		if (offset < 0 || limit < 0 || offset > numRows)
			return badRequest();

		int pages = numRows / limit;
		int previousOffset = offset - limit;
		int nextOffset = offset + limit;
		
		if (previousOffset < 0) {
			previousOffset = 0;
		}
		
		String next = routes.ImageController.getImages(offset + limit, limit)
				.absoluteURL(request());
		String previous = routes.ImageController.getImages(previousOffset, limit)
				.absoluteURL(request());
		
		if (offset == 0) {
			previous = null;
		}
		
		if (nextOffset > numRows - 1) {
			next = null;
		}

		List<ImageModel> imageModels = ImageModel.getSubList(offset, limit);

		ObjectMapper mapper = new ObjectMapper();

		ObjectNode rootNode = Json.newObject();
		rootNode.put("href", routes.ImageController.getImages(offset, limit)
				.absoluteURL(request()));
		rootNode.put("offset", offset);
		rootNode.put("limit", limit);
		rootNode.put("first", routes.ImageController.getImages(0, 25)
				.absoluteURL(request()));
		rootNode.put("previous", previous);
		rootNode.put("next", next);
		rootNode.put("last",
				routes.ImageController.getImages(pages * limit, limit)
						.absoluteURL(request()));

		ArrayNode images = mapper.convertValue(imageModels, ArrayNode.class);

		for (JsonNode image : images) {
			long id = image.get("id").asLong();

			((ObjectNode) image).put("href", routes.ImageController
					.getImage(id).absoluteURL(request()));
		}

		rootNode.put("items", images);

		return ok(rootNode);
	}
}
