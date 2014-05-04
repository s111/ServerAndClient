package metadata;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import models.Image;
import queryDB.QueryImage;
import queryDB.QueryTag;
import utils.HibernateUtil;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class MetadataUtil {
	public static Date getDate(File file) {
		Date date = null;

		try {
			Metadata metadata = ImageMetadataReader.readMetadata(file);

			ExifSubIFDDirectory directory = metadata
					.getDirectory(ExifSubIFDDirectory.class);

			if (directory != null) {
				date = directory
						.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
			}
		} catch (ImageProcessingException | IOException e) {
		}

		if (date == null) {
			date = new Date();
		}

		return date;
	}

	public static void loadXmpMetadataFromFile(long id) {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image image = queryImage.getImage(id);

		File file = new File(image.getFilename());

		queryImage.describeImage(id, XmpReader.getDescription(file));
		queryImage.rateImage(id, XmpReader.getRating(file));

		QueryTag queryTag = new QueryTag(HibernateUtil.getSessionFactory());

		List<String> tags = XmpReader.getTags(file);

		for (String tag : tags) {
			queryTag.tagImage(id, tag);
		}
	}
}
