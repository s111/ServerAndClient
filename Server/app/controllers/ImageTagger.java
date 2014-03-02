package controllers;

import models.ImageModel;
import models.TagModel;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class ImageTagger extends Controller {
	public static Result tag(long id) {
		DynamicForm requestData = Form.form().bindFromRequest();

		String tagData = requestData.get("value");

		if (tagData == null)
			return badRequest();

		String[] tags = tagData.split(",");

		ImageModel imageModel = ImageModel.get(id);

		for (String tag : tags) {
			imageModel.tag(TagModel.get(tag));
		}

		return ok();
	}
}
