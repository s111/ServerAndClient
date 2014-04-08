package controllers;

import java.io.File;
import java.io.IOException;

import models.Image;
import net.coobird.thumbnailator.Thumbnails;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import queryDB.QueryImage;
import queryDB.QueryThumbnail;
import utils.HibernateUtil;

public class ImageRotater extends Controller {
	public static Result rotate(long id, int angle) {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image image = queryImage.getImage(id);

		if (!(angle == 90 || angle == -90) || image == null) {
			return badRequest();
		}

		File file = new File(image.getFilename());

		try {
			Thumbnails.of(file).scale(1).rotate(angle).allowOverwrite(true)
					.toFile(file);
		} catch (IOException e) {
			Logger.warn("Could not rotate image");

			return internalServerError();
		}

		QueryThumbnail queryThumbnail = new QueryThumbnail(
				HibernateUtil.getSessionFactory());
		queryThumbnail.deleteAllThumbnails(image.getId());

		return ok();
	}
}
