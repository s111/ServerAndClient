package controllers;

import java.io.File;
import java.io.IOException;

import models.ImageModel;
import models.ThumbnailModel;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import thumbnail.generator.ThumbnailGenerator;

import com.google.common.base.Optional;

public class GetThumbnail extends Controller {
	public static Result getXSmall(long id) {
		return getThumbnail(id, ThumbnailModel.X_SMALL);
	}

	public static Result getSmall(long id) {
		return getThumbnail(id, ThumbnailModel.SMALL);
	}

	public static Result getMedium(long id) {
		return getThumbnail(id, ThumbnailModel.MEDIUM);
	}

	public static Result getLarge(long id) {
		return getThumbnail(id, ThumbnailModel.LARGE);
	}

	public static Result getXLarge(long id) {
		return getThumbnail(id, ThumbnailModel.X_LARGE);
	}

	public static Result getCompressed(long id) {
		return getThumbnail(id, ThumbnailModel.COMPRESSED);
	}

	public static Result getThumbnail(long id, int size) {
		Optional<ImageModel> imageModel = ImageModel.get(id);

		if (!imageModel.isPresent()) {
			return notFound();
		}

		File thumbnail = null;

		try {
			thumbnail = getThumbnailFile(imageModel.get(), size);
		} catch (IOException e) {
			return internalServerError();
		}

		return ok(thumbnail, true);
	}

	private static File getThumbnailFile(ImageModel imageModel, int size)
			throws IOException {
		Optional<ThumbnailModel> retrievedThumbnailModel = ThumbnailModel.get(
				imageModel.id, size);

		ThumbnailModel thumbnailModel;

		if (!retrievedThumbnailModel.isPresent()) {
			thumbnailModel = createNewThumbnail(imageModel, size);
		} else {
			thumbnailModel = retrievedThumbnailModel.get();
		}

		File thumbnail = new File(thumbnailModel.filename);

		return thumbnail;
	}

	private static ThumbnailModel createNewThumbnail(ImageModel imageModel,
			int size) throws IOException {
		ThumbnailModel thumbnailModel;

		ThumbnailGenerator thumbnailGenerator = new ThumbnailGenerator(
				imageModel, size);

		try {
			thumbnailGenerator.writeThumbnailToDisk();
		} catch (IOException exception) {
			logIOException(exception);
		}

		thumbnailModel = thumbnailGenerator.saveThumbnailToDatabase();

		return thumbnailModel;
	}

	private static void logIOException(IOException exception)
			throws IOException {
		Logger.error("Could not write thumbnail to disk: "
				+ exception.getMessage());

		throw exception;
	}
}
