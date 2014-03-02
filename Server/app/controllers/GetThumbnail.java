package controllers;

import java.io.File;

import models.ImageModel;
import models.ThumbnailModel;
import play.mvc.Controller;
import play.mvc.Result;

public class GetThumbnail extends Controller {
	public static Result getXSmall(long id) {
		ImageModel imageModel = ImageModel.get(id);

		if (imageModel == null)
			return notFound();

		File thumbnail = getThumbnail(imageModel, 0);

		return ok(thumbnail, true);
	}

	public static Result getSmall(long id) {
		ImageModel imageModel = ImageModel.get(id);

		if (imageModel == null)
			return notFound();

		File thumbnail = getThumbnail(imageModel, 1);

		return ok(thumbnail, true);
	}

	public static Result getMedium(long id) {
		ImageModel imageModel = ImageModel.get(id);

		if (imageModel == null)
			return notFound();

		File thumbnail = getThumbnail(imageModel, 2);

		return ok(thumbnail, true);
	}

	public static Result getLarge(long id) {
		ImageModel imageModel = ImageModel.get(id);

		if (imageModel == null)
			return notFound();

		File thumbnail = getThumbnail(imageModel, 3);

		return ok(thumbnail, true);
	}

	public static Result getXLarge(long id) {
		ImageModel imageModel = ImageModel.get(id);

		if (imageModel == null)
			return notFound();

		File thumbnail = getThumbnail(imageModel, 4);

		return ok(thumbnail, true);
	}

	private static File getThumbnail(ImageModel imageModel, int size) {
		ThumbnailModel thumbnailModel = imageModel.thumbnails.get(size);

		if (thumbnailModel == null) {
			/*
			 * this means that there is no entry for this thumbnail size
			 * generate one!
			 */
			
			// just gonna return image 01.png till we can generate thumbnails
			thumbnailModel = ThumbnailModel.create("../../images/01.png", size);
		}

		File thumbnail = new File(thumbnailModel.filename);

		return thumbnail;
	}
}
