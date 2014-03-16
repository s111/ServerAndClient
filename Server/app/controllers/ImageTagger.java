package controllers;

import models.ImageModel;
import models.TagModel;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.base.Optional;

public class ImageTagger extends Controller {
	public static Result tag(long id) {
		DynamicForm requestData = Form.form().bindFromRequest();

		String tagData = requestData.get("value");

		Optional<ImageModel> imageModel = ImageModel.get(id);

		if (tagData == null || !imageModel.isPresent())
			return badRequest();

		String[] tags = tagData.split(",");

		for (String tag : tags) {
			imageModel.get().addTag(TagModel.get(tag));
		}

		return ok();
	}
}
