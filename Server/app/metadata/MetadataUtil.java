package metadata;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
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

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.google.common.base.Optional;

public class MetadataUtil {
	private static final int DESCRIPTION = 0;
	private static final int RATING = 1;
	private static final int TAG = 2;

	public static Date getDate(File file) {
		Date date = null;

		try {
			Metadata metadata = ImageMetadataReader.readMetadata(file);

			ExifSubIFDDirectory directory = metadata
					.getDirectory(ExifSubIFDDirectory.class);

			date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
		} catch (ImageProcessingException | IOException e) {
		}

		return date;
	}

	private static void saveMetadataToFile(File file, Object metadata, int type) {
		Optional<TiffImageMetadata> retrievedExif = getExif(file);

		if (!retrievedExif.isPresent()) {
			return;
		}

		TiffImageMetadata exif = retrievedExif.get();

		ExifWriter exifWriter;

		try {
			exifWriter = new ExifWriter(file, exif);

			if (type == DESCRIPTION) {
				exifWriter.setDescription((String) metadata);
			} else if (type == RATING) {
				exifWriter.setRating((Integer) metadata);
			} else if (type == TAG) {
				Tag tag = new Tag();
				tag.setName((String) metadata);

				Set<Tag> tags = new HashSet<>();
				tags.add(tag);

				exifWriter.setTags(tags);
			}

			exifWriter.save();
		} catch (ImageReadException | ImageWriteException | IOException exception) {
			Logger.warn("Could not save metadata to image: "
					+ file.getAbsolutePath());

			return;
		}
	}

	public static void saveDescriptionToFile(File file, String description) {
		saveMetadataToFile(file, description, DESCRIPTION);
	}

	public static void saveRatingToFile(File file, int rating) {
		saveMetadataToFile(file, rating, RATING);
	}

	public static void saveTagToFile(File file, String tag) {
		saveMetadataToFile(file, tag, TAG);
	}

	public static void loadMetadataFromFile(long id) {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image image = queryImage.getImage(id);

		File file = new File(image.getFilename());
		ExifReader exifReader = getExifReader(file);

		if (exifReader == null) {
			return;
		}

		queryImage.describeImage(id, exifReader.getDescription());
		queryImage.rateImage(id, exifReader.getRating());

		QueryTag queryTag = new QueryTag(HibernateUtil.getSessionFactory());

		Set<Tag> tags = exifReader.getTags();

		for (Tag tag : tags) {
			queryTag.tagImage(id, tag.getName());
		}

		Date date = null;

		try {
			Metadata metadata = ImageMetadataReader.readMetadata(file);

			ExifSubIFDDirectory directory = metadata
					.getDirectory(ExifSubIFDDirectory.class);

			date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
		} catch (IOException | ImageProcessingException exception) {
		}

		if (date != null) {
			queryImage.setDateTaken(id, date);
		}
	}

	private static ExifReader getExifReader(File file) {
		Optional<TiffImageMetadata> retrievedExif = getExif(file);

		if (!retrievedExif.isPresent()) {
			return null;
		}

		TiffImageMetadata exif = retrievedExif.get();
		ExifReader exifReader = new ExifReader(file, exif);

		try {
			exifReader.readMetadata();
		} catch (ImageReadException exception) {
			return null;
		}

		return exifReader;
	}

	private static Optional<TiffImageMetadata> getExif(File file) {
		IImageMetadata metadata = null;

		try {
			metadata = Imaging.getMetadata(file);
		} catch (ImageReadException | IOException e) {
			return Optional.absent();
		}

		if (!(metadata instanceof JpegImageMetadata)) {
			return Optional.absent();
		}

		JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
		TiffImageMetadata exif = jpegMetadata.getExif();

		return Optional.fromNullable(exif);
	}
}
