package controllers;

import generators.AbsoluteURLGenerator;
import generators.ImageModelJsonGenerator;

import java.io.File;

import models.ImageModel;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;

public class GetImage extends Controller {
	public static Result info(long id) {
		Optional<ImageModel> imageModel = ImageModel.get(id);

		if (!imageModel.isPresent()) {
			return notFound();
		}

		AbsoluteURLGenerator absoluteURLGenerator = new AbsoluteURLGenerator(
				imageModel.get(), request());

		ImageModelJsonGenerator imageModelJsonGenerator = new ImageModelJsonGenerator(
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
