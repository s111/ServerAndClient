package controllers;

import java.io.File;
import java.io.IOException;

import models.ImageModel;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Files;

public class ImageUploader extends Controller {
	public static final String IMAGE_DIRECTORY = "../../images/";

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

			ObjectNode rootNode = imageModel.generateImageInfoJSON(request());

			return created(rootNode);
		} else {
			return badRequest();
		}
	}
}
