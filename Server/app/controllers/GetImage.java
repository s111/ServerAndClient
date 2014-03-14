package controllers;

import generators.AbsoluteURLGenerator;
import generators.ImageModelJsonGenerator;

import java.io.File;

import models.ImageModel;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

public class GetImage extends Controller {
	public static Result info(long id) {
		ImageModel imageModel = ImageModel.get(id);

		if (imageModel == null)
			return notFound();

		AbsoluteURLGenerator absoluteURLGenerator = new AbsoluteURLGenerator(
				imageModel, request());

		ImageModelJsonGenerator imageModelJsonGenerator = new ImageModelJsonGenerator(
				imageModel, absoluteURLGenerator);

		JsonNode imageInfoNode = imageModelJsonGenerator.toJson();

		return ok(imageInfoNode);
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
