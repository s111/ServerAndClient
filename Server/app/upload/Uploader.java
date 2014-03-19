package upload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import json.generators.ImageInfoJsonGenerator;
import metadata.PrepareImageModel;
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

public class Uploader {
	public static final String IMAGE_DIRECTORY = "../../images/";

	private File image;
	private File newImage;
	private File uploadDirectory;

	private String extension;

	private boolean uploaded;

	private ImageModel imageModel;

	public Uploader(File image) {
		this.image = image;

		uploadDirectory = new File(IMAGE_DIRECTORY);

		trySettingExtension();
	}

	public boolean upload() {
		if (extension == null) {
			return false;
		}

		uploaded = tryToMoveTemporaryFileToUploadDirectory();

		if (uploaded) {
			imageModel = createImageModel();
		}

		return uploaded;
	}

	public JsonNode toJson(Request request) {
		if (!uploaded) {
			throw new IllegalStateException(
					"Cannot generate json if the upload did not succeed");
		}

		JsonNode imageInfoNode = generateJson(request, imageModel);

		return imageInfoNode;
	}

	private ImageModel createImageModel() {
		String filename = newImage.getName();

		ImageModel imageModel = ImageModel.create(IMAGE_DIRECTORY + filename);

		PrepareImageModel.loadImageModelWithMetadataFromFile(imageModel);

		/* TODO Remove the id:{id} tag before release */
		imageModel.tags.add(TagModel.create("id:" + imageModel.id));
		imageModel.save();

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

	private boolean tryToMoveTemporaryFileToUploadDirectory() {
		try {
			newImage = tryToCreateNewFile(newImage);

			tryToCopyImageToNewFile(newImage);
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

	private void trySettingExtension() {
		try {
			extension = getExtension();
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
