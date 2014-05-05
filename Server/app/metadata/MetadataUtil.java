package metadata;

import java.io.File;
import java.util.Date;
import java.util.List;

import models.Image;
import queryDB.QueryImage;
import queryDB.QueryTag;
import utils.HibernateUtil;

public class MetadataUtil {
	public static void loadXmpMetadataFromFile(long id) {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image image = queryImage.getImage(id);

		File file = new File(image.getFilename());

		Date creationDate = new Date(XmpReader.getCreationDate(file));

		if (creationDate.equals(new Date(0))) {
			creationDate = new Date();

			XmpWriter.setCreationDate(file, creationDate.getTime());
		}

		queryImage.describeImage(id, XmpReader.getDescription(file));
		queryImage.rateImage(id, XmpReader.getRating(file));
		queryImage.setDateTaken(id, creationDate);

		QueryTag queryTag = new QueryTag(HibernateUtil.getSessionFactory());

		List<String> tags = XmpReader.getTags(file);

		for (String tag : tags) {
			queryTag.tagImage(id, tag);
		}
	}
}
