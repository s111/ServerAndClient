package controllers;

import generators.ImageInfoURLGenerator;
import generators.ImageListJsonGenerator;
import generators.ImageListURLGenerator;

import java.util.ArrayList;
import java.util.List;

import models.ImageModel;
import models.TagModel;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;

public class TagController extends Controller {
	public static Result getImages(String tag, int offset, int limit) {
		if (isNotWithinBoundaries(tag, offset, limit))
			return badRequest();

		Optional<TagModel> retrievedTagModel = TagModel.get(tag);

		List<ImageModel> imageModels;

		if (retrievedTagModel.isPresent()) {
			imageModels = retrievedTagModel.get().images;
		} else {
			imageModels = new ArrayList<>();
		}

		ImageListURLGenerator imageListURLGenerator = new ImageListURLGenerator(
				offset, limit, request());

		ImageInfoURLGenerator imageInfoURLGenerator = new ImageInfoURLGenerator(
				request());

		ImageListJsonGenerator imageListJsonGenerator = new ImageListJsonGenerator(
				imageModels, imageListURLGenerator, imageInfoURLGenerator);

		JsonNode imageListNode = imageListJsonGenerator.toJson();

		return ok(imageListNode);
	}

	private static boolean isNotWithinBoundaries(String tag, int offset,
			int limit) {
		Optional<TagModel> retrievedTagModel = TagModel.get(tag);

		int numRows = 0;

		if (retrievedTagModel.isPresent()) {
			numRows = retrievedTagModel.get().images.size();
		}

		return (offset < 0 || limit < 0 || offset > numRows);
	}
}
