package metadata;

import java.io.File;
import java.io.IOException;

import models.ImageModel;
import models.TagModel;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;

import play.Logger;

public class PrepareImageModel {
	public static void loadImageModelWithMetadataFromFile(ImageModel imageModel) {
		File image = new File(imageModel.filename);

		try {
			setMetadata(imageModel, image);
		} catch (IOException | ImageReadException exception) {
			Logger.warn("Could not read exif metadata from: "
					+ image.getAbsolutePath());
		}
	}

	private static void setMetadata(ImageModel imageModel, File image)
			throws ImageReadException, IOException {
		IImageMetadata metadata = Imaging.getMetadata(image);

		if (!(metadata instanceof JpegImageMetadata)) {
			return;
		}

		JpegImageMetadata JpegMetadata = (JpegImageMetadata) metadata;
		TiffImageMetadata exif = JpegMetadata.getExif();

		ExifReader exifReader = new ExifReader(image, exif);
		exifReader.readMetadata();

		String description = exifReader.getDescription();
		String tagList = exifReader.getTags();
		int rating = exifReader.getRating();

		if (description != null) {
			imageModel.description = exifReader.getDescription();
		}

		if (tagList != null) {
			String[] tags = tagList.split(",");

			for (String tag : tags) {
				imageModel.tags.add(TagModel.create(tag));
			}
		}

		if (rating != 0) {
			imageModel.rating = exifReader.getRating();
		}
	}
}
