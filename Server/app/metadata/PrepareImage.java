package metadata;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import models.Image;
import models.Tag;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;

import play.Logger;
import queryDB.QueryImage;
import queryDB.QueryTag;
import utils.HibernateUtil;

import com.google.common.base.Optional;

public class PrepareImage {
	public static void writeImageMetadataToFile(long id) {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image image = queryImage.getImage(id);

		File file = new File(image.getFilename());

		Optional<TiffImageMetadata> exif = getExif(file);

		if (exif.isPresent()) {
			try {
				ExifWriter exifWriter = new ExifWriter(file, exif.get());
				exifWriter.setDescription(image.getDescription());

				Integer rating = image.getRating();

				exifWriter.setRating(rating == null ? 0 : rating);

				Set<Tag> tags = image.getTags();
				String tagList = "";

				for (Tag tag : tags) {
					tagList += tag.getName() + ",";
				}

				tagList.substring(0, tagList.length() - 1);

				exifWriter.setTags(tagList);
				exifWriter.save();
			} catch (IOException | ImageReadException | ImageWriteException exception) {
				Logger.of("logger").warn(
						"Could not write metadata to: "
								+ file.getAbsolutePath());
			}
		}
	}

	public static void loadImageWithMetadataFromFile(Image image) {
		File file = new File(image.getFilename());

		try {
			fillImageWithMetadata(image, file);
		} catch (IOException | ImageReadException exception) {
			Logger.of("logger").warn(
					"Could not read exif metadata from: "
							+ file.getAbsolutePath());
		}
	}

	private static void fillImageWithMetadata(Image image, File file)
			throws ImageReadException, IOException {
		Optional<TiffImageMetadata> retrievedExif = getExif(file);

		if (!retrievedExif.isPresent()) {
			Logger.of("logger").warn(
					"Could not read exif metadata from: "
							+ file.getAbsolutePath());

			return;
		}

		TiffImageMetadata exif = retrievedExif.get();

		ExifReader exifReader = new ExifReader(file, exif);
		exifReader.readMetadata();

		String description = exifReader.getDescription();
		String tagList = exifReader.getTags();
		int rating = exifReader.getRating();

		long id = image.getId();

		if (tagList != null) {
			String[] tags = tagList.split(",");

			for (String tag : tags) {
				QueryTag queryTag = new QueryTag(
						HibernateUtil.getSessionFactory());
				queryTag.tagImage(id, tag);
			}
		}

		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		if (description != null) {
			queryImage.describeImage(id, description);
		}

		if (rating != 0) {
			queryImage.rateImage(id, rating);
		}
	}

	private static Optional<TiffImageMetadata> getExif(File image) {
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
