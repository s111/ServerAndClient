import java.io.File;

import controllers.ImageUploader;
import models.ImageModel;
import play.Application;
import play.GlobalSettings;

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

			if (filename.matches("^(.+).png$")) {
				if (filename.contains("thumb"))
					continue;
				if (ImageModel.find.where().eq("filename", filenameInDatabase)
						.findUnique() == null) {
					ImageModel.create(filenameInDatabase);
				}
			}
		}
	}
}