package controllers;

import java.util.List;

import json.generators.ImageListJsonGenerator;
import models.ImageModel;
import play.mvc.Controller;
import play.mvc.Result;
import url.generators.ImageInfoURLGenerator;
import url.generators.ImageListURLGenerator;

import com.fasterxml.jackson.databind.JsonNode;

public class ImageController extends Controller {
	public static Result getImages(int offset, int limit) {
		if (isNotWithinBoundaries(offset, limit))
			return badRequest();

		if (limit == 0) {
			limit = ImageModel.getRowCount();
		}

		response().setHeader("list-change-count",
				ImageModel.listChangeCount.toString());

		List<ImageModel> imageModels = ImageModel.getList(offset, limit);

		ImageListURLGenerator imageListURLGenerator = new ImageListURLGenerator(
				offset, limit, request());

		ImageInfoURLGenerator imageInfoURLGenerator = new ImageInfoURLGenerator(
				request());

		ImageListJsonGenerator imageListJsonGenerator = new ImageListJsonGenerator(
				imageModels, imageListURLGenerator, imageInfoURLGenerator);

		JsonNode imageListNode = imageListJsonGenerator.toJson();

		return ok(imageListNode);
	}

	private static boolean isNotWithinBoundaries(int offset, int limit) {
		int numRows = ImageModel.getRowCount();

		return (offset < 0 || limit < 0 || offset > numRows);
	}
}
