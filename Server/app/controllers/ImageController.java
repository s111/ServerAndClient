package controllers;

import static helpers.JsonHelper.notFoundNode;

import java.io.File;

import models.ImageModel;
import play.mvc.Controller;
import play.mvc.Result;

public class ImageController extends Controller {
	public static Result search() {
		return TODO;
	}

	public static Result getImage(Long imageId) {
		ImageModel imageModel = ImageModel.getImage(imageId);

		if (imageModel != null) {
			File image = new File(imageModel.filename);

			if (image.exists())
				return ok(image, true);
		}

		return notFound(notFoundNode());
	}

	public static Result getComments(Long imageId) {
		return TODO;
	}

	public static Result addComment(Long imageId) {
		return TODO;
	}

	public static Result removeComment(Long imageId, Long commentId) {
		return TODO;
	}

	public static Result getTags(Long imageId) {
		return TODO;
	}

	public static Result addTag(Long imageId) {
		return TODO;
	}

	public static Result removeTag(Long imageId, String tagName) {
		return TODO;
	}

	public static Result setRating(Long imageId) {
		return TODO;
	}

	public static Result removeRating(Long imageId) {
		return TODO;
	}
}
