package controllers;

import java.io.File;
import java.io.IOException;

import models.Image;
import models.Thumbnail;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import queryDB.QueryImage;
import queryDB.QueryThumbnail;
import thumbnail.generator.ThumbnailGenerator;
import utils.HibernateUtil;

public class GetThumbnail extends Controller {
	public static Result getXSmall(long id) {
		return getThumbnail(id, 0);
	}

	public static Result getSmall(long id) {
		return getThumbnail(id, 1);
	}

	public static Result getMedium(long id) {
		return getThumbnail(id, 2);
	}

	public static Result getLarge(long id) {
		return getThumbnail(id, 3);
	}

	public static Result getXLarge(long id) {
		return getThumbnail(id, 4);
	}

	public static Result getCompressed(long id) {
		return getThumbnail(id, 5);
	}

	public static Result getThumbnail(long id, int size) {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image image = queryImage.getImage(id);

		if (image == null) {
			return notFound();
		}

		File thumbnail = null;

		try {
			thumbnail = getThumbnailFile(image, size);
		} catch (IOException e) {
			return internalServerError();
		}

		return ok(thumbnail, true);
	}

	private static File getThumbnailFile(Image image, int size)
			throws IOException {
		QueryThumbnail queryThumbnail = new QueryThumbnail(
				HibernateUtil.getSessionFactory());

		Thumbnail thumbnail = queryThumbnail.getThumbnail(image.getId(), size);

		if (thumbnail == null) {
			createNewThumbnail(image, size);

			thumbnail = queryThumbnail.getThumbnail(image.getId(), size);
		}

		File file = new File(thumbnail.getFilename());

		return file;
	}

	private static void createNewThumbnail(Image image, int size)
			throws IOException {
		ThumbnailGenerator thumbnailGenerator = new ThumbnailGenerator(image,
				size);

		try {
			thumbnailGenerator.writeThumbnailToDisk();
		} catch (IOException exception) {
			logIOException(exception);
		}

		thumbnailGenerator.saveThumbnailToDatabase();
	}

	private static void logIOException(IOException exception)
			throws IOException {
		Logger.error("Could not write thumbnail to disk: "
				+ exception.getMessage());

		throw exception;
	}
}
