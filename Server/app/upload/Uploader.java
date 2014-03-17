package upload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import json.generators.ImageInfoJsonGenerator;
import models.ImageModel;
import models.TagModel;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;

import play.Logger;
import play.mvc.Http.Request;
import url.generators.ImageInfoURLGenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Files;

import controllers.ImageUploader;

public class Uploader {
	private File image;
	private File newFile;
	private File uploadDirectory;

	private String extension;

	private boolean uploaded;

	public Uploader(File image) {
		this.image = image;

		trySettingExtension();
	}

	public boolean upload() {
		makeSureUploadDirectoryExists();
		uploaded = tryTomoveTemporaryFileToUploadDirectory();

		return uploaded;
	}

	public JsonNode toJson(Request request) {
		if (!uploaded) {
			throw new IllegalStateException(
					"Cannot generate json if the upload did not succeed");
		}

		ImageModel imageModel = createImageModel();
		JsonNode imageInfoNode = generateJson(request, imageModel);

		return imageInfoNode;
	}

	private ImageModel createImageModel() {
		String filename = newFile.getName();

		ImageModel imageModel = ImageModel.create(ImageUploader.IMAGE_DIRECTORY
				+ filename);
		/* TODO Remove this before release */
		imageModel.addTag(TagModel.create("id:" + imageModel.id));

		return imageModel;
	}

	private JsonNode generateJson(Request request, ImageModel imageModel) {
		ImageInfoURLGenerator absoluteURLGenerator = new ImageInfoURLGenerator(
				request);

		ImageInfoJsonGenerator imageModelJsonGenerator = new ImageInfoJsonGenerator(
				imageModel, absoluteURLGenerator);

		JsonNode imageInfoNode = imageModelJsonGenerator.toJson();

		return imageInfoNode;
	}

	public boolean hasValidExtension() {
		return extension.matches("\\.(jpg|png)");
	}

	private boolean tryTomoveTemporaryFileToUploadDirectory() {
		try {
			newFile = tryToCreateNewFile(newFile);

			tryToCopyImageToNewFile(newFile);
		} catch (IOException exception) {
			return false;
		} finally {
			boolean imageIsDeleted = image.delete();

			if (!imageIsDeleted) {
				Logger.warn("Failed to delete temporary uploaded file");
			}
		}

		return true;
	}

	private void tryToCopyImageToNewFile(File newFile) throws IOException {
		try {
			Files.copy(image, newFile);
		} catch (IOException exception) {
			Logger.error("Could not copy temporary image to upload directory");

			throw exception;
		}
	}

	private File tryToCreateNewFile(File newFile) throws IOException {
		try {
			newFile = File.createTempFile("image", extension, uploadDirectory);
		} catch (IOException exception) {
			Logger.error("Could not create new file");

			throw exception;
		}

		return newFile;
	}

	private void makeSureUploadDirectoryExists() {
		uploadDirectory = new File(ImageUploader.IMAGE_DIRECTORY);

		if (!uploadDirectory.exists()) {
			uploadDirectory.mkdir();
		}
	}

	private void trySettingExtension() {
		try {
			this.extension = getExtension();
		} catch (MimeTypeException e) {
			Logger.error("Failed to get extension from file due to IOException: "
					+ image.getAbsolutePath());
		} catch (IOException e) {
			Logger.warn("Failed to get extension from file due to MimeTypeException: "
					+ image.getAbsolutePath());
		}
	}

	private String getExtension() throws IOException, MimeTypeException {
		TikaConfig config = TikaConfig.getDefaultConfig();

		BufferedInputStream stream = new BufferedInputStream(
				new FileInputStream(image));
		MediaType mediaType = config.getMimeRepository().detect(stream,
				new Metadata());
		MimeType mimeType = config.getMimeRepository().forName(
				mediaType.toString());

		return mimeType.getExtension();
	}
}
