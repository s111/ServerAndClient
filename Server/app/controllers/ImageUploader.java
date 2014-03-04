package controllers;

import java.io.File;
import java.io.IOException;

import models.ImageModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

import com.fasterxml.jackson.databind.ObjectMapper;
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
			
			long id = imageModel.id;

			ObjectMapper mapper = new ObjectMapper();

			ObjectNode rootNode = Json.newObject();
			rootNode.put("href", routes.GetImage.info(id)
					.absoluteURL(request()));

			ImageModel nextImageModel = ImageModel.getNext(id);
			ImageModel previousImageModel = ImageModel.getPrevious(id);
			ImageModel firstImageModel = ImageModel.getFirst();
			ImageModel lastImageModel = ImageModel.getLast();

			long nextId = -1;
			long previousId = -1;
			long firstId = -1;
			long lastId = -1;

			if (nextImageModel != null) {
				nextId = nextImageModel.id;
			}

			if (previousImageModel != null) {
				previousId = previousImageModel.id;
			}

			if (firstImageModel != null) {
				firstId = firstImageModel.id;
			}

			if (lastImageModel != null) {
				lastId = lastImageModel.id;
			}

			rootNode.put("next", getAbsoluteURLToImageOrNull(nextId));

			rootNode.put("previous", getAbsoluteURLToImageOrNull(previousId));

			rootNode.put("first", getAbsoluteURLToImageOrNull(firstId));

			rootNode.put("last", getAbsoluteURLToImageOrNull(lastId));

			ObjectNode imageJSON = mapper.convertValue(imageModel, ObjectNode.class);

			rootNode.put("image", imageJSON);

			return created(rootNode);
		} else {
			return badRequest();
		}
	}
	
	private static String getAbsoluteURLToImageOrNull(long previousId) {
		return previousId > 0 ? routes.GetImage.info(previousId)
				.absoluteURL(request()) : null;
	}
}
