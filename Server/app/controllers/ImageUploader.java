package controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import models.ImageModel;

import org.apache.commons.io.FileUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;

import play.Logger;
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

			String extension = null;

			try {
				extension = getExtensionFromMIME(file);
			} catch (MimeTypeException exception) {
				return handleMimeTypeExceptionWhileGettingExtension(file);
			} catch (IOException exception) {
				return handleIOExceptionWhileGettingExtension(file);
			}

			if (!extension.matches("\\.(jpg|png)"))
				return badRequest();

			File imageDirectory = new File(IMAGE_DIRECTORY);

			if (!imageDirectory.exists()) {
				imageDirectory.mkdir();
			}

			try {
				newFile = File.createTempFile("image", extension,
						imageDirectory);

				Files.copy(file, newFile);

				file.delete();
			} catch (IOException exception) {
				Logger.warn("IOException while trying to move file to image directory");

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

	private static Result handleIOExceptionWhileGettingExtension(File file) {
		Logger.warn("Failed to get extension from file due to IOException: "
				+ file.getAbsolutePath());
		Logger.warn("Can't continue with file upload");

		return internalServerError();
	}

	private static Result handleMimeTypeExceptionWhileGettingExtension(File file) {
		Logger.warn("Failed to get extension from file due to MimeTypeException: "
				+ file.getAbsolutePath());
		Logger.warn("Can't continue with file upload");

		return internalServerError();
	}

	private static String getExtensionFromMIME(File file) throws IOException,
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
}
