import java.io.File;

import metadata.PrepareImageModel;
import models.ImageModel;
import models.TagModel;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import upload.Uploader;

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

		for (File image : listOfFiles) {
			String filename = image.getName();

			String filenameInDatabase = Uploader.IMAGE_DIRECTORY + filename;

			if (filename.matches("^(.+).(png|jpg)$")) {
				if (filename.contains("thumb") || filename.contains("tmp"))
					continue;
				if (ImageModel.find.where().eq("filename", filenameInDatabase)
						.findUnique() == null) {
					ImageModel imageModel = ImageModel
							.create(filenameInDatabase);

					PrepareImageModel
							.loadImageModelWithMetadataFromFile(imageModel);

					imageModel.addTag(TagModel.create("id:" + imageModel.id));
				}
			}
		}
	}
}