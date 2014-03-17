package controllers;

import java.io.File;

import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import upload.Uploader;

import com.fasterxml.jackson.databind.JsonNode;

public class ImageUploader extends Controller {
	public static final String IMAGE_DIRECTORY = "../../images/";

	public static Result upload() {
		MultipartFormData body = request().body().asMultipartFormData();

		FilePart value = null;

		if (body != null) {
			value = body.getFile("value");
		} else {
			return badRequest();
		}

		if (value != null) {
			File image = value.getFile();

			Uploader uploader = new Uploader(image);

			if (!uploader.hasValidExtension()) {
				return badRequest();
			}

			boolean uploadSucceeded = uploader.upload();

			if (!uploadSucceeded) {
				return internalServerError();
			}

			JsonNode imageInfoNode = uploader.toJson(request());

			return ok(imageInfoNode);
		} else {
			return badRequest();
		}
	}
}
