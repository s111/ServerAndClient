package controllers;

import java.io.File;

import models.ImageModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class GetImage extends Controller {
	public static Result info(long id) {
		ImageModel imageModel = ImageModel.get(id);

		if (imageModel == null)
			return badRequest();

		ObjectMapper mapper = new ObjectMapper();

		ObjectNode rootNode = Json.newObject();
		rootNode.put("href", routes.GetImage.info(id)
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

		ObjectNode image = mapper.convertValue(imageModel, ObjectNode.class);

		rootNode.put("image", image);

		return ok(rootNode);
	}

	public static Result file(long id) {
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

	private static String getAbsoluteURLToImageOrNull(long previousId) {
		return previousId > 0 ? routes.GetImage.info(previousId)
				.absoluteURL(request()) : null;
	}
}
