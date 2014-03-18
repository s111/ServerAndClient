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
		Optional<ImageModel> retrievedImageModel = ImageModel.get(id);

		if (!retrievedImageModel.isPresent()) {
			return notFound();
		}

		ImageModel imageModel = retrievedImageModel.get();

		response().setHeader("info-change-count",
				imageModel.infoChangeCount.toString());

		ImageInfoURLGenerator absoluteURLGenerator = new ImageInfoURLGenerator(
				request());

		ImageInfoJsonGenerator imageModelJsonGenerator = new ImageInfoJsonGenerator(
				imageModel, absoluteURLGenerator);

		JsonNode imageInfoNode = imageModelJsonGenerator.toJson();

		return ok(imageInfoNode);
	}

	public static Result file(long id) {
		Optional<ImageModel> retrievedImageModel = ImageModel.get(id);

		if (!retrievedImageModel.isPresent()) {
			return badRequest();
		}

		ImageModel imageModel = retrievedImageModel.get();

		response().setHeader("image-change-count",
				imageModel.imageChangeCount.toString());

		File image = new File(imageModel.filename);

		if (image.exists()) {
			return ok(image, true);
		} else {
			return notFound();
		}
	}
}
