package upload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import json.generators.ImageInfoJsonGenerator;
import metadata.PrepareImage;
import models.Image;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;

import play.Logger;
import play.mvc.Http.Request;
import queryDB.QueryImage;
import queryDB.QueryTag;
import url.generators.ImageInfoURLGenerator;
import utils.HibernateUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Files;

public class Uploader {
	public static final String IMAGE_DIRECTORY = "../../images/";

	private File file;
	private File newFile;
	private File uploadDirectory;

	private String extension;

	private boolean uploaded;

	private Image image;

	public Uploader(File file) {
		this.file = file;

		uploadDirectory = new File(IMAGE_DIRECTORY);

		trySettingExtension();
	}

	public boolean upload() {
		if (extension == null) {
			return false;
		}

		uploaded = tryToMoveTemporaryFileToUploadDirectory();

		if (uploaded) {
			image = createImage();
		}

		return uploaded;
	}

	public JsonNode toJson(Request request) {
		if (!uploaded) {
			throw new IllegalStateException(
					"Cannot generate json if the upload did not succeed");
		}

		JsonNode imageInfoNode = generateJson(request, image);

		return imageInfoNode;
	}

	private Image createImage() {
		String filename = newFile.getName();

		Image image = new Image();
		image.setFilename(IMAGE_DIRECTORY + filename);

		PrepareImage.loadImageWithMetadataFromFile(image);

		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());
		queryImage.addImage(image);

		QueryTag queryTag = new QueryTag(HibernateUtil.getSessionFactory());
		/* TODO Remove the id:{id} tag before release */
		queryTag.tagImage(image.getId(), "id:" + image.getId());

		return image;
	}

	private JsonNode generateJson(Request request, Image image) {
		ImageInfoURLGenerator absoluteURLGenerator = new ImageInfoURLGenerator(
				request);

		ImageInfoJsonGenerator imageInfoJsonGenerator = new ImageInfoJsonGenerator(
				image, absoluteURLGenerator);

		JsonNode imageInfoNode = imageInfoJsonGenerator.toJson();

		return imageInfoNode;
	}

	public boolean hasValidExtension() {
		return extension.matches("\\.(jpg|png)");
	}

	private boolean tryToMoveTemporaryFileToUploadDirectory() {
		try {
			newFile = tryToCreateNewFile(newFile);

			tryToCopyImageToNewFile(newFile);
		} catch (IOException exception) {
			return false;
		} finally {
			boolean imageIsDeleted = file.delete();

			if (!imageIsDeleted) {
				Logger.of("logger").warn(
						"Failed to delete temporary uploaded file");
			}
		}

		return true;
	}

	private void tryToCopyImageToNewFile(File newFile) throws IOException {
		try {
			Files.copy(file, newFile);
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
					+ file.getAbsolutePath());
		} catch (IOException e) {
			Logger.of("logger").warn(
					"Failed to get extension from file due to MimeTypeException: "
							+ file.getAbsolutePath());
		}
	}

	private String getExtension() throws IOException, MimeTypeException {
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
