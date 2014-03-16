package controllers;

import models.ImageModel;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.base.Optional;

public class ImageRater extends Controller {
	public static Result rate(long id) {
		DynamicForm requestData = Form.form().bindFromRequest();

		int rating = 0;

		try {
			rating = Integer.parseInt(requestData.get("value"));
		} catch (NumberFormatException exception) {
			return badRequest();
		}

		Optional<ImageModel> imageModel = ImageModel.get(id);

		if (rating < 1 || rating > 5 || !imageModel.isPresent())
			return badRequest();

		imageModel.get().setRating(rating);

		return ok();
	}
}
