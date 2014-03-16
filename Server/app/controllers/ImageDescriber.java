package controllers;

import models.ImageModel;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.base.Optional;

public class ImageDescriber extends Controller {
	public static Result describe(long id) {
		DynamicForm requestData = Form.form().bindFromRequest();

		String description = requestData.get("value");

		Optional<ImageModel> imageModel = ImageModel.get(id);

		if (description == null || description.length() > 255
				|| !imageModel.isPresent()) {
			return badRequest();
		}

		imageModel.get().setDescription(description);

		return ok();
	}
}
