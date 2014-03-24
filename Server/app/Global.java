import java.io.File;

import metadata.PrepareImage;
import models.Image;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import queryDB.QueryImage;
import queryDB.QueryTag;
import upload.Uploader;
import utils.HibernateUtil;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application application) {
		File directory = new File(Uploader.IMAGE_DIRECTORY);

		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				Logger.error("Could not create upload directory");
				return;
			}
		}

		File[] listOfFiles = directory.listFiles();

		for (File file : listOfFiles) {
			String filename = file.getName();

			String filenameInDatabase = Uploader.IMAGE_DIRECTORY + filename;

			if (filename.matches("^(.+).(png|jpg)$")) {
				if (filename.contains("thumb") || filename.contains("tmp")) {
					continue;
				}

				QueryImage queryImage = new QueryImage(
						HibernateUtil.getSessionFactory());

				if (queryImage.getImage(filenameInDatabase) == null) {
					Image image = new Image();
					image.setFilename(filenameInDatabase);

					queryImage.addImage(image);

					PrepareImage.loadImageWithMetadataFromFile(image);

					long id = image.getId();

					QueryTag queryTag = new QueryTag(
							HibernateUtil.getSessionFactory());
					queryTag.tagImage(id, "id:" + id);
				}
			}
		}
	}
}