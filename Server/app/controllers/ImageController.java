package controllers;

import models.ImageModel;
import play.mvc.Controller;
import play.mvc.Result;
import collection.ImageModelList;

import com.fasterxml.jackson.databind.JsonNode;

public class ImageController extends Controller {
	public static Result getImages(int offset, int limit) {
		if (isNotWithinBoundaries(offset, limit))
			return badRequest();

		if (limit == 0) {
			limit = ImageModel.getRowCount();
		}

		ImageModelList imageModelList = new ImageModelList(request(), offset,
				limit);

		JsonNode imageListNode = imageModelList.generateJSON();

		return ok(imageListNode);
	}

	private static boolean isNotWithinBoundaries(int offset, int limit) {
		int numRows = ImageModel.getRowCount();

		return (offset < 0 || limit < 0 || offset > numRows);
	}
}
