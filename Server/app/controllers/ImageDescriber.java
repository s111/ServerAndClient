package controllers;

import models.ImageModel;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class ImageDescriber extends Controller {
	public static Result describe(long id) {
		DynamicForm requestData = Form.form().bindFromRequest();
		
		String description = requestData.get("value");
		
		if (description == null || description.length() > 255) return badRequest();
		
		ImageModel imageModel = ImageModel.get(id);
		
		imageModel.description = description;
		imageModel.save();
		
		return ok();
	}
}
