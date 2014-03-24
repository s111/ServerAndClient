package controllers;

import java.util.List;

import json.generators.ImageListJsonGenerator;
import models.Image;
import models.Tag;
import play.mvc.Controller;
import play.mvc.Result;
import queryDB.QueryTag;
import url.generators.ImageInfoURLGenerator;
import url.generators.ImageListURLGenerator;
import utils.HibernateUtil;

import com.fasterxml.jackson.databind.JsonNode;

public class TagController extends Controller {
	public static Result getImages(String tag, int offset, int limit) {
		if (isNotWithinBoundaries(tag, offset, limit))
			return badRequest();

		QueryTag queryTag = new QueryTag(HibernateUtil.getSessionFactory());

		List<Image> images = queryTag.getImages(tag);

		ImageListURLGenerator imageListURLGenerator = new ImageListURLGenerator(
				offset, limit, request());

		ImageInfoURLGenerator imageInfoURLGenerator = new ImageInfoURLGenerator(
				request());

		ImageListJsonGenerator imageListJsonGenerator = new ImageListJsonGenerator(
				images, imageListURLGenerator, imageInfoURLGenerator);

		JsonNode imageListNode = imageListJsonGenerator.toJson();

		return ok(imageListNode);
	}

	private static boolean isNotWithinBoundaries(String tag, int offset,
			int limit) {
		QueryTag queryTag = new QueryTag(HibernateUtil.getSessionFactory());
		Tag retrievedTag = queryTag.getTag(tag);

		int numRows = 0;

		if (tag != null) {
			numRows = retrievedTag.getImages().size();
		}

		return (offset < 0 || limit < 0 || offset > numRows);
	}
}
