package controllers;

import java.io.File;

import json.generators.ImageInfoJsonGenerator;
import models.ImageModel;
import play.mvc.Controller;
import play.mvc.Result;
import url.generators.ImageInfoURLGenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;

public class GetImage extends Controller {
	public static Result info(long id) {
		Optional<ImageModel> imageModel = ImageModel.get(id);

		if (!imageModel.isPresent()) {
			return notFound();
		}

		ImageInfoURLGenerator absoluteURLGenerator = new ImageInfoURLGenerator(
				request());

		ImageInfoJsonGenerator imageModelJsonGenerator = new ImageInfoJsonGenerator(
				imageModel.get(), absoluteURLGenerator);

		JsonNode imageInfoNode = imageModelJsonGenerator.toJson();

		return ok(imageInfoNode);
	}

	public static Result file(long id) {
		Optional<ImageModel> imageModel = ImageModel.get(id);

		if (!imageModel.isPresent()) {
			return badRequest();
		}

		File image = new File(imageModel.get().filename);

		if (image.exists()) {
			return ok(image, true);
		} else {
			return notFound();
		}
	}
}
