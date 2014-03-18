package controllers;

import models.ImageModel;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.base.Optional;

public class ResourceChanged extends Controller {
	public static Result info(long id) {
		Optional<ImageModel> retrievedImageModel = ImageModel.get(id);

		if (!retrievedImageModel.isPresent()) {
			return badRequest();
		}

		ImageModel imageModel = retrievedImageModel.get();

		response().setHeader("info-change-count",
				imageModel.infoChangeCount.toString());

		return ok();
	}

	public static Result getImages(int offset, int limit) {
		response().setHeader("list-change-count",
				ImageModel.listChangeCount.toString());

		return ok();
	}

	public static Result file(long id) {
		Optional<ImageModel> retrievedImageModel = ImageModel.get(id);

		if (!retrievedImageModel.isPresent()) {
			return badRequest();
		}

		ImageModel imageModel = retrievedImageModel.get();

		response().setHeader("image-change-count",
				imageModel.imageChangeCount.toString());

		return ok();
	}
}
