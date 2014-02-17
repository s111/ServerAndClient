package controllers;

import java.io.File;

import models.ImageModel;
import play.mvc.Controller;
import play.mvc.Result;

public class ImageController extends Controller {
	public static Result getImage(long id) {
		ImageModel imageModel = ImageModel.getImageModel(id);
		
		if (imageModel == null) return badRequest();
		
		File image = new File(imageModel.filename);
		
		if (image.exists()) {
			return ok();
		} else {
			return badRequest();
		}
	}
}
