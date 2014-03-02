package controllers;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import models.ImageModel;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

public class ImageUploader extends Controller {
	private static final String IMAGE_DIRECTORY = "../../images/";

	public static Result upload() {
		MultipartFormData body = request().body().asMultipartFormData();
		
		FilePart image = null;

		if (body != null) {
			image = body.getFile("value");
		} else {
			return badRequest();
		}

		if (image != null) {
			File file = image.getFile();
			File newFile;

			try {
				newFile = File.createTempFile("image", ".png", new File(
						IMAGE_DIRECTORY));

				Files.move(file, newFile);
			} catch (IOException e) {
				return internalServerError();
			}

			String filename = newFile.getName();

			ImageModel imageModel = ImageModel.create(IMAGE_DIRECTORY
					+ filename);

			return ok("{\"id\":" + imageModel.id + "}").as(
					"application/json; charset=utf-8");
		} else {
			return badRequest();
		}
	}
}
