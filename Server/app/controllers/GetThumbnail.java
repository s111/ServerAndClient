package controllers;

import java.io.File;
import java.io.IOException;

import models.ImageModel;
import models.ThumbnailModel;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FilenameUtils;

import play.mvc.Controller;
import play.mvc.Result;

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

	public static Result getThumbnail(long id, int size) {
		ImageModel imageModel = ImageModel.get(id);

		if (imageModel == null)
			return notFound();

		File thumbnail = null;

		try {
			thumbnail = getThumbnailFile(imageModel, size);
		} catch (IOException e) {
			return internalServerError();
		}

		return ok(thumbnail, true);
	}

	private static File getThumbnailFile(ImageModel imageModel, int size)
			throws IOException {
		ThumbnailModel thumbnailModel = imageModel.thumbnails.get(size);

		if (thumbnailModel == null) {
			File rawImage = new File(imageModel.filename);
			String filename = generateThumbnailFilename(rawImage, size);

			int w = 32;
			int h = 32;

			switch (size) {
			case ThumbnailModel.X_SMALL:
				break;
			case ThumbnailModel.SMALL:
				w = 64;
				h = 64;
				break;
			case ThumbnailModel.MEDIUM:
				w = 128;
				h = 128;
				break;
			case ThumbnailModel.LARGE:
				w = 640;
				h = 480;
				break;
			case ThumbnailModel.X_LARGE:
				w = 1024;
				h = 768;
				break;
			default:
				break;
			}

			Thumbnails.of(rawImage).size(w, h).toFile(filename);
			thumbnailModel = ThumbnailModel.create(filename, size);
		}

		File thumbnail = new File(thumbnailModel.filename);

		return thumbnail;
	}

	private static String generateThumbnailFilename(File file, int size) {
		String baseName = FilenameUtils.getBaseName(file.getAbsolutePath());
		String extension = FilenameUtils.getExtension(file.getAbsolutePath());

		return ImageUploader.IMAGE_DIRECTORY + "thumb" + baseName + "size"
				+ size + "." + extension;
	}
}
