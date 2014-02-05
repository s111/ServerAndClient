package controllers;

import static helpers.JsonHelper.badRequestNode;
import static helpers.JsonHelper.notFoundNode;
import static helpers.JsonHelper.okNode;
import static helpers.JsonHelper.serverErrorNode;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import models.ImageModel;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.Request;
import play.mvc.Result;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Files;

public class ImageIO extends Controller {
	private static final String UPLOAD_DIRECTORY = "public/upload/";

	public static Result uploadImage() {
		File file = getFile(request());

		if (file == null)
			return badRequest(badRequestNode());

		String relativePathToFile;

		try {
			relativePathToFile = moveFileToUploadDirectory(file);
		} catch (IOException | MimeTypeException exception) {
			return internalServerError(serverErrorNode());
		}

		Long imageId = addImageToDB(relativePathToFile);

		return ok(imageIdNode(imageId));
	}

	private static File getFile(Request request) {
		MultipartFormData body = request.body().asMultipartFormData();

		try {
			return body.getFile("image").getFile();
		} catch (NullPointerException exception) {
			return null;
		}
	}

	private static String moveFileToUploadDirectory(File file)
			throws IOException, MimeTypeException {
		String extension = getExtension(file);

		File newFile = File.createTempFile("image", extension, new File(
				UPLOAD_DIRECTORY));
		
		String relativePathToFile = UPLOAD_DIRECTORY + newFile.getName();
		
		Files.move(file, newFile);

		return relativePathToFile;
	}

	private static String getExtension(File file) throws IOException,
			MimeTypeException {
		TikaConfig config = TikaConfig.getDefaultConfig();

		BufferedInputStream stream = new BufferedInputStream(
				new FileInputStream(file));
		MediaType mediaType = config.getMimeRepository().detect(stream,
				new Metadata());
		MimeType mimeType = config.getMimeRepository().forName(
				mediaType.toString());

		return mimeType.getExtension();
	}

	private static Long addImageToDB(String relativePathToFile) {
		ImageModel imageModel = ImageModel.create(relativePathToFile);

		return imageModel.id;
	}

	private static ObjectNode imageIdNode(Long imageId) {
		ObjectNode node = okNode();
		ObjectNode data = Json.newObject();

		node.put("data", data);
		data.put("id", imageId);

		return node;
	}

	public static Result removeImage(Long imageId) {
		ImageModel imageModel = ImageModel.getImage(imageId);

		if (imageModel != null) {
			imageModel.delete();

			File image = new File(imageModel.filename);

			if (image.exists())
				image.delete();

			return ok(okNode());
		}

		return notFound(notFoundNode());
	}
}
