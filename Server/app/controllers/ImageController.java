package controllers;

import java.util.List;

import json.generators.ImageListJsonGenerator;
import models.Image;
import play.mvc.Controller;
import play.mvc.Result;
import queryDB.QueryImage;
import url.generators.ImageInfoURLGenerator;
import url.generators.ImageListURLGenerator;
import utils.HibernateUtil;

import com.fasterxml.jackson.databind.JsonNode;

public class ImageController extends Controller {
	public static Result getImages(int offset, int limit) {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		int numRows = queryImage.getImages().size();

		if (offset < 0 || limit < 0 || offset > numRows) {
			return badRequest();
		}

		if (limit == 0) {
			limit = numRows;
		}

		List<Image> images = queryImage.getImages(offset, limit);

		ImageListURLGenerator imageListURLGenerator = new ImageListURLGenerator(
				offset, limit, request());

		ImageInfoURLGenerator imageInfoURLGenerator = new ImageInfoURLGenerator(
				request());

		ImageListJsonGenerator imageListJsonGenerator = new ImageListJsonGenerator(
				images, imageListURLGenerator, imageInfoURLGenerator);

		JsonNode imageListNode = imageListJsonGenerator.toJson();

		return ok(imageListNode);
	}
}
