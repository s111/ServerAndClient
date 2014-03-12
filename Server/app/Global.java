import java.io.File;

import models.ImageModel;
import models.TagModel;
import play.Application;
import play.GlobalSettings;
import controllers.ImageUploader;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application application) {
		File directory = new File(ImageUploader.IMAGE_DIRECTORY);

		if (!directory.isDirectory())
			return;

		File[] listOfFiles = directory.listFiles();

		for (File image : listOfFiles) {
			String filename = image.getName();

			String filenameInDatabase = ImageUploader.IMAGE_DIRECTORY
					+ filename;

			if (filename.matches("^(.+).(png|jpg)$")) {
				if (filename.contains("thumb"))
					continue;
				if (ImageModel.find.where().eq("filename", filenameInDatabase)
						.findUnique() == null) {
					ImageModel imageModel = ImageModel
							.create(filenameInDatabase);

					imageModel.tag(TagModel.create("id:" + imageModel.id));
				}
			}
		}
	}
}