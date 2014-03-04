package controllers;

import java.io.File;

import models.ImageModel;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class GetImage extends Controller {
	public static Result info(long id) {
		ImageModel imageModel = ImageModel.get(id);

		if (imageModel == null)
			return badRequest();

		ObjectNode rootNode = imageModel.generateImageInfoJSON(request());

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
}
