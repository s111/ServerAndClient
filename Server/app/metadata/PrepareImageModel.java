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

import com.google.common.base.Optional;

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
		Optional<TiffImageMetadata> retrievedExif = getMetadataTable(image);

		if (!retrievedExif.isPresent()) {
			Logger.warn("Could not read exif metadata from: "
					+ image.getAbsolutePath());

			return;
		}

		TiffImageMetadata exif = retrievedExif.get();

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
				imageModel.addTag(TagModel.create(tag));
			}
		}

		if (rating != 0) {
			imageModel.rating = exifReader.getRating();
		}
	}

	public static Optional<TiffImageMetadata> getMetadataTable(File image) {
		IImageMetadata metadata = null;

		try {
			metadata = Imaging.getMetadata(image);
		} catch (ImageReadException | IOException e) {
			return Optional.absent();
		}

		if (!(metadata instanceof JpegImageMetadata)) {
			return Optional.absent();
		}

		JpegImageMetadata JpegMetadata = (JpegImageMetadata) metadata;
		TiffImageMetadata exif = JpegMetadata.getExif();

		return Optional.fromNullable(exif);
	}
}
