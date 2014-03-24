package controllers;

import java.io.File;

import json.generators.ImageInfoJsonGenerator;
import models.Image;
import play.mvc.Controller;
import play.mvc.Result;
import queryDB.QueryImage;
import url.generators.ImageInfoURLGenerator;
import utils.HibernateUtil;

import com.fasterxml.jackson.databind.JsonNode;

public class GetImage extends Controller {
	public static Result info(long id) {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image image = queryImage.getImage(id);

		if (image == null) {
			return notFound();
		}

		ImageInfoURLGenerator absoluteURLGenerator = new ImageInfoURLGenerator(
				request());

		ImageInfoJsonGenerator imageInfoJsonGenerator = new ImageInfoJsonGenerator(
				image, absoluteURLGenerator);

		JsonNode imageInfoNode = imageInfoJsonGenerator.toJson();

		return ok(imageInfoNode);
	}

	public static Result file(long id) {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());
		Image image = queryImage.getImage(id);

		if (image == null) {
			return badRequest();
		}

		File file = new File(image.getFilename());

		if (file.exists()) {
			return ok(file, true);
		} else {
			return notFound();
		}
	}
}
