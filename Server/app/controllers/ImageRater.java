package controllers;

import models.ImageModel;
import play.mvc.Controller;
import play.mvc.Result;

public class ImageRater extends Controller {
	public static Result rate(long id) {
		ImageModel imageModel = ImageModel.get(id);
		
		imageModel.rating = 5;
		imageModel.save();
		
		return ok();
	}
}
