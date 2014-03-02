import java.io.File;

import models.ImageModel;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application application) {
		File directory = new File("../../images");

		if (!directory.isDirectory())
			return;

		File[] listOfFiles = directory.listFiles();

		for (File image : listOfFiles) {
			String filename = image.getName();

			String filenameInDatabase = "../../images/" + filename;

			if (filename.matches("^(.+).png$")) {
				if (ImageModel.find.where().eq("filename", filenameInDatabase)
						.findUnique() == null) {
					ImageModel.create(filenameInDatabase);
				}

			}
		}
	}
}