package controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import models.ImageModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ImageController extends Controller {
	public static Result getImage(long id) {
		ImageModel imageModel = ImageModel.getImageModel(id);
		
		if (imageModel == null) return badRequest();
		
		File image = new File(imageModel.filename);
		
		if (image.exists()) {
			return ok(image, true);
		} else {
			return badRequest();
		}
	}
	
	public static Result getImages(int offset, int limit) throws IOException {
		List<ImageModel> imageModels = ImageModel.getAll();
		
		ObjectMapper mapper = new ObjectMapper();
		
		ObjectNode rootNode = Json.newObject();
		
		rootNode.put("href", routes.ImageController.getImages(offset, limit).absoluteURL(request()));
		
		JsonNode images = mapper.convertValue(imageModels, JsonNode.class);
		
		rootNode.put("items", images);
		
		return ok(rootNode);
	}
}
