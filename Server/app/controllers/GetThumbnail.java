package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import models.ImageModel;
import models.ThumbnailModel;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FilenameUtils;

import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.base.Optional;

public class GetThumbnail extends Controller {
	public static Result getXSmall(long id) {
		return getThumbnail(id, ThumbnailModel.X_SMALL);
	}

	public static Result getSmall(long id) {
		return getThumbnail(id, ThumbnailModel.SMALL);
	}

	public static Result getMedium(long id) {
		return getThumbnail(id, ThumbnailModel.MEDIUM);
	}

	public static Result getLarge(long id) {
		return getThumbnail(id, ThumbnailModel.LARGE);
	}

	public static Result getXLarge(long id) {
		return getThumbnail(id, ThumbnailModel.X_LARGE);
	}

	public static Result getCompressed(long id) {
		return getThumbnail(id, ThumbnailModel.COMPRESSED);
	}

	public static Result getThumbnail(long id, int size) {
		Optional<ImageModel> imageModel = ImageModel.get(id);

		if (!imageModel.isPresent()) {
			return notFound();
		}

		File thumbnail = null;

		try {
			thumbnail = getThumbnailFile(imageModel.get(), size);
		} catch (IOException e) {
			return internalServerError();
		}

		return ok(thumbnail, true);
	}

	private static File getThumbnailFile(ImageModel imageModel, int size)
			throws IOException {
		Optional<ThumbnailModel> retrievedThumbnailModel = ThumbnailModel.get(
				imageModel.id, size);

		ThumbnailModel thumbnailModel;

		if (!retrievedThumbnailModel.isPresent()) {
			File rawImage = new File(imageModel.filename);
			String filename = generateThumbnailFilename(rawImage, size);

			BufferedImage bufferedImage = ImageIO.read(rawImage);
			int imageWidth = bufferedImage.getWidth();
			int imageHeight = bufferedImage.getHeight();

			int[] sizes = { 48, 64, 128, 192, 256, imageWidth };

			int w = sizes[size];
			int h = sizes[size];

			if (h > 256) {
				h = imageHeight;
			}

			Thumbnails.of(rawImage).size(w, h).toFile(filename);

			thumbnailModel = ThumbnailModel.create(imageModel, filename, size);
		} else {
			thumbnailModel = retrievedThumbnailModel.get();
		}

		File thumbnail = new File(thumbnailModel.filename);

		return thumbnail;
	}

	private static String generateThumbnailFilename(File file, int size) {
		String baseName = FilenameUtils.getBaseName(file.getAbsolutePath());
		String extension = FilenameUtils.getExtension(file.getAbsolutePath());

		return ImageUploader.IMAGE_DIRECTORY + "thumb" + baseName + "size"
				+ size + "." + extension;
	}
}
