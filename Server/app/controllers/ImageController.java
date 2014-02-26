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
		if (offset < 0 || limit < 0) return badRequest();
		List<ImageModel> imageModels = ImageModel.getSubList(offset, limit);

		ObjectMapper mapper = new ObjectMapper();

		ObjectNode rootNode = Json.newObject();

		rootNode.put("href", routes.ImageController.getImages(offset, limit)
				.absoluteURL(request()));
		rootNode.put("offset", offset);
		rootNode.put("limit", limit);

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
